/*
 * Copyright (c) 2019, Ganom <https://github.com/Ganom>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.runelite.client.plugins.fightcave;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.helpers.HelperPlugin;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;

import static net.runelite.client.plugins.helpers.HelperDelay.rand10to100;
import static net.runelite.client.plugins.helpers.HelperDelay.rand35to70;
import static net.runelite.client.plugins.helpers.HelperInput.Click;
import static net.runelite.client.plugins.helpers.HelperInput.Delay;
import static net.runelite.client.plugins.helpers.HelperPlugin.menuTarget;
import static net.runelite.client.plugins.helpers.HelperWidget.PrayMageEnabled;

@Slf4j
@Singleton
public class FightCaveOverlay extends Overlay {
    private final FightCavePlugin plugin;
    private final Client client;
    private final SpriteManager spriteManager;

    @Inject
    private ConfigManager configManager;

    @Inject
    FightCaveOverlay(final Client client, final FightCavePlugin plugin, final SpriteManager spriteManager, ConfigManager configManager) {
        this.client = client;
        this.plugin = plugin;
        this.spriteManager = spriteManager;
        this.configManager = configManager;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGHEST);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    private boolean onceone = false;
    private boolean oncetwo = false;
    private boolean needFinish = false;

    @Override
    public Dimension render(Graphics2D graphics) {

        String tempRunning = configManager.getConfiguration("fightcave", "run");

        for (FightCaveContainer npc : plugin.getFightCaveContainer()) {
            if (npc.getNpc() == null) {
                continue;
            }

            final int ticksLeft = npc.getTicksUntilAttack();
            final FightCaveContainer.AttackStyle attackStyle = npc.getAttackStyle();

            if (ticksLeft <= 0) {
                continue;
            }

            final String ticksLeftStr = String.valueOf(ticksLeft);
            final int font = plugin.getFontStyle().getFont();
            final boolean shadows = plugin.isShadows();
            Color color = (ticksLeft <= 1 ? Color.WHITE : attackStyle.getColor());
            final Point canvasPoint = npc.getNpc().getCanvasTextLocation(graphics, Integer.toString(ticksLeft), 0);

            if (npc.getNpcName().equals("TzTok-Jad")) {
                color = (ticksLeft <= 1 || ticksLeft == 8 ? attackStyle.getColor() : Color.WHITE);

                BufferedImage pray = getPrayerImage(npc.getAttackStyle());

                if (pray == null) {
                    continue;
                }

                renderImageLocation(graphics, npc.getNpc().getCanvasImageLocation(ImageUtil.resizeImage(pray, 36, 36), 0), pray, 12, 30);
            }

            OverlayUtil.renderTextLocation(graphics, ticksLeftStr, plugin.getTextSize(), font, color, canvasPoint, shadows, 0);
        }

        if (plugin.isTickTimersWidget()) {
            if (!plugin.getRangedTicks().isEmpty()) {
                widgetHandler(graphics,
                        Prayer.PROTECT_FROM_MISSILES,
                        plugin.getRangedTicks().get(0) == 1 ? Color.WHITE : Color.GREEN,
                        Integer.toString(plugin.getRangedTicks().get(0)),
                        plugin.isShadows()
                );
            }
            if (!plugin.getMeleeTicks().isEmpty()) {
                widgetHandler(graphics,
                        Prayer.PROTECT_FROM_MELEE,
                        plugin.getMeleeTicks().get(0) == 1 ? Color.WHITE : Color.RED,
                        Integer.toString(plugin.getMeleeTicks().get(0)),
                        plugin.isShadows()
                );
            }
            if (!plugin.getMageTicks().isEmpty()) {

                widgetHandler(graphics,
                        Prayer.PROTECT_FROM_MAGIC,
                        plugin.getMageTicks().get(0) == 1 ? Color.WHITE : Color.CYAN,
                        Integer.toString(plugin.getMageTicks().get(0)),
                        plugin.isShadows()
                );

                if (WaveOverlay.booleanFromString(tempRunning) || needFinish) {

                    if (menuTarget.contains("magic")) {

                        if (plugin.getMageTicks().get(0) == 4 && !onceone && PrayMageEnabled) {
                            onceone = true;
                            needFinish = true;
                            Executors.newSingleThreadExecutor().execute(() -> {
                                Delay(rand10to100);
                                Delay(rand35to70);
                                Delay(rand35to70);
                                Delay(rand10to100);
                                Click();
                            });

                        } else if (plugin.getMageTicks().get(0) == 2 && !oncetwo && needFinish && !PrayMageEnabled) {
                            oncetwo = true;
                            needFinish = false;
                            Executors.newSingleThreadExecutor().execute(() -> {
                                Delay(rand10to100);
                                Delay(rand35to70);
                                Delay(rand35to70);
                                Delay(rand10to100);
                                Click();
                            });

                        } else if (plugin.getMageTicks().get(0) == 1) {
                            onceone = false;
                            oncetwo = false;
                        }

                    }else {
                        onceone = false;
                        oncetwo = false;
                    }

                } else {
                    onceone = false;
                    oncetwo = false;
                }
            }else {
                onceone = false;
                oncetwo = false;
            }
        }
        return null;
    }


    private void widgetHandler(Graphics2D graphics, Prayer prayer, Color color, String ticks, boolean shadows) {
        if (prayer != null) {
            Rectangle bounds = OverlayUtil.renderPrayerOverlay(graphics, client, prayer, color);

            if (bounds != null) {
                renderTextLocation(graphics, ticks, 16, plugin.getFontStyle().getFont(), color, centerPoint(bounds), shadows);
            }
        }
    }

    private BufferedImage getPrayerImage(FightCaveContainer.AttackStyle attackStyle) {
        switch (attackStyle) {
            case MAGE:
                return spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MAGIC, 0);
            case MELEE:
                return spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MELEE, 0);
            case RANGE:
                return spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MISSILES, 0);
        }
        return null;
    }

    private void renderImageLocation(Graphics2D graphics, Point imgLoc, BufferedImage image, int xOffset, int yOffset) {
        int x = imgLoc.getX() + xOffset;
        int y = imgLoc.getY() - yOffset;

        graphics.drawImage(image, x, y, null);
    }

    private void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint, boolean shadows) {
        graphics.setFont(new Font("Arial", fontStyle, fontSize));
        if (canvasPoint != null) {
            final Point canvasCenterPoint = new Point(
                    canvasPoint.getX() - 3,
                    canvasPoint.getY() + 6);
            final Point canvasCenterPoint_shadow = new Point(
                    canvasPoint.getX() - 2,
                    canvasPoint.getY() + 7);
            if (shadows) {
                OverlayUtil.renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
            }
            OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
        }
    }

    private Point centerPoint(Rectangle rect) {
        int x = (int) (rect.getX() + rect.getWidth() / 2);
        int y = (int) (rect.getY() + rect.getHeight() / 2);
        return new Point(x, y);
    }
}

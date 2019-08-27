/*
 * Copyright (c) 2019, Hermetism <https://github.com/Hermetism>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.herblore;

import net.runelite.api.Skill;
import net.runelite.client.plugins.helpers.*;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static net.runelite.client.plugins.helpers.HelperColor.cBlue;
import static net.runelite.client.plugins.herblore.hItems.hItemsListBank;
import static net.runelite.client.plugins.herblore.hItems.hItemsListInventory;
import static net.runelite.client.util.StackFormatter.quantityToRSDecimalStack;

class hOverlayMenu extends Overlay {

    private final hConfig config;
    private final PanelComponent panelComponent = new PanelComponent();
    private XpTrackerService xpTrackerService;
    private HelperBank helperBank;
    private HelperInventory helperInventory;

    @Inject
    public hOverlayMenu(hConfig config, XpTrackerService xpTrackerService, HelperBank helperBank, HelperInventory helperInventory) {
        setPosition(OverlayPosition.TOP_LEFT);
        this.config = config;
        this.xpTrackerService = xpTrackerService;
        this.helperBank = helperBank;
        this.helperInventory = helperInventory;
    }

    static long startTime = 0;
    static boolean started = false;

    @Override
    public Dimension render(Graphics2D graphics) {

        if (config.RunAltarBuiltIn() && !started) {
            started = true;
            startTime = System.currentTimeMillis();
        }

        if (!config.RunAltarBuiltIn() && started) {
            started = false;
            startTime = 0;
        }

        panelComponent.getChildren().clear();
        panelComponent.setBorder(new Rectangle(2, 2, 2, 2));
        panelComponent.setGap(new Point(0, 2));

        hItemsListBank = helperBank.CheckItemAvailableInBank(hItemsListBank);
        hItemsListInventory = helperInventory.CheckItemAvailableInInventory(hItemsListInventory);

        Items();

        if (config.showExp()) {
            xpTracker();
        }

        if (config.showStats()) {
            runningStatus();
        }

        return panelComponent.render(graphics);
    }


    private void Items() {
        if (HelperRegion.regionString != null) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Region ")
                    .leftColor(cBlue)
                    .right(HelperRegion.regionString)
                    .rightColor(HelperColor.cGreen)
                    .build());
        }

        if (config.showSessionTime()) {
            long elapsedTime;
            elapsedTime = System.currentTimeMillis() - startTime;
            DateFormat simple = new SimpleDateFormat("HH:mm:ss");
            simple.setTimeZone(TimeZone.getTimeZone("UTC"));

            if (started && elapsedTime > 0) {
                String f = simple.format(elapsedTime);
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Elapsed : ")
                        .leftColor(cBlue)
                        .right(f)
                        .rightColor(HelperColor.cGreen)
                        .build());

            }
        }

    }

    private void runningStatus() {

        if (HelperThread.isBusy()) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Busy!")
                    .color(HelperColor.cGreen)
                    .build());
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Not Busy..")
                    .color(HelperColor.cYellow)
                    .build());
        }
    }


    private void xpTracker() {

        int actions = xpTrackerService.getActions(Skill.HERBLORE);
        if (actions > 0) {
            int ExpPerHour = xpTrackerService.getXpHr(Skill.HERBLORE);
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Herblore XP/hr ")
                    .leftColor(cBlue)
                    .right(quantityToRSDecimalStack(ExpPerHour))
                    .rightColor(HelperColor.cGreen)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Level in ")
                    .leftColor(cBlue)
                    .right(xpTrackerService.getTimeTillGoal(Skill.HERBLORE))
                    .rightColor(HelperColor.cGreen)
                    .build());
        }
    }
}


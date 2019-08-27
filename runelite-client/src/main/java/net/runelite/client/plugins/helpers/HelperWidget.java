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
package net.runelite.client.plugins.helpers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static net.runelite.client.plugins.helpers.HelperDraw.renderPoly;

@Slf4j
public class HelperWidget extends Overlay {

    private static final String CONFIG_GROUP = "helpers";
    private final Client client;
    private final ConfigManager configManager;

    public static PointerInfo mouseLocationOnScreen = MouseInfo.getPointerInfo();
    public static LocalPoint opponentPoint = new LocalPoint(0, 0);

    static boolean WidgetCombatEnabled = true;
    static boolean WidgetBankingEnabled = true;
    static boolean WidgetNPCPromptsEnabled = true;
    static boolean WidgetPlayerEnabled = true;
    static boolean WidgetRunecraftEnabled = true;
    static boolean WidgetPrayerEnabled = true;

    public static boolean bankIsOpen;
    public static boolean dialogOpen = false;
    public static boolean chatCraftOptionOne = false;
    private static boolean CombatEnabled = true;
    private static boolean BankEnabled = true;

    public static boolean TalkingToShopOwner = false;

    public static Widget bank;
    public static Widget inventory;
    public static Widget furniture;
    public static Widget specialAttackToggle;
    public static Widget runningToggle;
    public static Widget chatboxCraftOptionOne;
    public static Widget superGlassMake;
    public static Widget spellOuraniaTeleport;
    public static Widget spellNpcContact;
    public static Widget npcContactDarkMage;
    public static Widget bankDepositInventory;
    public static Widget withdrawOptionALL;
    public static Widget withdrawOptionOne;
    public static Widget bankTabTwo;

    public static boolean fightingNPC = false;
    public static String opponentNameSingle = "";
    public static String opponentNamesMulti = "";
    public static int numberOfOpponents = 0;
    public static int runningEnabled = 0;
    public static int totalBankValue = 0;

    public static boolean PrayMageEnabled = false;
    //  public static boolean PrayMeleeEnabled = false;
    //  public static boolean PrayRangeEnabled = false;


    @Inject
    public HelperWidget(Client client, ConfigManager configManager) {
        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.configManager = configManager;
    }

    @Getter(AccessLevel.PACKAGE)
    private Actor lastOpponent;
    private Instant lastTime;
    @Getter(AccessLevel.PACKAGE)
    public static Instant lastTickUpdate;
    @Getter(AccessLevel.PACKAGE)
    private WorldPoint lastPlayerLocation;
    private static final Duration WAIT = Duration.ofSeconds(5);

    public static ItemContainer inventoryItemContainer;

    public void onGameTick(GameTick event) {
        lastTickUpdate = Instant.now();
        lastPlayerLocation = client.getLocalPlayer().getWorldLocation();
        if (WidgetCombatEnabled) {
            if (lastOpponent != null
                    && lastTime != null
                    && client.getLocalPlayer().getInteracting() == null) {
                if (Duration.between(lastTime, Instant.now()).compareTo(WAIT) > 0) {
                    lastOpponent = null;
                }
            }
        }
    }

    public void onInteractingChanged(InteractingChanged event) {

        if (WidgetCombatEnabled) {
            if (event.getSource() != client.getLocalPlayer()) {
                return;
            }

            Actor opponent = event.getTarget();

            if (opponent == null) {
                lastTime = Instant.now();
                return;
            }

            lastOpponent = opponent;
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        HelperThread.getThreadStates();
        CombatEnabled = configManager.getConfiguration(CONFIG_GROUP, "widgetEnableCombat").equals("true");
        BankEnabled = configManager.getConfiguration(CONFIG_GROUP, "widgetBanking").equals("true");
        superGlassMake = client.getWidget(WidgetInfo.SPELL_SUPERGLASS_MAKE);

        if (WidgetPrayerEnabled) {

            PrayMageEnabled = client.getVar(Varbits.PRAYER_PROTECT_FROM_MAGIC) == 1;
            // PrayMeleeEnabled = client.getVar(Varbits.PRAYER_PROTECT_FROM_MELEE) == 1;
            // PrayRangeEnabled = client.getVar(Varbits.PRAYER_PROTECT_FROM_MISSILES) == 1;
        }

        if (WidgetPlayerEnabled) {
            runningToggle = client.getWidget(WidgetInfo.MINIMAP_TOGGLE_RUN_ORB);
            runningEnabled = client.getVar(VarPlayer.RUNNING);
            specialAttackToggle = client.getWidget(WidgetInfo.MINIMAP_TOGGLE_SPEC_ORB);
        }

        if (WidgetNPCPromptsEnabled) {

            furniture = client.getWidget(WidgetInfo.FURNITURE_MENU_OPTION_2);
            chatboxCraftOptionOne = client.getWidget(WidgetInfo.CHATBOX_CRAFT_CRAFT_ONE);
            chatCraftOptionOne = chatboxCraftOptionOne != null && !chatboxCraftOptionOne.isHidden();
            Widget s = client.getWidget(WidgetInfo.SHOP_INVENTORY_ITEMS_CONTAINER);
            Widget c = client.getWidget(WidgetInfo.DIALOG_PLAYER);
            Widget n = client.getWidget(WidgetInfo.DIALOG_NPC);
            Widget opt = client.getWidget(WidgetInfo.DIALOG_OPTION);
            Widget npcc = client.getWidget(WidgetInfo.CHATBOX_CRAFT_PARENT);
            dialogOpen = c != null || n != null || opt != null || npcc != null;
            TalkingToShopOwner = s != null && !s.isHidden();

        }

        if (WidgetRunecraftEnabled) {

            spellOuraniaTeleport = client.getWidget(WidgetInfo.SPELL_OURANIA_TELEPORT);
            spellNpcContact = client.getWidget(WidgetInfo.SPELL_NPC_CONTACT);
            npcContactDarkMage = client.getWidget(WidgetInfo.DARK_MAGE_NPC_CONTACT);

        }

        if (WidgetBankingEnabled) {
            inventoryItemContainer = client.getItemContainer(InventoryID.INVENTORY);
            inventory = client.getWidget(WidgetInfo.INVENTORY);
            bank = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
            bankIsOpen = bank != null && !bank.isHidden();
            bankDepositInventory = client.getWidget(WidgetInfo.BANK_DEPOSIT_INVENTORY);
            withdrawOptionALL = client.getWidget(WidgetInfo.BANK_WITHDRAW_ALL);
            withdrawOptionOne = client.getWidget(WidgetInfo.BANK_WITHDRAW_ONE);
            Widget bcc = client.getWidget(WidgetInfo.BANK_CONTENT_CONTAINER);
            if (bcc != null) {
                Widget[] bc = bcc.getDynamicChildren();
                for (Widget ss : bc) {
                    if (ss.getIndex() == 2) {
                        bankTabTwo = ss;
                    }
                }
            }

        }

        if (WidgetCombatEnabled) {

            final Actor opponent = getLastOpponent();
            if (opponent != null) {
                if (opponent instanceof NPC) {
                    opponentNameSingle = opponent.getName();
                    opponentPoint = opponent.getLocalLocation();
                    fightingNPC = true;
                }
            }

            final List<NPC> npcs = client.getNpcs();
            numberOfOpponents = 0;
            StringBuilder sb = new StringBuilder();
            for (NPC npc : npcs) {

                String npcName;
                String name = npc.getName();
                if (name == null) {
                    name = " ";
                }

                if (npc.getCombatLevel() > 0) {
                    npcName = Text.removeTags(name) + " (" + npc.getCombatLevel() + ")";
                } else {
                    npcName = Text.removeTags(name);
                }


                Point textLocationName = npc.getCanvasTextLocation(graphics, npcName, npc.getLogicalHeight() + 40);
                Point textLocationDead = npc.getCanvasTextLocation(graphics, "Dead", npc.getLogicalHeight() + 40);

                Polygon opPoly = npc.getConvexHull();
                if (npc.getInteracting() == client.getLocalPlayer()) {
                    numberOfOpponents++;
                    sb.append(npc.getName()).append(",");
                    fightingNPC = true;
                    opponentNameSingle = npc.getName();

                    if (numberOfOpponents > 1) {
                        opponentNamesMulti = sb.toString();
                    }

                    if (textLocationName != null) {
                        if (npc.isDead()) {
                            OverlayUtil.renderTextLocation(graphics, textLocationDead, "Dead", Color.RED);
                        } else {

                            if (npcName != null) {
                                OverlayUtil.renderTextLocation(graphics, textLocationName, npcName, Color.WHITE);
                                renderPoly(graphics, Color.RED, opPoly);
                            }
                        }
                    }

                } else {
                    if (textLocationName != null) {
                        if (npc.isDead()) {
                            OverlayUtil.renderTextLocation(graphics, textLocationDead, "Dead", Color.RED);
                        } else {
                            renderPoly(graphics, Color.WHITE, opPoly);
                            if (npcName != null) {
                                OverlayUtil.renderTextLocation(graphics, textLocationName, npcName, Color.WHITE);
                            }
                        }
                    }

                }
            }
        }

        /* RENDER BANK BOOTHS */
        if (WidgetBankingEnabled) {

            Set<GameObject> boothObjects = HelperPlugin.getBoothObjects();
            if (boothObjects != null) {
                for (GameObject boothObject : boothObjects) {
                    if (boothObject.getWorldLocation().distanceTo2D(client.getLocalPlayer().getWorldLocation()) <= 12) {
                        Polygon p = boothObject.getConvexHull();
                        HelperDraw.renderPoly(graphics, Color.YELLOW, p);
                    }
                }
            }

        }

        return null;
    }

    /**
     * WIDGET COMBAT ENABLED - SWITCH
     */
    public static void WidgetCombatEnabledTrue() {
        if (CombatEnabled) {
            WidgetCombatEnabled = true;
            return;
        }
        WidgetCombatEnabled = false;
    }


    /**
     * WIDGET BANK ENABLED - SWITCH
     */
    public static void WidgetBankEnabledTrue() {
        if (BankEnabled) {
            WidgetBankingEnabled = true;
            return;
        }
        WidgetBankingEnabled = false;
    }

    /**
     * DISABLE ALL EXTRA HELPER WIDGETS
     */
    public static void disableAllHelperWidgets() {

        WidgetCombatEnabled = false;
        WidgetBankingEnabled = false;
        WidgetNPCPromptsEnabled = false;
        WidgetRunecraftEnabled = false;
        WidgetPlayerEnabled = false;

    }

    /**
     * ENABLE ALL EXTRA HELPER WIDGETS (Default)
     */
    public static void enableAllHelperWidgets() {

        WidgetCombatEnabledTrue();
        WidgetBankEnabledTrue();
        WidgetNPCPromptsEnabled = true;
        WidgetRunecraftEnabled = true;
        WidgetPlayerEnabled = true;
    }

    /**
     * WIDGET COMBAT ENABLED FALSE - SWITCH
     */
    public static void WidgetCombatEnabledFalse() {
        WidgetCombatEnabled = false;
    }

    /**
     * WIDGET BANK ENABLED FALSE - SWITCH
     */
    public static void WidgetBankEnabledFalse() {
        WidgetBankingEnabled = false;
    }

}

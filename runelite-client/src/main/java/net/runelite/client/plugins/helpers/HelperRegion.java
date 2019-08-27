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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientInt;
import net.runelite.api.Varbits;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindGameObjectLocation;
import static net.runelite.client.plugins.helpers.HelperFind.FindNPCLocationByID;
import static net.runelite.client.plugins.helpers.HelperInput.Delay;
import static net.runelite.client.plugins.helpers.HelperInput.MoveAndClick;
import static net.runelite.client.plugins.helpers.HelperWidget.bankIsOpen;

@Slf4j
public class HelperRegion extends Overlay {

    private final Client client;
    private static LocalPoint PlayerLastLocation = new LocalPoint(0, 0);

    public static int currentBankTab;
    public static int currentWithdrawSetting;
    public static int currentInventoryTab;
    public static int currentRegionID = 0;


    @Getter
    public static int[] boothTile = new int[]{0, 0};

    public static int[] ladderTile = new int[]{0, 0};
    public static int[] jatix = new int[]{0, 0};
    public static int[] vialOfWaterInt = new int[]{0, 0};
    public static boolean isMoving = false;
    private static boolean once = false;

    public static String regionString = "";

    @Inject
    public HelperRegion(Client client) {
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);
        this.client = client;

    }

    @Override
    public Dimension render(Graphics2D graphics) {
        RegionSpecificChanges(client, graphics);
        currentInventoryTab = client.getVar(VarClientInt.INTERFACE_TAB);
        currentBankTab = client.getVar(Varbits.CURRENT_BANK_TAB);
        currentWithdrawSetting = client.getVar(Varbits.WITHDRAW_SETTING);
        return null;
    }

    private static void RegionSpecificChanges(Client client, Graphics2D graphics) {
        WorldPoint localWorld = client.getLocalPlayer().getWorldLocation();
        LocalPoint CurrentPlayerLocation = client.getLocalPlayer().getLocalLocation();
        currentRegionID = localWorld.getRegionID();

        int x1 = CurrentPlayerLocation.getX();
        int y1 = CurrentPlayerLocation.getY();

        int x2 = PlayerLastLocation.getX();
        int y2 = PlayerLastLocation.getY();


        isMoving = false;
        if (x1 != x2 || y1 != y2) {

            PlayerLastLocation = new LocalPoint(x1, y1);
            isMoving = true;
        }

        switch (currentRegionID) {

            case 11573:
                regionString = "Taverly";
                jatix = HelperFind.FindNPCLocationByID(8532, client, graphics);

                Widget vialOfWater = client.getWidget(WidgetInfo.NPC_SHOP_INVENTORY_CONTAINER);

                if (vialOfWater != null) {
                    vialOfWaterInt = HelperTransform.xyFromWidget(vialOfWater.getChild(4), client);
                }

                break;


            case 12342:
                boothTile = FindGameObjectLocation(client, graphics, 3098, 3493, 10355, 1);
                regionString = "Edgeville";
                break;


            case 12338:
                boothTile = FindGameObjectLocation(client, graphics, 3091, 3242, 10355, 1);
                regionString = "Draynor";
                break;


            case 13878:
                boothTile = FindGameObjectLocation(client, graphics, 3513, 3480, 24347, 1);
                regionString = "Canafis";
                break;


            case 11828:
                boothTile = FindGameObjectLocation(client, graphics, 2946, 3367, 24101, 1);
                regionString = "Falador W";
                break;


            case 12084:
                boothTile = FindGameObjectLocation(client, graphics, 3015, 3354, 27253, 1);
                regionString = "Falador E";
                break;


            case 9776:
                boothTile = FindGameObjectLocation(client, graphics, 2444, 3083, 4483, 2);
                regionString = "Castle Wars";
                break;


            case 12597:
                boothTile = FindGameObjectLocation(client, graphics, 3186, 3438, 10583, 1);
                regionString = "Varrock W";
                break;


            case 12853:
                boothTile = FindGameObjectLocation(client, graphics, 3256, 3419, 10583, 1);
                regionString = "Varrock E";
                break;


            case 12598:
                regionString = "Varrock GE";
                boothTile = FindNPCLocationByID(1030, client, graphics);
                break;


            case 10806:
                boothTile = FindGameObjectLocation(client, graphics, 2729, 3494, 25808, 1);
                regionString = "Seers Village";
                break;


            case 11061:
                boothTile = FindGameObjectLocation(client, graphics, 2807, 3442, 10355, 1);
                regionString = "Catherby";
                break;


            case 10292:
                boothTile = FindGameObjectLocation(client, graphics, 2618, 3331, 10355, 1);
                regionString = "Ardourgne N";
                break;


            case 10547:
                boothTile = FindGameObjectLocation(client, graphics, 2656, 3283, 10356, 1);
                regionString = "Ardourgne S";
                break;


            case 11423:
                boothTile = FindGameObjectLocation(client, graphics, 2838, 10206, 6084, 1);
                regionString = "Keldagrim";
                break;


            case 8253:
                boothTile = FindGameObjectLocation(client, graphics, 2099, 3920, 16700, 1);
                regionString = "Lunar Isle";
                break;


            case 12119:
                boothTile = FindNPCLocationByID(7417, client, graphics);
                regionString = "Inside ZMI";
                break;


            case 9778:
                regionString = "Outside ZMI";
                ladderTile = HelperFind.FindGameObjectLocation(client, graphics, 2452, 3231, 29635, 2);
                break;


            case 9033:
                regionString = "Nightmare Zone";
                break;


            case 10288:
                regionString = "Yanille/NMZ";
                break;


            case 10032:
                regionString = "Yanille W";
                break;


            default:
                regionString = "Unknown";
                break;
        }
    }

    public static void WaitForBankOpen(Client client, Boolean stop) {

        if (stop) {
            return;
        }

        Delay(rand40to70);
        MoveAndClick(boothTile[0], boothTile[1], false);

        int t = client.getTickCount();
        int timeOut = client.getTickCount();
        while (!bankIsOpen) {

            Delay(rand100to175);

            if (t + 3 < client.getTickCount()) {

                if (!once) {
                    once = true;
                    MoveAndClick(boothTile[0], boothTile[1], false);
                    t = client.getTickCount();
                }

                if (once) {
                    once = false;
                    t = client.getTickCount();
                }
            }

            if(timeOut + 30 <= client.getTickCount()){
                log.debug("[x_o] Got stuck opening the bank");
                break;
            }
        }

        while (HelperWidget.bankDepositInventory.isHidden()) {
            Delay(rand45to75);
        }

        Delay(rand100to175);
    }

    public static void WaitForBankOpen(Client client, int ticksToWait, boolean stop) {
        if (stop) {
            return;
        }

        Delay(rand40to70);
        MoveAndClick(boothTile[0], boothTile[1], false);

        int t = client.getTickCount();
        while (!bankIsOpen) {

            Delay(rand100to175);

            if (t + ticksToWait < client.getTickCount()) {

                if (!once) {
                    once = true;
                    MoveAndClick(boothTile[0], boothTile[1], false);
                    t = client.getTickCount();
                }

                if (once) {
                    once = false;
                    t = client.getTickCount();
                }
            }

        }

        while (HelperWidget.bankDepositInventory.isHidden()) {
            Delay(rand45to75);
        }

        Delay(rand100to175);
    }

    public static void WaitForBankClose() {

        while (bankIsOpen) {
            Delay(rand35to70);
        }

        Delay(rand100to175);
    }
}

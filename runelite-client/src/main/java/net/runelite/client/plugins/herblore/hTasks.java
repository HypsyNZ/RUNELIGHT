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


import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.helpers.*;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.time.Instant;

import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetAndClick;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.helpers.HelperInput.*;
import static net.runelite.client.plugins.helpers.HelperRegion.WaitForBankClose;
import static net.runelite.client.plugins.helpers.HelperWidget.*;


public class hTasks implements Runnable {

    private final Client client;
    private final String nameOfTask;

    @Inject
    private ConfigManager configManager;

    @Inject
    private hConfig config;

    /**
     * Tasks to be completed by this plugin
     *
     * @param client     Instance of Client
     * @param nameOfTask Name of the Task
     * @param config     Instance of Config
     */
    hTasks(Client client, String nameOfTask, hConfig config) {
        this.client = client;
        this.nameOfTask = nameOfTask;
        this.config = config;
    }


    private void widgetItem(Widget widget, int index) {
        if (!config.RunAltarBuiltIn() || STOP) {
            return;
        }

        WidgetItem item = widget.getWidgetItem(index);
        if (item.getId() == config.herbs().getDirty()) {
            FindWidgetItemAndClick(item, client, true);
        }
    }

    private void widgetItemSlow(Widget widget, int index) {
        if (!config.RunAltarBuiltIn() || STOP) {
            return;
        }

        WidgetItem item = widget.getWidgetItem(index);
        if (item.getId() == config.herbs().getDirty()) {
            FindWidgetItemAndClick(item, client, false);
        }
    }

    public volatile static boolean STOP = false;

    public void run() {

        Widget inventoryWidget = HelperWidget.inventory;
        if (inventoryWidget != null) {

            switch (nameOfTask) {

                case "clean": {

                    Delay(rand20to40);

                    if (HelperRegion.currentInventoryTab != 3) {
                        HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
                        Delay(rand75to100);
                    }

                    boolean fail = false;
                    int ft = client.getTickCount();
                    while (inventory.getWidgetItem(0).getId() != config.herbs().getDirty()) {
                        Delay(rand75to100);
                        if (ft + 6 < client.getTickCount()) {
                            fail = true;
                        }
                    }

                    if (fail) {
                        return;
                    }

                    Delay(rand75to100);
                    widgetItemSlow(inventory, 0);

                    int i = 1;
                    while (i <= 27) {

                        widgetItem(inventory, i);

                        if (!config.RunAltarBuiltIn() || STOP) {
                            break;
                        }

                        i++;
                    }

                    if (!config.RunAltarBuiltIn() || STOP) {
                        return;
                    }

                    Delay(rand175to300);
                    Delay(rand75to100);

                    // Wait for Bank to Open
                    HelperRegion.WaitForBankOpen(client, STOP);

                    Delay(rand75to100);

                    FindWidgetAndClick(bankDepositInventory, client, true);

                }
                break;


                case "withdraw": {

                    if (config.modeToUse() == Mode.CLEAN) {

                        // Withdraw Grimy
                        Widget herbToClean = HelperBank.bWidgetByID(config.herbs().getDirty(), client);
                        if (herbToClean != null) {
                            FindWidgetAndClick(herbToClean, client, true);
                        } else {
                            STOP = true;
                            configManager.setConfiguration("h", "hAltarBuiltIn", false);
                            break;

                        }
                    }

                    if (config.modeToUse() == Mode.MIX) {


                        Widget herbToMix = HelperBank.bWidgetByID(config.herbs().getClean(), client);
                        Widget vialOfWater = HelperBank.bWidgetByID(ItemID.VIAL_OF_WATER, client);

                        if (herbToMix != null && vialOfWater != null) {

                            FindWidgetAndClick(herbToMix, client, false);
                            Delay(rand45to75);
                            FindWidgetAndClick(vialOfWater, client, true);

                        } else {
                            STOP = true;
                            configManager.setConfiguration("h", "hAltarBuiltIn", false);
                            return;
                        }

                    }

                    if (config.modeToUse() == Mode.FINISH) {

                        Widget unfPot = HelperBank.bWidgetByID(config.herbs().getUnfinished(), client);
                        Widget finishItem = HelperBank.bWidgetByID(config.herbs().getFinishItem(), client);

                        if (unfPot != null && finishItem != null) {

                            FindWidgetAndClick(unfPot, client, false);
                            Delay(rand45to75);
                            FindWidgetAndClick(finishItem, client, true);

                        } else {
                            STOP = true;
                            configManager.setConfiguration("h", "hAltarBuiltIn", false);
                            return;
                        }
                    }

                    Delay(rand125to200);

                    // Exit Bank
                    PressKeyRandom(KeyEvent.VK_ESCAPE);
                    WaitForBankClose();

                    Delay(rand20to40);

                }
                break;


                case "mix": {

                    Delay(rand175to300);

                    // Open Inventory If It is Closed
                    if (HelperRegion.currentInventoryTab != 3) {
                        HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
                    } else {
                        Delay(rand45to75);
                    }


                    WidgetItem lastHerb = null;
                    WidgetItem vialOfWater = null;

                    long startTime = Instant.now().toEpochMilli();
                    int ticks = client.getTickCount();
                    while (lastHerb == null || vialOfWater == null) {

                        for (WidgetItem iherb : inventory.getWidgetItems()) {
                            if (iherb.getId() == config.herbs().getClean()) {
                                lastHerb = iherb;
                            }
                        }

                        Delay(rand45to75);

                        for (WidgetItem i : inventory.getWidgetItems()) {
                            if (i.getId() == ItemID.VIAL_OF_WATER) {
                                vialOfWater = i;
                                break;
                            }
                        }

                        if (!config.RunAltarBuiltIn() || STOP) {
                            return;
                        }


                        if(startTime + 5000 < Instant.now().toEpochMilli() && ticks + 4 > client.getTickCount()){

                                Delay(rand175to300);

                                // Bank
                                HelperRegion.WaitForBankOpen(client, STOP);

                                Delay(rand175to300);

                                // Deposit
                                FindWidgetAndClick(bankDepositInventory, client, true);

                                Delay(rand35to70);

                                return;
                        }

                    }

                    Delay(rand25to50);
                    Delay(rand1to100);

                    FindWidgetItemAndClick(lastHerb, client, false);
                    Delay(rand45to75);
                    FindWidgetItemAndClick(vialOfWater, client, true);

                    Delay(rand25to50);
                    Delay(rand1to100);

                    long f1 = System.currentTimeMillis();
                    int randWindow = getRandomNumberInRange(1875, 2150);
                    ticks = client.getTickCount();
                    while (true) {
                        long f2 = System.currentTimeMillis();
                        boolean f = HelperWidget.dialogOpen || chatCraftOptionOne;

                        if (!config.RunAltarBuiltIn() || f || STOP) {
                            break;
                        }

                        // It should have been randomWindow amount of time and at least 2 ticks before we retry
                        if (f1 + randWindow < f2 && ticks + 2 > client.getTickCount()) {
                            f1 = System.currentTimeMillis();
                            ticks = client.getTickCount();

                                FindWidgetItemAndClick(lastHerb, client, false);
                                Delay(rand45to75);
                                FindWidgetItemAndClick(vialOfWater, client, true);

                        }
                    }

                    if (!config.RunAltarBuiltIn() || STOP) {
                        return;
                    }

                    // Space
                    Delay(rand300to900);
                    PressKeyRandom(KeyEvent.VK_SPACE);

                    // Check for Space
                    long s = System.currentTimeMillis();
                    int len = HelperInventory.CurrentInventoryLen;
                    while (len == 28) {
                        long s1 = System.currentTimeMillis();
                        if (!config.RunAltarBuiltIn() || STOP) {

                            break;
                        }
                        len = HelperInventory.CurrentInventoryLen;
                        if (s + rand900to1100 < s1) {
                            s = System.currentTimeMillis();
                            PressKeyRandom(KeyEvent.VK_SPACE);
                        }
                        Delay(rand35to70);
                    }


                    // Wait for pots
                    while (HelperInventory.CurrentInventoryLen > 14) {
                        if (!config.RunAltarBuiltIn() || STOP) {
                            break;
                        }
                        Delay(rand35to70);
                    }

                    if (!config.RunAltarBuiltIn() || STOP) {
                        return;
                    }

                    Delay(rand175to300);

                    // Bank
                    HelperRegion.WaitForBankOpen(client, STOP);

                    Delay(rand175to300);

                    // Deposit
                    FindWidgetAndClick(bankDepositInventory, client, true);


                    Delay(rand35to70);

                }
                break;

                case "finish": {

                    Delay(rand175to300);

                    // Open Inventory If It is Closed
                    if (HelperRegion.currentInventoryTab != 3) {
                        HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
                    } else {
                        Delay(rand45to75);
                    }


                    WidgetItem lastUnf = null;
                    WidgetItem finishItem = null;

                    long startTime = Instant.now().toEpochMilli();
                    int ticks = client.getTickCount();
                    while (lastUnf == null || finishItem == null) {

                        for (WidgetItem unfPot : inventory.getWidgetItems()) {
                            if (unfPot.getId() == config.herbs().getUnfinished()) {
                                lastUnf = unfPot;
                            }
                        }

                        Delay(rand45to75);

                        for (WidgetItem finItem : inventory.getWidgetItems()) {
                            if (finItem.getId() == config.herbs().getFinishItem()) {
                                finishItem = finItem;
                                break;
                            }
                        }

                        if (!config.RunAltarBuiltIn() || STOP) {
                            break;
                        }

                        if(startTime + 5000 < Instant.now().toEpochMilli() && ticks + 4 > client.getTickCount()){

                            Delay(rand175to300);

                            // Bank
                            HelperRegion.WaitForBankOpen(client, STOP);

                            Delay(rand175to300);

                            // Deposit
                            FindWidgetAndClick(bankDepositInventory, client, true);

                            Delay(rand35to70);

                            return;
                        }
                    }

                    Delay(rand25to50);
                    Delay(rand1to100);

                    if (lastUnf != null && finishItem != null) {
                        FindWidgetItemAndClick(lastUnf, client, false);
                        Delay(rand45to75);
                        FindWidgetItemAndClick(finishItem, client, true);
                    } else {
                        STOP = true;
                    }

                    Delay(rand25to50);
                    Delay(rand1to100);

                    long f1 = System.currentTimeMillis();
                    int randWindow = getRandomNumberInRange(1875, 2150);

                    ticks = client.getTickCount();
                    while (true) {
                        long f2 = System.currentTimeMillis();
                        boolean f = HelperWidget.dialogOpen || chatCraftOptionOne;

                        if (!config.RunAltarBuiltIn() || f || STOP) {
                            break;
                        }

                        // It should have been randomWindow amount of time and at least 2 ticks before we retry
                        if (f1 + randWindow < f2 && ticks + 2 > client.getTickCount()) {
                            f1 = System.currentTimeMillis();
                            ticks = client.getTickCount();

                            if (lastUnf != null && finishItem != null) {
                                FindWidgetItemAndClick(lastUnf, client, false);
                                Delay(rand45to75);
                                FindWidgetItemAndClick(finishItem, client, true);
                            } else {
                                STOP = true;
                            }

                        }
                    }

                    if (!config.RunAltarBuiltIn() || STOP) {
                        return;
                    }

                    // Space
                    Delay(rand300to900);
                    PressKeyRandom(KeyEvent.VK_SPACE);

                    // Check for Space
                    long s = System.currentTimeMillis();
                    int len = HelperInventory.CurrentInventoryLen;
                    while (len == 28) {
                        long s1 = System.currentTimeMillis();
                        if (!config.RunAltarBuiltIn() || STOP) {

                            break;
                        }
                        len = HelperInventory.CurrentInventoryLen;
                        if (s + rand900to1100 < s1) {
                            s = System.currentTimeMillis();
                            PressKeyRandom(KeyEvent.VK_SPACE);
                        }
                        Delay(rand35to70);
                    }


                    // Wait for pots
                    while (HelperInventory.CurrentInventoryLen > 14) {
                        if (!config.RunAltarBuiltIn() || STOP) {
                            break;
                        }
                        Delay(rand35to70);
                    }

                    if (!config.RunAltarBuiltIn() || STOP) {
                        return;
                    }

                    Delay(rand175to300);

                    // Bank
                    HelperRegion.WaitForBankOpen(client, STOP);

                    Delay(rand175to300);

                    // Deposit
                    FindWidgetAndClick(bankDepositInventory, client, true);


                    Delay(rand35to70);

                }
                break;

            }
        }


    }

}
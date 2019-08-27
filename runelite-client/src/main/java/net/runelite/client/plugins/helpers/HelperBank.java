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

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.util.List;

import static net.runelite.client.plugins.helpers.HelperWidget.bank;

@Slf4j
public class HelperBank {

    private final Client client;
    private final ItemManager itemManager;

    @Inject
    public HelperBank(ItemManager itemManager, Client client) {
        this.client = client;
        this.itemManager = itemManager;
    }

    /**
     * @param Octagon An octagonal list containing all items you want to track in the bank
     *                <p>
     *                String = Item Name
     *                Integer = Item ID
     *                Integer = GE Price
     *                Integer = Bank Price
     *                Widget = Item Widget
     *                Integer = ItemX
     *                Integer = ItemY
     *                Integer = ItemQuantity
     * @return Updated List
     */
    public List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> CheckItemAvailableInBank(List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> Octagon) {

        ItemContainer bankInventory = client.getItemContainer(InventoryID.BANK);
        if (bankInventory == null) {
            log.debug("Bank Item Container was Null, Returned unchanged Octagonal List");
            return Octagon;
        }

        if (bank == null) {
            log.debug("Bank Widget was Null, Returned unchanged Octagonal List");
            return Octagon;
        }

        Widget[] sb = bank.getDynamicChildren();
        if (sb == null) {
            log.debug("Bank Children were Null, Returned unchanged Octagonal List");
            return Octagon;
        }

        // Reset
        for (Widget ss : sb) {
            for (int i2 = 0; i2 < Octagon.size(); i2++) {
                Octagon.set(i2, new Octagon<>(Octagon.get(i2).getFirst(), Octagon.get(i2).getSecond(), 0, 0, null, 0, 0, 0));
            }
        }

        HelperWidget.totalBankValue = 0;

        // Update
        for (Widget ss : sb) {
            int id = ss.getItemId();

            for (int i2 = 0; i2 < Octagon.size(); i2++) {
                if (id == Octagon.get(i2).getSecond()) {

                    int quantity = ss.getItemQuantity();
                    int gePrice = itemManager.getItemPrice(Octagon.get(i2).getSecond());
                    int value = gePrice * quantity;

                    Octagon.set(i2, new Octagon<>(
                            Octagon.get(i2).getFirst(),    // item name
                            Octagon.get(i2).getSecond(),   // item id
                            gePrice,                         // ge price
                            value,                           // bank price
                            ss,                              // item widget
                            0,                           // x
                            0,                           // y
                            quantity                         // quantity
                    ));
                }
            }

        }
        return Octagon;
    }

    /**
     * Get a Widget that represents and item in the bank using the Bank Widget
     *
     * @param ItemID of the item you want
     * @return widget of the item or null
     */
    public static Widget bWidgetByID(int ItemID, Client client) {

        bank = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        if (bank == null) {
            log.debug("Failed to Find a Widget by its ID from WidgetInfo.BANK_ITEM_CONTAINER:" + ItemID);
            return null;
        }

        Widget[] sb = bank.getDynamicChildren();
        if (sb == null) {
            log.debug("Failed to load dynamic children from WidgetInfo.BANK_ITEM_CONTAINER:" + ItemID);
            return null;
        }

        for (Widget ss : sb) {
            int id = ss.getItemId();
            if (id == ItemID) {
                log.debug("Return item from bank container id: " +ss.getId() + " | name: " + ss.getName() + " | x: " + ss.getBounds().x  + " | y: " + ss.getBounds().y);
                return ss;
            }
        }

        log.info("Failed to iterate over children in WidgetInfo.BANK_ITEM_CONTAINER");
        return null;
    }

    /**
     * Get a Bank Item Widget by Item ID from the List
     *
     * @param itemID     Item ID
     * @param gItemsList The Inventory List
     * @return Item Widget
     */
    public static Widget bWidgetByID(int itemID, List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> gItemsList) {
        int i2 = 0;
        while (i2 < gItemsList.size()) {
            int ItemID = gItemsList.get(i2).getSecond();
            if (itemID == ItemID) {
                return gItemsList.get(i2).getFifth();
            }
            i2++;
        }
        return null;
    }

    /**
     * Get a Bank Item Quantity from its Widget or return 0
     * @param item the bank items widget
     * @return quantity in bank or 0
     */
    public static int bQuanByWidget(Widget item){
        return item != null ? item.getItemQuantity() : 0;
    }

    /**
     * Get an Bank Item Value by its Item ID
     *
     * @param itemID     Item ID
     * @param gItemsList The Bank List
     * @return Total Bank Value of Item
     */
    public static int bValueByID(int itemID, List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> gItemsList) {
        int i2 = 0;
        while (i2 < gItemsList.size()) {
            int ItemID = gItemsList.get(i2).getSecond();
            if (itemID == ItemID) {
                return gItemsList.get(i2).getFourth();
            }
            i2++;
        }
        log.info("Failed to get ValueByID from Octagonal List that represents the Bank for ItemID: " + itemID);
        return 0;
    }

}

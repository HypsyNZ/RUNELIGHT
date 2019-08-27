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

import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.runelite.client.plugins.helpers.HelperWidget.inventory;

public class HelperInventory {

    private final Client client;
    private final ItemManager itemManager;

    @Inject
    public HelperInventory(ItemManager itemManager, Client client) {
        this.client = client;
        this.itemManager = itemManager;
    }

    /**
     * Current Inventory Length
     */
    public static int CurrentInventoryLen = 0;

    /**
     * @param Octagon An octagonal list containing all items you want to track in the inventory
     *                <p>
     *                String = Item Name
     *                Integer = Item ID
     *                Integer = GE Price
     *                Integer = Bank Price
     *                Widget = Item Widget ( UNUSED )
     *                Integer = ItemX
     *                Integer = ItemY
     *                Integer = ItemQuantity
     * @return Updated List
     */
    public List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> CheckItemAvailableInInventory(List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> Octagon) {
        ItemContainer inv = client.getItemContainer(InventoryID.INVENTORY);

        if (inv == null) {
            return Octagon;
        }

        if (inventory == null) {
            return Octagon;
        }

        Item[] items = inv.getItems();

        // Count
        CurrentInventoryLen = items.length;
        for (Item item : items) {
            if (item.getId() <= 0) {
                CurrentInventoryLen = CurrentInventoryLen - 1;
            }
        }

        if (!isInventoryDifferent(items)) {
            return Octagon;
        }


        // Reset
        for (WidgetItem item : inventory.getWidgetItems()) {
            for (int i2 = 0; i2 < Octagon.size(); i2++) {
                Octagon.set(i2, new Octagon<>(Octagon.get(i2).getFirst(), Octagon.get(i2).getSecond(), 0, 0, null, 0, 0, 0));
            }
        }

        // Update
        for (WidgetItem item : inventory.getWidgetItems()) {
            int id = item.getId();

            for (int i2 = 0; i2 < Octagon.size(); i2++) {
                if (id == Octagon.get(i2).getSecond()) {

                    int quantity = item.getQuantity();
                    int gePrice = itemManager.getItemPrice(Octagon.get(i2).getSecond());
                    int value = gePrice * quantity;

                    Rectangle rect = item.getCanvasBounds();
                    int[] xy = HelperTransform.xyFromEdge(rect, client);
                    if (xy[0] != 0) {

                        Octagon.set(i2, new Octagon<>(
                                Octagon.get(i2).getFirst(),    // item name
                                Octagon.get(i2).getSecond(),   // item id
                                gePrice,                         // ge price
                                value,                           // inventory price
                                Octagon.get(i2).getFifth(),    // boolean ! Unused
                                xy[0],                           // x
                                xy[1],                           // y
                                quantity                         // quantity
                        ));

                    }
                }
            }

        }
        return Octagon;
    }

    /**
     * Get an Inventory Item Clickable XY by its Item ID
     *
     * @param itemID     Item ID
     * @param gItemsList The Inventory List
     * @return X[0], Y[1]
     */
    public static int[] InventoryItemXYbyID(int itemID, List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> gItemsList) {
        int i2 = 0;
        while (i2 < gItemsList.size()) {
            int ItemID = gItemsList.get(i2).getSecond();
            if (itemID == ItemID) {
                int x = gItemsList.get(i2).getSixth();
                int y = gItemsList.get(i2).getSeventh();
                return new int[]{x, y};
            }
            i2++;
        }
        return new int[]{0, 0};
    }

    /**
     * Get an Inventory Item Quantity by its Item ID
     *
     * @param itemID     Item ID
     * @param gItemsList The Inventory List
     * @return Item Quantity (Stackable Only)
     */
    public static int InventoryItemQuantityByID(int itemID, List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> gItemsList) {
        int i2 = 0;
        while (i2 < gItemsList.size()) {
            int ItemID = gItemsList.get(i2).getSecond();
            if (itemID == ItemID) {
                return gItemsList.get(i2).getEighth();
            }
            i2++;
        }
        return 0;
    }

    /**
     * Get an Inventory Item Value by its Item ID
     *
     * @param itemID     Item ID
     * @param gItemsList The Inventory List
     * @return Total Inventory Value of Item (Stackable only)
     */
    public static int InventoryItemValueByID(int itemID, List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> gItemsList) {
        int i2 = 0;
        while (i2 < gItemsList.size()) {
            int ItemID = gItemsList.get(i2).getSecond();
            if (itemID == ItemID) {
                return gItemsList.get(i2).getFourth();
            }
            i2++;
        }
        return 0;
    }

    private int itemsHash;

    private boolean isInventoryDifferent(Item[] items) {

        Map<Integer, Integer> mapCheck = new HashMap<>();

        for (Item item : items) {
            mapCheck.put(item.getId(), item.getQuantity());
        }

        int curHash = mapCheck.hashCode();

        if (curHash != itemsHash) {
            itemsHash = curHash;
            return true;
        }

        return false;
    }

}

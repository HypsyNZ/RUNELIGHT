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

import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.helpers.Octagon;

import java.util.ArrayList;
import java.util.List;

public class hItems {
    private static void Items() {
        hItemsListBank = new ArrayList<>();
        hItemsListInventory = new ArrayList<>();

        hItemsListBank.add(new Octagon<>("Grimy Ranarr ", ItemID.GRIMY_RANARR_WEED, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Grimy Snapdragon ", ItemID.GRIMY_SNAPDRAGON, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Grimy Toadflax ", ItemID.GRIMY_TOADFLAX, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Grimy Kwuarm ", ItemID.GRIMY_KWUARM, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Grimy Cadantine ", ItemID.GRIMY_CADANTINE, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Grimy Avantoe ", ItemID.GRIMY_AVANTOE, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Grimy Irit ", ItemID.GRIMY_IRIT_LEAF, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Snapdragon ", ItemID.SNAPDRAGON, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Ranarr ", ItemID.RANARR_WEED, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Toadflax ", ItemID.TOADFLAX, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Kwuarm ", ItemID.KWUARM, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Cadantine ", ItemID.CADANTINE, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Avantoe ", ItemID.AVANTOE, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Irit ", ItemID.IRIT_LEAF, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Vial of Water ", ItemID.VIAL_OF_WATER, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Vial of Water ", ItemID.VIAL_OF_WATER_9086, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Ranarr Potion (unf) ", ItemID.RANARR_POTION_UNF, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Snapdragon Pot (unf) ", ItemID.SNAPDRAGON_POTION_UNF, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Toadflax Potion (unf) ", ItemID.TOADFLAX_POTION_UNF, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Kwuarm Pot (unf) ", ItemID.KWUARM_POTION_UNF, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Cadantine Pot (unf) ", ItemID.CADANTINE_POTION_UNF, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Avantoe Pot (unf) ", ItemID.AVANTOE_POTION_UNF, 0, 0, null, 0, 0, 0));
        hItemsListBank.add(new Octagon<>("Irit Pot (unf) ", ItemID.IRIT_POTION_UNF, 0, 0, null, 0, 0, 0));

        hItemsListInventory = hItemsListBank;
    }

    static void initItems() {
        Items();
    }

    static List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> hItemsListBank = new ArrayList<>();
    static List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> hItemsListInventory = new ArrayList<>();

}

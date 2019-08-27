package net.runelite.client.plugins.herblore;

import net.runelite.api.ItemID;

import javax.annotation.Nullable;

public enum Herbs {
    RANARR(ItemID.GRIMY_RANARR_WEED, ItemID.RANARR_WEED, ItemID.RANARR_POTION_UNF, ItemID.SNAPE_GRASS),
    SNAPDRAGON(ItemID.GRIMY_SNAPDRAGON, ItemID.SNAPDRAGON,ItemID.SNAPDRAGON_POTION_UNF, ItemID.RED_SPIDERS_EGGS),
    TOADFLAX(ItemID.GRIMY_TOADFLAX, ItemID.TOADFLAX,ItemID.TOADFLAX_POTION_UNF, ItemID.TOADS_LEGS),
    KWUARM(ItemID.GRIMY_KWUARM, ItemID.KWUARM,ItemID.KWUARM_POTION_UNF, ItemID.LIMPWURT_ROOT),
    CADANTINE(ItemID.GRIMY_CADANTINE, ItemID.CADANTINE,ItemID.CADANTINE_POTION_UNF, ItemID.WHITE_BERRIES),
    AVANTOE(ItemID.GRIMY_AVANTOE, ItemID.AVANTOE,ItemID.AVANTOE_POTION_UNF,ItemID.SNAPE_GRASS),
    IRIT(ItemID.GRIMY_IRIT_LEAF, ItemID.IRIT_LEAF,ItemID.IRIT_POTION_UNF, ItemID.EYE_OF_NEWT),
    TORSTOL(ItemID.GRIMY_TORSTOL, ItemID.TORSTOL,ItemID.TORSTOL_POTION_UNF, ItemID.JANGERBERRIES),
    LANTADYME(ItemID.GRIMY_LANTADYME, ItemID.LANTADYME,ItemID.LANTADYME_POTION_UNF, ItemID.POTATO_CACTUS),
    DWARF_WEED(ItemID.GRIMY_DWARF_WEED, ItemID.DWARF_WEED,ItemID.DWARF_WEED_POTION_UNF, ItemID.WINE_OF_ZAMORAK),
    HARRALANDER(ItemID.GRIMY_HARRALANDER, ItemID.HARRALANDER,ItemID.HARRALANDER_POTION_UNF, ItemID.VOLCANIC_ASH);

    private final Integer dirty;
    private final Integer clean;
    private final Integer unfinished;
    private final Integer finishItem;

    Herbs(Integer dirty, Integer clean, Integer unfinished, Integer finishItem) {
        this.dirty = dirty;
        this.clean = clean;
        this.unfinished = unfinished;
        this.finishItem = finishItem;
    }

    public Integer getDirty() {
        return dirty;
    }

    public Integer getClean() {
        return clean;
    }

    public Integer getUnfinished() {
        return unfinished;
    }

    public Integer getFinishItem() {
        return finishItem;
    }
}


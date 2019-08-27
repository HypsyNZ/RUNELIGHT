package net.runelite.client.plugins.helpers;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.Map;

@Getter
public enum Booth {
    CANAFIS(24347),
    SEERS(25808),
    FALADOR(24101, 27253),
    EDGEVILLE(10355),
    KELDAGRIM(6084),
    VARROCK(10583, 34810),
    ARDOURNGE(10356),
    LUNAR(16700);

    public final int[] boothID;

    Booth(int... boothID) {
        this.boothID = boothID;
    }

    private static final Map<Integer, Booth> BOOTHS;

    static {
        ImmutableMap.Builder<Integer, Booth> builder = new ImmutableMap.Builder<>();

        for (Booth booth : values()) {
            for (int treeId : booth.boothID) {
                builder.put(treeId, booth);
            }
        }

        BOOTHS = builder.build();
    }

    static Booth findBooth(int objectId) {
        return BOOTHS.get(objectId);
    }
}

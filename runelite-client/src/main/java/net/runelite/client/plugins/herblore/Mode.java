package net.runelite.client.plugins.herblore;

public enum Mode {

    MIX(1),
    CLEAN(2),
    FINISH(3);

    private final Integer value;

    Mode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
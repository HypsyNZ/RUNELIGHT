package net.runelite.client.plugins.simpleclick;

import java.awt.event.KeyEvent;

public enum KeyEventKey{
    END(KeyEvent.VK_END),
    HOME(KeyEvent.VK_HOME),
    INSERT(KeyEvent.VK_INSERT),
    DELETE(KeyEvent.VK_DELETE),
    PAGEUP(KeyEvent.VK_PAGE_UP),
    PAGEDOWN(KeyEvent.VK_PAGE_DOWN),
    CAPSLOCK(KeyEvent.VK_CAPS_LOCK),
    TAB(KeyEvent.VK_TAB),
    ONE(KeyEvent.VK_1),
    TWO(KeyEvent.VK_2),
    THREE(KeyEvent.VK_3),
    FOUR(KeyEvent.VK_4),
    FIVE(KeyEvent.VK_5),
    SIX(KeyEvent.VK_6),
    SEVEN(KeyEvent.VK_7),
    EIGHT(KeyEvent.VK_8),
    NINE(KeyEvent.VK_9),
    ZERO(KeyEvent.VK_0),
    F12(KeyEvent.VK_F12),
    F11(KeyEvent.VK_F11),
    F10(KeyEvent.VK_F10),
    F9(KeyEvent.VK_F9),
    F8(KeyEvent.VK_F8),
    F7(KeyEvent.VK_F7),
    F6(KeyEvent.VK_F6),
    F5(KeyEvent.VK_F5),
    F4(KeyEvent.VK_F4),
    F3(KeyEvent.VK_F3),
    F2(KeyEvent.VK_F3),
    F1(KeyEvent.VK_F3),
    NUM1(KeyEvent.VK_NUMPAD1),
    NUM2(KeyEvent.VK_NUMPAD2),
    NUM3(KeyEvent.VK_NUMPAD3),
    NUM4(KeyEvent.VK_NUMPAD4),
    NUM5(KeyEvent.VK_NUMPAD5),
    NUM6(KeyEvent.VK_NUMPAD6),
    NUM7(KeyEvent.VK_NUMPAD7),
    NUM8(KeyEvent.VK_NUMPAD8),
    NUM9(KeyEvent.VK_NUMPAD9),
    SHIFT(KeyEvent.VK_SHIFT),
    ALT(KeyEvent.VK_ALT),
    CTRL(KeyEvent.VK_CONTROL);

    private final Integer value;

    KeyEventKey(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
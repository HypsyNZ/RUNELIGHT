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

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.Executors;

import static net.runelite.client.plugins.helpers.HelperDelay.rand125to200;
import static net.runelite.client.plugins.helpers.HelperDelay.rand20to40;
import static net.runelite.client.plugins.helpers.HelperPlugin.helperMotionFactory;
import static net.runelite.client.plugins.helpers.HelperPlugin.helperMotionFactoryFast;

@Slf4j
public class HelperInput {

    private volatile static boolean holdDownKey = false;
    private volatile static boolean holdDownKeyOnce = false;

    private static Robot r;

    static {
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }


    /**
     * Wait for a set delay using the Input Robot
     *
     * @param delay the amount of time to wait in Milliseconds
     */
    public static void Delay(int delay) {
        r.delay(delay);
    }


    /**
     * Press a key at normal speed using Input Robot
     *
     * @param key the KeyEvent key to press
     */
    public static void PressKeyRandom(int key) {
        r.keyPress(key);
        Delay(HelperDelay.rand25to50);
        r.keyRelease(key);
        Delay(HelperDelay.rand25to50);
    }

    /**
     * Mouse mouse using the MotionFactory and then Click
     *
     * @param x x-co-ordinate
     * @param y y-co-ordinate
     */
    public static void MoveAndClick(int x, int y, boolean fast) {

        if (x < 50 && y < 50) {
            return;
        }

        try {
            if (fast) {

                //log.info("Moving Mouse FAST to x: " + x + " y: " + y);
                helperMotionFactoryFast.moveClick(x, y);

            } else {

                //log.info("Moving Mouse SLOW to x: " + x + " y: " + y);
                helperMotionFactory.moveClick(x, y);

            }
            //log.info("Assuming we moved mouse with helperMotionFactory");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }



    }

    public static void MoveMouse(int x, int y, boolean fast) {

        if (x < 50 && y < 50) {
            return;
        }

        try {
            if (fast) {

                //log.info("Moving Mouse FAST to x: " + x + " y: " + y);
                helperMotionFactoryFast.move(x, y);

            } else {

                //log.info("Moving Mouse SLOW to x: " + x + " y: " + y);
                helperMotionFactory.move(x, y);

            }
            //log.info("Assuming we moved mouse with helperMotionFactory");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Click Left at normal speed using Input Robot, No Move
     */
    public static void Click() {
        Delay(rand20to40);
        r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Delay(rand20to40);
        r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Delay(rand20to40);
    }

//
//    /**
//     * Move mouse at set speed using Berstein polynomials
//     */
//    public static void MoveMouseBersteinPolynominal(int endX, int endY) {
//
//        Point end = new Point(endX, endY);
//
//        mouseLocationOnScreen = MouseInfo.getPointerInfo();
//        int x1 = HelperWidget.mouseLocationOnScreen.getLocation().x;
//        int y1 = HelperWidget.mouseLocationOnScreen.getLocation().y;
//
//        Point s = new Point(x1, y1);
//
//        Point[] cooardList;
//        double t;    //the time interval
//        double k = .025;
//        cooardList = new Point[4];
//
//        //set the beginning and end points
//        cooardList[0] = s;
//        cooardList[3] = new Point(end.x, end.y);
//
//        int xout = Math.abs(end.x - s.x) / 10;
//        int yout = Math.abs(end.y - s.y) / 10;
//        int x, y;
//
//        x = s.x < end.x
//                ? s.x + ((xout > 0) ? getRandomNumberInRange(1, xout) : 1)
//                : s.x - ((xout > 0) ? getRandomNumberInRange(1, xout) : 1);
//        y = s.y < end.y
//                ? s.y + ((yout > 0) ? getRandomNumberInRange(1, yout) : 1)
//                : s.y - ((yout > 0) ? getRandomNumberInRange(1, yout) : 1);
//        cooardList[1] = new Point(x, y);
//
//        x = end.x < s.x
//                ? end.x + ((xout > 0) ? getRandomNumberInRange(1, xout) : 1)
//                : end.x - ((xout > 0) ? getRandomNumberInRange(1, xout) : 1);
//        y = end.y < s.y
//                ? end.y + ((yout > 0) ? getRandomNumberInRange(1, yout) : 1)
//                : end.y - ((yout > 0) ? getRandomNumberInRange(1, yout) : 1);
//        cooardList[2] = new Point(x, y);
//
//        double px, py;
//        int speed = getRandomNumberInRange(5, 10);
//        for (t = k; t <= 1 + k; t += k) {
//
//            px = (cooardList[0].x + t * (-cooardList[0].x * 3 + t * (3 * cooardList[0].x - cooardList[0].x * t)))
//                    + t * (3 * cooardList[1].x + t * (-6 * cooardList[1].x + cooardList[1].x * 3 * t))
//                    + t * t * (cooardList[2].x * 3 - cooardList[2].x * 3 * t) + cooardList[3].x * t * t * t;
//
//            py = (cooardList[0].y + t * (-cooardList[0].y * 3 + t * (3 * cooardList[0].y - cooardList[0].y * t)))
//                    + t * (3 * cooardList[1].y + t * (-6 * cooardList[1].y + cooardList[1].y * 3 * t))
//                    + t * t * (cooardList[2].y * 3 - cooardList[2].y * 3 * t) + cooardList[3].y * t * t * t;
//
//
//            r.mouseMove((int) px, (int) py);
//
//            r.delay(getRandomNumberInRange(speed, speed * 2));
//        }
//    }
//
//    /**
//     * Move mouse at normal speed using Fitts Law
//     */
//    public static void MoveFittsLaw(int x, int y) {
//
//        if (HelperWidget.shouldStop) {
//            return;
//        }
//
//        nSteps = getRandomNumberInRange(100, 150);
//
//        HelperFind.mouseMoved = lastxMouse != mouseLocationOnScreen.getLocation().x || lastyMouse != mouseLocationOnScreen.getLocation().y;
//
//        PointerInfo mouseLocationOnScreen = MouseInfo.getPointerInfo();
//        int x1 = mouseLocationOnScreen.getLocation().x;
//        int y1 = mouseLocationOnScreen.getLocation().y;
//        lastxMouse = x1;
//        lastyMouse = y1;
//
//        if(x1 == x || y1 == y){
//            return;
//        }
//
//        double dx = (x - x1) / ((double) nSteps);
//        double dy = (y - y1) / ((double) nSteps);
//
//        stepSizeX = (int) dx;
//        stepSizeY = (int) dy;
//
//        int stepX;
//        int stepY;
//        int a = 0;
//        int uwu = getRandomNumberInRange(1, 20);
//        for (int step = 1; step <= nSteps; step++) {
//            log.info(Integer.toString(a));
//            if (step > nSteps % 90 - uwu) {
//
//                dx = (x - x1 + a) / ((double) nSteps);
//                dy = (y - y1 + a) / ((double) nSteps);
//
//                stepX = (int) (x1 + dx * step);
//                stepY = (int) (y1 + dy * step);
//
//                a--;
//
//                //70
//            }
//            else if (step > nSteps % 80 - uwu * 2) {
//
//                dx = (x - x1 + a) / ((double) nSteps);
//                dy = (y - y1 + a) / ((double) nSteps);
//
//                stepX = (int) (x1 + dx * step);
//                stepY = (int) (y1 + dy * step);
//
//
//                a++;
//
//            }
//            else {
//
//                stepX = (int) (x1 + dx * step);
//                stepY = (int) (y1 + dy * step);
//
//            }
//
//            r.delay(1);
//            r.mouseMove(stepX, stepY);
//
//        }
//
//
//        HelperFind.timeSinceMove = System.currentTimeMillis();
//        r.delay(rand25to50);
//    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            log.info("Min was greater than Max while generating a Random Number");
            return 0;
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    /**
     * Click Right at normal speed using Input Robot
     *
     * @param x X co-ords to Right click
     * @param y Y co-ords to Right click
     */
    public static void ClickRight(int x, int y) {
        r.mouseMove(x, y);
        r.delay(HelperDelay.rand45to75);
        r.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        r.delay(HelperDelay.rand25to50);
        r.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        r.delay(HelperDelay.rand25to50);
    }


    /**
     * Click Right and then Left Click on a Menu menuOption
     *
     * @param x               X co-ords to Right click
     * @param y               Y co-ords to Right click
     * @param menuOptionAsInt Menu menuOption as int (1, 5, 10, all = 999)
     */
    public static void ClickRightLeft(int x, int y, int menuOptionAsInt) {
        switch (menuOptionAsInt) {

            case 1: {
                r.delay(HelperDelay.rand45to75);
                ClickRight(x, y);
                r.delay(rand125to200);

                int xr2 = getRandomNumberInRange(x - 30, x + 30);
                int yr1 = getRandomNumberInRange(y - 2, y + 2);
                MoveAndClick(xr2, yr1 + 65, true);

                r.delay(HelperDelay.rand10to100);
                break;
            }


            case 5: {
                r.delay(HelperDelay.rand45to75);
                ClickRight(x, y);
                r.delay(rand125to200);

                int xr2 = getRandomNumberInRange(x - 30, x + 30);
                int yr1 = getRandomNumberInRange(y - 2, y + 2);
                MoveAndClick(xr2, yr1 + 85, true);

                r.delay(HelperDelay.rand10to100);
                break;
            }

            case 10: {
                r.delay(HelperDelay.rand45to75);
                ClickRight(x, y);
                r.delay(rand125to200);

                int xr2 = getRandomNumberInRange(x - 30, x + 30);
                int yr1 = getRandomNumberInRange(y - 2, y + 2);
                MoveAndClick(xr2, yr1 + 105, true);

                r.delay(HelperDelay.rand10to100);
                break;
            }

            case 999: {
                r.delay(HelperDelay.rand45to75);
                ClickRight(x, y);
                r.delay(rand125to200);

                int xr2 = getRandomNumberInRange(x - 30, x + 30);
                int yr1 = getRandomNumberInRange(y - 2, y + 2);
                MoveAndClick(xr2, yr1 + 45, true);

                r.delay(HelperDelay.rand10to100);
                break;
            }


        }
    }


    /**
     * HoldKeyListener for Holding Keys Down
     *
     * <p>
     * HoldKeyDown() = Hold Down the current HoldKeyListener()
     * HoldKeyRelease() = Release the current HoldKeyListener()
     * </P>
     *
     * @param keyToHold the key to start holding down after HoldKeyDown() is called
     */
    public static void HoldKeyListener(int keyToHold) {

        if (holdDownKey && !holdDownKeyOnce) {
            holdDownKeyOnce = true;
            hkdr(keyToHold);
        }
    }


    /**
     * Holds down the HoldKey() being looped by Robot
     */
    public static void HoldKeyDown() {
        holdDownKey = true;
        holdDownKeyOnce = false;
    }


    /**
     * Releases the HoldKey() being held down with HoldKeyDown()
     */
    public static void HoldKeyRelease() {
        if (holdDownKey && holdDownKeyOnce) {
            holdDownKeyOnce = false;
            holdDownKey = false;
        }
    }


    /**
     * HoldKeyDown Robot
     */
    private static void hkdr(int keyToHold) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Robot rs = new Robot();

                holdDownKey = true;

                // Press Key
                rs.keyPress(keyToHold);

                // Wait Until holdDownKey is false
                while (holdDownKey) {
                    rs.delay(10);
                }

                // Release Key
                rs.keyRelease(keyToHold);

            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
    }

    public enum KeyToPress {
        END(KeyEvent.VK_END),
        HOME(KeyEvent.VK_HOME),
        PAGEUP(KeyEvent.VK_PAGE_UP),
        PAGEDOWN(KeyEvent.VK_PAGE_DOWN),
        CAPSLOCK(KeyEvent.VK_CAPS_LOCK),
        TAB(KeyEvent.VK_TAB),
        ONE(KeyEvent.VK_1),
        TWO(KeyEvent.VK_2),
        THREE(KeyEvent.VK_3),
        FOUR(KeyEvent.VK_4),
        F12(KeyEvent.VK_F12),
        F11(KeyEvent.VK_F11),
        F10(KeyEvent.VK_F10),
        F9(KeyEvent.VK_F9),
        F8(KeyEvent.VK_F8),
        F7(KeyEvent.VK_F7),
        F6(KeyEvent.VK_F6),
        F5(KeyEvent.VK_F5),
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

        KeyToPress(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}

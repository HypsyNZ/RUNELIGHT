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
package net.runelite.client.plugins.quickswitch;

import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

public class QuickSwitchInputListener implements KeyListener {

    private ThreadMain t;
    private QuickSwitchConfig quickSwitchConfig;

    @Inject
    QuickSwitchInputListener(ThreadMain t, QuickSwitchConfig quickSwitchConfig) {
        this.t = t;
        this.quickSwitchConfig = quickSwitchConfig;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = quickSwitchConfig.keyForSwap().getValue();
        if (e.getKeyCode() == key) {


            if (quickSwitchConfig.swapTabsPrayInventory()) {


                // Prayer Tab
                if (QuickSwitchOverlay.invtab == 3) {
                    RobotPressKey RPK = new RobotPressKey(KeyEvent.VK_F5);
                    t.tThread = new Thread(RPK);
                    t.tThread.start();
                }

                // Inventory
                else if (QuickSwitchOverlay.invtab == 5) {
                    RobotPressKey RPK = new RobotPressKey(KeyEvent.VK_ESCAPE);
                    t.tThread = new Thread(RPK);
                    t.tThread.start();
                }


                // Open Inventory Backup
                else if (quickSwitchConfig.fallBackInventory()) {
                    RobotPressKey RPK = new RobotPressKey(KeyEvent.VK_ESCAPE);
                    t.tThread = new Thread(RPK);
                    t.tThread.start();
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}


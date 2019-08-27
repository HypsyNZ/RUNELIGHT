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
package net.runelite.client.plugins.simpleclick;

import net.runelite.client.plugins.helpers.HelperInput;

import static net.runelite.client.plugins.helpers.HelperInput.Delay;
import static net.runelite.client.plugins.simpleclick.SimpleClickPlugin.getRandom;

class SimpleClickTasks implements Runnable {

    private final String nameOfTask;
    private final SimpleClickPlugin plugin;

    /**
     * Tasks to be completed by this plugin
     *
     * @param nameOfTask Name of the Task
     */
    SimpleClickTasks(String nameOfTask, SimpleClickPlugin plugin) {
        this.nameOfTask = nameOfTask;
        this.plugin = plugin;
    }

    public void run() {

        switch (nameOfTask) {

            /* 1 */
            case "click": {

                while (plugin.getStart()) {

                    HelperInput.Click();

                    int delayTime = getRandom(plugin.getCurrentMin(), plugin.getCurrentMax());
                    int passed = 0;

                    // Stop on demand
                    while (passed < delayTime) {

                        Delay(1);
                        if (!plugin.getStart()) {
                            break;
                        }

                        passed++;
                    }
                }

            }
            break;

            /* Any number of named tasks from TaskMan */

        }


    }

}
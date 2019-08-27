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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.helpers.HelperInput;


@ConfigGroup("h")
public interface hConfig extends Config {

    @ConfigItem(
            keyName = "runKey",
            name = "Run Key",
            description = "The key to start the herb plugin",
            position = 1
    )
    default HelperInput.KeyToPress keyForSwap() {
        return HelperInput.KeyToPress.END;
    }


    @ConfigItem(
            keyName = "herbToClean",
            name = "Herb to Use",
            description = "The herb to use",
            position = 2
    )
    default Herbs herbs() {
        return Herbs.SNAPDRAGON;
    }

    @ConfigItem(
            keyName = "modeToUse",
            name = "Mode to Use",
            description = "The herb to clean",
            position = 4
    )
    default Mode modeToUse() {
        return Mode.MIX;
    }

    @ConfigItem(

            keyName = "wireFrame",
            name = "Show Player Frame",
            description = "Shows the Player Wire Frame",
            position = 11
    )
    default boolean toggleWireFrame() {
        return false;
    }

    @ConfigItem(

            keyName = "showStats",
            name = "Show Plugin Stats",
            description = "Show the status of the plugin",
            position = 12
    )
    default boolean showStats() {
        return true;
    }

    @ConfigItem(

            keyName = "showSessionTime",
            name = "Show Sesh Timer",
            description = "Shows how long its been since the plugin started",
            position = 15
    )
    default boolean showSessionTime() {
        return true;
    }

    @ConfigItem(

            keyName = "showEXP",
            name = "Show EXP Stats",
            description = "Show the EXP per hour",
            position = 16

    )
    default boolean showExp() {
        return true;
    }


    @ConfigItem(
            keyName = "hAltarBuiltIn",
            name = "",
            description = "",
            hidden = true
    )
    default boolean RunAltarBuiltIn() {
        return false;
    }

}

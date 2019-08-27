/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.simpleclick;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("simpleclick")
public interface SimpleClickConfig extends Config {

    @ConfigItem(
            keyName = "clickOne",
            name = "Start Key",
            description = "The key to start random click one (ms)",
            position = 1,
            group = "Click One"
    )
    default KeyEventKey clickOneKey() {
        return KeyEventKey.END;
    }

    @Range(
            max = 10000,
            min = 1
    )
    @ConfigItem(
            keyName = "clickOneMin",
            name = "Min Time",
            description = "Minimum Time to Click (ms)",
            group = "Click One"
    )
    default int clickOneMin() {
        return 500;
    }

    @Range(
            max = 10000,
            min = 1
    )
    @ConfigItem(
            keyName = "clickOneMax",
            name = "Max Time",
            description = "Max Time to Click (ms)",
            group = "Click One"
    )
    default int clickOneMax() {
        return 1000;
    }

    @ConfigItem(
            keyName = "clickTwo",
            name = "Start Key",
            description = "The key to start random click one (ms)",
            position = 1,
            group = "Click Two"
    )
    default KeyEventKey clickTwoKey() {
        return KeyEventKey.END;
    }

    @Range(
            max = 10000,
            min = 1
    )
    @ConfigItem(
            keyName = "clickTwoMin",
            name = "Min Time",
            description = "Minimum Time to Click (ms)",
            group = "Click Two"
    )
    default int clickTwoMin() {
        return 1;
    }

    @Range(
            max = 10000,
            min = 1
    )
    @ConfigItem(
            keyName = "clickTwoMax",
            name = "Max Time",
            description = "Max Time to Click (ms)",
            group = "Click Two"
    )
    default int clickTwoMax() {
        return 1000;
    }

    @ConfigItem(
            keyName = "showMinMax",
            name = "Show Min/Max",
            description = "Show Min and Max time on Overlay",
            group = "Debug"
    )
    default boolean showMinMax() {
        return false;
    }



}

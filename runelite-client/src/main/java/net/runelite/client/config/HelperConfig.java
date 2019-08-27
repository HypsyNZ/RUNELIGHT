/*
 * Copyright (c) 2018 Abex
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
package net.runelite.client.config;

@ConfigGroup("helpers")
public interface HelperConfig extends Config {

    // If you manually change Widget settings you might break an active plugin.

    @ConfigItem(
            keyName = "widgetEnableCombat",
            name = "NPC Combat Overlay",
            description = "Show ALL NPC Health / Level and Death Status",
            group = "Combat"
    )
    default boolean widgetEnableComabat() {
        return true;
    }

    @ConfigItem(
            keyName = "showOpponentCountInfo",
            name = "Opponent Info",
            description = "Show Number of and Name of NPC Opponents (Your Target / Their Target)",
            group = "Combat"
    )
    default boolean showOpponentCountInfo() {
        return true;
    }

    @ConfigItem(
            keyName = "playerWidgets",
            name = "Player Widgets",
            description = "Enables or Disables Player related Widgets",
            warning = "<html><center>These are supposed to be Enabled/Disabled by Plugins.</center></html>",
            group = "Widgets"
    )
    default boolean playerWidgets() {
        return true;
    }

    @ConfigItem(
            keyName = "widgetNPCprompts",
            name = "NPC Widgets",
            description = "Enables or Disables NPC related Widgets",
            warning = "<html><center>These are supposed to be Enabled/Disabled by Plugins.</center></html>",
            group = "Widgets"
    )
    default boolean widgetNPCprompts() {
        return true;
    }

    @ConfigItem(
            keyName = "widgetRunecrafting",
            name = "Runcrafting Widgets",
            description = "Enables or Disables Runecrafting related widgets",
            warning = "<html><center>These are supposed to be Enabled/Disabled by Plugins.</center></html>",
            group = "Widgets"
    )
    default boolean widgetRunecrafting() {
        return true;
    }

    @ConfigItem(
            keyName = "widgetBanking",
            name = "Banking Widgets",
            description = "Enables or Disables Banking/Inventory related widgets",
            warning = "<html><center>These are supposed to be Enabled/Disabled by Plugins.</center></html>",
            group = "Widgets"
    )
    default boolean widgetBanking() {
        return true;
    }

    @ConfigItem(
            keyName = "widgetPrayer",
            name = "Prayer Widgets",
            description = "Enables or Disables Prayer related widgets",
            warning = "<html><center>These are supposed to be Enabled/Disabled by Plugins.</center></html>",
            group = "Widgets"
    )
    default boolean widgetPrayer() {
        return true;
    }

    @ConfigItem(
            keyName = "showDebug",
            name = "[D] Debug Info",
            description = "Show Widget Debug Info",
            group = "Debug"
    )
    default boolean showDebug() {
        return false;
    }

    @ConfigItem(
            keyName = "showRegionInfo",
            name = "[D] Region Info",
            description = "Show Region Debug Info",
            group = "Debug"
    )
    default boolean showRegionInfo() {
        return false;
    }

    @ConfigItem(
            keyName = "showWidgetInfo",
            name = "[D] Widget Info",
            description = "Show Widget Debug Info",
            group = "Debug"
    )
    default boolean showWidgetInfo() {
        return false;
    }


    @ConfigItem(
            keyName = "showOpponentInfo",
            name = "[D] Opponent Info",
            description = "Show Opponent Debug Info",
            group = "Debug"
    )
    default boolean showOpponentInfo() {
        return false;
    }


    @ConfigItem(
            keyName = "mouseSnapShot",
            name = "[D] Mouse Snapshot",
            description = "Show Mouse Snapshot Debug Info",
            group = "Debug"
    )
    default boolean showMouseSnapShot() {
        return false;
    }


    @Range(
            max = 2000,
            min = 250
    )
    @ConfigItem(
            keyName = "basemovetimeSlow",
            name = "Base Move Time",
            description = "The base movement speed without random additions etc",
            group = "Movement"
    )
    default int baseMove() {
        return 250;
    }

    @Range(
            max = 500,
            min = 70
    )
    @ConfigItem(
            keyName = "basemovetimeFast",
            name = "Fast Base Time",
            description = "The base movement time for fast actions",
            group = "Movement"
    )
    default int baseMoveFast() {
        return 125;
    }

    @Range(
            max = 500,
            min = 1
    )
    @ConfigItem(
            keyName = "reactTime",
            name = "Base React Time",
            description = "The base reaction speed without random additions etc",
            group = "Movement"
    )
    default int baseReact() {
        return 45;
    }

    @Range(
            max = 500,
            min = 1
    )
    @ConfigItem(
            keyName = "reactTimeVariation",
            name = "React Time Variation",
            description = "The variation in your reaction speed",
            group = "Movement"
    )
    default int baseReactVar() {
        return 45;
    }

    @ConfigItem(
            keyName = "itemsToEscape",
            name = "",
            description = "Press escape when you click on these items in the Bank",
            group = "Auto Escape"
    )
    default String getEscapeItems()
    {
        return "coal, iron ore, mithril ore";
    }

    @ConfigItem(
            keyName = "shouldEscape",
            name = "Escape on Click",
            description = "Press escape when you click on these items in the Bank",
            group = "Auto Escape"
    )
    default boolean escapeOnClick() {
        return true;
    }

    @ConfigItem(
            keyName = "spaceBarCraft",
            name = "",
            description = "Press space when you see this crafting option (option 1)",
            group = "Auto Space Bar"
    )
    default String getSpaceBarCraft()
    {
        return "mithril bar";
    }

    @ConfigItem(
            keyName = "autoSpaceBCraft",
            name = "Auto Space Bar",
            description = "Press space when you see this crafting option (option 1)",
            group = "Auto Space Bar"
    )
    default boolean autoSpaceBCraft() {
        return true;
    }

    @ConfigItem(
            keyName = "autoYesDialog",
            name = "Auto Yes",
            description = "Automatically press 1 when Dialog options are Yes/No",
            group = "Auto Dialogs"
    )
    default boolean autoYesDialog() {
        return true;
    }

    @ConfigItem(
            keyName = "autoFetch",
            name = "Fetch Planks",
            description = "Automatically fetch planks when its the option",
            group = "Auto Dialogs"
    )
    default boolean autoFetchLogs() {
        return true;
    }

    @ConfigItem(
            keyName = "autoLarder",
            name = "Auto Larder",
            description = "Automatically press 2 when the furniture menu is open (Oak Larder)",
            group = "Auto Dialogs"
    )
    default boolean autoFurnMenu() {
        return true;
    }
}

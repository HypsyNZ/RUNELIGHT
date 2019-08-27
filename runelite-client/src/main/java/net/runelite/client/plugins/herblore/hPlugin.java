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

import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.events.ConfigChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

import static net.runelite.client.plugins.herblore.hItems.initItems;

@PluginDescriptor(
        name = "Herblore Plugin",
        description = "A Herblore Plugin",
        tags = {"npcs", "players", "projectiles"},
        enabledByDefault = false,
        type = PluginType.SKILLING
)
@PluginDependency(XpTrackerPlugin.class)
public class hPlugin extends Plugin {

    @Inject
    private hItems hItems;
    @Inject
    private Client client;
    @Inject
    private KeyManager keyManager;
    @Inject
    private hMain hMain;
    @Inject
    private hOverlayMenu hOverlayMenu;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ConfigManager configManager;
    @Inject
    private hInputListener listener;
    @Inject
    private hConfig hConfig;
    @Inject
    private EventBus eventBus;

    @Provides
    hConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(hConfig.class);
    }

    private static final String CONFIG_GROUP = "h";

    @Override
    protected void startUp() {
        addSubscriptions();
        keyManager.registerKeyListener(listener);
        overlayManager.add(hMain);
        overlayManager.add(hOverlayMenu);
        configManager.setConfiguration("h", "hAltarBuiltIn", false);
        initItems();
    }

    private void addSubscriptions() {
        eventBus.subscribe(ConfigChanged.class, this, this::onConfigChanged);
    }

    public void onConfigChanged(ConfigChanged event) {
        if (!CONFIG_GROUP.equals(event.getGroup())) {
            return;
        }

        updateConfig();
    }

    static boolean startHerb = false;

    public void updateConfig() {
        startHerb = hConfig.RunAltarBuiltIn();
    }

    @Override
    protected void shutDown() throws Exception {
        keyManager.unregisterKeyListener(listener);
        overlayManager.remove(hMain);
        overlayManager.remove(hOverlayMenu);

    }

}

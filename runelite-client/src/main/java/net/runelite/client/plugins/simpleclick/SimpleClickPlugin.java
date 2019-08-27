/*'
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Abexlry <abexlry@gmail.com>
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

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@PluginDescriptor(
        name = "Simple Clicker",
        description = "Allows you setup various random clicks",
        tags = {"click", "random"},
        enabledByDefault = false,
        type = PluginType.SKILLING
)
@Singleton
public class SimpleClickPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private SimpleClickConfig config;

    @Inject
    private KeyManager keyManager;

    @Inject
    private SimpleClickListener inputListener;

    @Inject
    private EventBus eventBus;

    @Inject
    private SimpleClickOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    Boolean start;

    @Getter(AccessLevel.PACKAGE)
    private int currentMin;

    @Getter(AccessLevel.PACKAGE)
    private int currentMax;

    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private int activeKey = 0;

    @Override
    protected void startUp() throws Exception {
        addSubscriptions();
        setStart(false);
        overlayManager.add(overlay);
        keyManager.registerKeyListener(inputListener);
    }

    @Override
    protected void shutDown() throws Exception {
        eventBus.unregister(this);
        overlayManager.remove(overlay);
        keyManager.unregisterKeyListener(inputListener);
    }

    private void addSubscriptions() {
        eventBus.subscribe(ConfigChanged.class, this, this::onConfigChanged);
    }

    @Provides
    SimpleClickConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(SimpleClickConfig.class);
    }

    private void onConfigChanged(ConfigChanged configChanged) {
        if (!configChanged.getGroup().equals("simpleclick")) {
            return;
        }

        updateConfig();
    }

    @Schedule(
            period = 100,
            unit = ChronoUnit.MILLIS
    )
    public void updateConfigTask() {
        updateConfig();
    }

    void updateConfig() {

        if(activeKey == 0){
            this.currentMin = 0;
            this.currentMax = 0;
        }

        if(activeKey == 1){
            this.currentMin = config.clickOneMin();
            this.currentMax = config.clickOneMax();
        }

        if(activeKey == 2){
            this.currentMin = config.clickTwoMin();
            this.currentMax = config.clickTwoMax();
        }
    }

    static int getRandom(int min, int max) {
        if (min >= max) {
            return 0;
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}

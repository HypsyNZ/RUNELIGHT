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

import com.github.hypsynz.naturalmouse.api.MouseMotionFactory;
import com.github.hypsynz.naturalmouse.support.*;
import com.github.hypsynz.naturalmouse.util.FlowTemplates;
import com.google.common.annotations.VisibleForTesting;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.HelperConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

import static net.runelite.client.plugins.helpers.HelperInput.Delay;
import static net.runelite.client.plugins.helpers.HelperInput.PressKeyRandom;
import static net.runelite.client.plugins.helpers.HelperWidget.*;


@Slf4j
@PluginDescriptor(
        name = "Infinity Helper",
        loadWhenOutdated = true,
        hidden = true
)
public class HelperPlugin extends Plugin {

    @Inject
    private Client client;
    @Inject
    private KeyManager keyManager;
    @Inject
    private HelperWidget overlaytwo;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ConfigManager configManager;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private HelperConfig helperConfig;
    @Inject
    private HelperOverlay helperOverlay;
    @Inject
    private HelperRegion regionOverlay;
    @Inject
    private EventBus eventBus;
    @Inject
    private ScheduledExecutorService executorService;

    @Setter(AccessLevel.PACKAGE)
    private String getEscapeItems;
    @Setter(AccessLevel.PACKAGE)
    private String getSpaceBarItems;

    @Getter
    private static final Set<GameObject> boothObjects = new HashSet<>();
    @Getter
    public static MouseMotionFactory helperMotionFactory;
    static MouseMotionFactory helperMotionFactoryFast;

    private static final String CONFIG_GROUP = "helpers";
    public static int baseMove;
    public static int baseMoveFast;
    private static int baseReact;
    private static int baseReactVar;

    public static String menuTarget = "";
    public static String menuOption = "";
    public static String menuTargetRaw = "";
    public static String menuOptionRaw = "";

    private boolean onceSpace = false;
    private String lastTarget = "";
    private long lastTime = 0;

    private List<String> escapeItems = new ArrayList<>();
    private List<String> spaceBarItems = new ArrayList<>();

    @Override
    protected void startUp() {

        updateConfig();
        escapeItems = getEscapeItems();
        spaceBarItems = getSpaceBarItems();
        addSubscriptions();
        HelperThread.getThreadStates();

        overlayManager.add(overlaytwo);
        overlayManager.add(helperOverlay);
        overlayManager.add(regionOverlay);
        WidgetCombatEnabled = helperConfig.widgetEnableComabat();
        log.info("[x_o] Started Helper Plugin");
    }

    private void addSubscriptions() {
        eventBus.subscribe(ConfigChanged.class, this, this::onConfigChanged);
        eventBus.subscribe(GameObjectSpawned.class, this, this::onGameObjectSpawned);
        eventBus.subscribe(GameStateChanged.class, this, this::onGameStateChanged);
        eventBus.subscribe(GameObjectDespawned.class, this, this::onGameObjectDespawned);
        eventBus.subscribe(GameObjectChanged.class, this, this::onGameObjectChanged);
        eventBus.subscribe(InteractingChanged.class, this, overlaytwo::onInteractingChanged);
        eventBus.subscribe(MenuEntryAdded.class, this, this::onMenuEntryAdded);
        eventBus.subscribe(MenuOptionClicked.class, this, this::onMenuOptionClicked);
    }

    private void onMenuEntryAdded(MenuEntryAdded event) {

        MenuEntry[] menuEntries = client.getMenuEntries();
        int last = menuEntries.length - 1;

        MenuEntry menuEntry = menuEntries[last];
        menuTarget = menuEntry.getTarget().toLowerCase();
        menuOption = menuEntry.getOption().toLowerCase();
        menuTargetRaw = menuEntry.getTarget();
        menuOptionRaw = menuEntry.getOption();
        log.debug("Menu target is: {}", menuTarget);
        log.debug("Menu option is: {}", menuOption);

    }


    private boolean onceFurn = false;

    private String lastDialog = "";

    @Schedule(
            period = 75,
            unit = ChronoUnit.MILLIS
    )
    public void furnitureCheck() {

        Widget scw = client.getWidget(WidgetInfo.FURNITURE_MENU_OPTION_2);
        Widget dialog = client.getWidget(219,1);

        if (scw != null && !scw.isHidden() && autoLarder) {

            Widget[] scwd = scw.getDynamicChildren();
            Widget clickArea = scwd[6];

            // Don't press 2 if we can't make it
            if (!onceFurn && !clickArea.isHidden()) {

                executorService.execute(() -> {

                    onceFurn = true;
                    log.debug("Executor :: Starting");
                    Delay(HelperDelay.rand175to300);
                    PressKeyRandom(KeyEvent.VK_2);

                });

                return;
            }

        } else{
            onceFurn = false;
        }

        if(dialog != null && !dialog.isHidden()){

            Widget[] d = dialog.getDynamicChildren();
            String text = d[1].getText();
            log.debug("Dialog Text: "+ text);

            // Reset if dialog text changes
            if(!lastDialog.equals(text)){
                lastDialog = text;
                dialogOnce = false;
            }

            if (!dialogOnce && text.contains("Yes") && autoDialog) {

                executorService.execute(() -> {

                    dialogOnce = true;
                    log.debug("Executor :: Starting");
                    Delay(HelperDelay.rand175to300);
                    PressKeyRandom(KeyEvent.VK_1);

                });

                return;
            }

            if (!dialogOnce && text.contains("Fetch") && autoFetch) {

                executorService.execute(() -> {

                    dialogOnce = true;
                    log.debug("Executor :: Starting");
                    Delay(HelperDelay.rand175to300);
                    PressKeyRandom(KeyEvent.VK_1);

                });

            }

        }else{
            lastDialog = "";
            dialogOnce = false;
        }
    }
    private boolean dialogOnce = false;

    @Schedule(
            period = 75,
            unit = ChronoUnit.MILLIS
    )
    public void spaceBarCheck() {

        Widget scw = client.getWidget(WidgetInfo.CHATBOX_CRAFT_CRAFT_ONE);

        if (scw != null && !scw.isHidden() && autoSpace) {

            boolean match = false;
            for (String item : spaceBarItems) {
                if (Text.standardize(scw.getName()).equals(item)) {
                    log.debug("SpaceBarItem :: Match");
                    match = true;
                    break;
                }
            }

            if (match && !onceSpace) {

                executorService.execute(() -> {

                    onceSpace = true;
                    log.debug("Executor :: Starting");
                    Delay(HelperDelay.rand150to500);
                    PressKeyRandom(KeyEvent.VK_SPACE);

                });

            }

            if (!match) {

                log.debug("SpaceBarItem :: No Match :: Reset");
                onceSpace = false;

            }

        } else{
            onceSpace = false;
        }
    }

    private void onMenuOptionClicked(MenuOptionClicked event) {
        log.debug("Event Identifier: {} | Option: {} | Target: {} || Type: {}", event.getIdentifier(), event.getOption(), event.getTarget(), event.getOpcode());

        // Expire after 1500ms
        if (lastTime + 1500 < Instant.now().toEpochMilli()) {
            lastTarget = "";
            lastTime = 0;
        }

        // Only exit once // Ignore Double Clicks
        if (lastTarget.equals(event.getTarget()) && lastTime + 200 > Instant.now().toEpochMilli()) {
            return;
        }

        if (autoEscape && bankIsOpen && event.getActionParam1() == WidgetInfo.BANK_ITEM_CONTAINER.getId()) {

            log.debug("onMenuOptionClicked :: bankIsOpen");
            lastTarget = event.getTarget();
            lastTime = Instant.now().toEpochMilli();

            for (String item : escapeItems) {

                if (Text.standardize(event.getTarget().toLowerCase()).equals(item)) {
                    log.debug("Match :: Executor Starting");
                    executorService.execute(() -> {
                        Delay(HelperDelay.rand150to500);
                        PressKeyRandom(KeyEvent.VK_ESCAPE);
                    });
                    break;
                }
            }
        }
    }

    @VisibleForTesting
    private List<String> getEscapeItems() {
        final String escapeItems = this.getEscapeItems.toLowerCase();

        if (escapeItems.isEmpty()) {
            return Collections.emptyList();
        }

        return Text.fromCSV(escapeItems);
    }

    @VisibleForTesting
    private List<String> getSpaceBarItems() {
        final String escapeItems = this.getSpaceBarItems.toLowerCase();

        if (escapeItems.isEmpty()) {
            return Collections.emptyList();
        }

        return Text.fromCSV(escapeItems);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(overlaytwo);
        overlayManager.remove(helperOverlay);
        overlayManager.remove(regionOverlay);
        log.info("[x_o] ShutDown Helper Plugin");
    }

    @Schedule(
            period = 600,
            unit = ChronoUnit.SECONDS
    )
    public void updateMotionFactory() {
        updateMouseMotionFactory();
    }

    private static void updateMouseMotionFactory() {
        MouseMotionFactory factory = new MouseMotionFactory();
        List<Flow> flows = new ArrayList<>();


        int randomRandom = HelperInput.getRandomNumberInRange(1, 3);
        switch (randomRandom) {
            case 1:
                flows.add(new Flow(FlowTemplates.variatingFlow()));
                break;

            case 2:
                flows.add(new Flow(FlowTemplates.slowStartupFlow()));
                break;

            case 3:
                flows.add(new Flow(FlowTemplates.slowStartup2Flow()));
                break;
        }

        randomRandom = HelperInput.getRandomNumberInRange(1, 4);
        switch (randomRandom) {

            case 1:
                flows.add(new Flow(FlowTemplates.random()));
                break;

            case 2:
                flows.add(new Flow(FlowTemplates.randomFlowLow()));
                break;

            case 3:
                flows.add(new Flow(FlowTemplates.randomFlowHigh()));
                break;

            case 4:
                flows.add(new Flow(FlowTemplates.randomFlowWide()));
                break;
        }

        randomRandom = HelperInput.getRandomNumberInRange(1, 3);
        switch (randomRandom) {
            case 1:
                flows.add(new Flow(FlowTemplates.jaggedFlow()));
                break;

            case 2:
                flows.add(new Flow(FlowTemplates.interruptedFlow()));
                break;

            case 3:
                flows.add(new Flow(FlowTemplates.interruptedFlow2()));
                break;
        }

        DefaultSpeedManager manager = new DefaultSpeedManager(flows);
        factory.setDeviationProvider(new SinusoidalDeviationProvider(12));
        factory.setNoiseProvider(new DefaultNoiseProvider(2.3D));
        manager.setMouseMovementBaseTimeMs(baseMove);
        factory.getNature().setReactionTimeBaseMs(baseReact);
        factory.getNature().setReactionTimeVariationMs(baseReactVar);

        DefaultOvershootManager overshootManager = (DefaultOvershootManager) factory.getOvershootManager();
        overshootManager.setOvershoots(0);

        factory.setSpeedManager(manager);
        helperMotionFactory = factory;

        manager.setMouseMovementBaseTimeMs(baseMoveFast);
        factory.setSpeedManager(manager);
        helperMotionFactoryFast = factory;


        log.info("[x_o] Helper Motion Factory and Randoms have been Updated");
    }

    private boolean autoLarder = false;
    private boolean autoDialog = false;
    private boolean autoFetch = false;
    private boolean autoSpace = false;
    private boolean autoEscape = false;

    public void updateConfig() {
        baseMove = helperConfig.baseMove();
        baseMoveFast = helperConfig.baseMoveFast();
        baseReact = helperConfig.baseReact();
        baseReactVar = helperConfig.baseReactVar();
        updateMouseMotionFactory();
        WidgetCombatEnabled = helperConfig.widgetEnableComabat();
        WidgetNPCPromptsEnabled = helperConfig.widgetNPCprompts();
        WidgetRunecraftEnabled = helperConfig.widgetRunecrafting();
        WidgetBankingEnabled = helperConfig.widgetBanking();
        WidgetPlayerEnabled = helperConfig.playerWidgets();
        WidgetPrayerEnabled = helperConfig.widgetPrayer();
        this.getEscapeItems = helperConfig.getEscapeItems();
        this.getSpaceBarItems = helperConfig.getSpaceBarCraft();
        autoSpace = helperConfig.autoSpaceBCraft();
        autoLarder = helperConfig.autoFurnMenu();
        autoDialog = helperConfig.autoYesDialog();
        autoFetch = helperConfig.autoFetchLogs();
        autoEscape = helperConfig.escapeOnClick();
    }

    public void onConfigChanged(ConfigChanged event) {
        if (!CONFIG_GROUP.equals(event.getGroup())) {
            return;
        }

        spaceBarItems = getSpaceBarItems();
        escapeItems = getEscapeItems();
        updateConfig();
    }

    public void onGameObjectSpawned(final GameObjectSpawned event) {
        GameObject gameObject = event.getGameObject();
        Booth booth = Booth.findBooth(gameObject.getId());
        if (booth != null) {
            boothObjects.add(gameObject);
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() != GameState.LOGGED_IN) {
            boothObjects.clear();
        }
    }

    public void onGameObjectDespawned(final GameObjectDespawned event) {
        boothObjects.remove(event.getGameObject());
    }


    public void onGameObjectChanged(final GameObjectChanged event) {
        boothObjects.remove(event.getGameObject());
    }
}

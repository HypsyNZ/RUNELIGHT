/*
 * Copyright (c) 2018, Seth <Sethtroll3@gmail.com>
 * Copyright (c) 2019, Brandon White <bmwqg@live.com>
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
package net.runelite.client.plugins.blastfurnace;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.Executors;

import static net.runelite.api.NullObjectID.NULL_9092;
import static net.runelite.api.ObjectID.CONVEYOR_BELT;
import static net.runelite.client.plugins.helpers.HelperWidget.inventoryItemContainer;
import static net.runelite.client.util.MenuUtil.swap;

@PluginDescriptor(
        name = "Blast Furnace",
        description = "Show helpful information for the Blast Furnace minigame",
        tags = {"minigame", "overlay", "skilling", "smithing"}
)
@Slf4j
@Singleton
public class BlastFurnacePlugin extends Plugin {
    private static final int BAR_DISPENSER = NULL_9092;
    private static final String FOREMAN_PERMISSION_TEXT = "Okay, you can use the furnace for ten minutes. Remember, you only need half as much coal as with a regular furnace.";

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    GameObject conveyorBelt;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    GameObject barDispenser;

    private ForemanTimer foremanTimer;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private BlastFurnaceOverlay overlay;

    @Inject
    private MouseManager mouseManager;

    @Inject
    private BlastFurnaceCofferOverlay cofferOverlay;

    @Inject
    private BlastFurnaceClickBoxOverlay clickBoxOverlay;

    @Inject
    private Client client;

    @Inject
    private ItemManager itemManager;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Inject
    private BlastFurnaceConfig config;

    @Inject
    private EventBus eventBus;

    @Getter(AccessLevel.PACKAGE)
    private boolean showConveyorBelt;
    @Getter(AccessLevel.PACKAGE)
    private boolean showBarDispenser;

    private static final String FILL_OPTION = "fill";
    private static final String EMPTY_OPTION = "empty";

    private int coalInInventory = 0;
    private int emptyInventorySpace = 0;
    private Item[] inventoryItems;


    @Override
    protected void startUp() throws Exception {
        updateConfig();
        addSubscriptions();

        overlayManager.add(overlay);
        overlayManager.add(cofferOverlay);
        overlayManager.add(clickBoxOverlay);

        HelperWidget.WidgetBankEnabledTrue();
    }

    @Override
    protected void shutDown() {
        eventBus.unregister(this);
        infoBoxManager.removeIf(ForemanTimer.class::isInstance);
        overlayManager.remove(overlay);
        overlayManager.remove(cofferOverlay);
        overlayManager.remove(clickBoxOverlay);
        conveyorBelt = null;
        barDispenser = null;
        foremanTimer = null;
    }

    private void addSubscriptions() {
        eventBus.subscribe(ConfigChanged.class, this, this::onConfigChanged);
        eventBus.subscribe(GameObjectSpawned.class, this, this::onGameObjectSpawned);
        eventBus.subscribe(GameTick.class, this, this::onGameTick);
        eventBus.subscribe(MenuEntryAdded.class, this, this::onMenuEntryAdded);
        eventBus.subscribe(MenuOptionClicked.class, this, this::onMenuOptionClicked);

    }


    @Schedule(
            period = 10,
            unit = ChronoUnit.MILLIS
    )
        public void coalInBagCertain() {

        Widget text = client.getWidget(WidgetInfo.DIALOG_SPRITE_TEXT);
        if(text == null) {
            return;
        }

        String s = text.getText();
        if(s.equals("")){
            return;
        }

        if (s.contains("empty")) {
            amountOfCoalInCoalBag = 0;
            log.debug("[o_o] Empty :: {}", s);
            return;
        }

        String coalAmount = s.replaceAll("\\D+", "");
        int coal = Integer.parseInt(coalAmount);
        if (coal > 0) {
            log.debug("[o_o] Filled :: {}", s);
            amountOfCoalInCoalBag = coal;
        }

    }

    @Schedule(
            period = 10,
            unit = ChronoUnit.MILLIS
    )
    public void inventoryCounter() {
        inventoryItemContainer = client.getItemContainer(InventoryID.INVENTORY);

        if (inventoryItemContainer == null) {
            return;
        }

        inventoryItems = inventoryItemContainer.getItems();
    }

    private static void updateAmountOfCoalInBag(int delta) {
        amountOfCoalInCoalBag = Math.max(0, Math.min(27, amountOfCoalInCoalBag + delta));
    }

    @Getter(AccessLevel.PACKAGE)
    private static int amountOfCoalInCoalBag;

    private void onMenuOptionClicked(MenuOptionClicked event) {

        if (inventoryItems != null) {

            Executors.newSingleThreadExecutor().execute(() -> {

                inventoryCounter();
                coalInInventory = (int) Arrays.stream(inventoryItems).filter(i -> i.getId() == ItemID.COAL).count();
                emptyInventorySpace = (int) Arrays.stream(inventoryItems).filter(i -> i.getId() != -1).count();

                log.info("coalInventory: {} || coalBag: {} || emptySpace: {} || option: {} || shitDocs1: {}", coalInInventory, amountOfCoalInCoalBag, emptyInventorySpace, event.getOption().toLowerCase(), event.getActionParam1());

                switch (event.getOption().toLowerCase()) {
                    case FILL_OPTION:

                        if (coalInInventory == 0 && emptyInventorySpace == 1 && amountOfCoalInCoalBag == 0) {
                            log.info("[x_o] Clicked before inventory itemContainer had itemIDs, manually set coalBag to 27");
                            amountOfCoalInCoalBag = 27;
                            break;

                        }

                        updateAmountOfCoalInBag(coalInInventory);
                        break;

                    case EMPTY_OPTION:
                        int difference = 28 - emptyInventorySpace;
                        updateAmountOfCoalInBag(-difference);
                        break;
                }

            });

        } else {

            log.info("[x_x] InventoryItems were null || coal: {} || emptySpace: {} || option: {}", coalInInventory, emptyInventorySpace, event.getOption().toLowerCase());

        }
    }

    private void onGameObjectSpawned(GameObjectSpawned event) {

        GameObject gameObject = event.getGameObject();

        if (conveyorBelt == null || barDispenser == null) {

            switch (gameObject.getId()) {
                case CONVEYOR_BELT:
                    conveyorBelt = gameObject;
                    break;

                case BAR_DISPENSER:
                    barDispenser = gameObject;
                    break;
            }

        }
    }

    private void onGameTick(GameTick event) {

        Widget npcDialog = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
        if (npcDialog == null) {
            return;
        }

        // blocking dialog check until 5 minutes needed to avoid re-adding while dialog message still displayed
        boolean shouldCheckForemanFee = client.getRealSkillLevel(Skill.SMITHING) < 60
                && (foremanTimer == null || Duration.between(Instant.now(), foremanTimer.getEndTime()).toMinutes() <= 5);

        if (shouldCheckForemanFee) {
            String npcText = Text.sanitizeMultilineText(npcDialog.getText());

            if (npcText.equals(FOREMAN_PERMISSION_TEXT)) {
                infoBoxManager.removeIf(ForemanTimer.class::isInstance);

                foremanTimer = new ForemanTimer(this, itemManager);
                infoBoxManager.addInfoBox(foremanTimer);
            }
        }
    }

    public void onMenuEntryAdded(MenuEntryAdded event) {

        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        final String option = Text.standardize(event.getOption());
        final String target = Text.standardize(event.getTarget());

        if (getAmountOfCoalInCoalBag() == 27) {
            swap(client, "empty", option, target, false);
        }
    }

    @Provides
    BlastFurnaceConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(BlastFurnaceConfig.class);
    }

    private void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("blastfurnace")) {
            updateConfig();
        }
    }

    private void updateConfig() {
        this.showBarDispenser = config.showBarDispenser();
        this.showConveyorBelt = config.showConveyorBelt();
    }

    static void getDispenserAndConveyor(Graphics2D graphics, Client client, BlastFurnacePlugin plugin) {

        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();

        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }

                GameObject[] gameObjects = tile.getGameObjects();
                if (gameObjects != null) {
                    for (GameObject gameObject : gameObjects) {
                        if (gameObject != null) {

                            switch (gameObject.getId()) {
                                case CONVEYOR_BELT:
                                    plugin.setConveyorBelt(gameObject);
                                    ;
                                    break;

                                case BAR_DISPENSER:
                                    plugin.setBarDispenser(gameObject);
                                    break;
                            }

                        }

                    }
                }
            }
        }

    }

}

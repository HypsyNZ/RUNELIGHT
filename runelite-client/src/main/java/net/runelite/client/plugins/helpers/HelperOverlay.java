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

import net.runelite.api.Client;
import net.runelite.client.config.HelperConfig;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.client.plugins.helpers.HelperWidget.*;


public class HelperOverlay extends Overlay {

    private final Client client;
    private final PanelComponent panelComponent = new PanelComponent();
    private final HelperConfig helperConfig;
    private boolean first = false;

    @Inject
    private iHelperDebug iHelperDebug;

    @Inject
    public HelperOverlay(HelperConfig helperConfig, Client client) {
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.HIGHEST);
        this.helperConfig = helperConfig;
        this.client = client;

    }



    @Override
    public Dimension render(Graphics2D graphics) {
        HelperThread.getThreadStates();

        mouseLocationOnScreen = MouseInfo.getPointerInfo();
       // HelperFind.mouseMoved = lastxMouse != mouseLocationOnScreen.getLocation().x || lastyMouse != mouseLocationOnScreen.getLocation().y;

        if (!first && helperConfig.showDebug()) {
            first = true;
            iHelperDebug.open();
        }

        if (helperConfig.showDebug()) {
            iHelperDebug.addVarLog();
        }

        panelComponent.getChildren().clear();
        panelComponent.setBorder(new Rectangle(2, 2, 2, 2));
        panelComponent.setGap(new java.awt.Point(0, 2));

        if (numberOfOpponents != 0 && WidgetCombatEnabled && helperConfig.showOpponentCountInfo()) {


            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Opponents: ").leftColor(Color.CYAN)
                    .right(Integer.toString(numberOfOpponents))
                    .build());

            if (numberOfOpponents == 1) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(opponentNameSingle)
                        .build());

            } else if (numberOfOpponents > 1) {

                String[] names = opponentNamesMulti.split(",");
                int i = 0;
                while (names.length > i) {
                    panelComponent.getChildren().add(TitleComponent.builder()
                            .text(names[i])
                            .build());
                    i++;
                }
            }

        }
        return panelComponent.render(graphics);
    }

}

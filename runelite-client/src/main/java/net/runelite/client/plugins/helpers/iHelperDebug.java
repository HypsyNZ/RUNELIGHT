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
package net.runelite.client.plugins.helpers;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.HelperConfig;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static net.runelite.client.plugins.helpers.HelperWidget.mouseLocationOnScreen;

@Slf4j
class iHelperDebug extends JFrame {

    private final Client client;
    private final HelperConfig helperConfig;
    private final JPanel tracker = new JPanel();

    @Inject
    iHelperDebug(Client client, HelperConfig helperConfig) {
        this.client = client;
        this.helperConfig = helperConfig;

        setTitle("iHelper Debug");
        setIconImage(ClientUI.ICON);

        setLayout(new BorderLayout());

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        tracker.setLayout(new DynamicGridLayout(0, 1, 0, 3));


        final JPanel trackerWrapper = new JPanel();
        trackerWrapper.setLayout(new BorderLayout());
        trackerWrapper.add(tracker, BorderLayout.NORTH);

        final JScrollPane trackerScroller = new JScrollPane(trackerWrapper);
        trackerScroller.setPreferredSize(new Dimension(280, 820));

        final JScrollBar vertical = trackerScroller.getVerticalScrollBar();
        vertical.addAdjustmentListener(new AdjustmentListener() {
            int lastMaximum = actualMax();

            private int actualMax() {
                return vertical.getMaximum() - vertical.getModel().getExtent();
            }

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (vertical.getValue() >= lastMaximum) {
                    vertical.setValue(actualMax());
                }
                lastMaximum = actualMax();
            }
        });

        add(trackerScroller, BorderLayout.CENTER);

        final JPanel trackerOpts = new JPanel();
        trackerOpts.setLayout(new FlowLayout());

        add(trackerOpts, BorderLayout.SOUTH);

        pack();
    }

    void addVarLog() {

        SwingUtilities.invokeLater(() ->
        {
            tracker.removeAll();

            if (helperConfig.showRegionInfo()) {

                // HelperRegion
                JLabel header = new JLabel("Region Settings || Tick " + client.getTickCount());
                header.setFont(FontManager.getRunescapeSmallFont());
                header.setBorder(new CompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.LIGHT_GRAY_COLOR),
                        BorderFactory.createEmptyBorder(3, 6, 0, 0)
                ));
                tracker.add(header);
                tracker.add(new JLabel(String.format(" Inventory Tab : %s", HelperRegion.currentInventoryTab)));
                tracker.add(new JLabel(String.format(" Bank Tab : %s", HelperRegion.currentBankTab)));
                tracker.add(new JLabel(String.format(" Withdraw Setting : %s", HelperRegion.currentWithdrawSetting)));
                tracker.add(new JLabel(String.format(" RegionID : %s", HelperRegion.currentRegionID)));
                tracker.add(new JLabel(String.format(" Region String : %s", HelperRegion.regionString)));
                tracker.add(new JLabel(String.format(" Player Moving : %s", HelperRegion.isMoving)));
                tracker.add(new JLabel(String.format(" Booth Tile: X: %s | Y: %s", HelperRegion.getBoothTile()[0], HelperRegion.getBoothTile()[1])));

            } else {

                JLabel header = new JLabel("iHelper Debug || Tick " + client.getTickCount());
                header.setFont(FontManager.getRunescapeSmallFont());
                header.setBorder(new CompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.LIGHT_GRAY_COLOR),
                        BorderFactory.createEmptyBorder(3, 6, 0, 0)
                ));
                tracker.add(header);
            }

            if(helperConfig.showMouseSnapShot()) {
                JLabel headerc = new JLabel("Mouse Action Snapshot");
                headerc.setFont(FontManager.getRunescapeSmallFont());
                headerc.setBorder(new CompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.LIGHT_GRAY_COLOR),
                        BorderFactory.createEmptyBorder(3, 6, 0, 0)
                ));
                tracker.add(headerc);
                if (mouseLocationOnScreen != null) {
                    tracker.add(new JLabel(String.format(" MouseX: %s", mouseLocationOnScreen.getLocation().x)));
                    tracker.add(new JLabel(String.format(" MouseY: %s", mouseLocationOnScreen.getLocation().y)));
                }
            }

            // HelperWidget
            if (helperConfig.showWidgetInfo()) {
                JLabel headers = new JLabel("Widget Settings");
                headers.setFont(FontManager.getRunescapeSmallFont());
                headers.setBorder(new CompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.LIGHT_GRAY_COLOR),
                        BorderFactory.createEmptyBorder(3, 6, 0, 0)
                ));
                tracker.add(headers);
                tracker.add(new JLabel(String.format(" Furniture Widget: %s", HelperWidget.furniture != null)));
                tracker.add(new JLabel(String.format(" Inventory Widget: %s", HelperWidget.inventory != null)));
                tracker.add(new JLabel(String.format(" Bank Widget: %s", HelperWidget.bank != null)));
                if (HelperWidget.bank != null) {
                    tracker.add(new JLabel(String.format(" Bank Open: %s", HelperWidget.bankIsOpen)));
                }
                tracker.add(new JLabel(String.format(" Shop Open: %s ", HelperWidget.TalkingToShopOwner)));
                tracker.add(new JLabel(String.format(" Dialog Open: %s", HelperWidget.dialogOpen)));

                tracker.add(new JLabel(String.format(" Widget NPC: %s", HelperWidget.WidgetNPCPromptsEnabled)));
                tracker.add(new JLabel(String.format(" Widget BANK: %s", HelperWidget.WidgetBankingEnabled)));
                tracker.add(new JLabel(String.format(" Widget COMBAT: %s", HelperWidget.WidgetCombatEnabled)));
                tracker.add(new JLabel(String.format(" Widget RC: %s", HelperWidget.WidgetRunecraftEnabled)));
                tracker.add(new JLabel(String.format(" Player Running : %s", HelperWidget.runningEnabled)));
            }


            // HelperWidget OpInfo
            if(helperConfig.showOpponentInfo()) {
                tracker.add(new JLabel(""));
                JLabel headeroi = new JLabel("Opponent Information");
                headeroi.setFont(FontManager.getRunescapeSmallFont());
                headeroi.setBorder(new CompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.LIGHT_GRAY_COLOR),
                        BorderFactory.createEmptyBorder(3, 6, 0, 0)
                ));
                tracker.add(headeroi);
                tracker.add(new JLabel(String.format(" Opponents: %s ", HelperWidget.numberOfOpponents)));
                tracker.add(new JLabel(String.format(" Opponent String S: %s ", HelperWidget.opponentNameSingle)));
                tracker.add(new JLabel(String.format(" Opponent String M: %s ", HelperWidget.opponentNamesMulti)));
                tracker.add(new JLabel(String.format(" Opponent Point: X: %s | Y: %s", HelperWidget.opponentPoint.getX(), HelperWidget.opponentPoint.getY())));
            }

            tracker.revalidate();
        });
    }

    public void open() {
        setVisible(true);
        toFront();
        repaint();
    }

    public void close() {
        tracker.removeAll();
        setVisible(false);
    }
}

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

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;

import java.awt.*;

import static java.awt.Color.CYAN;
import static net.runelite.client.plugins.helpers.HelperInput.MoveAndClick;

@Slf4j
public class HelperFind {

    public static void FindWidgetItemAndClick(WidgetItem item, Client client, boolean fast) {

        Rectangle invItem = item.getCanvasBounds();
        int[] i = HelperTransform.xyInvItem(invItem, client);
        if (i[0] != 0) {
            //log.info("Attempting to click on a WidgetItem at x: " + i[0] +" y: " + i[1]);

            MoveAndClick(i[0], i[1], fast);

        } else {
            //log.info("WidgetItem CanvasBounds not Found: " + item.getId());
        }

    }

    public static void FindWidgetAndClick(Widget item, Client client,boolean fast) {

        Rectangle invItem = item.getBounds();
        //log.info("invItem Rect: x: " + invItem.x);
        int[] i = HelperTransform.xyInvItem(invItem, client);
        if (i[0] != 0) {
           // log.info("Attempting to move mouse and click");
            MoveAndClick(i[0], i[1],fast);

        }
    }

    /**
     * Find an NPC by their ID
     */
    public static int[] FindNPCLocationByID(int id, Client client, Graphics2D graphics) {
        for (NPC npc : client.getNpcs()) {
            final int npcID = npc.getId();

            npc.setModelHeight(1);
            Polygon p = npc.getConvexHull();
            if (npcID == id) {

                int[] i = HelperTransform.xyFromCenter(p, client);
                if (i[0] != 0) {

                    HelperDraw.renderPoly(graphics, Color.orange, p);
                    HelperDraw.drawCircle(graphics, (int) p.getBounds().getCenterX() - 1, (int) p.getBounds().getCenterY() - 1, 1, Color.MAGENTA);

                    // Random Click around center point
                    return new int[]{i[0], i[1]};
                }
            }
        }

        return new int[]{0, 0};
    }

    /**
     * Finds an NPC on a particular Tile
//     */
//   // public static int[] FindNPCTileLocation(Client client, Graphics2D graphics, int TileWorldX, int TileWorldY, int NPCID) {
//        for (NPC npc : client.getNpcs()) {
//            final int npcID = npc.getId();
//
//            if (npcID == NPCID) {
//
//                WorldPoint npcpoint = npc.getWorldLocation();
//                if (npcpoint.getX() == TileWorldX && npcpoint.getY() == TileWorldY) {
//                    Polygon p = npc.getConvexHull();
//
//                    int[] i = HelperTransform.xyFromCenter(p, client);
//                    if (i[0] != 0) {
//                        HelperDraw.renderPoly(graphics, Color.orange, p);
//                        HelperDraw.drawCircle(graphics, (int) p.getBounds().getCenterX() - 1, (int) p.getBounds().getCenterY() - 1, 1, Color.MAGENTA);
//
//                        // Random Click around center point
//                        return new int[]{i[0], i[1]};
//                    }
//
//                }
//            }
//
//        }
//
//        return new int[]{0, 0};
//    }

    /**
     * Finds a GameObject on a Particular tile
     */
    public static int[] FindGameObjectLocation(Client client, Graphics2D graphics, int TileWorldX, int TileWorldY, int GameObjectID, int anchorPoint) {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = client.getPlane();

        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }

                Player player = client.getLocalPlayer();
                if (player == null) {
                    continue;
                }

                GameObject[] gameObjects = tile.getGameObjects();
                if (gameObjects != null) {
                    for (GameObject gameObject : gameObjects) {
                        if (gameObject != null) {

                            WorldPoint twp = tile.getWorldLocation();
                            if (twp.getX() == TileWorldX && twp.getY() == TileWorldY) {

                           //     log.info("Tile x: " + TileWorldX + "y: " + TileWorldY);

                                if (gameObject.getId() == GameObjectID) {
                                    Polygon p = gameObject.getConvexHull();
                                    int[] i;

                                    switch (anchorPoint) {

                                        case 1:
                                            // Edges
                                            i = HelperTransform.xyFromEdge(p, client);
                                            if (i[0] != 0) {
                                              //  log.info("TileEdges x: " + i[0] + "y: " + i[1]);
                                                HelperDraw.renderPoly(graphics, Color.orange, p);
                                                HelperDraw.drawCircle(graphics, (int) p.getBounds().getCenterX() - 1, (int) p.getBounds().getCenterY() - 1, 1, Color.MAGENTA);
                                                return new int[]{i[0], i[1]};

                                            }
                                            break;
                                        case 2:
                                            // Center

                                            i = HelperTransform.xyFromCenter(p, client);
                                            if (i[0] != 0) {
                                                HelperDraw.renderPoly(graphics, Color.orange, p);
                                                HelperDraw.drawCircle(graphics, (int) p.getBounds().getCenterX() - 1, (int) p.getBounds().getCenterY() - 1, 1, Color.MAGENTA);
                                                return new int[]{i[0], i[1]};
                                            }

                                            break;
                                        case 3:  // TopLeft

                                            i = HelperTransform.xyAnchorTopLeft(p, client);
                                            if (i[0] != 0) {
                                                HelperDraw.renderPoly(graphics, Color.orange, p);
                                                HelperDraw.drawCircle(graphics, (int) p.getBounds().getMinX() + 1, (int) p.getBounds().getMinY() + 1, 1, Color.MAGENTA);
                                                return new int[]{i[0], i[1]};
                                            }
                                            break;

                                        default:
                                            return new int[]{0, 0};
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new int[]{0, 0};
    }

    /**
     * Finds a GameObject on a Particular tile
     */
    public static int[] FindGameObjectOdd(Client client, Graphics2D graphics, int GameObjectID, int anchorPoint) {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = client.getPlane();

        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }

                Player player = client.getLocalPlayer();
                if (player == null) {
                    continue;
                }

                GameObject[] gameObjects = tile.getGameObjects();
                if (gameObjects != null) {
                    for (GameObject gameObject : gameObjects) {
                        if (gameObject != null) {

                            if (gameObject.getId() == GameObjectID) {
                                Polygon p = gameObject.getConvexHull();
                                int[] i;

                                switch (anchorPoint) {

                                    case 1:
                                        // Edges

                                        i = HelperTransform.xyFromEdge(p, client);
                                        if (i[0] != 0) {
                                            HelperDraw.renderPoly(graphics, Color.orange, p);
                                            HelperDraw.drawCircle(graphics, (int) p.getBounds().getCenterX() - 1, (int) p.getBounds().getCenterY() - 1, 1, Color.MAGENTA);
                                            return new int[]{i[0], i[1]};

                                        }
                                        break;
                                    case 2:
                                        // Center

                                        i = HelperTransform.xyFromCenter(p, client);
                                        if (i[0] != 0) {
                                            HelperDraw.renderPoly(graphics, Color.orange, p);
                                            HelperDraw.drawCircle(graphics, (int) p.getBounds().getCenterX() - 1, (int) p.getBounds().getCenterY() - 1, 1, Color.MAGENTA);
                                            return new int[]{i[0], i[1]};
                                        }

                                        break;
                                    case 3:
                                        // TopLeft

                                        i = HelperTransform.xyAnchorTopLeft(p, client);
                                        if (i[0] != 0) {
                                            HelperDraw.renderPoly(graphics, Color.orange, p);
                                            HelperDraw.drawCircle(graphics, (int) p.getBounds().getMinX() + 1, (int) p.getBounds().getMinY() + 1, 1, Color.MAGENTA);
                                            return new int[]{i[0], i[1]};
                                        }
                                        break;

                                    default:
                                        return new int[]{0, 0};
                                }

//                                i = HelperTransform.xyFromCenter(p, client);
//                                if (i[0] != 0) {
//                                    HelperDraw.renderPoly(graphics, Color.orange, p);
//                                    HelperDraw.drawCircle(graphics, (int) p.getBounds().getCenterX() - 1, (int) p.getBounds().getCenterY() - 1, 1, Color.MAGENTA);
//                                    return new int[]{i[0], i[1]};
//                                }
                            }
                        }
                    }
                }
            }
        }
        return new int[]{0, 0};
    }

    /**
     * Find a Tiles screen location from its world point
     */
    public static int[] FindTileLocation(int TileWorldX, int TileWorldY, Graphics2D graphics, Client client) {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();

        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    return new int[]{0, 0};
                }

                Player player = client.getLocalPlayer();
                if (player == null) {
                    return new int[]{0, 0};
                }

                WorldPoint twp = tile.getWorldLocation();
                if (twp.getX() == TileWorldX && twp.getY() == TileWorldY) {
                    LocalPoint lp = tile.getLocalLocation();
                    int size = 1;
                    Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, lp, size);
                    int[] i = HelperTransform.xyFromCenter(tilePoly, client);
                    if (i[0] != 0 && tilePoly != null) {

                        HelperDraw.drawCircle(graphics, tilePoly.xpoints[0], tilePoly.ypoints[0], 2, Color.GREEN);
                        HelperDraw.drawCircle(graphics, tilePoly.xpoints[1], tilePoly.ypoints[1], 2, Color.ORANGE);
                        HelperDraw.drawCircle(graphics, tilePoly.xpoints[2], tilePoly.ypoints[2], 2, Color.RED);
                        HelperDraw.drawCircle(graphics, tilePoly.xpoints[3], tilePoly.ypoints[3], 2, CYAN);

                        return new int[]{i[0], i[1]};

                    }
                }
            }
        }
        return new int[]{0, 0};
    }

    /**
     * Click an area with a tiles WorldPoint at the center // 1 = 3x3 // 2 = 5x5 // 3 = 7x7
     */
    public static int[] ClickTileLargeArea(int WorldCenterX, int WorldCenterY, Graphics2D graphics, int Size, Client client) {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();

        int WorldCenterXx = HelperTransform.getRandomNumberInRange((WorldCenterX - Size), (WorldCenterX + Size));
        int WorldCenterYx = HelperTransform.getRandomNumberInRange((WorldCenterY - Size), (WorldCenterY + Size));

        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];
                if (tile == null) {
                    return new int[]{0, 0};
                }

                WorldPoint twp = tile.getWorldLocation();
                Polygon poly = Perspective.getCanvasTilePoly(client, tile.getLocalLocation());
                int[] i = HelperTransform.xyFromEdge(poly, client);
                if (i[0] != 0) {
                    if (twp.getX() == WorldCenterXx && twp.getY() == WorldCenterYx) {
                        HelperDraw.drawSquare(poly, graphics);
                        return new int[]{i[0], i[1]};
                    }
                }
            }

        }
        return new int[]{0, 0};
    }
}

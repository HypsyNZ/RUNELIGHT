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

import net.runelite.api.Player;

import java.awt.*;

import static java.awt.Color.CYAN;

public class HelperDraw {
    public static void renderPoly(Graphics2D graphics, Color color, Polygon polygon) {
        if (polygon != null) {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(2));
            graphics.draw(polygon);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
            graphics.fill(polygon);
        }
    }

    public static void renderPlayerWireframe(Graphics2D graphics, Player player, Color color) {
        Polygon[] polys = player.getPolygons();

        if (polys == null) {
            return;
        }

        graphics.setColor(color);

        for (Polygon p : polys) {
            graphics.drawPolygon(p);
        }
    }

    public static void drawCircle(Graphics g, int x, int y, int radius, Color color) {

        int diameter = radius * 2;
        g.setColor(color);
        g.fillOval(x - radius, y - radius, diameter, diameter);

    }

    public static void drawSquare(Polygon poly, Graphics2D graphics) {
        drawCircle(graphics, poly.xpoints[0], poly.ypoints[0], 2, Color.GREEN);
        drawCircle(graphics, poly.xpoints[1], poly.ypoints[1], 2, Color.ORANGE);
        drawCircle(graphics, poly.xpoints[2], poly.ypoints[2], 2, Color.RED);
        drawCircle(graphics, poly.xpoints[3], poly.ypoints[3], 2, CYAN);

    }
}

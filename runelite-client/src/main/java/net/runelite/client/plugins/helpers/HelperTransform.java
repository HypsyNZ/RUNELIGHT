package net.runelite.client.plugins.helpers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class HelperTransform {

    public static int [] xyInvItem(Rectangle r, Client client){
        try {
            double scaleFactorY = client.getStretchedDimensions().getHeight() / client.getRealDimensions().height;
            double scaleFactorX = client.getStretchedDimensions().getWidth() / client.getRealDimensions().width;

        int padx = r.getBounds().width / 6 * 2;
        int pady = r.getBounds().height / 6 * 2;

        double minx = scaleFactorX * r.getBounds().getMinX() + padx;
        double maxx = scaleFactorX * r.getBounds().getMaxX() - padx;

        double miny = scaleFactorY * r.getBounds().getMinY() + 20 + pady;
        double maxy = scaleFactorY * r.getBounds().getMaxY() + 20 - pady;

        int x = HelperTransform.getRandomNumberInRange((int) minx, (int) maxx);
        int y = HelperTransform.getRandomNumberInRange((int) miny, (int) maxy);

        if (x == 0 || y == 0) {
            return new int[]{0, 0};
        }

        return new int[]{x, y};

        } catch (NullPointerException ignore) {
          //  log.info("invItem null");
        }
        return new int[]{0, 0};
    }

    public static <T> int[] xyFromEdge(T RectPoly, Client client) {
        try {
            double scaleFactorY = client.getStretchedDimensions().getHeight() / client.getRealDimensions().height;
            double scaleFactorX = client.getStretchedDimensions().getWidth() / client.getRealDimensions().width;

          //  log.info("Scale Factor x: " + scaleFactorX + " y: " +  scaleFactorY);

            String s = RectPoly.getClass().getSimpleName().toLowerCase();

         //   log.info("Simple Type Name: " + s);

            if (s.contains("rectangle")) {

                Rectangle r = (Rectangle) RectPoly;

                int padx = r.getBounds().width / 5 * 2;
                int pady = r.getBounds().height / 5 * 2;

              //  log.info("Bounds Pad x: " + padx + " y: " +  pady);

                double minx = scaleFactorX * r.getBounds().getMinX() + padx;
                double maxx = scaleFactorX * r.getBounds().getMaxX() - padx;

                double miny = scaleFactorY * r.getBounds().getMinY() + 20 + pady;
                double maxy = scaleFactorY * r.getBounds().getMaxY() + 20 - pady;

             //   log.info("Min x: " + miny + " Max y: " +  maxy);

                int x = HelperTransform.getRandomNumberInRange((int) minx, (int) maxx);
                int y = HelperTransform.getRandomNumberInRange((int) miny, (int) maxy);

             //   log.info("xyFromEdge x: " + x + "y: " + y);
                if (x == 0 || y == 0) {
                    return new int[]{0, 0};
                }

                return new int[]{x, y};
            }

            if (s.contains("polygon")) {

                Polygon p = (Polygon) RectPoly;

                int padx = p.getBounds().width / 5 * 2;
                int pady = p.getBounds().height / 5 * 2;

               // log.info("Bounds Pad x: " + padx + " y: " +  pady);

                double minx = scaleFactorX * p.getBounds().getMinX() + padx;
                double maxx = scaleFactorX * p.getBounds().getMaxX() - padx;

                double miny = scaleFactorY * p.getBounds().getMinY() + 20 + pady;
                double maxy = scaleFactorY * p.getBounds().getMaxY() + 20 - pady;

               // log.info("Min x: " + miny + " Max y: " +  maxy);

                int x = HelperTransform.getRandomNumberInRange((int) minx, (int) maxx);
                int y = HelperTransform.getRandomNumberInRange((int) miny, (int) maxy);

               // log.info("xyFromEdge x: " + x + "y: " + y);
                if (x == 0 || y == 0) {
                    return new int[]{0, 0};
                }

                return new int[]{x, y};
            }

        } catch (NullPointerException ignore) {
         //   log.info("Null Pointer xyFromEdge");
        }
        return new int[]{0, 0};
    }

    public static <T> int[] xyAnchorTopLeft(T RectPoly, Client client) {
        try {
            double scaleFactorY = client.getStretchedDimensions().getHeight() / client.getRealDimensions().height;
            double scaleFactorX = client.getStretchedDimensions().getWidth() / client.getRealDimensions().width;

            String s = RectPoly.getClass().getSimpleName().toLowerCase();
            if (s.contains("rectangle")) {

                Rectangle r = (Rectangle) RectPoly;


                int halfx = r.getBounds().width / 2;
                int halfy = r.getBounds().height / 2;

                int padx = r.getBounds().width / 5 * 2;
                int pady = r.getBounds().height / 5 * 2;

                double minx = scaleFactorX * r.getBounds().getMinX() + padx;
                double maxx = scaleFactorX * r.getBounds().getMaxX() - halfx;

                double miny = scaleFactorY * r.getBounds().getMinY() + 20 + pady;
                double maxy = scaleFactorY * r.getBounds().getMaxY() + 20 - halfy;

                int x = HelperTransform.getRandomNumberInRange((int) minx, (int) maxx);
                int y = HelperTransform.getRandomNumberInRange((int) miny, (int) maxy);

                if (x == 0 || y == 0) {
                    return new int[]{0, 0};
                }

                return new int[]{x, y};
            }

            if (s.contains("polygon")) {

                Polygon p = (Polygon) RectPoly;

                int halfx = p.getBounds().width / 2;
                int halfy = p.getBounds().height / 2;

                int padx = p.getBounds().width / 5 * 2;
                int pady = p.getBounds().height / 5 * 2;

                double minx = scaleFactorX * p.getBounds().getMinX() + padx;
                double maxx = scaleFactorX * p.getBounds().getMaxX() - halfx;

                double miny = scaleFactorY * p.getBounds().getMinY() + 20 + pady;
                double maxy = scaleFactorY * p.getBounds().getMaxY() + 20 - halfy;

                int x = HelperTransform.getRandomNumberInRange((int) minx, (int) maxx);
                int y = HelperTransform.getRandomNumberInRange((int) miny, (int) maxy);

                if (x == 0 || y == 0) {
                    return new int[]{0, 0};
                }

                return new int[]{x, y};
            }
        } catch (NullPointerException ignore) {
        }
        return new int[]{0, 0};
    }

    public static <T> int[] xyFromCenter(T RectPoly, Client client) {
        try {
            double scaleFactorY = client.getStretchedDimensions().getHeight() / client.getRealDimensions().height;
            double scaleFactorX = client.getStretchedDimensions().getWidth() / client.getRealDimensions().width;

            String s = RectPoly.getClass().getSimpleName().toLowerCase();

            if (s.contains("rectangle")) {

                Rectangle r = (Rectangle) RectPoly;

                int padx = r.getBounds().width / 4;
                int pady = r.getBounds().height / 4;

                double xv = scaleFactorX * r.getBounds().getCenterX() - padx;
                double yv = scaleFactorY * r.getBounds().getCenterY() + 20 - pady;

                int newx = HelperTransform.getRandomNumberInRange((int) xv, (int) xv + (padx * 2));
                int newy = HelperTransform.getRandomNumberInRange((int) yv, (int) yv + (pady * 2));
                if (newx == 0 || newy == 0) {
                    return new int[]{0, 0};
                }
                return new int[]{newx, newy};

            }

            if (s.contains("polygon")) {

                Polygon p = (Polygon) RectPoly;

                int padx = p.getBounds().width / 4;
                int pady = p.getBounds().height / 4;

                double xv = scaleFactorX * p.getBounds().getCenterX() - padx;
                double yv = scaleFactorY * p.getBounds().getCenterY() + 20 - pady;

                int newx = HelperTransform.getRandomNumberInRange((int) xv, (int) xv + (padx * 2));
                int newy = HelperTransform.getRandomNumberInRange((int) yv, (int) yv + (pady * 2));
                if (newx == 0 || newy == 0) {
                    return new int[]{0, 0};
                }
                return new int[]{newx, newy};
            }
        } catch (NullPointerException ignore) {
        }
        return new int[]{0, 0};
    }

    public static int[] xyFromWidget(Widget widget, Client client) {
        if (widget != null) {
            double scaleFactorY = client.getStretchedDimensions().getHeight() / client.getRealDimensions().height;
            double scaleFactorX = client.getStretchedDimensions().getWidth() / client.getRealDimensions().width;

            int padx = widget.getBounds().width / 5 * 2;
            int pady = widget.getBounds().height / 5 * 2;

            double minx = scaleFactorX * widget.getBounds().getMinX() + padx;
            double maxx = scaleFactorX * widget.getBounds().getMaxX() - padx;

            double miny = scaleFactorY * widget.getBounds().getMinY() + 20 + pady;
            double maxy = scaleFactorY * widget.getBounds().getMaxY() + 20 - pady;

            int x = HelperTransform.getRandomNumberInRange((int) minx, (int) maxx);
            int y = HelperTransform.getRandomNumberInRange((int) miny, (int) maxy);

            if (x == 0 || y == 0) {
                return new int[]{0, 0};
            }

            return new int[]{x, y};
        }

        return new int[]{0, 0};
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            return 0;
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static double getRandomNumberInRangeDouble(double min, double max) {

        if (min >= max) {
            return 0;
        }

        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static boolean getRandomBoolean() {
        int bool = getRandomNumberInRange(1,2);
        return bool == 1;
    }


    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isNotNullOrEmpty(Integer str) {
        return str != null;
    }

    public static String getValueOrDefault(String value, String defaultValue) {
        return isNotNullOrEmpty(value) ? value : defaultValue;
    }

    public static Integer getValueOrDefault(Integer value, Integer defaultValue) {
        return isNotNullOrEmpty(value) ? value : defaultValue;
    }

    public static int searchIndex(MenuEntry[] entries, String option) {
        for (int i = entries.length - 1; i >= 0; i--) {
            MenuEntry entry = entries[i];
            String entryOption = Text.removeTags(entry.getOption()).toLowerCase();
            if (entryOption.contains(option.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    public static String convertTime(long time) {
        String finalTime = "";
        long days = (time / (24 * 60));
        long hour = (time % (24 * 60)) / 60;
        long minutes = (time % (24 * 60)) % 60;

        if (days >= 1) {

            finalTime = String.format("%02dD:%02dH:%02dM",
                    TimeUnit.DAYS.toDays(days),
                    TimeUnit.HOURS.toHours(hour),
                    TimeUnit.MINUTES.toMinutes(minutes));
            return finalTime;
        } else {
            finalTime = String.format("%02dH:%02dM",
                    TimeUnit.HOURS.toHours(hour),
                    TimeUnit.MINUTES.toMinutes(minutes));
            return finalTime;
        }
    }


}

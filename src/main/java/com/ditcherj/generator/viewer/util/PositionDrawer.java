package com.ditcherj.generator.viewer.util;

import com.ditcherj.generator.dto.Position;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan Ditcher on 30/04/2017.
 */
public class PositionDrawer {

    private static final Map<Integer, Color> COLOR_MAP;
    private static final Map<Integer, Color> STAR_COLOR_MAP;

    static {
        COLOR_MAP = new HashMap<>();
        COLOR_MAP.put(Integer.valueOf(10), new Color(225, 244, 253));
        COLOR_MAP.put(Integer.valueOf(20), new Color(199, 234, 251));
        COLOR_MAP.put(Integer.valueOf(30), new Color(171, 225, 250));
        COLOR_MAP.put(Integer.valueOf(40), new Color(142, 216, 248));
        COLOR_MAP.put(Integer.valueOf(50), new Color(109, 207, 246));
        COLOR_MAP.put(Integer.valueOf(60), new Color(68, 200, 245));
        COLOR_MAP.put(Integer.valueOf(70), new Color(0, 192, 243));
        COLOR_MAP.put(Integer.valueOf(80), new Color(0, 185, 242));
        COLOR_MAP.put(Integer.valueOf(90), new Color(0, 179, 240));
        COLOR_MAP.put(Integer.valueOf(100), new Color(0, 174, 239));

        STAR_COLOR_MAP = new HashMap<>();
        STAR_COLOR_MAP.put(Integer.valueOf(10), new Color(245, 245, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(20), new Color(245, 225, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(30), new Color(245, 200, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(40), new Color(245, 175, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(50), new Color(245, 140, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(60), new Color(245, 125, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(70), new Color(245, 100, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(80), new Color(245, 75, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(90), new Color(245, 50, 20));
        STAR_COLOR_MAP.put(Integer.valueOf(100), new Color(245, 20, 20));
    }

    public BufferedImage draw(Position position, BufferedImage image){
        image = deepCopy(image);

        Graphics2D g_image2d = image.createGraphics();
        g_image2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g_image2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int s = 120;
        int am = 220;
        int m = 310;
        int dm = 400;
        int d = 490;
        int sw = 535;
        int gk = 580;

        int center = image.getWidth() / 2;
        int left = 70;
        int right = image.getWidth() - 70;

        if(position.getSt() >1)
            drawPosition(g_image2d, position.getSt(), center, s, "FC");

        if(position.getAmc() >1)
            drawPosition(g_image2d, position.getAmc(), center, am, "AMC");

        if(position.getAml() >1)
            drawPosition(g_image2d, position.getAml(), left, am, "AML");

        if(position.getAmr() >1)
            drawPosition(g_image2d, position.getAmr(), right, am, "AMR");

        if(position.getMr() >1)
            drawPosition(g_image2d, position.getMr(), right, m, "MR");

        if(position.getMl() >1)
            drawPosition(g_image2d, position.getMl(), left, m, "ML");

        if(position.getMc() >1)
            drawPosition(g_image2d, position.getMc(), center, m, "MC");

        if(position.getWbr() >1)
            drawPosition(g_image2d, position.getWbr(), right, dm, "WBR");

        if(position.getWbl() >1)
            drawPosition(g_image2d, position.getWbl(), left, dm, "WBL");

        if(position.getDm() >1)
            drawPosition(g_image2d, position.getDm(), center, dm, "DM");

        if(position.getDr() >1)
            drawPosition(g_image2d, position.getDr(), right, d, "DR");

        if(position.getDl() >1)
            drawPosition(g_image2d, position.getDl(), left, d, "DL");

        if(position.getDc() >1)
            drawPosition(g_image2d, position.getDc(), center, d, "DC");

        if(position.getSw() >1)
            drawPosition(g_image2d, position.getSw(), center, sw, "SW");

        if(position.getGk() >1)
            drawPosition(g_image2d, position.getGk(), center, gk, "GK");

        return image;
    }

    public void drawPosition(Graphics2D g2d, int value, int x, int y, String text){
        int r = 15;
        Color color = getRangeColor(value, 1, 20);
        if(value > 11 && value < 18)
            color = color.darker();
        g2d.setColor(color);
        g2d.fillOval(x, y, r, r);
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x, y, r, r);
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y + r*2);
    }

    public static Color getRangeColor(int value, double min, double max)
    {
        return getRangeColor(value, min, max, COLOR_MAP);
    }

    public static Color getStarRangeColor(int value, double min, double max)
    {
        return getRangeColor(value, min, max, STAR_COLOR_MAP);
    }

    public static Color getRangeColor(int value, double min, double max, Map color_map)
    {
        double scale = ((double)value - min) / (max - (min - 1.0D));
        if(scale < 0.10000000000000001D)
            return (Color)color_map.get(Integer.valueOf(10));
        if(scale < 0.20000000000000001D)
            return (Color)color_map.get(Integer.valueOf(20));
        if(scale < 0.29999999999999999D)
            return (Color)color_map.get(Integer.valueOf(30));
        if(scale < 0.40000000000000002D)
            return (Color)color_map.get(Integer.valueOf(40));
        if(scale < 0.5D)
            return (Color)color_map.get(Integer.valueOf(50));
        if(scale < 0.59999999999999998D)
            return (Color)color_map.get(Integer.valueOf(60));
        if(scale < 0.69999999999999996D)
            return (Color)color_map.get(Integer.valueOf(70));
        if(scale < 0.80000000000000004D)
            return (Color)color_map.get(Integer.valueOf(80));
        if(scale < 0.90000000000000002D)
            return (Color)color_map.get(Integer.valueOf(90));
        else
            return (Color)color_map.get(Integer.valueOf(100));
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}

package com.javigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Utils {

    private static Log logger = LogFactory.getLog(Utils.class);

    public static <T> T findOneBy(Collection<T> findIn, Predicate<T> cond) {
        return findIn.stream().filter(cond).findAny().orElse(null);
    }

    public static <T> ArrayList<T> findOnesBy(Collection<T> findIn, Predicate<T> cond) {
        return  new ArrayList<T>(findIn.stream().filter(cond).collect(Collectors.toList()));
    }

    public static void info(Object o) {
        logger.info(o);
    }

    public static Color colorWithAlpha(Color color, float opacity) {
        System.out.println(Map(opacity, 0f,1f, 0f,255f));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) Map(opacity, 0f,1f, 255f,0f));
    }

    public static float Map( float value, float sourceFrom, float sourceTo, float targetFrom, float targetTo) {
        return ((value - sourceFrom) / (sourceTo - sourceFrom) * (targetTo - targetFrom) + targetFrom);
    }

    public static boolean runningFromIntelliJ()
    {
        String classPath = System.getProperty("java.class.path");
        return classPath.contains("idea_rt.jar");
    }

    //REFERENCE : https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java/37758533
    public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate(0, 0);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
    
}

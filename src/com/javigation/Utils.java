package com.javigation;

import io.mavsdk.telemetry.Telemetry;
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

    public static float CalculateBearing(Telemetry.Position pos1, Telemetry.Position pos2) {
        return CalculateBearing(pos1.getLatitudeDeg(), pos1.getLongitudeDeg(), pos2.getLatitudeDeg(), pos2.getLongitudeDeg());
    }

    public static float CalculateBearing(double lat1, double lon1, double lat2, double lon2) {
        double diff = lon2 - lon1;
        double x = Math.cos(Math.toRadians(lat2)) * Math.sin(Math.toRadians(diff));
        double y = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) - Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(diff));
        double bearing = (float) Math.atan2(x, y);
        bearing = Math.toDegrees(bearing);
        return  (float)bearing;
    }

    public static Telemetry.Position FindRelativePosition(Telemetry.Position position, double bearing, float distanceMeters) {
        double latRad = Math.toRadians(position.getLatitudeDeg());
        double lonRad = Math.toRadians(position.getLongitudeDeg());
        double bearingRad = Math.toRadians(bearing);

        double lat = Math.asin( Math.sin(latRad) * Math.cos(distanceMeters / (Statics.RADIUS_OF_EARTH * 1000)) + Math.cos(latRad) * Math.sin(distanceMeters / (Statics.RADIUS_OF_EARTH * 1000)) * Math.cos(bearingRad));
        double lon = lonRad + Math.atan2(Math.sin(bearingRad) * Math.sin(distanceMeters / (Statics.RADIUS_OF_EARTH * 1000)) * Math.cos(latRad), Math.cos(distanceMeters / (Statics.RADIUS_OF_EARTH * 1000))-Math.sin(latRad) * Math.sin(lat));

        return new Telemetry.Position(Math.toDegrees(lat), Math.toDegrees(lon), position.getAbsoluteAltitudeM(), position.getRelativeAltitudeM());
    }

    public static float DistanceBetweenCordinatesM(Telemetry.Position pos1, Telemetry.Position pos2){

        // Haversine
        double distanceLongitude = Math.toRadians(pos1.getLongitudeDeg()) - Math.toRadians(pos2.getLongitudeDeg());
        double distanceLatitude = Math.toRadians(pos1.getLatitudeDeg()) - Math.toRadians(pos2.getLatitudeDeg());
        double i = Math.pow(Math.sin(distanceLatitude / 2), 2)
                + Math.cos(Math.toRadians(pos2.getLatitudeDeg())) * Math.cos(Math.toRadians(pos1.getLatitudeDeg()))
                * Math.pow(Math.sin(distanceLongitude / 2),2);

        double j = Math.abs(2 * Math.asin(Math.sqrt(i)));

        //convert meters
        return (float)(Statics.RADIUS_OF_EARTH * j * 1000);

    }
    
}

package com.javigation.GUI.map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import com.javigation.Utils;
import com.javigation.flight.DroneController;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;

import javax.imageio.ImageIO;

public class DronePainter extends AbstractPainter<JXMapViewer>
{
    public final Map<DroneController, Integer> drones = new HashMap<DroneController, Integer>();

    private static final int ICON_COUNT = 8;
    public static final List<BufferedImage> droneIcons = new ArrayList<BufferedImage>();
    public DronePainter()
    {
        if ( droneIcons.size() == 0 )
            loadIcons();
        setAntialiasing(true);
        setCacheable(false);
    }

    private void loadIcons() {
        for( int i = 0; i < ICON_COUNT; i++ ) {
            BufferedImage loadedIcon = null;
            try {
                loadedIcon = ImageIO.read(DefaultWaypointRenderer.class.getResource("/images/droneIcons/drone" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            droneIcons.add(loadedIcon);
        }
    }

    public void addDrone(DroneController drone)
    {
        if (!drones.containsKey(drone)) {
            this.drones.put(drone, nextIcon());
        }

    }

    public void removeDrone(DroneController drone)
    {
        if (drones.containsKey(drone)) {
            this.drones.remove(drone);
        }

    }

    private int nextIcon() {
        int nextIconNumber = drones.size() % ICON_COUNT;
        while( drones.size() < ICON_COUNT && drones.containsValue(nextIconNumber) ) {
            nextIconNumber = (nextIconNumber + 1) % ICON_COUNT;
        }
        return nextIconNumber;
    }

    private JXMapViewer map;
    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height)
    {
        this.map = map;
        Rectangle viewportBounds = map.getViewportBounds();

        g.translate(-viewportBounds.getX(), -viewportBounds.getY());

        for (Map.Entry<DroneController, Integer> drone : drones.entrySet())
        {
            try {
                if (drone.getKey() != null && drone.getKey().connection.isDroneConnected && drone.getKey().Telemetry != null && drone.getKey().connection.controller.Telemetry.Position != null)
                    paintDrone(g, map, drone.getKey(), droneIcons.get(drone.getValue()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        g.translate(viewportBounds.getX(), viewportBounds.getY());

    }

    private float lastHeading = -1;
    private BufferedImage lastIcon = null;
    public void paintDrone(Graphics2D g, JXMapViewer map, DroneController drone, BufferedImage droneIcon)
    {
        Point2D point = map.getTileFactory().geoToPixel(drone.Telemetry.GeoPosition(), map.getZoom());
        int x = (int)point.getX() -droneIcon.getWidth() / 2;
        int y = (int)point.getY() -droneIcon.getHeight() / 2;

        if (lastIcon == null || (drone.Telemetry.Attitude != null && drone.Telemetry.Attitude.getYawDeg() != lastHeading) )
            lastIcon = Utils.rotateImageByDegrees(droneIcon, drone.Telemetry.Attitude.getYawDeg());

        if (lastIcon == null) {
            lastIcon = droneIcon;
            lastHeading = 0;
        }

        BufferedImage rotated = Utils.rotateImageByDegrees(droneIcon, (drone.Telemetry.Attitude != null ? drone.Telemetry.Attitude.getYawDeg() : 0));

        g.drawImage(rotated, x, y, null);
    }

}

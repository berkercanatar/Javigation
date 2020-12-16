package com.javigation.GUI.map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import com.javigation.DroneController;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;

import javax.imageio.ImageIO;

public class DronePainter extends AbstractPainter<JXMapViewer>
{
    private final Map<DroneController, Integer> drones = new HashMap<DroneController, Integer>();

    private static final int ICON_COUNT = 8;
    private static final List<BufferedImage> droneIcons = new ArrayList<BufferedImage>();
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
                paintDrone(g, map, drone.getKey(), droneIcons.get(drone.getValue()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        g.translate(viewportBounds.getX(), viewportBounds.getY());

    }

    public void paintDrone(Graphics2D g, JXMapViewer map, DroneController drone, BufferedImage droneIcon)
    {
        Point2D point = map.getTileFactory().geoToPixel(drone.GetGeoPosition(), map.getZoom());
        int x = (int)point.getX() -droneIcon.getWidth() / 2;
        int y = (int)point.getY() -droneIcon.getHeight() / 2;

        g.drawImage(droneIcon, x, y, null);
    }

}


package org.jxmapviewer.input;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Listens to single mouse clicks on the map and returns the GeoPosition
 *
 * @author Richard Eigenmann
 */
public abstract class MapClickListener extends MouseAdapter {

    private final JXMapViewer viewer;

    public enum MouseButton {
        RIGHT,
        LEFT
    }

    /**
     * Creates a mouse listener for the jxmapviewer which returns the
     * GeoPosition of the the point where the mouse was clicked.
     * 
     * @param viewer the jxmapviewer
     */
    public MapClickListener(JXMapViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Gets called on mouseClicked events, calculates the GeoPosition and fires
     * the mapClicked method that the extending class needs to implement.
     * 
     * @param evt the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent evt) {
        final boolean left = SwingUtilities.isLeftMouseButton(evt);
        final boolean right = SwingUtilities.isRightMouseButton(evt);
        final boolean singleClick = (evt.getClickCount() == 1);

        if (((left^right) && singleClick)) {
            MouseButton button = right?MouseButton.RIGHT:MouseButton.LEFT;
            Rectangle bounds = viewer.getViewportBounds();
            int x = bounds.x + evt.getX();
            int y = bounds.y + evt.getY();
            Point pixelCoordinates = new Point(x, y);
            mapClicked(button,viewer.getTileFactory().pixelToGeo(pixelCoordinates, viewer.getZoom()), evt);
        }
    }

    /**
     * This method needs to be implemented in the extending class to handle the
     * map clicked event.
     * 
     * @param location The {@link GeoPosition} of the click event
     */
    public abstract void mapClicked(MouseButton mouseButton,GeoPosition location, MouseEvent e);
}

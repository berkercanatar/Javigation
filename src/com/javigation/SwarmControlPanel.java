package com.javigation;

import com.javigation.DroneConnection;
import io.reactivex.Completable;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SwarmControlPanel extends JPanel {

    private Image droneImage;
    private JPanel dPanel;

    public SwarmControlPanel(DroneConnection connection){
        this.setPreferredSize( new Dimension( 350, 800 ) );
        this.setBackground( Color.BLUE );
        this.setLayout( new GridLayout( 7, 1) );

        for (int i = 0; i < connection.Connections.size(); i++) {
            if (connection.Connections.get(i) != null)
            {
                try {
                    droneImage = ImageIO.read(DefaultWaypointRenderer.class.getResource("/images/droneIcons/drone" + i + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dPanel = new DronePanel( droneImage, connection );
                this.add(dPanel);
            }
        }
    }
}

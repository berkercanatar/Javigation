package com.javigation.GUI;

import com.javigation.drone_link.DroneConnection;

import javax.swing.*;
import java.awt.*;

public class DronePanel extends JPanel {

    DroneConnection c;

    public DronePanel(Image droneImage, DroneConnection c){
        this.c = c;
        this.setLayout( new GridLayout( 3, 1 ) );
        this.setBackground( Color.ORANGE );
        this.add( new DroneCanvas( droneImage ) );
        this.add( new JLabel( "" + c.MavSDKPort ));
        this.add( new JLabel( "" + c.MavlinkPort ));
    }

}
class DroneCanvas extends JPanel{
    Image droneImg;
    public DroneCanvas( Image droneImg ){
        this.droneImg = droneImg;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage( droneImg, 0, 0, this);
    }
}

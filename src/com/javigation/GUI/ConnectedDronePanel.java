package com.javigation.GUI;

import com.javigation.drone_link.DroneConnection;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ConnectedDronePanel extends JPanel {
    DroneConnection connection;
    BufferedImage arm,battery,velocity;
    boolean isArmed;
    float batteryValue;
    float horizontalSpeed;
    private static final Color MAIN_COLOR = new Color(21, 53, 68);



    public ConnectedDronePanel(DroneConnection connection){
        this.setPreferredSize(new Dimension(200,200));
        this.connection = connection;
        this.setBackground(MAIN_COLOR);
        isArmed = connection.controller.Telemetry.Armed;
        batteryValue = connection.controller.Telemetry.Battery.getRemainingPercent();
        horizontalSpeed = connection.controller.Telemetry.HorizontalSpeed();
        
        try{
            arm = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/telemetry/arm.png").getPath()));
            battery = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/telemetry/battery_level.png").getPath()));
            velocity = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/telemetry/horizontal_speed.png").getPath()));

        } catch (IOException e){
        }



    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}

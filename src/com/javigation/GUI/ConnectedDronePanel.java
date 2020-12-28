package com.javigation.GUI;

import com.javigation.GUI.map.DronePainter;
import com.javigation.drone_link.DroneConnection;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class ConnectedDronePanel extends JPanel {
    DroneConnection connection;
    BufferedImage arm,battery,velocity;
    boolean isArmed;
    float batteryValue;
    double horizontalSpeed;
    private static final Color MAIN_COLOR = new Color(21, 53, 68);
    BufferedImage droneIcon;
    JCheckBox box;
    GridBagConstraints gbc = new GridBagConstraints();



    public ConnectedDronePanel(DroneConnection connection){
        this.connection = DroneConnection.Get();
        this.setPreferredSize(new Dimension(200,200));
        this.setBackground(MAIN_COLOR);
        isArmed = connection.controller.Telemetry.Armed;
        batteryValue = connection.controller.Telemetry.Battery.getRemainingPercent();
        horizontalSpeed = (double) connection.controller.Telemetry.HorizontalSpeed();

        int iconNumber = GUIManager.dronePainter.drones.get(connection.controller);
        droneIcon = DronePainter.droneIcons.get(iconNumber);
        box = new JCheckBox();
        box.setPreferredSize(new Dimension(25,25));
        setLayout(new BorderLayout(0,35 ));
        box.setLocation(140,110);
        add(box, BorderLayout.EAST);


        
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
        g.drawImage(droneIcon, 65, 30,null);
        g.drawImage(arm,10, 110,null );
        g.drawImage(battery, 50, 110, null);
        g.drawImage(velocity, 90, 110, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font( "Tahoma", Font.BOLD, 10 ));
        g.drawString("" +isArmed, 10, 143);
        g.drawString("%"+ batteryValue*100, 45, 143);
        g.drawString(new DecimalFormat("##.##").format(horizontalSpeed), 92, 143);
    }
}

package com.javigation.GUI;

import com.javigation.GUI.flight_control_panels.AutopilotControlPanel;
import com.javigation.GUI.flight_control_panels.AutopilotControlPanelButton;
import com.javigation.GUI.flight_control_panels.DroneControlPanel;
import com.javigation.GUI.map.DronePainter;
import com.javigation.drone_link.DroneConnection;
import com.javigation.flight.StateMachine;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectedDronePanel extends JPanel {
    DroneConnection connection;
    BufferedImage arm,battery,velocity;
    public static final Color MAIN_COLOR = new Color(21, 53, 68);
    BufferedImage droneIcon;
    public JCheckBox box;
    GridBagConstraints gbc = new GridBagConstraints();



    public ConnectedDronePanel(DroneConnection connection){
        this.connection = connection;
        this.setPreferredSize(new Dimension(200,150));
        setMaximumSize(getPreferredSize());
        this.setBackground(MAIN_COLOR);

        int iconNumber = GUIManager.dronePainter.drones.get(connection.controller);
        droneIcon = DronePainter.droneIcons.get(iconNumber);
        box = new JCheckBox();
        box.setPreferredSize(new Dimension(25,25));
        box.setBounds(160,110,25,25);
        add(box);

        Border border = new LineBorder(Color.ORANGE, 4, true);
        setBorder(border);
        
        try{
            arm = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/telemetry/arm.png").getPath()));
            battery = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/telemetry/battery_level.png").getPath()));
            velocity = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/telemetry/horizontal_speed.png").getPath()));
        } catch (IOException e){
            e.printStackTrace();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (DroneControlPanel.controllingDrone != connection && !connection.controller.stateMachine.CheckState(StateMachine.StateTypes.FOLLOWER)) {
                    DroneControlPanel.controllingDrone = connection;
                    AutopilotControlPanel.INSTANCE.OnStateChanged(StateMachine.StateTypes.DUMMY, true);
                }
            }
        });

        java.util.Timer updateTimer = new Timer();

        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        };

        updateTimer.schedule(updateTask, 0, 500);


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            setBackground(DroneControlPanel.controllingDrone == connection ? GUIManager.COLOR_PURPLE : MAIN_COLOR);
            g.drawImage(droneIcon, 65, 30, null);
            g.drawImage(arm, 10, 110, null);
            g.drawImage(battery, 88, 110, null);
            g.drawImage(velocity, 160, 110, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Tahoma", Font.BOLD, 10));
            g.setColor(connection.controller.Telemetry.Armed ? Color.GREEN : Color.RED);
            g.drawString(connection.controller.Telemetry.Armed ? "ARMED" : "DISARMED", 10, 143);
            g.drawString(Math.round(100 * connection.controller.Telemetry.Battery.getRemainingPercent()) + "%", 85, 143);
            g.drawString(Math.round(connection.controller.Telemetry.HorizontalSpeed()) + " m/s", 155, 143);
            g.setFont(new Font("Tahoma", Font.BOLD, 16));
            g.setColor(Color.yellow);
            if ( connection.controller.stateMachine.CheckState(StateMachine.StateTypes.LEADER)) {
                g.drawString("L", 140, 50);
            } else if (connection.controller.stateMachine.CheckState(StateMachine.StateTypes.FOLLOWER)) {
                g.drawString("F", 140, 50);
            }
        } catch (NullPointerException ex) {}
    }
}

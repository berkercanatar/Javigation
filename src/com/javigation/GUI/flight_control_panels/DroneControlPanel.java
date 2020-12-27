package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import com.javigation.flight.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class DroneControlPanel extends JPanel {

    public static DroneConnection controllingDrone;

    private static final int PANEL_WIDTH = 480;
    private static final int PANEL_HEIGHT = 180;

    private ControlStickButtons rightButtonsContainer, leftButtonsContainer;

    public static boolean IsControlling = false;

    public DroneControlPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new GridLayout(0, 2, 50, 0));
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 5, 20, Utils.colorWithAlpha(GUIManager.COLOR_PURPLE, 0.20f)));

        DroneConnection.Get();
        //DroneConnection.Get(14541);
        //DroneConnection.Get(14542);

        generateButtons();
        addMouseListener(new MouseAdapter() { });

    }

    private void generateButtons() {
        JButton ascend = new DroneControlPanelButton(this,Command.CommandType.ASCEND);
        JButton descend = new DroneControlPanelButton(this,Command.CommandType.DESCEND);
        JButton yaw_ccw = new DroneControlPanelButton(this,Command.CommandType.YAW_CCW);
        JButton yaw_cw = new DroneControlPanelButton(this,Command.CommandType.YAW_CW);

        JButton pitch_down = new DroneControlPanelButton(this,Command.CommandType.PITCH_DOWN);
        JButton pitch_up = new DroneControlPanelButton(this,Command.CommandType.PITCH_UP);
        JButton roll_left = new DroneControlPanelButton(this,Command.CommandType.ROLL_LEFT);
        JButton roll_right = new DroneControlPanelButton(this,Command.CommandType.ROLL_RIGHT);

        leftButtonsContainer = new ControlStickButtons(yaw_ccw, ascend, descend, yaw_cw);
        rightButtonsContainer = new ControlStickButtons(roll_left, pitch_down, pitch_up, roll_right);

        add(leftButtonsContainer, 0);
        add(rightButtonsContainer, 1);
    }



}

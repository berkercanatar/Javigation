package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.drone_link.mavlink.DroneConnection;
import com.javigation.flight.Command;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;

public class AutopilotControlPanel extends JPanel {

    private static final int PANEL_WIDTH = 180;
    private static final int PANEL_HEIGHT = 350;

    public AutopilotControlPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new FlowLayout( FlowLayout.LEFT));
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 5, 20, Utils.colorWithAlpha(GUIManager.COLOR_PURPLE, 0.20f)));

        generateButtons();

    }
    private void generateButtons() {

        JButton land_takeoff = new AutopilotControlPanelButton(this,
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.TAKEOFF, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanTakeOff()),
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.LAND, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanLand()));

        add(land_takeoff);

        JButton mission_pause_resume = new AutopilotControlPanelButton(this,
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.MISSION_PAUSE, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanPauseMission()),
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.MISSION_RESUME, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanResumeMission()));

        add(mission_pause_resume);

        JButton rtl = new AutopilotControlPanelButton(this,
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.RTL, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanRTL()));

        add(rtl);
    }
}

package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.flight.Command;
import com.javigation.flight.StateMachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class AutopilotControlPanel extends JPanel {

    private static final int PANEL_WIDTH = 180;
    private static final int PANEL_HEIGHT = 320;

    public static AutopilotControlPanel INSTANCE;

    public AutopilotControlPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new FlowLayout( FlowLayout.LEFT));
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 5, 20, Utils.colorWithAlpha(GUIManager.COLOR_PURPLE, 0.20f)));

        generateButtons();
        INSTANCE = this;

        addMouseListener(new MouseAdapter() { });
    }

    private void generateButtons() {

        JButton land_takeoff = new AutopilotControlPanelButton(this,
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.TAKEOFF, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanTakeOff()),
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.LAND, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanLand()));
        add(land_takeoff);

        JButton rtl = new AutopilotControlPanelButton(this,
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.RTL, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanRTL()));
        add(rtl);

        JButton mission_pause_resume = new AutopilotControlPanelButton(this,
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.MISSION_START, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanStartMission()),
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.MISSION_PAUSE, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanPauseMission()),
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.MISSION_RESUME, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanResumeMission()));
        add(mission_pause_resume);

        JButton mission_abort = new AutopilotControlPanelButton(this,
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.MISSION_ABORT, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanAbortMission()));
        add(mission_abort);

        //add(new JSeparator(JSeparator.HORIZONTAL));

        MissionUploadButton upload_mission = new MissionUploadButton(this);
        JButton plan_mission = new MissionPlanButton(this, upload_mission);
        add(plan_mission);

        add(upload_mission);

        JButton hold = new AutopilotControlPanelButton(this,
                new AutopilotControlPanelButton.ButtonFunction(Command.CommandType.HOLD, () -> DroneControlPanel.controllingDrone.controller.stateMachine.CanHold()));
        add(hold);
    }

    public void OnStateChanged(StateMachine.StateTypes changedType, boolean isAdded) {
        for ( Component comp : getComponents() ) {
            if ( comp instanceof AutopilotControlPanelButton ) {
                AutopilotControlPanelButton button = (AutopilotControlPanelButton) comp;
                ((AutopilotControlPanelButton) comp).OnStateChanged(changedType, isAdded);
            }
        }
    }

}

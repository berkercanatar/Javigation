package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.flight.CommandChain;
import com.javigation.flight.FlightMission;
import com.javigation.flight.StateMachine;
import io.mavsdk.mission.Mission;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MissionUploadButton extends JButton {

    public static MissionUploadButton INSTANCE;
    public boolean IsPlanning = false;
    private AutopilotControlPanel autopilotControlPanel;

    private ImageIcon planMissionIcon, cancelPlanMissionIcon;

    private Border redBorder, blackBorder;

    public MissionUploadButton(AutopilotControlPanel autopilotControlPanel) {
        INSTANCE = this;
        setFocusPainted(false);
        setRolloverEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIgnoreRepaint(true);

        this.autopilotControlPanel = autopilotControlPanel;

        setIgnoreRepaint(true);
        setOpaque(false);
        setContentAreaFilled(false);

        setIcon(new ImageIcon( MissionPlanButton.class.getClassLoader().getResource("images/controlPanel/upload_mission.png") ));

        redBorder = new RoundedBorder(Color.RED, 3, 10, Utils.colorWithAlpha(Color.BLACK, 0.80f));
        blackBorder = new RoundedBorder(Color.BLACK, 2, 10, Utils.colorWithAlpha(Color.BLACK, 0.80f));

        resetSizeIcon();

        setBorder(redBorder);

        createListener();

    }

    private void createListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (canUploadMission()) {
                    CommandChain.Create(DroneControlPanel.controllingDrone.controller).UploadMission(new Mission.MissionPlan(FlightMission.MissionItems)).Perform();
                }
                resetSizeIcon();
            }
        });
    }

    public void resetSizeIcon() {
        setPreferredSize( new Dimension( getIcon().getIconWidth() + 10,
                getIcon().getIconHeight() + 10));
        setBorder( canUploadMission() ? blackBorder : redBorder );
        setMinimumSize( getPreferredSize() );
        setMaximumSize( getPreferredSize() );
    }

    private boolean canUploadMission() {
        return FlightMission.MissionItems.size() > 0 && DroneControlPanel.controllingDrone != null && DroneControlPanel.controllingDrone.isDroneConnected && !DroneControlPanel.controllingDrone.controller.stateMachine.CheckState(StateMachine.StateTypes.MISSON_RUNNING);
    }

}

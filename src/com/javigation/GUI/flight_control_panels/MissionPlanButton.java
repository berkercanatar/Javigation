package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MissionPlanButton extends JButton {

    public boolean IsPlanning = false;
    private AutopilotControlPanel autopilotControlPanel;
    private MissionUploadButton missionUploadButton;

    private ImageIcon planMissionIcon, donePlanMissionIcon;

    private Border yellowBorder = new RoundedBorder(Color.YELLOW, 3, 10, Utils.colorWithAlpha(Color.CYAN, 0.80f));
    private Border greenBorder = new RoundedBorder(Color.GREEN, 3, 10, Utils.colorWithAlpha(Color.CYAN, 0.80f));

    public MissionPlanButton(AutopilotControlPanel autopilotControlPanel, MissionUploadButton missionUploadButton) {
        setFocusPainted(false);
        setRolloverEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIgnoreRepaint(true);

        this.autopilotControlPanel = autopilotControlPanel;
        this.missionUploadButton = missionUploadButton;

        setIgnoreRepaint(true);
        setOpaque(false);
        setContentAreaFilled(false);

        planMissionIcon = new ImageIcon( MissionPlanButton.class.getClassLoader().getResource("images/controlPanel/plan_mission.png") );
        donePlanMissionIcon = new ImageIcon( MissionPlanButton.class.getClassLoader().getResource("images/controlPanel/mission_plan_done.png") );

        resetSizeIcon();

        setBorder(yellowBorder);

        createListener();

    }

    private void createListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                IsPlanning = !IsPlanning;
                if (IsPlanning) {

                }
                resetSizeIcon();
            }
        });
    }

    private void resetSizeIcon() {
        setIcon( IsPlanning ? donePlanMissionIcon : planMissionIcon );
        setPreferredSize( new Dimension( getIcon().getIconWidth() + 10,
                getIcon().getIconHeight() + 10));
        setBorder( IsPlanning ? greenBorder : yellowBorder );
        setMinimumSize( getPreferredSize() );
        setMaximumSize( getPreferredSize() );
    }

}

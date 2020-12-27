package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.flight.FlightMission;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MissionUploadButton extends JButton {

    public boolean IsPlanning = false;
    private AutopilotControlPanel autopilotControlPanel;

    private ImageIcon planMissionIcon, cancelPlanMissionIcon;

    private Border redBorder, blackBorder;

    public MissionUploadButton(AutopilotControlPanel autopilotControlPanel) {
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
                IsPlanning = !IsPlanning;
                if (IsPlanning) {

                }
                resetSizeIcon();
            }
        });
    }

    private void resetSizeIcon() {
        setPreferredSize( new Dimension( getIcon().getIconWidth() + 10,
                getIcon().getIconHeight() + 10));
        setBorder( FlightMission.Mission == null ? redBorder : blackBorder );
        setMinimumSize( getPreferredSize() );
        setMaximumSize( getPreferredSize() );
    }

}

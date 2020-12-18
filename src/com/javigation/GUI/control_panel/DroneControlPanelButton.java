package com.javigation.GUI.control_panel;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.flight.Command;
import com.javigation.flight.CommandChain;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DroneControlPanelButton extends JButton {

    private DroneControlPanel droneControlPanel;
    private Command.CommandType buttonType;

    private static Map<Command.CommandType, ImageIcon> buttonIcons;

    public DroneControlPanelButton(DroneControlPanel droneControlPanel, Command.CommandType buttonType) {

        this.droneControlPanel = droneControlPanel;
        this.buttonType = buttonType;

        if ( buttonIcons == null ) {
            buttonIcons = new HashMap< Command.CommandType, ImageIcon >();
            for ( Command.CommandType type : Command.CommandType.values() ) {
                URL iconPath = DroneControlPanelButton.class.getClassLoader().getResource("images/controlPanel/" + type.name().toLowerCase(Locale.ENGLISH) + ".png");
                if (iconPath != null)
                    buttonIcons.put( type , new ImageIcon( iconPath ));
            }
        }
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 3, 0, Utils.colorWithAlpha(GUIManager.COLOR_BLUE, 1f)));
        setIcon( buttonIcons.get(buttonType));
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setText(buttonType.name());

        createListener();
        
    }

    private void createListener() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (buttonType) {
                    case TAKEOFF:
                    case LAND:
                    case ASCEND:
                    case DESCEND:
                    case RTL:
                    case MISSION_PAUSE:
                    case MISSION_RESUME:
                    case MISSION_ABORT:
                    case PITCH_DOWN:
                    case PITCH_UP:
                    case ROLL_LEFT:
                    case ROLL_RIGHT:
                    case YAW_CCW:
                    case YAW_CW:
                }
            }
        });
    }
}

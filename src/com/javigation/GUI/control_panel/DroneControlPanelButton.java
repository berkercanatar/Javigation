package com.javigation.GUI.control_panel;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DroneControlPanelButton extends JButton {

    private DroneControlPanel droneControlPanel;
    private ButtonType buttonType;

    public static enum ButtonType {

        TAKEOFF,
        LAND,
        ASCEND,
        DESCEND,
        RTL,
        MISSION_PAUSE,
        MISSION_RESUME,
        MISSION_ABORT,
        PITCH_DOWN,
        PITCH_UP,
        ROLL_LEFT,
        ROLL_RIGHT,
        YAW_CCW,
        YAW_CW,

    }

    private static Map<ButtonType, ImageIcon> buttonIcons;

    public DroneControlPanelButton(DroneControlPanel droneControlPanel, ButtonType buttonType) {

        this.droneControlPanel = droneControlPanel;
        this.buttonType = buttonType;

        if ( buttonIcons == null ) {
            buttonIcons = new HashMap< ButtonType, ImageIcon >();
            for ( ButtonType type : ButtonType.values() ) {
                buttonIcons.put( type , new ImageIcon( DroneControlPanelButton.class.getClassLoader().getResource("images/controlPanel/" + type.name().toLowerCase(Locale.ENGLISH) + ".png")));
            }
        }
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 3, 0, Utils.colorWithAlpha(GUIManager.COLOR_BLUE, 1f)));
        setIcon( buttonIcons.get(buttonType));
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setText(buttonType.name());

    }
}

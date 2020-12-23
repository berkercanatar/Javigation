package com.javigation.GUI.control_panel;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.drone_link.mavlink.DroneConnection;
import com.javigation.flight.Command;
import com.javigation.flight.CommandChain;
import jdk.jshell.execution.Util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        DroneConnection.Get();
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 3, 0, Utils.colorWithAlpha(GUIManager.COLOR_BLUE, 1f)));
        setIcon( buttonIcons.get(buttonType));
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);

        setFont(new Font("TimesRoman", Font.BOLD, 7));

        setText(buttonType.name());

        setPreferredSize( new Dimension( buttonIcons.get(buttonType).getIconWidth(), (buttonIcons.get(buttonType).getIconHeight())+20 ));


        createListener();
        
    }

    private long last = 0;

    private void createListener() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (buttonType) {
                    case TAKEOFF:
                        DroneConnection.Get().drone.getTelemetry().getPosition().sample(2, TimeUnit.SECONDS).subscribe(isArmed -> {
                            //System.out.println("subscribe");
                            long now = Calendar.getInstance().getTimeInMillis();
                            System.out.println(now - last);
                            last = now;
                            //(isArmed ? DroneConnection.Get().controller.TakeOff() : DroneConnection.Get().controller.Arm().andThen(DroneConnection.Get().controller.TakeOff())).subscribe();
                        });
                        break;
                    case LAND:
                        DroneConnection.Get().controller.Land().subscribe();
                        break;
                    case ASCEND:
                        GUIManager.dronePainter.addDrone(DroneConnection.Get().controller);
                        break;
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

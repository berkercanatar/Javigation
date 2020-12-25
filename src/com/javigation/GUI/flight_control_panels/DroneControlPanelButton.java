package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.RoundedBorder;
import com.javigation.GUI.popup.PopupManager;
import com.javigation.Statics;
import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import com.javigation.flight.Command;
import io.mavsdk.offboard.Offboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DroneControlPanelButton extends JButton {

    private DroneControlPanel droneControlPanel;
    private Command.CommandType buttonType;

    private static Map<Command.CommandType, ImageIcon> _buttonIcons;
    public static Map<Command.CommandType, ImageIcon> buttonIcons() {
        if (_buttonIcons != null) {
            return _buttonIcons;
        }

        _buttonIcons = new HashMap< Command.CommandType, ImageIcon >();
        for ( Command.CommandType type : Command.CommandType.values() ) {
            URL iconPath = DroneControlPanelButton.class.getClassLoader().getResource("images/controlPanel/" + type.name().toLowerCase(Locale.ENGLISH) + ".png");
            if (iconPath != null)
                _buttonIcons.put( type , new ImageIcon( iconPath ));
        }
        return  _buttonIcons;
    }

    public DroneControlPanelButton(DroneControlPanel droneControlPanel, Command.CommandType buttonType) {
        setFocusPainted(false);
        setRolloverEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIgnoreRepaint(true);
        this.droneControlPanel = droneControlPanel;
        this.buttonType = buttonType;
        setIgnoreRepaint(true);
        setOpaque(false);
        setContentAreaFilled(false);

        DroneConnection.Get();

        setPreferredSize( new Dimension( buttonIcons().get(buttonType).getIconWidth() + 10, buttonIcons().get(buttonType).getIconHeight() + 10));
        setMinimumSize( getPreferredSize() );
        setMaximumSize( getPreferredSize() );

        setBorder(new RoundedBorder(Color.BLACK, 2, 10, Utils.colorWithAlpha(Color.BLACK, 0.80f)));
        setIcon(buttonIcons().get(buttonType));

        createListener();
        
    }

    private long last = 0;

    private void createListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            Offboard.VelocityBodyYawspeed rollLeftMotion = new Offboard.VelocityBodyYawspeed(0f, -Statics.MANUAL_CONTROL_HORIZONTAL_VELOCITY_MS, 0f, 0f);
            Offboard.VelocityBodyYawspeed rollRightMotion = new Offboard.VelocityBodyYawspeed(0f, Statics.MANUAL_CONTROL_HORIZONTAL_VELOCITY_MS, 0f, 0f);
            Offboard.VelocityBodyYawspeed pitchDownMotion = new Offboard.VelocityBodyYawspeed(Statics.MANUAL_CONTROL_HORIZONTAL_VELOCITY_MS, 0f, 0f, 0f);
            Offboard.VelocityBodyYawspeed pitchUpMotion = new Offboard.VelocityBodyYawspeed(-Statics.MANUAL_CONTROL_HORIZONTAL_VELOCITY_MS, 0f, 0f, 0f);
            Offboard.VelocityBodyYawspeed yawCwMotion = new Offboard.VelocityBodyYawspeed(0f, 0f, 0f, Statics.MANUAL_CONTROL_YAW_DEG_S);
            Offboard.VelocityBodyYawspeed yawCcwMotion = new Offboard.VelocityBodyYawspeed(0f, 0f, 0f, -Statics.MANUAL_CONTROL_YAW_DEG_S);
            Offboard.VelocityBodyYawspeed ascendMotion = new Offboard.VelocityBodyYawspeed(0f, 0f, -Statics.MANUAL_CONTROL_VERTICAL_VELOCITY_MS, 0f);
            Offboard.VelocityBodyYawspeed descendMotion = new Offboard.VelocityBodyYawspeed(0f, 0f, Statics.MANUAL_CONTROL_VERTICAL_VELOCITY_MS, 0f);

            @Override
            public void mousePressed(MouseEvent e) {
                PopupManager.showInfo("Mouse Pressed");
                Offboard.VelocityBodyYawspeed motion;
                switch (buttonType) {
                        //DroneConnection.Get().drone.getTelemetry().getPosition().sample(2, TimeUnit.SECONDS).subscribe(isArmed -> {
                    case PITCH_DOWN:
                        motion = pitchDownMotion;
                        break;
                    case PITCH_UP:
                        motion = pitchUpMotion;
                        break;
                    case ROLL_LEFT:
                        motion = rollLeftMotion;
                        break;
                    case ROLL_RIGHT:
                        motion = rollRightMotion;
                        break;
                    case YAW_CCW:
                        motion = yawCcwMotion;
                        break;
                    case YAW_CW:
                        motion = yawCwMotion;
                        break;
                    case ASCEND:
                        motion = ascendMotion;
                        break;
                    case DESCEND:
                        motion = descendMotion;
                        break;
                    default:
                        return;
                }
                DroneControlPanel.controllingDrone.drone.getOffboard().setVelocityBody(motion).andThen( DroneControlPanel.controllingDrone.drone.getOffboard().start() ).subscribe();
            }

            private Offboard.VelocityBodyYawspeed holdMotion = new Offboard.VelocityBodyYawspeed(0f, 0f, 0f, 0f);

            @Override
            public void mouseReleased(MouseEvent e) {
                DroneControlPanel.controllingDrone.drone.getOffboard().setVelocityBody(holdMotion).subscribe();
            }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}

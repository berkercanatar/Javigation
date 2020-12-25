package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.drone_link.mavlink.DroneConnection;
import com.javigation.flight.Command;
import com.javigation.flight.CommandChain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BooleanSupplier;

public class AutopilotControlPanelButton extends JButton{

    private AutopilotControlPanel autopilotControlPanel;
    private ButtonFunction[] buttonFunctions;
    private ButtonFunction activeType;

    public static class ButtonFunction {
        public Command.CommandType CommandType;
        public BooleanSupplier Condition;

        public ButtonFunction(Command.CommandType commandType, BooleanSupplier condition) {
            CommandType = commandType;
            Condition = condition;
        }

        public Boolean check() {
            return Condition.getAsBoolean();
        }
    }

    public AutopilotControlPanelButton(AutopilotControlPanel autopilotControlPanel, ButtonFunction... buttonFunctions) {

        setFocusPainted(false);
        setRolloverEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIgnoreRepaint(true);
        activeType = buttonFunctions[0];

        this.buttonFunctions = buttonFunctions;
        this.autopilotControlPanel = autopilotControlPanel;

        setIgnoreRepaint(true);
        setOpaque(false);
        setContentAreaFilled(false);

        resetSizeIcon();

        setBorder(new RoundedBorder(Color.BLACK, 2, 10, Utils.colorWithAlpha(Color.BLACK, 0.80f)));


        createListener();

        startTimer();

    }

    private void startTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for (ButtonFunction buttonFunction : buttonFunctions) {
                    try {
                        Utils.info(DroneControlPanel.controllingDrone.controller.stateMachine);
                        if (buttonFunction.check()) {
                            setVisible(true);
                            activeType = buttonFunction;
                            resetSizeIcon();
                            return;
                        }
                    } catch (NullPointerException ex) {
                        if (!(activeType == buttonFunctions[0])) {
                            activeType = buttonFunctions[0];
                            resetSizeIcon();
                        }
                    }
                }
                setVisible(false);
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 500);

    }

    private void createListener() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (activeType.CommandType) {
                    case TAKEOFF:
                        if (activeType.check())
                            CommandChain.Create(DroneControlPanel.controllingDrone.controller).TakeOff(15f).Perform();
                        break;
                    case LAND:
                        if (activeType.check())
                            DroneConnection.Get().controller.Land().subscribe();
                        break;
                    case ASCEND:
                        break;
                    case DESCEND:
                    case RTL:
                    case MISSION_PAUSE:
                    case MISSION_RESUME:
                    case MISSION_ABORT:
                }
            }
        });
    }

    private void resetSizeIcon() {
        setIcon( DroneControlPanelButton.buttonIcons().get(activeType.CommandType));
        setPreferredSize( new Dimension( DroneControlPanelButton.buttonIcons().get(activeType.CommandType).getIconWidth() + 10,
                                        DroneControlPanelButton.buttonIcons().get(activeType.CommandType).getIconHeight() + 10));
        setMinimumSize( getPreferredSize() );
        setMaximumSize( getPreferredSize() );
    }

}


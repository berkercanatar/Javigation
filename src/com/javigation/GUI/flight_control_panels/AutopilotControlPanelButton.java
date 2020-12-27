package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.flight.Command;
import com.javigation.flight.CommandChain;
import com.javigation.flight.StateMachine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BooleanSupplier;

public class AutopilotControlPanelButton extends JButton{

    private AutopilotControlPanel autopilotControlPanel;
    private ButtonFunction[] buttonFunctions;
    private ButtonFunction activeType;
    private Border redBorder, blackBorder;

    public void OnStateChanged(StateMachine.StateTypes changedType, boolean isAdded) {
        for (ButtonFunction buttonFunction : buttonFunctions) {
            try {
                if (buttonFunction.check()) {
                    changeEnabled(true);
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
        changeEnabled(false);
    }

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

        redBorder = new RoundedBorder(Color.RED, 3, 10, Utils.colorWithAlpha(Color.BLACK, 0.80f));
        blackBorder = new RoundedBorder(Color.BLACK, 2, 10, Utils.colorWithAlpha(Color.BLACK, 0.80f));
        changeEnabled(false);


        createListener();

    }


    private void createListener() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getBorder() == redBorder)
                    return;
                switch (activeType.CommandType) {
                    case TAKEOFF:
                        if (activeType.check())
                            CommandChain.Create(DroneControlPanel.controllingDrone.controller).TakeOff(12f).Perform();
                        break;
                    case LAND:
                        if (activeType.check())
                            CommandChain.Create(DroneControlPanel.controllingDrone.controller).Land().Perform();
                        break;
                    case RTL:
                        if(activeType.check())
                            CommandChain.Create(DroneControlPanel.controllingDrone.controller).RTL().Perform();
                        break;
                    case MISSION_PAUSE:
                        if(activeType.check())
                            CommandChain.Create(DroneControlPanel.controllingDrone.controller).MissionPause().Perform();
                            break;
                    case MISSION_RESUME:
                        if(activeType.check())
                            CommandChain.Create(DroneControlPanel.controllingDrone.controller).MissionResume().Perform();
                        break;
                    case MISSION_ABORT:
                        break;
                    case HOLD:
                        if(activeType.check())
                            CommandChain.Create(DroneControlPanel.controllingDrone.controller).Hold().Perform();
                        break;
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

    private void changeEnabled(boolean isEnabled) {
        setBorder(isEnabled ? blackBorder : redBorder);
        if (!isEnabled) {
            activeType = buttonFunctions[0];
            resetSizeIcon();
        }
    }

}


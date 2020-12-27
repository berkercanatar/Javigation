package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.GUIManager;
import com.javigation.drone_link.DroneConnection;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import com.javigation.flight.DroneController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.function.Function;

public class TelemetryPanel extends JPanel {

    public enum TelemType {
        AIR_SPEED,
        ALTITUDE,
        ARM,
        BATTERY_LEVEL,
        FLIGHT_TIME,
        HOME_DISTANCE,
        HORIZONTAL_SPEED,
        ROLL,
        PITCH,
        LATITUDE,
        LONGITUDE,
        MODE,
        PASS,
        SATELLITE,
        VERTICAL_SPEED,
        VOLTAGE,
        YAW,
    }

    public static TelemetryPanel INSTANCE;

    public static final ArrayList<TelemetryComponent> TelemetryComponents = new ArrayList<TelemetryComponent>();

    public TelemetryPanel() {
        INSTANCE = this;

        //setLayout(new SpringLayout());
        setLayout(new FlowLayout(FlowLayout.LEADING, 5, 4));
        setBackground(GUIManager.COLOR_BLUE);

        Function<DroneController, DroneTelemetry> function = controller -> controller.Telemetry;

        TelemetryComponents.add( new TelemetryComponent("BATTERY", TelemType.BATTERY_LEVEL, function, "%" ) );
        TelemetryComponents.add( new TelemetryComponent("VOLT", TelemType.VOLTAGE, function, "V" ) );
        TelemetryComponents.add( new TelemetryComponent("GPS SATS", TelemType.SATELLITE, function ) );
        TelemetryComponents.add( new TelemetryComponent("ALT", TelemType.ALTITUDE, function, "m" ) );
        TelemetryComponents.add( new TelemetryComponent("HEADING", TelemType.YAW, function, "°" ) );
        TelemetryComponents.add( new TelemetryComponent("H.SPEED", TelemType.HORIZONTAL_SPEED, function, "m/s" ) );
        TelemetryComponents.add( new TelemetryComponent("V.SPEED", TelemType.VERTICAL_SPEED, function, "m/s" ) );
        TelemetryComponents.add( new TelemetryComponent("HOME", TelemType.HOME_DISTANCE, function, "m" ) );
        TelemetryComponents.add( new TelemetryComponent(TelemType.ARM, function) );
        TelemetryComponents.add( new TelemetryComponent("MODE", TelemType.MODE, function ) );

        for ( TelemetryComponent telemetryComponent : TelemetryComponents ) {
            add(telemetryComponent);
        }

        java.util.Timer telemetryTimer = new java.util.Timer();

        TimerTask telemetryTimerTask = new TimerTask() {
            @Override
            public void run() {
                for ( TelemetryComponent telemetryComponent : TelemetryComponents ) {
                    if (DroneControlPanel.controllingDrone != null)
                        telemetryComponent.Update(DroneControlPanel.controllingDrone.controller);
                }
            }
        };

        telemetryTimer.schedule(telemetryTimerTask, 0, 200);

    }

}

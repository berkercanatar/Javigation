package com.javigation.GUI.flight_control_panels;

import com.javigation.drone_link.DroneConnection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TimerTask;

public class TelemetryPanel extends JPanel {

    public enum TelemType {
        AIR_SPEED,
        ALTITUDE,
        ARM,
        BATTERY_LEVEL,
        FLIGHT_TIME,
        HOME_DISTANCE,
        HORIZONTAL_SPEED,
        IMU,
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

        TelemetryComponents.add( new TelemetryComponent("BATTERY", TelemType.BATTERY_LEVEL, controller -> {
            return controller.Telemetry.Battery.getRemainingPercent();
        }, "%" ) );

        for ( TelemetryComponent telemetryComponent : TelemetryComponents ) {
            add(telemetryComponent);
        }

        java.util.Timer telemetryTimer = new java.util.Timer();

        TimerTask telemetryTimerTask = new TimerTask() {
            @Override
            public void run() {
                for ( TelemetryComponent telemetryComponent : TelemetryComponents ) {
                    telemetryComponent.Update(DroneControlPanel.controllingDrone.controller);
                }
            }
        };

        telemetryTimer.schedule(telemetryTimerTask, 0, 200);

    }

}

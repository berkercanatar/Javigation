package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Statics;
import com.javigation.Utils;
import com.javigation.flight.DroneController;
import io.mavsdk.telemetry.Telemetry;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Locale;
import java.util.function.Function;

public class TelemetryComponent extends JPanel {



    JLabel labelValue;
    Function<DroneController, Object> function;
    TelemetryPanel.TelemType telemType;
    String unit;

    public TelemetryComponent(String text, TelemetryPanel.TelemType telemType , Function<DroneController, Object> function) {
        this(text, telemType, function, null);
    }

    public TelemetryComponent(String text, TelemetryPanel.TelemType telemType , Function<DroneController, Object> function, String unit) {
        this.unit = unit;
        setPreferredSize( new Dimension(100, 30));
        JLabel labelHeader = new JLabel(text + ":", new ImageIcon(TelemetryComponent.class.getClassLoader().
                getResource("images/telemetry/" + telemType.name().toLowerCase(Locale.ENGLISH) + ".png")), JLabel.LEFT);

        labelValue = new JLabel("?");
        labelValue.setForeground(Statics.TELEMETRY_DEFAULT_COLOR);

        this.function = function;
        this.telemType = telemType;
    }

    public void Update(DroneController controller) {
        if (DroneControlPanel.controllingDrone == null) {
            labelValue.setText("?");
            labelValue.setForeground(Statics.TELEMETRY_DEFAULT_COLOR);
            return;
        }
        Object value = function.apply(controller);
        switch (telemType) {
            case ARM:
                boolean armed = (boolean)value;
                labelValue.setText( armed ? "ARMED" : "DISARMED" );
                labelValue.setForeground( armed ? Color.green : Color.red );
                break;

            case PASS:
                boolean pass = (boolean) value;
                labelValue.setText( "PREFLIGHT CHECK " + (pass ? "PASS" : "FAIL"));
                labelValue.setForeground( pass ? Color.green : Color.red );
                break;

            case BATTERY_LEVEL:
                float battery_percent = (float) value;
                labelValue.setText(value.toString());
                labelValue.setForeground((battery_percent>60f) ? Color.green : ((battery_percent>40f) ? Color.yellow : ((battery_percent>25f) ? Color.yellow : Color.red)));
                break;

            case IMU:
                float degree = (float) value;
                labelValue.setText(value.toString());
                labelValue.setForeground( (degree>30f) ? Color.orange: Statics.TELEMETRY_DEFAULT_COLOR);
                break;

            case ALTITUDE:
                float altitude = (float) value;
                labelValue.setText(value.toString());
                labelValue.setForeground( (altitude > 10f) ? Statics.TELEMETRY_DEFAULT_COLOR : Color.orange);
                break;

            case HOME_DISTANCE:
                float home_distance = (float) value;
                labelValue.setText( value.toString());
                labelValue.setForeground( (home_distance < 50f) ? Color.green : ((home_distance < 75) ? Color.orange : Color.red));
                break;

            case MODE:
                Telemetry.FlightMode mode = (Telemetry.FlightMode) value;
                labelValue.setText(value.toString());
                labelValue.setForeground(Color.blue);
                break;

            default:
                labelValue.setText(value.toString());
        }
        labelValue.setText(labelValue.getText() + (unit != null ? " " + unit : ""));
    }
}

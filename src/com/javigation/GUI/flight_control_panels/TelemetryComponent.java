package com.javigation.GUI.flight_control_panels;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Statics;
import com.javigation.Utils;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import com.javigation.flight.DroneController;
import io.mavsdk.telemetry.Telemetry;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Locale;
import java.util.function.Function;

public class TelemetryComponent extends JPanel {



    JLabel labelValue;
    Function<DroneController, DroneTelemetry> function;
    TelemetryPanel.TelemType telemType;
    String unit;

    public TelemetryComponent(String text, TelemetryPanel.TelemType telemType , Function<DroneController, DroneTelemetry> function) {
        this(text, telemType, function, null);
    }

    public TelemetryComponent(TelemetryPanel.TelemType telemType , Function<DroneController, DroneTelemetry> function) {
        this(null, telemType, function, null);
    }


    public TelemetryComponent(String text, TelemetryPanel.TelemType telemType , Function<DroneController, DroneTelemetry> function, String unit) {
        this.unit = unit;
        setPreferredSize( new Dimension(telemType == TelemetryPanel.TelemType.MODE ? 250 : 170, 30));
        setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
        Utils.info(telemType.name());
        JLabel labelHeader = new JLabel((text != null ? text + ":" : ""), new ImageIcon(TelemetryComponent.class.getClassLoader().
                getResource("images/telemetry/" + telemType.name().toLowerCase(Locale.ENGLISH) + ".png")), JLabel.LEFT);
        labelHeader.setForeground(Color.black);
        labelHeader.setFont(new Font("ComicSansMS", Font.BOLD, 14));

        labelValue = new JLabel("?");
        labelValue.setForeground(Statics.TELEMETRY_DEFAULT_COLOR);
        labelValue.setFont(new Font("ComicSansMS", Font.BOLD, 14));

        this.function = function;
        this.telemType = telemType;
        setBackground(TelemetryPanel.INSTANCE.getBackground());

        add(labelHeader);
        add(labelValue);
    }

    public void Update(DroneController controller) {
        if (DroneControlPanel.controllingDrone == null) {
            labelValue.setText("?");
            labelValue.setForeground(Statics.TELEMETRY_DEFAULT_COLOR);
            return;
        }
        try {
            String text = labelValue.getText();
            Color color = labelValue.getForeground();
            DroneTelemetry telem = function.apply(controller);
            switch (telemType) {

                case AIR_SPEED:
                    text = String.valueOf(Math.round(telem.AirSpeed()));
                    break;

                case ALTITUDE:
                    float altitude = telem.Position.getRelativeAltitudeM();
                    text = String.format("%.2f", altitude);
                    color = (altitude > 10f) ? Statics.TELEMETRY_DEFAULT_COLOR : Color.orange;
                    break;

                case ARM:
                    boolean armed = telem.Armed;
                    text = armed ? "ARMED" : "DISARMED";
                    color = armed ? Color.green : Color.red;
                    break;

                case BATTERY_LEVEL:
                    int battery_percent = (int)(telem.Battery.getRemainingPercent() * 100);
                    text = String.valueOf(battery_percent);
                    color = (battery_percent>60f) ? Color.green : ((battery_percent>40f) ? Color.orange : ((battery_percent>25f) ? Color.yellow : Color.red));
                    break;

                case FLIGHT_TIME:

                    break;

                case HOME_DISTANCE:
                    float home_distance = Math.round(Utils.DistanceBetweenCordinatesM(telem.Position, telem.Home));
                    text = String.valueOf(home_distance);
                    color = (home_distance < 50f) ? Color.green : ((home_distance < 75) ? Color.orange : Color.red);
                    break;

                case HORIZONTAL_SPEED:
                    text = String.valueOf(Math.round(telem.HorizontalSpeed()));
                    break;

                case LATITUDE:
                    text = String.valueOf(telem.Position.getLatitudeDeg());
                    break;

                case LONGITUDE:
                    text = String.valueOf(telem.Position.getLongitudeDeg());
                    break;

                case MODE:
                    Telemetry.FlightMode mode = telem.FlightMode;
                    text = mode.toString();
                    color = Color.CYAN;
                    break;

                case PASS:
                    boolean pass = telem.CheckPassed;
                    text = "PREFLIGHT CHECK " + (pass ? "PASS" : "FAIL");
                    color = pass ? Color.green : Color.red;
                    break;

                case PITCH:
                    float pitchAngle = Math.round(telem.Attitude.getPitchDeg());
                    text = String.valueOf(pitchAngle);
                    color = (pitchAngle>30f) ? Color.orange: Statics.TELEMETRY_DEFAULT_COLOR;
                    break;

                case ROLL:
                    float rollAngle = Math.round(telem.Attitude.getRollDeg());
                    text = String.valueOf(rollAngle);
                    color = (rollAngle>30f) ? Color.orange: Statics.TELEMETRY_DEFAULT_COLOR;
                    break;

                case SATELLITE:
                    int sats = telem.GPS.getNumSatellites();
                    text = String.valueOf(sats);
                    color = sats < 5 ? Color.RED : Color.GREEN;
                    break;

                case VERTICAL_SPEED:
                    text = String.format("%.1f", -telem.Velocity.getDownMS());
                    break;

                case VOLTAGE:
                    text = String.format("%.1f", telem.Battery.getVoltageV());
                    break;

                case YAW:
                    text = String.valueOf(Math.round(telem.Attitude.getYawDeg()));
                    break;
            }
            labelValue.setText(text + (unit != null ? unit : ""));
            labelValue.setForeground(color);
        } catch (NullPointerException ex) {

        }
    }
}

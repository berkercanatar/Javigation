package com.javigation.drone_link.mavlink;

import com.javigation.flight.DroneController;
import io.mavsdk.System;
import io.mavsdk.telemetry.Telemetry;
import org.jxmapviewer.viewer.GeoPosition;

public class DroneTelemetry {

    public DroneConnection connection;
    private DroneController controller;
    private System drone;

    public DroneTelemetry(DroneConnection connection) {
        this.connection = connection;
        controller = connection.controller;
        drone = controller.drone;
    }


    public boolean Armed;
    public boolean InAir;


    public Telemetry.Position Position;
    public GeoPosition GeoPosition() { return new GeoPosition(Position.getLatitudeDeg(), Position.getLongitudeDeg()); }


}

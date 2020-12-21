package com.javigation.drone_link.mavlink;

import com.javigation.Utils;
import com.javigation.flight.DroneController;
import io.mavsdk.System;
import java.util.ArrayList;

public class DroneConnection implements MavSDKServerReadyListener {

    public static ArrayList<DroneConnection> Connections = new ArrayList<DroneConnection>();

    public MavSDKServer server;
    public System drone;
    public DroneController controller;
    public int MavlinkPort;
    public int MavSDKPort;
    public int VideoPort;

    private static final int MAVLINK_BASE_PORT = 14540;
    private static final int MAVSDK_SERVER_BASE_PORT = 4790;
    private static final int VIDEO_BASE_PORT = 5600;

    private boolean isDroneConnected = false;

    private DroneConnection(int incomingMavlinkPort) {
        this(incomingMavlinkPort, MAVSDK_SERVER_BASE_PORT + (incomingMavlinkPort - MAVLINK_BASE_PORT), VIDEO_BASE_PORT + (incomingMavlinkPort - MAVLINK_BASE_PORT));
    }

    private DroneConnection(int incomingMavlinkPort, int localMavSDKPort, int videoPort) {
        MavlinkPort = incomingMavlinkPort;
        MavSDKPort = localMavSDKPort;
        VideoPort = videoPort;
        server = new MavSDKServer(this, incomingMavlinkPort, localMavSDKPort);
    }

    public static DroneConnection Get() {
        return Get(MAVLINK_BASE_PORT);
    }

    public static DroneConnection Get(int mavlinkPort) {
        DroneConnection connection = Utils.findOneBy(Connections, con -> con.MavlinkPort == mavlinkPort);
        if ( connection == null )
            connection = new DroneConnection(mavlinkPort);
            Connections.add(connection);
        return connection;
    }
    public static DroneConnection Get(DroneConnection droneConnection) { //Ensures given DroneConnection is in the static list.
        DroneConnection connection = Utils.findOneBy(Connections, con -> con.MavlinkPort == droneConnection.MavlinkPort);
        if ( connection == null )
            Connections.add(droneConnection);
        return connection;
    }

    public static DroneConnection GetByVideoPort(int videoPort) {
        return Utils.findOneBy(Connections, con -> con.VideoPort == videoPort);
    }
    @Override
    public void onServerInitialized() {
        drone = new System("127.0.0.1", MavSDKPort);
        DroneConnection connection = this;
    }

    public static void onDroneConnected(DroneConnection connection) {
        if (connection.isDroneConnected)
            return;
        connection.isDroneConnected = true;
        connection.controller = new DroneController(connection);
        connection.controller.Telemetry = new DroneTelemetry(connection);
        connection.controller.SubscribeForTelemetry();

        java.lang.System.out.println("CONNECTED " + connection.MavlinkPort);
    }

    public static void onDroneDisconnected(DroneConnection connection) {
        if (!connection.isDroneConnected)
            return;
        connection.isDroneConnected = false;

        java.lang.System.out.println("DISCONNECTED " + connection.MavlinkPort);
    }


}

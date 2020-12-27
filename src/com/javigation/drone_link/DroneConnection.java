package com.javigation.drone_link;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.flight_control_panels.DroneControlPanel;
import com.javigation.Utils;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import com.javigation.drone_link.mavlink.MavSDKServer;
import com.javigation.drone_link.mavlink.MavSDKServerReadyListener;
import com.javigation.flight.DroneController;
import com.javigation.flight.StateMachine;
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

    public boolean isDroneConnected = false;

    private DroneConnection(int incomingMavlinkPort) {
        this(incomingMavlinkPort, MAVSDK_SERVER_BASE_PORT + (incomingMavlinkPort - MAVLINK_BASE_PORT), VIDEO_BASE_PORT + (incomingMavlinkPort - MAVLINK_BASE_PORT));
    }

    private DroneConnection(int incomingMavlinkPort, int localMavSDKPort, int videoPort) {
        MavlinkPort = incomingMavlinkPort;
        MavSDKPort = localMavSDKPort;
        VideoPort = videoPort;
        server = new MavSDKServer(this, incomingMavlinkPort, localMavSDKPort);
    }

    private DroneConnection(int incomingMavlinkPort, String serialPort, int baudRate) {
        MavlinkPort = incomingMavlinkPort;
        MavSDKPort = 4790;
        VideoPort = 5600;
        server = new MavSDKServer(this, "/dev/ttyUSB0", 57600, MavSDKPort);
    }

    public static DroneConnection Get() {
        return Get(MAVLINK_BASE_PORT);
    }

    public static DroneConnection test = null;
    public static DroneConnection Get(boolean useSerial) {
        if ( test != null )
            return test;
        else{
            test = new DroneConnection(14540, "/dev/ttyUSB2", 57600);
            return test;
        }

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
        connection.controller.stateMachine = new StateMachine(connection.controller);
        connection.controller.Telemetry.SubscribeForTelemetry();
        GUIManager.dronePainter.addDrone(connection.controller);
        java.lang.System.out.println("CONNECTED " + connection.MavlinkPort);

        if (DroneControlPanel.controllingDrone == null)
            DroneControlPanel.controllingDrone = connection;

    }

    public static void onDroneDisconnected(DroneConnection connection) {
        if (!connection.isDroneConnected)
            return;
        connection.isDroneConnected = false;

        GUIManager.dronePainter.removeDrone(connection.controller);

        java.lang.System.out.println("DISCONNECTED " + connection.MavlinkPort);

        if (DroneControlPanel.controllingDrone == connection) {
            if (DroneConnection.Connections.size() > 0)
                DroneControlPanel.controllingDrone = DroneConnection.Connections.get(0);
            else
                DroneControlPanel.controllingDrone = null;
        }

        connection.controller = null;
    }

}

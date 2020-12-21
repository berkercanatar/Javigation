package com.javigation.flight;

import com.javigation.GUI.GUIManager;
import com.javigation.drone_link.mavlink.DroneConnection;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import io.mavsdk.System;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Completable;
import org.jxmapviewer.viewer.GeoPosition;

import java.lang.reflect.Method;

public class DroneController {

    public System drone;
    private DroneConnection connection;
    public DroneTelemetry Telemetry;

    public DroneController(DroneConnection connection) {
        this.connection = connection;
        connection.controller = this;
        drone = new System("127.0.0.1", connection.MavSDKPort);
        GUIManager.dronePainter.addDrone(this);
    }

    public void SubscribeForTelemetry() {
        drone.getTelemetry().getPosition().subscribe( position -> {
            synchronized (Telemetry) {
                Telemetry.Position = position;
                //GUIManager.mapViewer.repaint();
            }
        });
    }

    public Completable Arm(){
        return drone.getAction().arm();
    }

    public Completable Disarm() {
        return drone.getAction().disarm();
    }

    public Completable TakeOff() {
        return drone.getAction().takeoff();
    }

    public Completable TakeOff(float altitude) {
        drone.getAction().setTakeoffAltitude(altitude).subscribe();
        return drone.getAction().takeoff();
    }

    public Completable Land() {
        return drone.getAction().land();
    }

    public GeoPosition GetGeoPosition() {
        Telemetry.Position dronePos = drone.getTelemetry().getPosition().blockingFirst();
        return new GeoPosition(dronePos.getLatitudeDeg(), dronePos.getLongitudeDeg());
    }

    public void performCommandChain(CommandChain chain, Method doOnComplete) {

        Completable commandStack;
        for ( Command cmd : chain.CommandList ) {

        }
    }



}

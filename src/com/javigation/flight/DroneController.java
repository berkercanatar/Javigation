package com.javigation.flight;

import com.javigation.GUI.GUIManager;
import com.javigation.drone_link.mavlink.DroneConnection;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import io.mavsdk.System;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Completable;
import io.reactivex.Observable;
import org.jxmapviewer.viewer.GeoPosition;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class DroneController {

    public System drone;
    public DroneConnection connection;
    public DroneTelemetry Telemetry;
    public StateMachine stateMachine;

    public DroneController(DroneConnection connection) {
        this.connection = connection;
        connection.controller = this;
        stateMachine = new StateMachine(this);
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

        Completable commandStack = Completable.timer(0, TimeUnit.MILLISECONDS);
        for ( Command cmd : chain.CommandList ) {
            switch (cmd.commandType) {
                case TAKEOFF:
                    commandStack = commandStack.andThen(drone.getAction().setTakeoffAltitude(cmd.getArg("alt")))
                            .andThen(drone.getAction().takeoff());
                    if (!drone.getTelemetry().getArmed().blockingFirst())
                        commandStack = commandStack.andThen(drone.getAction().arm());
                    break;
            }
        }
        commandStack.subscribe();
    }



}

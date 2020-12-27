package com.javigation.flight;

import com.javigation.GUI.GUIManager;
import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import io.mavsdk.System;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Completable;
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
        drone = new System("127.0.0.1", connection.MavSDKPort);
        GUIManager.dronePainter.addDrone(this);
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
                    if (!connection.controller.Telemetry.Armed)
                        commandStack = commandStack.andThen(drone.getAction().arm()).repeatUntil(() -> Telemetry.Armed);
                    break;
                case LAND:
                    commandStack = commandStack.andThen(drone.getAction().land());
                    break;
                case RTL:
                    commandStack = commandStack.andThen(drone.getAction().returnToLaunch());
                    break;
                case GO_TO_LOCATION:
                    Float heading = cmd.getArg("heading") == null ? Telemetry.Attitude.getYawDeg() : cmd.getArg("heading");
                    commandStack = commandStack.andThen(drone.getAction().gotoLocation(cmd.getArg("lat"), cmd.getArg("lon"), Telemetry.Position.getAbsoluteAltitudeM(), heading));
                    break;
                case HOLD:
                    if (Telemetry.FlightMode == io.mavsdk.telemetry.Telemetry.FlightMode.OFFBOARD)
                        commandStack = commandStack.andThen(drone.getOffboard().stop());
                    else
                        commandStack = commandStack.andThen(drone.getAction().gotoLocation(Telemetry.Position.getLatitudeDeg(), Telemetry.Position.getLongitudeDeg(), Telemetry.Position.getAbsoluteAltitudeM(), Telemetry.Attitude.getYawDeg()));
                    Utils.info("SET HOLD MODE");
                    break;
            }
        }
        commandStack.subscribe();
    }



}

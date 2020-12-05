package com.javigation;

import io.mavsdk.System;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Completable;
import org.jxmapviewer.viewer.GeoPosition;

public class DroneController {

    public System drone;

    public DroneController() {
        drone = new System("127.0.0.1", 4790);
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




}

package com.javigation.flight;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.popup.PopupManager;
import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import io.mavsdk.System;
import io.mavsdk.offboard.Offboard;
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
                        commandStack = commandStack.andThen(drone.getOffboard().setVelocityBody( new Offboard.VelocityBodyYawspeed(0f, 0f, 0f ,0f) ).andThen(drone.getOffboard().start()).andThen(drone.getOffboard().stop()));
                     break;
                case MISSION_UPLOAD:
                    commandStack = commandStack.andThen(drone.getMission().uploadMission(cmd.getArg("mission"))).doOnComplete(() -> {
                        connection.controller.stateMachine.SetState(StateMachine.StateTypes.MISSION_UPLOADED);
                        PopupManager.showSuccess("Mission uploaded!");
                    });
                    break;
                case MISSION_START, MISSION_RESUME:
                    commandStack = commandStack.andThen(drone.getMission().startMission()).delay(1, TimeUnit.SECONDS).andThen(drone.getMission().startMission());
                    break;
                case MISSION_PAUSE:
                    commandStack = commandStack.andThen(drone.getMission().pauseMission()).doOnComplete(() -> connection.controller.stateMachine.SetState(StateMachine.StateTypes.MISSION_PAUSED));
                    break;
                case MISSION_ABORT:
                    if (connection.controller.Telemetry.FlightMode == io.mavsdk.telemetry.Telemetry.FlightMode.MISSION)
                        commandStack = commandStack.andThen(drone.getMission().pauseMission());
                    commandStack = commandStack.andThen(drone.getMission().clearMission()).doOnComplete( () -> {
                        FlightMission.RemoveAllWaypoints();
                        PopupManager.showAlert("MISSION ABORTED!");
                        connection.controller.stateMachine.ClearState(StateMachine.StateTypes.MISSON_RUNNING);
                        connection.controller.stateMachine.ClearState(StateMachine.StateTypes.MISSION_PAUSED);
                        connection.controller.stateMachine.ClearState(StateMachine.StateTypes.MISSION_UPLOADED);
                    } );
            }
        }
        commandStack.subscribe();
    }



}

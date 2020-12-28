package com.javigation.drone_link.mavlink;

import com.javigation.GUI.flight_control_panels.AutopilotControlPanel;
import com.javigation.GUI.flight_control_panels.DroneControlPanel;
import com.javigation.GUI.flight_control_panels.MissionUploadButton;
import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import com.javigation.flight.CommandChain;
import com.javigation.flight.DroneController;
import com.javigation.flight.StateChangedListener;
import com.javigation.flight.StateMachine;
import io.mavsdk.System;
import io.mavsdk.mission.Mission;
import io.mavsdk.telemetry.Telemetry;
import org.jxmapviewer.viewer.GeoPosition;

public class DroneTelemetry implements StateChangedListener {

    public DroneConnection connection;
    public Telemetry.EulerAngle Attitude;
    private DroneController controller;
    private System drone;
    private StateMachine stateMachine;
    private Mission.MissionProgress MissionProgress;

    public DroneTelemetry(DroneConnection connection) {
        this.connection = connection;
        controller = connection.controller;
        drone = controller.drone;
    }


    public boolean Armed;
    public boolean InAir;

    public Telemetry.FlightMode FlightMode;
    public Telemetry.LandedState LandedState;
    public Telemetry.Battery Battery;
    public Boolean CheckPassed;
    public Telemetry.VelocityNed Velocity;
    public Telemetry.GpsInfo GPS;
    public Telemetry.Position Home;


    public Telemetry.Position Position;
    public GeoPosition GeoPosition() { return new GeoPosition(Position.getLatitudeDeg(), Position.getLongitudeDeg()); }
    public Float AirSpeed() {return (float)Math.sqrt( Math.pow(Velocity.getNorthMS(), 2) + Math.pow(Velocity.getEastMS(), 2) + Math.pow(Velocity.getDownMS(), 2) ); }
    public Float HorizontalSpeed() {return (float)Math.sqrt( Math.pow(Velocity.getNorthMS(), 2) + Math.pow(Velocity.getEastMS(), 2) ); }


    public void SubscribeForTelemetry() {

        stateMachine = controller.stateMachine;

        io.mavsdk.telemetry.Telemetry telem = drone.getTelemetry();

        drone.getTelemetry().getPosition().subscribe( position -> {
            synchronized (this) {
                Position = position;
            }
        });

        telem.getArmed().subscribe( isArmed -> {
            synchronized (this) {
                connection.controller.Telemetry.Armed = isArmed;
                if (isArmed)
                    stateMachine.SetState(StateMachine.StateTypes.ARMED);
                else
                    stateMachine.SetState(StateMachine.StateTypes.DISARMED);
            }
        });

        telem.getInAir().subscribe( isInAir -> {
            synchronized (this) {
                InAir = isInAir;
                if (isInAir)
                    stateMachine.SetState(StateMachine.StateTypes.IN_AIR);
                else
                    stateMachine.SetState(StateMachine.StateTypes.ON_GROUND);
            }
        });

        telem.getHealthAllOk().subscribe( checkPassed -> {
            synchronized (this) {
                CheckPassed = checkPassed;
                if (checkPassed)
                    stateMachine.SetState(StateMachine.StateTypes.PREFLIGHTCHECK_PASS);
                else
                    stateMachine.ClearState(StateMachine.StateTypes.PREFLIGHTCHECK_PASS);
            }
        });

        telem.getAttitudeEuler().subscribe( attitude -> {
            synchronized (this) {
                Attitude = attitude;
            }
        });

        telem.getFlightMode().subscribe( flightMode -> {
            synchronized (this) {
                FlightMode = flightMode;
                switch (flightMode) {
                    case OFFBOARD:
                        stateMachine.SetState(StateMachine.StateTypes.OFFBOARD);
                        break;
                    case RETURN_TO_LAUNCH:
                        stateMachine.SetState(StateMachine.StateTypes.RTL_RUNNING);
                        break;
                    case HOLD:
                        stateMachine.SetState(StateMachine.StateTypes.HOLD);
                        break;
                    case MISSION:
                        stateMachine.SetState(StateMachine.StateTypes.MISSON_RUNNING);
                        break;
                }

                if (FlightMode != Telemetry.FlightMode.MISSION)
                    controller.stateMachine.ClearState(StateMachine.StateTypes.MISSON_RUNNING);
            }
        });

        drone.getTelemetry().getLandedState().subscribe( landedState -> {
            switch ( landedState ) {
                case IN_AIR:
                    if (LandedState == Telemetry.LandedState.TAKING_OFF) {
                        OnTakeOffCompleted();
                    }
                    break;
                case ON_GROUND:
                    if (LandedState == Telemetry.LandedState.LANDING) {
                        OnLandingCompleted();
                    }
                    break;
                case TAKING_OFF:
                    stateMachine.SetState(StateMachine.StateTypes.TAKING_OFF);
                    break;
                case LANDING:
                    stateMachine.SetState(StateMachine.StateTypes.LANDING);
                    break;
            }
            LandedState = landedState;
        });

        drone.getTelemetry().getVelocityNed().subscribe( velocity -> {
            Velocity = velocity;
            if (FlightMode == Telemetry.FlightMode.OFFBOARD && !DroneControlPanel.IsControlling) {
                if (AirSpeed() < 0.25) {
                    CommandChain.Create(controller).Hold().Perform();
                }
            }
        });

        drone.getTelemetry().getBattery().subscribe( battery -> {
            Battery = battery;
        });

        drone.getTelemetry().getGpsInfo().subscribe( gpsInfo -> {
            GPS = gpsInfo;
        });

        drone.getTelemetry().getHome().subscribe( home -> {
            Home = home;
        });

        drone.getMission().getMissionProgress().subscribe( missionProgress -> {
            MissionProgress = missionProgress;
            if (MissionProgress.getCurrent() == MissionProgress.getTotal()) {
                CommandChain.Create(controller).Hold().Perform();
            }
        });


    }

    @Override
    public void OnStateChanged(StateMachine.StateTypes changedType, boolean isAdded) {
        AutopilotControlPanel.INSTANCE.OnStateChanged(changedType, isAdded);

        if(changedType == StateMachine.StateTypes.MISSON_RUNNING)
            MissionUploadButton.INSTANCE.resetSizeIcon();
    }

    @Override
    public void OnTakeOffCompleted() {
        stateMachine.ClearState(StateMachine.StateTypes.TAKING_OFF);
        Utils.info("TAKE OFF COMPLETED");
    }

    @Override
    public void OnLandingCompleted() {
        stateMachine.ClearState(StateMachine.StateTypes.LANDING);
        Utils.info("LANDING COMPLETED");
    }
}

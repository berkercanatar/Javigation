package com.javigation.flight;

import com.javigation.Statics;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import io.mavsdk.System;

import java.util.ArrayList;
import java.util.Arrays;

public class StateMachine {

    public ArrayList<StateTypes> ActiveStates;
    public DroneController controller;
    private System drone;
    private DroneTelemetry telemetry;

    public enum StateTypes {
        ON_GROUND,
        ON_AIR,
        ARMED,
        DISARMED,
        OFFBOARD,
        HOLD,
        ON_THE_WAY,
        MISSON_RUNNING,
        MISSION_PAUSED,
        MISSION_UPLOADED,
        PREFLIGHTCHECK_PASS,
        FAILSAFE_ENABLED,
        TAKING_OFF,
        LANDING,
        FOLLOWER,
        LEADER,
    }

    public StateMachine(DroneController controller) {
        this.controller = controller;
        drone = controller.drone;
        telemetry = controller.Telemetry;
        ActiveStates = new ArrayList<StateTypes>();
        ActiveStates.addAll(Statics.DefaultStates);
    }

    public void SetState( StateTypes state ) {
        if (!ActiveStates.contains(state))
            ActiveStates.add(state);
    }

    public void ClearState( StateTypes state ) {
        if (ActiveStates.contains(state))
            ActiveStates.remove(state);
    }

    private boolean yes( StateTypes... states ) {
        return ActiveStates.containsAll(Arrays.asList(states));
    }

    private boolean no( StateTypes... states ) {
        for ( StateTypes state : states ) {
            if (ActiveStates.contains(state))
                return false;
        }
        return true;
    }

    public boolean CanTakeOff() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.ON_GROUND
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED
                );
    }

    public boolean CanLand() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.ON_AIR,
                        StateTypes.ARMED
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED
                );
    }

    public boolean CanArm() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.ON_AIR,
                        StateTypes.DISARMED
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED
                );
    }

    public boolean CanDisarm() {
        return
                yes(
                        StateTypes.ON_GROUND,
                        StateTypes.ARMED
                ) &&
                no(
                        StateTypes.ON_AIR,
                        StateTypes.HOLD,
                        StateTypes.OFFBOARD,
                        StateTypes.MISSON_RUNNING,
                        StateTypes.ON_THE_WAY,
                        StateTypes.LANDING
                );
    }


}

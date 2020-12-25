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
        IN_AIR,
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
        RTL_RUNNING
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
        FixConflicts(state);
    }

    public void ClearState( StateTypes state ) {
        if (ActiveStates.contains(state))
            ActiveStates.remove(state);
    }



    private void FixConflicts(StateTypes state) {
        switch (state) {
            case ARMED:
                ClearState(StateTypes.DISARMED);
                break;
            case DISARMED:
                ClearState(StateTypes.ARMED);
                break;
            case IN_AIR:
                ClearState(StateTypes.ON_GROUND);
                break;
            case ON_GROUND:
                ClearState(StateTypes.IN_AIR);
                break;
            case MISSION_PAUSED:
                ClearState(StateTypes.MISSON_RUNNING);
                break;
            case MISSON_RUNNING:
                ClearState(StateTypes.MISSION_PAUSED);
                break;
        }
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
                        StateTypes.FAILSAFE_ENABLED,
                        StateTypes.TAKING_OFF
                );
    }

    public boolean CanLand() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.IN_AIR,
                        StateTypes.ARMED
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED,
                        StateTypes.LANDING
                );
    }

    public boolean CanArm() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.ON_GROUND,
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
                        StateTypes.IN_AIR,
                        StateTypes.HOLD,
                        StateTypes.OFFBOARD,
                        StateTypes.MISSON_RUNNING,
                        StateTypes.ON_THE_WAY,
                        StateTypes.LANDING
                );
    }

    public boolean CanRTL() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.IN_AIR,
                        StateTypes.ARMED
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED,
                        StateTypes.RTL_RUNNING
                );
    }

    public boolean CanStartMission() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.MISSION_UPLOADED
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED,
                        StateTypes.MISSION_PAUSED,
                        StateTypes.MISSON_RUNNING
                );
    }

    public boolean CanPauseMission() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.MISSION_UPLOADED,
                        StateTypes.MISSON_RUNNING
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED,
                        StateTypes.MISSION_PAUSED
                );
    }


    public boolean CanResumeMission() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.MISSION_UPLOADED,
                        StateTypes.MISSION_PAUSED
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED,
                        StateTypes.MISSON_RUNNING
                );
    }


    public boolean CanAbort() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED
                );
    }

    @Override
    public String toString() {
        return "StateMachine{" +
                "ActiveStates=" + ActiveStates +
                '}';
    }
}

package com.javigation.flight;

import com.javigation.GUI.flight_control_panels.AutopilotControlPanel;
import com.javigation.GUI.flight_control_panels.AutopilotControlPanelButton;
import com.javigation.Statics;
import com.javigation.Utils;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import io.mavsdk.System;

import javax.swing.plaf.nimbus.State;
import java.util.*;

public class StateMachine {

    public ArrayList<StateTypes> ActiveStates;
    public DroneController controller;
    private System drone;
    private DroneTelemetry telemetry;
    private static final List<StateTypes> FLIGHT_MODES = Arrays.asList(new StateTypes[]{StateTypes.OFFBOARD, StateTypes.RTL_RUNNING, StateTypes.TAKING_OFF, StateTypes.LANDING, StateTypes.HOLD});

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
        RTL_RUNNING,
        DUMMY
    }

    public StateMachine(DroneController controller) {
        this.controller = controller;
        drone = controller.drone;
        telemetry = controller.Telemetry;
        ActiveStates = new ArrayList<StateTypes>();
        ActiveStates.addAll(Statics.DefaultStates);

        StateMachine machine = this;
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Utils.info(machine);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public void SetState( StateTypes state ) {
        if (!CheckState(state)) {
            if(CanSetState(state))
                ActiveStates.add(state);
            FixConflicts(state);
            if (CheckState(state)) {
                AutopilotControlPanel.INSTANCE.OnStateChanged(state, true);
                telemetry.OnStateChanged(state, true);
            }
        }
    }

    public void ClearState( StateTypes state ) {
        if (CheckState(state)) {
            ActiveStates.remove(state);
            AutopilotControlPanel.INSTANCE.OnStateChanged(state, false);
            telemetry.OnStateChanged(state, false);
        }
    }

    public boolean CheckState( StateTypes state ) {
        return ActiveStates.contains(state);
    }

    private boolean CanSetState (StateTypes state ) {
        if ( FLIGHT_MODES.contains(state) ) {
            if ( CheckState(StateTypes.DISARMED) )
                return false;

            for (StateTypes flightMode : FLIGHT_MODES) {
                if ( flightMode != state && CheckState(flightMode) )
                    ClearState(flightMode);
            }
            return true;
        }
        return true;
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
                        StateTypes.ON_GROUND,
                        StateTypes.DISARMED
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
                        StateTypes.MISSION_UPLOADED,
                        StateTypes.IN_AIR,
                        StateTypes.ARMED
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


    public boolean CanAbortMission() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.MISSON_RUNNING
                ) &&
                no(
                        StateTypes.FAILSAFE_ENABLED
                );
    }

    public boolean CanHold() {
        return
                yes(
                        StateTypes.PREFLIGHTCHECK_PASS,
                        StateTypes.ARMED,
                        StateTypes.IN_AIR
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

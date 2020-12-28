package com.javigation.flight;

import com.javigation.GUI.flight_control_panels.DroneControlPanel;
import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import io.mavsdk.offboard.Offboard;
import io.mavsdk.telemetry.Telemetry;
import org.jxmapviewer.viewer.GeoPosition;

import java.text.Normalizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Create Swarm Object +
 * Get needed variables(latitude,longitude,altitude
 * Define Followers and Leaders +
 * Connection between Leader and Followers
 * find the leader
 * main position => leader's position
 *
 * position ok
 * leader ? input : defineLeader
 */



public class Swarm {

    DroneConnection drone;
    DroneConnection drone2;
    DroneConnection drone3;
    DroneConnection leader;
    public static Formation formation;
    Leader lead;
    Follower follow1;
    Follower follow2;
    public static Swarm ActiveSwarm;
    private Timer swarmTimer;


    public Swarm(DroneConnection drone, DroneConnection drone2, DroneConnection drone3, Formation.FormationType format, boolean isLeaderSelected) {
        flyIndependently();
        ActiveSwarm = this;
        this.drone = drone;
        this.drone2 = drone2;
        this.drone3 = drone3;

        formation = new Formation(format);
        if(DroneConnection.Connections.size() >= 3 && !isLeaderSelected) {
            leader = formation.defineLeader(drone, drone2, drone3);

            if (leader == drone) {
                lead = new Leader(drone);
                follow1 = new Follower(drone2);
                follow2 = new Follower(drone3);
                flyTogether(drone);
            }

            else if (leader == drone2) {
                lead = new Leader(drone2);
                follow1 = new Follower(drone);
                follow2 = new Follower(drone3);
                flyTogether(drone2);
            }

            else if (leader == drone3) {
                lead = new Leader(drone3);
                follow1 = new Follower(drone2);
                follow2 = new Follower(drone);
                flyTogether(drone3);
            }
        }

    }


    public void flyTogether(DroneConnection l) {

        DroneControlPanel.controllingDrone = lead.getDrone();

        swarmTimer = new Timer();

        lead.getDrone().controller.stateMachine.SetState(StateMachine.StateTypes.LEADER);
        follow1.getDrone().controller.stateMachine.SetState(StateMachine.StateTypes.FOLLOWER);
        follow2.getDrone().controller.stateMachine.SetState(StateMachine.StateTypes.FOLLOWER);

        TimerTask swarmUpdateTask = new TimerTask() {
            @Override
            public void run() {
                Telemetry.Position leaderPos = lead.getDrone().controller.Telemetry.Position;
                float leaderHeading = lead.getDrone().controller.Telemetry.Attitude.getYawDeg();

                Telemetry.Position newPosition1 = Utils.FindRelativePosition(leaderPos,  (formation.format == Formation.FormationType.TRIANGLE ? 135 : 90) + leaderHeading, 20);
                CommandChain.Create(follow1.getDrone().controller).GoTo(newPosition1.getLatitudeDeg(), newPosition1.getLongitudeDeg(), leaderHeading).Perform();

                Telemetry.Position newPosition2 = Utils.FindRelativePosition(leaderPos, (formation.format == Formation.FormationType.TRIANGLE ? 225 : 270) + leaderHeading, 20);
                CommandChain.Create(follow2.getDrone().controller).GoTo(newPosition2.getLatitudeDeg(), newPosition2.getLongitudeDeg(), leaderHeading).Perform();
            }
        };

        swarmTimer.schedule(swarmUpdateTask, 0, 150);

    }


    public static void flyIndependently() {
        if (ActiveSwarm != null && ActiveSwarm.swarmTimer != null) {
            ActiveSwarm.swarmTimer.cancel();
            ActiveSwarm.swarmTimer.purge();
            ActiveSwarm.lead.getDrone().controller.stateMachine.ClearState(StateMachine.StateTypes.LEADER);
            ActiveSwarm.follow1.getDrone().controller.stateMachine.ClearState(StateMachine.StateTypes.FOLLOWER);
            ActiveSwarm.follow2.getDrone().controller.stateMachine.ClearState(StateMachine.StateTypes.FOLLOWER);
            ActiveSwarm = null;
        }
    }

}

package com.javigation.flight;

import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import io.mavsdk.offboard.Offboard;
import io.mavsdk.telemetry.Telemetry;
import org.jxmapviewer.viewer.GeoPosition;

import java.text.Normalizer;
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
    private int velocity;
    private Telemetry.Position leaderPosition;
    Formation formation;
    Leader lead;
    Follower follow1;
    Follower follow2;
    CommandChain leadCommand;

    public Swarm(DroneConnection drone, DroneConnection drone2, DroneConnection drone3, String format, boolean isLeaderSelected) {
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

        lead.getDrone().drone.getTelemetry().getPosition().subscribe( position -> {
            Telemetry.Position newPosition = formation.getRelativePosition(position, 3,4);
            CommandChain.Create(follow1.getDrone().controller).GoTo(newPosition.getLatitudeDeg(), newPosition.getLongitudeDeg()).Perform();


            Utils.info(newPosition.getLatitudeDeg() + "," + newPosition.getLongitudeDeg());

            Telemetry.Position newPosition2 = formation.getRelativePosition(position, -3,4);
            CommandChain.Create(follow2.getDrone().controller).GoTo(newPosition.getLatitudeDeg(), newPosition.getLongitudeDeg()).Perform();
        });


    }


    private void updatePosition() {

    }

}

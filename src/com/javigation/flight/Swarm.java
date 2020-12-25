package com.javigation.flight;

import com.javigation.drone_link.DroneConnection;
import io.mavsdk.telemetry.Telemetry;

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
    DroneConnection follower1;
    DroneConnection follower2;
    DroneConnection leader;
    private int velocity;
    public Telemetry.Position Position;
    Formation formation;

    public Swarm(DroneConnection drone, DroneConnection follower1, DroneConnection follower2, String format) {
        this.drone = drone;
        this.follower1 = follower1;
        this.follower2 = follower2;

        formation = new Formation(format);
        leader = formation.defineLeader(drone, follower1, follower2);

        if(leader == drone){

        }

        else if(leader == follower1){

        }

        else if(leader == follower2) {

        }

    }

    public void flyTogether() {

    }


    private void updateVelocity() {
    }

    private void updatePosition() {

    }

}

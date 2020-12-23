package com.javigation.flight;

import com.javigation.Statics;
import com.javigation.drone_link.mavlink.*;
import com.javigation.flight.DroneController;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Flowable;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;
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

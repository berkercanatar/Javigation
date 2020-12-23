package com.javigation.flight;

import com.javigation.drone_link.mavlink.DroneConnection;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Flowable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Follower {


    private ArrayList<Flowable<Telemetry.Position>> followerPosition;
    private DroneConnection follower;
    private boolean isLeader;
    private int time;

    //private ArrayList<Telemetry.Position>  follower2Position;

    public Follower(DroneConnection follower){
        this.follower = follower;
        followerPosition.add(follower.drone.getTelemetry().getPosition());
        isLeader = false;

    }

    public void updatePos( Flowable<Telemetry.Position> newPos, int time ){
        this.time = time;
        if ( time != 0)
            followerPosition.add(time, newPos );

    }

    public Flowable<Telemetry.Position> getPos(){

        return followerPosition.get(time);

    }

    public DroneConnection getFollower(){
        return follower;
    }

    public boolean checkStatus() {

        return isLeader;

    }


}

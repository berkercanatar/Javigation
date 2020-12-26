package com.javigation.flight;

import com.javigation.drone_link.DroneConnection;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Flowable;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;

public class Follower implements SwarmDrone {


    private ArrayList<GeoPosition> followerPosition;
    private DroneConnection follower;
    private boolean isLeader;
    private int time;

    //private ArrayList<Telemetry.Position>  follower2Position;

    public Follower(DroneConnection follower){
        this.follower = follower;
        followerPosition = new ArrayList<>();
        followerPosition.add(follower.controller.GetGeoPosition());
        isLeader = false;

    }

    public void updatePos( GeoPosition newPos, int time ){
        this.time = time;
        if ( time != 0)
            followerPosition.add(time, newPos );

    }

    public GeoPosition getPos(){

        return followerPosition.get(time);

    }

    public DroneConnection getDrone(){

        return follower;
    }

    public void setStatus( boolean isLeader ){
        this.isLeader = isLeader;
    }

    public boolean checkStatus() {

        return isLeader;

    }


}

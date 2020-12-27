package com.javigation.flight;

import com.javigation.drone_link.DroneConnection;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Flowable;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;

public class Leader implements SwarmDrone{

    private ArrayList<GeoPosition> leaderPosition;
    private DroneConnection leader;
    private boolean isLeader;
    private int time;

    public Leader(DroneConnection leader) {
        this.leader = leader;
        leaderPosition = new ArrayList<>();
        leaderPosition.add(leader.controller.GetGeoPosition());
        isLeader = true;

    }

    public void updatePos(GeoPosition position, int time){
        this.time = time;
        if (time != 0)
            leaderPosition.add(time, position );

    }

    public DroneConnection getDrone(){
        return leader;
    }

    public GeoPosition getPos(){

        return leaderPosition.get(time);

    }

    public void setStatus( boolean isLeader ){
        this.isLeader = isLeader;
    }

    public boolean checkStatus() {

        return isLeader;

    }
}

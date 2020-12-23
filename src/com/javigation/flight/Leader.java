package com.javigation.flight;

import com.javigation.drone_link.mavlink.DroneConnection;
import io.mavsdk.telemetry.Telemetry;
import io.reactivex.Flowable;

import java.util.ArrayList;

public class Leader {

    private ArrayList<Flowable<Telemetry.Position>> leaderPosition;
    private DroneConnection leader;
    private boolean isLeader;
    private int time;

    public Leader(DroneConnection leader) {
        this.leader = leader;
        leaderPosition.add(leader.drone.getTelemetry().getPosition());
        isLeader = true;

    }

    public void updatePos(Flowable<Telemetry.Position> newPos, int time ){
        this.time = time;
        if ( time != 0)
            leaderPosition.add(time, newPos );

    }

    public Flowable<Telemetry.Position> getPos(){

        return leaderPosition.get(time);

    }

    public DroneConnection getLeader(){
        return leader;
    }

    public boolean checkStatus() {

        return isLeader;

    }
}

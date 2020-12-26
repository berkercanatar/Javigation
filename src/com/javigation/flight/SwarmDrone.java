package com.javigation.flight;

import com.javigation.drone_link.DroneConnection;
import org.jxmapviewer.viewer.GeoPosition;

public interface SwarmDrone {

    GeoPosition getPos();
    DroneConnection getDrone();
    void setStatus( boolean isLeader );
    boolean checkStatus();
    void updatePos(GeoPosition position, int time);
}

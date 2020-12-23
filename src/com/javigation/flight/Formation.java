package com.javigation.flight;

import com.javigation.Statics;
import com.javigation.drone_link.mavlink.DroneConnection;
import com.javigation.drone_link.mavlink.DroneTelemetry;
import io.mavsdk.telemetry.Telemetry;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.util.ArrayList;

public class Formation {

    private String format;

    public Formation(String format){
        if(format == "Triangle"){
            this.format = "Triangle";
        }
        else if(format == "Horizontal"){
            this.format = "Horizontal";
        }

    }

    private double distance(double latitudeFollower, double latitudeLeader,   //(f1.lat, ?, f1.long, ?)
                            double longitudeFollower, double longitudeLeader){
        //radius in km
        double radius = 6371;

        // Haversine
        double distanceLongitude = Math.toRadians(longitudeLeader) - Math.toRadians(longitudeFollower);
        double distanceLatitude = Math.toRadians(latitudeLeader) - Math.toRadians(latitudeFollower);
        double i = Math.pow(Math.sin(distanceLatitude / 2), 2)
                + Math.cos(Math.toRadians(latitudeFollower)) * Math.cos(Math.toRadians(latitudeLeader))
                * Math.pow(Math.sin(distanceLongitude / 2),2);

        double j = 2 * Math.asin(Math.sqrt(i));

        //convert meters
        return (radius*j*1000);

    }

    private float metersToLatitude(GeoPosition near, double meters) {
        return (float) (meters / 110.6*1000);
    }

    private float metersToLongitude(GeoPosition near, double meters) {
        float lat = (float) Math.toRadians(near.getLatitude());
        return (float) (meters /
                        (111132.954 - 559.822 * Math.cos(2*lat) + 1.175 * Math.cos(4*lat)));
    }

    public DroneConnection defineLeader(DroneConnection leader, DroneConnection follower1,
                                            DroneConnection follower2){
        double d1 = distance(follower1.controller.Telemetry.GeoPosition().getLatitude(),leader.controller.Telemetry.GeoPosition().getLatitude(),
                follower1.controller.Telemetry.GeoPosition().getLongitude(), leader.controller.Telemetry.GeoPosition().getLongitude() );
        double d2 = distance(follower2.controller.Telemetry.GeoPosition().getLatitude(),leader.controller.Telemetry.GeoPosition().getLatitude(),
                follower2.controller.Telemetry.GeoPosition().getLongitude(), leader.controller.Telemetry.GeoPosition().getLongitude() );
        double d3 = distance(follower1.controller.Telemetry.GeoPosition().getLatitude(),follower2.controller.Telemetry.GeoPosition().getLatitude(),
                follower1.controller.Telemetry.GeoPosition().getLongitude(), follower2.controller.Telemetry.GeoPosition().getLongitude() );

        if( d1 >= d2 && d1 >= d3) {
            if(format == "Triangle")
                setPositionsForTriangle(follower2, leader, follower1);
            else
                setPositionsForHorizontal(follower2, leader, follower1);

            return follower2;
        }

        else if (d2 >= d1 && d2 >= d3){
            if(format == "Triangle")
                setPositionsForTriangle(follower1, leader, follower2);
            else
                setPositionsForHorizontal(follower1, leader, follower2);

            return follower1;
        }

        else if(d3>=d1 && d3 >= d2) {
            if(format == "Triangle")
                setPositionsForTriangle(leader, follower2, follower1);
            else
                setPositionsForHorizontal(leader, follower2, follower1);

            return leader;
        }

        return leader;

    }

    public void setPositionsForTriangle(DroneConnection leader, DroneConnection f1, DroneConnection f2 ){

        CommandChain leaderCommand = CommandChain.Create(leader.controller);
        CommandChain f1Command = CommandChain.Create(f1.controller);
        CommandChain f2Command = CommandChain.Create(f2.controller);
        //float leaderAltitude = leader.controller.Telemetry.Position.getAbsoluteAltitudeM();
        //float f1Altitude = f1.controller.Telemetry.Position.getAbsoluteAltitudeM();
        //float f2Altitude = f2.controller.Telemetry.Position.getAbsoluteAltitudeM();
        GeoPosition leaderPos = leader.controller.Telemetry.GeoPosition();
        double height =  (Math.sqrt(3) * Statics.CONSTANT_DISTANCE/2) *-1;
        double left = Statics.CONSTANT_DISTANCE / -2;
        double right = Statics.CONSTANT_DISTANCE / 2;

        GeoPosition positionL = getRelativePosition(leaderPos, height, left);
        GeoPosition positionR = getRelativePosition(leaderPos, height, right);

        double f1Latitude = f1.controller.Telemetry.GeoPosition().getLatitude();
        double f2Latitude = f2.controller.Telemetry.GeoPosition().getLatitude();

        double f1Longitude = f1.controller.Telemetry.GeoPosition().getLongitude();
        double f2Longitude = f2.controller.Telemetry.GeoPosition().getLongitude();


        double pos1 = distance(f1Latitude,positionL.getLatitude(), f1Longitude, positionL.getLongitude()); //F1
        double pos2 = distance(f1Latitude,positionR.getLatitude(), f1Longitude, positionR.getLongitude());
        double pos3 = distance(f2Latitude,positionL.getLatitude(), f2Longitude, positionL.getLongitude()); //F2
        double pos4 = distance(f2Latitude,positionR.getLatitude(), f2Longitude, positionR.getLongitude());

        if( pos1+pos2 > pos3+pos4) {
            if(pos1 > pos2) {
                f1Command.GoTo(positionR.getLatitude(), positionR.getLongitude()).Perform();
                f2Command.GoTo(positionL.getLatitude(), positionL.getLongitude()).Perform();
            }
            else{
                f1Command.GoTo(positionL.getLatitude(), positionL.getLongitude()).Perform();
                f2Command.GoTo(positionR.getLatitude(), positionR.getLongitude()).Perform();
            }
        }

        //leaderCommand.Arm().GoTo(32,32).Ascend(30).Descend(10).GoTo(32,32).Land().Disarm().Perform();


    }

    public void setPositionsForHorizontal(DroneConnection leader, DroneConnection f1, DroneConnection f2 ){
        CommandChain leaderCommand = CommandChain.Create(leader.controller);
        CommandChain f1Command = CommandChain.Create(f1.controller);
        CommandChain f2Command = CommandChain.Create(f2.controller);

        GeoPosition leaderPos = leader.controller.Telemetry.GeoPosition();

        GeoPosition positionL = getRelativePosition(leaderPos, 0, Statics.CONSTANT_DISTANCE * -1);
        GeoPosition positionR = getRelativePosition(leaderPos, 0, Statics.CONSTANT_DISTANCE);

        double f1Latitude = f1.controller.Telemetry.GeoPosition().getLatitude();
        double f2Latitude = f2.controller.Telemetry.GeoPosition().getLatitude();
        double f1Longitude = f1.controller.Telemetry.GeoPosition().getLongitude();
        double f2Longitude = f2.controller.Telemetry.GeoPosition().getLongitude();

        double pos1 = distance(f1Latitude,positionL.getLatitude(), f1Longitude, positionL.getLongitude()); //F1
        double pos2 = distance(f1Latitude,positionR.getLatitude(), f1Longitude, positionR.getLongitude());
        double pos3 = distance(f2Latitude,positionL.getLatitude(), f2Longitude, positionL.getLongitude()); //F2
        double pos4 = distance(f2Latitude,positionR.getLatitude(), f2Longitude, positionR.getLongitude());

        if( pos1+pos2 > pos3+pos4) {
            if(pos1 > pos2) {
                f1Command.GoTo(positionR.getLatitude(), positionR.getLongitude()).Perform();
                f2Command.GoTo(positionL.getLatitude(), positionL.getLongitude()).Perform();
            }
            else{
                f1Command.GoTo(positionL.getLatitude(), positionL.getLongitude()).Perform();
                f2Command.GoTo(positionR.getLatitude(), positionR.getLongitude()).Perform();
            }
        }
    }

    public GeoPosition getRelativePosition(GeoPosition pos, double forward, double right) {

        double lat = metersToLatitude(pos, forward);
        double lon = metersToLongitude(pos, right);

        return new GeoPosition(lat, lon);
    }
}

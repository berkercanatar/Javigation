package com.javigation.flight;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.flight_control_panels.MissionUploadButton;
import io.mavsdk.mission.Mission;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;

public class FlightMission {

    public static boolean IsPlanning = false;
    public static float flightSpeedM = 5f;
    public static float missionAltitude = 10f;

    public static ArrayList<Mission.MissionItem> MissionItems = new ArrayList<Mission.MissionItem>();

    public static void AddWaypoint(GeoPosition waypoint) {
        MissionItems.add(generateWaypointMissionItem(waypoint));
        drawMissionWaypoints();
        MissionUploadButton.INSTANCE.resetSizeIcon();
    }

    public static void RemoveLastWaypoint() {
        if (MissionItems.size() > 0) {
            MissionItems.remove(MissionItems.size() - 1);
            drawMissionWaypoints();
            MissionUploadButton.INSTANCE.resetSizeIcon();
        }
    }

    public static void RemoveAllWaypoints() {
        MissionItems.clear();
        drawMissionWaypoints();
    }

    public static void drawMissionWaypoints() {
        GUIManager.missionWaypointPainter.waypoints.clear();
        ArrayList<GeoPosition> waypoints = new ArrayList<GeoPosition>();
        for ( Mission.MissionItem missionItem : MissionItems ) {
            GUIManager.missionWaypointPainter.addWaypoint(new DefaultWaypoint(missionItem.getLatitudeDeg(), missionItem.getLongitudeDeg()));
            waypoints.add(new GeoPosition(missionItem.getLatitudeDeg(), missionItem.getLongitudeDeg()));
        }
        GUIManager.missionRoutePainter.track = waypoints;
    }

    public static Mission.MissionItem generateWaypointMissionItem(GeoPosition waypoint) {
        return new Mission.MissionItem(
                waypoint.getLatitude(),
                waypoint.getLongitude(),
                missionAltitude,
                flightSpeedM,
                true,
                Float.NaN,
                Float.NaN,
                Mission.MissionItem.CameraAction.NONE,
                Float.NaN,
                1.0);
    }
}

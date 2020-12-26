package com.javigation.flight;

import com.javigation.Utils;

import java.util.ArrayList;

public class CommandChain {

    public ArrayList<Command> CommandList = new ArrayList<Command>();
    private DroneController controller;

    CommandChain(DroneController controller) {
        this.controller = controller;
    }

    public void Perform() {
        controller.performCommandChain(this, null);
    }

    public static CommandChain Create(DroneController controller) {
        return new CommandChain( controller );
    }

    public CommandChain Arm() {
        CommandList.add(new Command(Command.CommandType.ARM));
        return this;
    }

    public CommandChain Disarm() {
        CommandList.add(new Command(Command.CommandType.DISARM));
        return this;
    }

    public CommandChain TakeOff(float altitude) {
        return TakeOff(altitude, false);
    }

    public CommandChain TakeOff(float altitude, boolean wait) {
        CommandList.add((new Command(Command.CommandType.TAKEOFF)).withArg("alt", altitude).withArg("wait", wait));
        return this;
    }

    public CommandChain Land() {
        CommandList.add(new Command(Command.CommandType.LAND));
        return this;
    }

    public CommandChain Ascend(double meters) {
        CommandList.add((new Command(Command.CommandType.ASCEND)).withArg("meters", meters));
        return this;
    }

    public CommandChain Descend(double meters) {
        CommandList.add((new Command(Command.CommandType.DESCEND)).withArg("meters", meters));
        return this;
    }

    public CommandChain SetAlt(double alt) {
        CommandList.add((new Command(Command.CommandType.SET_ALTITUDE)).withArg("alt", alt));
        return this;
    }

    public CommandChain SetHeading(double heading) {
        CommandList.add((new Command(Command.CommandType.SET_HEADING)).withArg("heading", heading));
        return this;
    }

    public CommandChain GoTo(double lat, double lon) {
        CommandList.add((new Command(Command.CommandType.GO_TO_LOCATION)).withArg("lat", lat).withArg("lon", lon));
        return this;
    }

    public CommandChain GoTo(double lat, double lon, boolean keepHeading) {
        float heading = keepHeading ? controller.Telemetry.Attitude.getYawDeg() : Utils.CalculateBearing(controller.Telemetry.Position.getLatitudeDeg(), controller.Telemetry.Position.getLongitudeDeg(), lat, lon);
        CommandList.add((new Command(Command.CommandType.GO_TO_LOCATION)).withArg("lat", lat).withArg("lon", lon).withArg("heading", heading));
        return this;
    }

    public CommandChain GoTo(double lat, double lon, double heading) {
        CommandList.add((new Command(Command.CommandType.GO_TO_LOCATION)).withArg("lat", lat).withArg("lon", lon).withArg("heading", heading));
        return this;
    }
    public CommandChain GoTo(double lat, double lon, double alt, double heading) {
        CommandList.add((new Command(Command.CommandType.GO_TO_LOCATION)).withArg("lat", lat).withArg("lon", lon).withArg("alt", alt).withArg("heading", heading));
        return this;
    }

    public CommandChain RTL() {
        CommandList.add(new Command(Command.CommandType.RTL));
        return this;
    }

    public CommandChain MissionPause() {
        CommandList.add(new Command(Command.CommandType.MISSION_PAUSE));
        return this;
    }

    public CommandChain MissionResume() {
        CommandList.add(new Command(Command.CommandType.MISSION_RESUME));
        return this;
    }

    public CommandChain MissionAbort() {
        CommandList.add(new Command(Command.CommandType.MISSION_ABORT));
        return this;
    }

    public CommandChain GoTo(double meters) {
        CommandList.add((new Command(Command.CommandType.DESCEND)).withArg("meters", meters));
        return this;
    }

    public CommandChain Hold() {
        CommandList.add(new Command(Command.CommandType.HOLD));
        return this;
    }
}

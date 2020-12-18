package com.javigation.flight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Command {

    public static enum CommandType {
        ARM,
        DISARM,
        TAKEOFF,
        LAND,
        ASCEND,
        DESCEND,
        SET_ALTITUDE,
        SET_HEADING,
        RTL,
        GO_TO_LOCATION,
        MISSION_PAUSE,
        MISSION_RESUME,
        MISSION_ABORT,
        PITCH_DOWN,
        PITCH_UP,
        ROLL_LEFT,
        ROLL_RIGHT,
        YAW_CCW,
        YAW_CW,
    }

    public CommandType commandType;
    private Map<String, Object> args = new HashMap<String, Object>();

    public Command( CommandType commandType ) {
        this.commandType = commandType;
    }

    public Command withArg( String argName, Object value ) {
        args.put(argName, value);
        return this;
    }

    public <T> T getArg( String argName ) {
        return (T)args.get(argName);
    }

}

package com.javigation;


import com.javigation.flight.StateMachine;

import java.awt.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class Statics {

    public static String JAVIGATION_DATA_FOLDER;
    public static Path JAVIGATION_FOLDER;

    public static final String GSTREAMER_x64_URL = "https://gstreamer.freedesktop.org/data/pkg/windows/1.18.2/mingw/gstreamer-1.0-mingw-x86_64-1.18.2.msi";
    public static final String GSTREAMER_x86_URL = "https://gstreamer.freedesktop.org/data/pkg/windows/1.18.2/mingw/gstreamer-1.0-mingw-x86-1.18.2.msi";

    public static Path GSTREAMER_ROOT;

    public static final int MAP_FPS = 15;
    public static final double CONSTANT_DISTANCE = 5;

    public static final float MANUAL_CONTROL_HORIZONTAL_VELOCITY_MS = 5f;
    public static final float MANUAL_CONTROL_VERTICAL_VELOCITY_MS = 2f;
    public static final float MANUAL_CONTROL_YAW_DEG_S = 25f;

    public static final List<StateMachine.StateTypes> DefaultStates = Arrays.asList(StateMachine.StateTypes.DISARMED, StateMachine.StateTypes.ON_GROUND);

    public static final double RADIUS_OF_EARTH = 6378.1;

    public static final Color TELEMETRY_DEFAULT_COLOR = Color.white;

}

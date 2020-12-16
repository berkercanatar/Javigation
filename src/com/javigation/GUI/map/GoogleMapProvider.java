package com.javigation.GUI.map;

import java.util.*;

public class GoogleMapProvider {

    private static final String SEC_GOOGLE_WORLD = "Galileo";
    private static final int TILE_SIZE = 256;

    public static enum MapType {
        SATELLITE,
        HYBRID,
        TERRAIN
    }

    private static final Map<MapType, HashMap<String, String>> MapTypeSettings = new EnumMap<MapType, HashMap<String, String>>(MapType.class) {{
        put(MapType.SATELLITE, new HashMap<String, String>() {{ put("server", "khm"); put("request", "kh"); put("version", "879"); put("arg", "v");}});
        put(MapType.HYBRID, new HashMap<String, String>() {{ put("server", "mt"); put("request", "vt"); put("version", "y"); put("arg", "lyrs");}});
        put(MapType.TERRAIN, new HashMap<String, String>() {{ put("server", "mt"); put("request", "vt"); put("version", "t@354,r@354000000"); put("arg", "lyrs");}});
    }};

    private static int getServerNum(int x, int y, int max) {
        return ( x + 2 * y ) % max;
    }

    private static String[] getSecGoogleWords(int x, int y) {
        String[] secs = new String[2];
        int secLen = ( (x * 3) + y ) % 8;
        secs[0] = (y >= 10000 && y < 100000) ? "&s=" : "";
        secs[1] = SEC_GOOGLE_WORLD.substring(0, secLen);
        return secs;
    }

    private static double[] LatLngToWorldCoordinates( double lat, double lon ) {
        double[] worldCoordinates = new double[2];
        double siny = Math.sin( (lat * Math.PI) / 180.0 );
        siny = Math.min(Math.max(siny, -0.9999), 0.9999);

        worldCoordinates[0] = TILE_SIZE * (0.5 + lon / 360);
        worldCoordinates[1] = TILE_SIZE * (0.5 - Math.log((1.0 + siny) / (1.0 - siny)) / (4.0 * Math.PI));

        return worldCoordinates;
    }

    public static String getURL(MapType mapType, double lat, double lon, int zoom) {
        double[] worldCoordinates = GoogleMapProvider.LatLngToWorldCoordinates(lat,lon);
        int x = (int)( (worldCoordinates[0] * Math.pow(2, zoom)) / 256.0 );
        int y = (int)( (worldCoordinates[1] * Math.pow(2, zoom)) / 256.0 );
        String server = MapTypeSettings.get(mapType).get("server");
        String request = MapTypeSettings.get(mapType).get("request");
        String version = MapTypeSettings.get(mapType).get("version");
        String arg = MapTypeSettings.get(mapType).get("arg");
        int serverNum = getServerNum(x, y, 4);
        String language = "en-US";
        String[] secs = getSecGoogleWords(x, y);

        return "http://" + server + serverNum + ".google.com/" + request + "/" + arg + "=" + version + "&hl=" + language +
                "&x=" + x + secs[0] + "&y=" + y + "&z=" + zoom + "&s=" + secs[1];


    }




}

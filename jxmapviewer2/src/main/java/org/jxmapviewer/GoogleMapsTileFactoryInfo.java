
package org.jxmapviewer;

import org.jxmapviewer.viewer.TileFactoryInfo;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Uses Google Maps API - it has several glitches, so don't use it unless you know what you're doing.
 * Most importantly, gmaps cuts of small text leftovers that reach into the visible tile. As a result
 * you cannot really tile the rendered tiles.
 */
public class GoogleMapsTileFactoryInfo extends TileFactoryInfo
{
    private static final int max = 20;

    private static final int TILE_SIZE = 256;

    private static final String SEC_GOOGLE_WORLD = "Galileo";

    public static enum MapType {
        SATELLITE,
        HYBRID,
        TERRAIN
    }

    private MapType mapType;

    private static final Map<MapType, HashMap<String, String>> MapTypeSettings = new EnumMap<MapType, HashMap<String, String>>(MapType.class) {{
        put(MapType.SATELLITE, new HashMap<String, String>() {{ put("server", "khm"); put("request", "kh"); put("version", "879"); put("arg", "v");}});
        put(MapType.HYBRID, new HashMap<String, String>() {{ put("server", "mt"); put("request", "vt"); put("version", "y"); put("arg", "lyrs");}});
        put(MapType.TERRAIN, new HashMap<String, String>() {{ put("server", "mt"); put("request", "vt"); put("version", "t@354,r@354000000"); put("arg", "lyrs");}});
    }};


    public GoogleMapsTileFactoryInfo(MapType mapType)
    {
        super("Google Maps - " + mapType.toString(),1,20,23,TILE_SIZE,true, true, "", "x", "y", "z");
        this.mapType = mapType;
    }

    private static int getServerNum(int x, int y, int max) {
        return ( x + 2 * y ) % max;
    }

    private static String[] getSecGoogleWords(int x, int y) {
        String[] secs = new String[2];
        int secLen = ( (x * 3) + y ) % 8;
        secs[0] = (y >= 10000 && y < 100000) ? "&s=" : "";
        secs[1] = SEC_GOOGLE_WORLD.substring(0, (secLen<0)?secLen + 8:secLen);
        return secs;
    }

    @Override
    public String getTileUrl(int x, int y, int zoom)
    {
        //System.out.println("testing for validity: X " + x + " Y = " + y);
        zoom = getTotalMapZoom() - zoom;

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

    @Override
    public String getAttribution() {
        return "\u00A9 Google";
    }

    @Override
    public String getLicense() {
        return "https://www.google.com/intl/en_US/help/terms_maps/";
    }





}

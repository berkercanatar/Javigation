package com.javigation;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Tile;

import java.util.*;

public class TileCleaner implements Runnable {

    private JXMapViewer mapViewer;
    private List<DefaultTileFactory> factories;

    private int zoomDifferenceToDelete = 4;

    public TileCleaner(JXMapViewer mapViewer, List<DefaultTileFactory> factories) {
        this.mapViewer = mapViewer;
        this.factories = factories;
    }

    @Override
    public void run() {
        while (true) {

            for(DefaultTileFactory factory : factories){
                if ( !mapViewer.getTileFactory().getInfo().getName().equals(factory.getInfo().getName()) ) {
                    factory.tileMap.clear();
                    factory.cache.needMoreMemory();
                } else { //factory = mapViewer.getTileFactory();
                    Set<Map.Entry<String, Tile>> tileSet = factory.tileMap.entrySet();
                    List<Map.Entry<String, Tile>> tileEntries = new ArrayList<Map.Entry<String, Tile>>();
                    tileEntries.addAll(tileSet);
                    int savedZoomLevelCount = zoomDifferenceToDelete * 2 + 1;
                    int currentZoom = mapViewer.getZoom();
                    GeoPosition currentCenterLatLon = mapViewer.getCenterPosition();
                    Map<Integer, int[]> savedZoomList = new HashMap<Integer, int[]>();
                    for (int i = currentZoom - zoomDifferenceToDelete; i <= currentZoom + zoomDifferenceToDelete; i++){
                        savedZoomList.put(i, LatLngToTileCoordinates(factory, currentCenterLatLon.getLatitude(), currentCenterLatLon.getLongitude(),i));
                    }

                    for ( int i = tileEntries.size() - 1 ; i >= 0 ; i-- ) {
                        Map.Entry<String, Tile> tileEntry = tileEntries.get(i);
                        Tile tile = tileEntry.getValue();
                        if (tile.isLoaded()) {
                            synchronized (factory.cache){

                                //DELETE IF ZOOM IS DIFFERENT
                                if(Math.abs(currentZoom - tile.getZoom()) >= zoomDifferenceToDelete){
                                    factory.tileMap.remove(tileEntry.getKey());
                                    if (factory.cache.imgmap.containsKey(tile.getURI()))
                                        factory.cache.imgmap.remove(tile.getURI());
                                    if (factory.cache.bytemap.containsKey(tile.getURI()))
                                        factory.cache.bytemap.remove(tile.getURI());
                                }

                                //DELETE IF COORDINATES ARE DIFFERENT
                                int maxTileDifferenceX = mapViewer.getViewportBounds().width / 256 ;
                                int maxTileDifferenceY = mapViewer.getViewportBounds().height / 256;
                                if ( !(savedZoomList.containsKey(tile.getZoom()) &&
                                        (Math.abs(tile.getX() - savedZoomList.get(tile.getZoom())[0]) <= maxTileDifferenceX) &&
                                        (Math.abs(tile.getY() - savedZoomList.get(tile.getZoom())[1]) <= maxTileDifferenceY) )){
                                        factory.tileMap.remove(tileEntry.getKey());
                                        if (factory.cache.imgmap.containsKey(tile.getURI()))
                                            factory.cache.imgmap.remove(tile.getURI());
                                        if (factory.cache.bytemap.containsKey(tile.getURI()))
                                            factory.cache.bytemap.remove(tile.getURI());
                                }
                            }
                        }
                    }
                }
            }


            System.gc();
            Runtime.getRuntime().gc();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static final int TILE_SIZE = 256;

    private static double[] LatLngToWorldCoordinates( double lat, double lon ) {
        double[] worldCoordinates = new double[2];
        double siny = Math.sin( (lat * Math.PI) / 180.0 );
        siny = Math.min(Math.max(siny, -0.9999), 0.9999);

        worldCoordinates[0] = TILE_SIZE * (0.5 + lon / 360);
        worldCoordinates[1] = TILE_SIZE * (0.5 - Math.log((1.0 + siny) / (1.0 - siny)) / (4.0 * Math.PI));

        return worldCoordinates;
    }

    private static int[] LatLngToTileCoordinates( DefaultTileFactory factory, double lat, double lon, int zoom ) {
        int[] tileCoordinates = new int[2];
        zoom = factory.getInfo().getTotalMapZoom() - zoom;
        double[] worldCoordinates = LatLngToWorldCoordinates(lat,lon);
        int x = (int)( (worldCoordinates[0] * Math.pow(2, zoom)) / 256.0 );
        int y = (int)( (worldCoordinates[1] * Math.pow(2, zoom)) / 256.0 );

        tileCoordinates[0] = x;
        tileCoordinates[1] = y;

        return tileCoordinates;
    }

}

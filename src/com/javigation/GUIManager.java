package com.javigation;

import org.jxmapviewer.GoogleMapsTileFactoryInfo;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.MapClickListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;

public class GUIManager {

    private static JXMapViewer mapViewer;
    private static List<DefaultTileFactory> mapFactories;
    private static WaypointPainter<Waypoint> waypointPainter;
    private static TabController tabControl;
    private static JPanel panelMain = new JPanel();


    public static void setupGUI(JFrame gui) {
        gui.setIconImage(new ImageIcon(GUIManager.class.getClassLoader().getResource("images/javigation.png")).getImage());
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gui.setLayout(new BorderLayout());
        gui.getContentPane().setBackground( new Color(46, 91, 114) );
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupMap();

        tabControl = new TabController();
        gui.add(tabControl, BorderLayout.CENTER);
        tabControl.tabGuiSettings.add(mapViewer);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Select a map type: ");

        String[] tfLabels = new String[mapFactories.size()];
        for (int i = 0; i < mapFactories.size(); i++)
        {
            tfLabels[i] = mapFactories.get(i).getInfo().getName();
        }

        final JComboBox combo = new JComboBox(tfLabels);
        combo.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                DefaultTileFactory factory = mapFactories.get(combo.getSelectedIndex());
                TileFactoryInfo info = factory.getInfo();
                GeoPosition loc = mapViewer.getAddressLocation();
                mapViewer.setZoom(Math.min(Math.max(mapViewer.getZoom(),info.getMinimumZoomLevel()),info.getMaximumZoomLevel()));
                mapViewer.setTileFactory(factory);
                mapViewer.setAddressLocation(loc);
                for(int i = 0; i < mapFactories.size(); i++) {
                    if(i!=combo.getSelectedIndex()){
                        DefaultTileFactory otherFactory = mapFactories.get(i);
                    }
                }
            }
        });

        panel.setLayout(new GridLayout());
        panel.add(label);
        panel.add(combo);

        final JLabel labelThreadCount = new JLabel("Threads: ");

        //gui.add(panel, BorderLayout.NORTH);
        //gui.add(mapViewer);
        //gui.add(labelThreadCount, BorderLayout.SOUTH);

        Timer t = new Timer(500, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Set<Thread> threads = Thread.getAllStackTraces().keySet();
                labelThreadCount.setText("" + ((DefaultTileFactory)mapViewer.getTileFactory()).tileMap.size());
                //((DefaultTileFactory)mapViewer.getTileFactory()).cache.needMoreMemory();
                //((DefaultTileFactory)mapViewer.getTileFactory()).tileMap.clear();
                labelThreadCount.setText("Threads: " + threads.size());
                //labelThreadCount.setText("Threads: " + factories.get(0).tileMap.size() + " - " + factories.get(0).cache.imgmap.size() + " - " + factories.get(0).cache.bytemap.size());
                //labelThreadCount.setText("Threads: " + mapViewer.getCenterPosition().getLatitude() + "," + mapViewer.getCenterPosition().getLongitude());

            }
        });


        t.start();

        gui.pack();
        gui.setSize(1200,800);
        gui.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gui.setVisible(true);

    }

    public static void setupMap() {
        mapViewer = new JXMapViewer();

        mapFactories = new ArrayList<DefaultTileFactory>();
        mapFactories.add(new DefaultTileFactory(new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.HYBRID)));
        mapFactories.add(new DefaultTileFactory(new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.SATELLITE)));
        mapFactories.add(new DefaultTileFactory(new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.TERRAIN)));
        mapFactories.add(new DefaultTileFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID)));
        mapFactories.add(new DefaultTileFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE)));
        mapFactories.add(new DefaultTileFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP)));
        mapFactories.add(new DefaultTileFactory(new OSMTileFactoryInfo()));

        mapViewer.setTileFactory(mapFactories.get(0));
        GeoPosition odeon = new GeoPosition(39.874259, 32.752582);
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(odeon);

        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));



        TileCleaner tileCleaner = new TileCleaner(mapViewer, mapFactories);
        Thread tileCleanerThread = new Thread(tileCleaner);
        tileCleanerThread.start();

        mapViewer.addMouseListener(new MapClickListener(mapViewer) {
            @Override
            public void mapClicked(MouseButton mouseButton, GeoPosition location) {
                switch (mouseButton){
                    case LEFT:
                        System.out.print("LEFT:");
                        break;
                    case RIGHT:
                        System.out.print("RIGHT:");
                        break;
                }
                System.out.println(location.getLatitude()+","+location.getLongitude());
            }
        });

        GeoPosition odeon1 = new GeoPosition(39.874259, 32.752582);
        GeoPosition odeon2 = new GeoPosition(39.87455175245056,32.7525320649147);

        List<GeoPosition> track = Arrays.asList(odeon1, odeon2);
        RoutePainter routePainter = new RoutePainter(track);

        Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(new DefaultWaypoint(odeon1), new DefaultWaypoint(odeon2)));

        waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);

        DronePainter dronePainter = new DronePainter();

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(routePainter, waypointPainter, dronePainter);
        mapViewer.setOverlayPainter(painter);
    }

}

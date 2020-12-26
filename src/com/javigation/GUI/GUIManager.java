package com.javigation.GUI;

import com.javigation.GUI.flight_control_panels.AutopilotControlPanel;
import com.javigation.GUI.flight_control_panels.DroneControlPanel;
import com.javigation.GUI.map.DronePainter;
import com.javigation.GUI.map.RoutePainter;
import com.javigation.GUI.map.TileCleaner;
import com.javigation.Statics;
import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import com.javigation.flight.CommandChain;
import com.javigation.flight.DroneController;
import com.javigation.flight.Swarm;
import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.swing.GstVideoComponent;
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
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GUIManager {

    public static JXMapViewer mapViewer;
    private static List<DefaultTileFactory> mapFactories;
    private static WaypointPainter<Waypoint> waypointPainter;
    private static TabController tabControl;
    private static JPanel panelMain = new JPanel();
    public static DronePainter dronePainter;

    public static final Color COLOR_BLUE = new Color(46, 91, 114);
    public static final Color COLOR_PURPLE = new Color(75, 45, 109);
    public static final Color COLOR_TRANSPARENT = new Color(0,0,0,0);

    public static Containers containers;


    public static void setupGUI(JFrame gui) {
        gui.setIconImage(new ImageIcon(GUIManager.class.getClassLoader().getResource("images/javigation.png")).getImage());
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gui.setLayout(new BorderLayout());
        gui.getContentPane().setBackground(COLOR_BLUE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        tabControl = new TabController();
        gui.add(tabControl, BorderLayout.CENTER);

        setupMap();
        setupGStreamer();
        setupMapControlPanel();
        setupMapAutopilotControlPanel();

        containers = new Containers(mapViewer, vc);

        //tabControl.tabFlightPlan.add(mapViewer);


        tabControl.tabFlightPlan.add(containers.MainContainer);

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

    private static void setupMapControlPanel() {

        DroneControlPanel pnl = new DroneControlPanel();

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.gridx = 1;
        gc.gridy = 1;
        int inset = 30;
        gc.insets = new Insets(-600, inset, inset, inset);
        mapViewer.add(pnl, gc);
    }

    private static void setupMapAutopilotControlPanel() {
        AutopilotControlPanel pnl = new AutopilotControlPanel();

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHEAST;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.gridx = 1;
        gc.gridy = 0;
        int inset = 30;
        gc.insets = new Insets(20, inset, inset, inset);
        mapViewer.add(pnl, gc);

    }

    public static GstVideoComponent vc;
    private static Pipeline pipe;
    public static JPanel gstPanel;

    public static void setupGStreamer() {
        Gst.init("Javigation");

        vc = new GstVideoComponent();
        Bin bin = Gst.parseBinFromDescription("videotestsrc ! videoconvert ! capsfilter caps=video/x-raw,width=1280,height=720",true);
        //Bin bin = Gst.parseBinFromDescription("v4l2src ! videoconvert",true);
        //Bin bin = Gst.parseBinFromDescription("udpsrc port=5600 ! application/x-rtp, encoding-name=H264, payload=96 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert",true);
        pipe = new Pipeline();
        pipe.addMany(bin, vc.getElement());
        Pipeline.linkMany(bin, vc.getElement());


        pipe.play();

    }

    public static void toggleOverMapTools(boolean show) {
        for (Component c : mapViewer.getComponents())
            c.setVisible(show);
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
                        if (DroneControlPanel.controllingDrone != null) {
                            DroneController controller = DroneControlPanel.controllingDrone.controller;
                            CommandChain.Create(controller).GoTo(location.getLatitude(), location.getLongitude(), false).Perform();
                        }
                        break;
                    case RIGHT:
                        System.out.print("RIGHT:");
                        Swarm swarm1 = new Swarm(DroneConnection.Get(14540), DroneConnection.Get(14541), DroneConnection.Get(14542), "Triangle", false);
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

        dronePainter = new DronePainter();

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(routePainter, waypointPainter, dronePainter);
        mapViewer.setOverlayPainter(painter);

        mapViewer.setLayout(new GridBagLayout());

        new Thread(new Runnable() {
            @Override
            public void run() {
                int repaint_interval_ms = 1000 / Statics.MAP_FPS;
                while (true) {
                    if ( System.currentTimeMillis() - mapViewer.lastPaintTime > (repaint_interval_ms - 10) )
                        mapViewer.repaint();
                    try {
                        Thread.sleep(repaint_interval_ms);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}

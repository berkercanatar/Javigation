package com.javigation;

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
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;
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

    public static final Color COLOR_BLUE = new Color(46, 91, 114);
    public static final Color COLOR_PURPLE = new Color(75, 45, 109);
    public static final Color COLOR_TRANSPARENT = new Color(0,0,0,0);


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

        setupMap();

        setupGStreamer();
        setupMapControlPanel();


        tabControl = new TabController();
        gui.add(tabControl, BorderLayout.CENTER);
        tabControl.tabFlightPlan.add(mapViewer);


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
        gc.gridy = 0;
        int inset = 30;
        gc.insets = new Insets(-350, inset, inset, inset);
        mapViewer.add(pnl, gc);
    }

    public static GstVideoComponent vc;
    private static Pipeline pipe;
    public static JPanel gstPanel;

    public static void setupGStreamer() {
        System.setProperty("jna.library.path", "D:\\gstreamer\\1.0\\mingw_x86_64\\bin\\;D:\\gstreamer\\1.0\\mingw_x86_64\\lib\\gstreamer-1.0\\");
        //System.setProperty("jna.debug_load", "true");
        Gst.init("CameraTest");

        vc = new GstVideoComponent();
        Bin bin = Gst.parseBinFromDescription(
                "videotestsrc ! videoconvert ! capsfilter caps=video/x-raw,width=1280,height=720",
                true);
        pipe = new Pipeline();
        pipe.addMany(bin, vc.getElement());
        Pipeline.linkMany(bin, vc.getElement());

        gstPanel = new JPanel(new BorderLayout());
        gstPanel.add(vc, BorderLayout.CENTER);

        int thickness = 3;
        int height = 234;
        int inset = 30;
        gstPanel.setPreferredSize(new Dimension(height*16/9 + thickness*2, height+thickness*2));
        gstPanel.setBorder(new LineBorder(Color.RED, thickness));

        mapViewer.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.SOUTHEAST;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(inset, inset, inset, inset);
        mapViewer.add(gstPanel, gc);

        //mapViewer.revalidate();

        pipe.play();
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

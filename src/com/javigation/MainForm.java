package com.javigation;

import io.mavsdk.action.Action;
import io.mavsdk.mission.Mission;
import io.mavsdk.offboard.Offboard;
import org.jxmapviewer.*;
import org.jxmapviewer.input.MapClickListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

public class MainForm extends JFrame {

    JPanel panelMain = new JPanel();
    //MapView mapView = new MapView();

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
    }

    public MainForm() {
        super("Javigation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(1200,800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(panelMain);

        final List<DefaultTileFactory> factories = new ArrayList<DefaultTileFactory>();
        factories.add(new DefaultTileFactory(new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.HYBRID)));
        factories.add(new DefaultTileFactory(new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.SATELLITE)));
        factories.add(new DefaultTileFactory(new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.TERRAIN)));
        factories.add(new DefaultTileFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID)));
        factories.add(new DefaultTileFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE)));
        factories.add(new DefaultTileFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP)));
        factories.add(new DefaultTileFactory(new OSMTileFactoryInfo()));

        final JXMapViewer mapViewer = new JXMapViewer();

        mapViewer.setTileFactory(factories.get(0));
        GeoPosition odeon = new GeoPosition(39.874259, 32.752582);
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(odeon);

        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        DroneController drone = new DroneController();
        drone.Arm().subscribe();


        mapViewer.addMouseListener(new MapClickListener(mapViewer) {
            @Override
            public void mapClicked(MouseButton mouseButton, GeoPosition location) {
                switch (mouseButton){
                    case LEFT:
                        System.out.print("LEFT:");
                        ((DefaultTileFactory)mapViewer.getTileFactory()).tileMap.clear();
                        System.gc();
                        Runtime.getRuntime().gc();
                        drone.TakeOff().subscribe();
                        break;
                    case RIGHT:
                        System.out.print("RIGHT:");
                        ((DefaultTileFactory)mapViewer.getTileFactory()).cache.needMoreMemory();
                        System.gc();
                        Runtime.getRuntime().gc();
                        drone.Land().subscribe();
                        break;
                }
                System.out.println(location.getLatitude()+","+location.getLongitude());
            }
        });

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Select a TileFactory ");

        String[] tfLabels = new String[factories.size()];
        for (int i = 0; i < factories.size(); i++)
        {
            tfLabels[i] = factories.get(i).getInfo().getName();
        }

        final JComboBox combo = new JComboBox(tfLabels);
        combo.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                DefaultTileFactory factory = factories.get(combo.getSelectedIndex());
                TileFactoryInfo info = factory.getInfo();
                GeoPosition loc = mapViewer.getAddressLocation();
                mapViewer.setZoom(Math.min(Math.max(mapViewer.getZoom(),info.getMinimumZoomLevel()),info.getMaximumZoomLevel()));
                mapViewer.setTileFactory(factory);
                mapViewer.setAddressLocation(loc);
                for(int i = 0; i < factories.size(); i++) {
                    if(i!=combo.getSelectedIndex()){
                        DefaultTileFactory otherFactory = factories.get(i);
                    }
                }
            }
        });

        panel.setLayout(new GridLayout());
        panel.add(label);
        panel.add(combo);

        final JLabel labelThreadCount = new JLabel("Threads: ");

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(mapViewer);
        add(labelThreadCount, BorderLayout.SOUTH);


        TileCleaner tileCleaner = new TileCleaner(mapViewer, factories);
        Thread tileCleanerThread = new Thread(tileCleaner);
        tileCleanerThread.start();



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

        setVisible(true);
    }

}

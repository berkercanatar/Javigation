package com.javigation;

import org.jxmapviewer.*;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

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

public class MainForm extends JFrame {

    JPanel panelMain = new JPanel();
    //MapView mapView = new MapView();

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
    }

    public MainForm() {
        super("Javigation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(panelMain);

        final List<TileFactory> factories = new ArrayList<TileFactory>();

        TileFactoryInfo ghInfo = new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.HYBRID);
        TileFactoryInfo gsInfo = new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.SATELLITE);
        TileFactoryInfo gtInfo = new GoogleMapsTileFactoryInfo(GoogleMapsTileFactoryInfo.MapType.TERRAIN);
        TileFactoryInfo veInfo = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
        TileFactoryInfo osmInfo = new OSMTileFactoryInfo();

        factories.add(new DefaultTileFactory(ghInfo));
        factories.add(new DefaultTileFactory(gsInfo));
        factories.add(new DefaultTileFactory(gtInfo));
        factories.add(new DefaultTileFactory(veInfo));
        factories.add(new DefaultTileFactory(osmInfo));

        final JXMapViewer mapViewer = new JXMapViewer();
        final JLabel labelAttr = new JLabel();
        mapViewer.setLayout(new BorderLayout());
        mapViewer.add(labelAttr, BorderLayout.SOUTH);

        TileFactory firstFactory = factories.get(0);
        mapViewer.setTileFactory(firstFactory);
        labelAttr.setText("TEST");

        GeoPosition odeon = new GeoPosition(39.874259, 32.752582);

        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(odeon);

        MouseInputListener mia= new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

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
                TileFactory factory = factories.get(combo.getSelectedIndex());
                TileFactoryInfo info = factory.getInfo();
                GeoPosition loc = mapViewer.getAddressLocation();
                mapViewer.setZoom(info.getMinimumZoomLevel());
                mapViewer.setTileFactory(factory);
                mapViewer.setAddressLocation(loc);
                labelAttr.setText(info.getAttribution() + " - " + info.getLicense());
            }
        });

        panel.setLayout(new GridLayout());
        panel.add(label);
        panel.add(combo);

        final JLabel labelThreadCount = new JLabel("Threads: ");

        // Display the viewer in a JFrame

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(mapViewer);
        add(labelThreadCount, BorderLayout.SOUTH);


        Timer t = new Timer(500, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Set<Thread> threads = Thread.getAllStackTraces().keySet();
                labelThreadCount.setText("Threads: " + threads.size());
            }
        });

        t.start();



        setVisible(true);

        double lat = 39.863599;
        double lon = 32.749791;

        System.out.println(GoogleMapProvider.getURL(GoogleMapProvider.MapType.SATELLITE,lat,lon,18));
        System.out.println(GoogleMapProvider.getURL(GoogleMapProvider.MapType.HYBRID,lat,lon,18));
        System.out.println(GoogleMapProvider.getURL(GoogleMapProvider.MapType.TERRAIN,lat,lon,18));
    }

}

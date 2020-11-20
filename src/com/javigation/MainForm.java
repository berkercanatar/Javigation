package com.javigation;

import javax.swing.*;

public class MainForm extends JFrame {

    JPanel panelMain = new JPanel();
    MapView mapView = new MapView();

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
    }

    public MainForm() {
        super("Javigation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        add(panelMain);
        panelMain.add(mapView);

        setVisible(true);

        double lat = 39.863599;
        double lon = 32.749791;

        System.out.println(GoogleMapProvider.getURL(GoogleMapProvider.MapType.SATELLITE,lat,lon,18));
        System.out.println(GoogleMapProvider.getURL(GoogleMapProvider.MapType.HYBRID,lat,lon,18));
        System.out.println(GoogleMapProvider.getURL(GoogleMapProvider.MapType.TERRAIN,lat,lon,18));
    }

}

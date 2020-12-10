package com.javigation;

import io.mavsdk.action.Action;
import io.mavsdk.mission.Mission;
import io.mavsdk.offboard.Offboard;
import org.jxmapviewer.*;
import org.jxmapviewer.input.MapClickListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;

public class MainForm extends JFrame {

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
    }

    public MainForm() {
        super("Javigation");
        GUIManager.setupGUI(this);

        DroneConnection.Get(14540);
    }

}

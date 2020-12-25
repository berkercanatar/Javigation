package com.javigation.GUI;

import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import com.javigation.drone_link.video.GStreamerDownloader;
import com.javigation.Statics;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import static com.javigation.Statics.JAVIGATION_FOLDER;

public class MainForm extends JFrame {


    public static void main(String[] args) {

        System.setProperty("sun.java2d.opengl", "true");

        boolean isDebugging = Utils.runningFromIntelliJ();
        //if (!isDebugging) //Skip splash screen when debugging
            //new SplashScreen();

        CheckLocalFolder();
        if (Platform.isWindows())
            CheckGStreamer();
        else
            LaunchMainForm();

    }

    private static void LaunchMainForm() {
        MainForm mainForm = new MainForm();
    }

    private static void CheckLocalFolder() {
        String appDataPath = "";
        if (Platform.isWindows())
            appDataPath = System.getenv("APPDATA");
        else if (Platform.isLinux())
            appDataPath = System.getProperty("user.home");
        else if (Platform.isMac())
            appDataPath = System.getProperty("user.home") + "/Library/Application Support";
        else
            appDataPath = System.getProperty("user.dir");

        JAVIGATION_FOLDER = Paths.get( appDataPath , "Javigation");
        if (!Files.isDirectory(JAVIGATION_FOLDER)) {
            try {
                Files.createDirectory(JAVIGATION_FOLDER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void CheckGStreamer() {

        Statics.GSTREAMER_ROOT = Paths.get(JAVIGATION_FOLDER.toString(), "gstreamer", "1.0", Platform.is64Bit() ? "mingw_x86_64" : "mingw_x86");
        setPathsForGStreamer();
        if (Files.isDirectory(Statics.GSTREAMER_ROOT)) {
            LaunchMainForm();
            return;
        }


        String downloadLink = Platform.is64Bit() ? Statics.GSTREAMER_x64_URL : Statics.GSTREAMER_x86_URL;


        JFrame frm = new JFrame();
        final JProgressBar current = new JProgressBar(0, 100);
        current.setSize(50, 100);
        current.setValue(0);
        current.setStringPainted(true);
        frm.add(current);
        frm.setVisible(true);
        frm.setLayout(new FlowLayout());
        frm.setSize(50, 100);
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final GStreamerDownloader worker = new GStreamerDownloader(downloadLink);
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("progress")) {
                    current.setValue((int) evt.getNewValue());
                } else if ( evt.getNewValue() == SwingWorker.StateValue.DONE ) {
                    try {
                        frm.dispatchEvent(new WindowEvent(frm, WindowEvent.WINDOW_CLOSING));
                        Path downloadedPath = worker.get();
                        installGstreamer(downloadedPath);
                    } catch (InterruptedException | ExecutionException e) {
                        // handle any errors here
                        e.printStackTrace();
                    }
                }
            }
        });

        worker.execute();
    }

    private static void installGstreamer(Path installerPath) {

        Process p = null;
        try {
            p = Runtime.getRuntime().exec( new String[]{ "msiexec" , "/a", installerPath.toString(), "/passive", "TARGETDIR=\"" + JAVIGATION_FOLDER + "\"" } );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LaunchMainForm();
    }

    private static void setPathsForGStreamer() {
        Kernel32 kernel = Kernel32.INSTANCE;
        String pathEnvVar = System.getenv("PATH");

        String GstBinDir = Paths.get(Statics.GSTREAMER_ROOT.toString(), "bin").toString();
        String GstLibDir = Paths.get(Statics.GSTREAMER_ROOT.toString(), "lib").toString();
        kernel.SetEnvironmentVariable("PATH", GstBinDir + File.pathSeparator + ((pathEnvVar == null || pathEnvVar.trim().isEmpty()) ? "" : pathEnvVar));
        System.setProperty("jna.library.path", GstLibDir);

    }

    public MainForm() {
        super("Javigation");
        GUIManager.setupGUI(this);

        DroneConnection.Get(14540);
    }

}

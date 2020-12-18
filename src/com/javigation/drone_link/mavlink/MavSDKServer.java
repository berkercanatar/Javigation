package com.javigation.drone_link.mavlink;

import com.sun.jna.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

interface MavSDKServerReadyListener {
    void onServerInitialized();
}

public class MavSDKServer  {

    DroneConnection connection;

    public MavSDKServer(DroneConnection connection, int incomingMavlinkPort, int localMavSDKPort) {
        String mavSdkProcPath = "";
        if (Platform.isLinux()) {
            mavSdkProcPath = MavSDKServer.class.getClassLoader().getResource("mavsdk_server/mavsdk_server_manylinux2010-x64").getPath();
        } else if (Platform.isWindows()) {
            mavSdkProcPath = MavSDKServer.class.getClassLoader().getResource("mavsdk_server/mavsdk_server_win32.exe").getPath();
        }

        this.connection = connection;

        launch(mavSdkProcPath, "-p", Integer.toString(localMavSDKPort),  "udp://:" + incomingMavlinkPort);

    }

    private void launch(final String ... args) {
        String threadID = "MAVSDK-Server";

        new Thread(new Runnable() {
            public void run() {
                ProcessBuilder b = new ProcessBuilder(args);
                b.redirectErrorStream(true);
                try {
                    Process process = b.start();

                    Thread shutdownMavlinkThread = new Thread(() -> process.destroy());
                    Runtime.getRuntime().addShutdownHook(shutdownMavlinkThread);

                    readOutput(process);
                    process.waitFor();
                    process.destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            private void readOutput(Process process) throws IOException {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    System.err.println(line);
                    if (line.contains("Server set to listen"))
                        connection.onServerInitialized();
                    else if (line.contains("Discovered"))
                        DroneConnection.onDroneConnected(connection);
                    else if (line.contains("Lost"))
                        DroneConnection.onDroneDisconnected(connection);
                }
            }
        }, threadID).start();
    }


}

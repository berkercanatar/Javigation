package com.javigation.drone_link.video;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class GStreamerDownloader extends SwingWorker<Path, Void> {

    private String downloadLink;

    public GStreamerDownloader(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    @Override
    protected Path doInBackground() throws Exception {
        URL downloadURL = null;
        try {
            downloadURL = new URL(downloadLink);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String fileName = downloadLink.substring( downloadLink.lastIndexOf('/')+1);
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));

        Path gstInstaller = null;
        try {
            gstInstaller = Files.createTempFile(fileName, ".msi");
            gstInstaller.toFile().deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(gstInstaller.toString());


        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) downloadURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fileSize = connection.getContentLength();

        long downloadedDataSize = 0;
        try (BufferedInputStream in = new BufferedInputStream(downloadURL.openStream());
            FileOutputStream fileOut = new FileOutputStream( gstInstaller.toFile() );
            BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut, 1024) ) {

            System.out.println(fileSize);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) >= 0) {
                bufferedOut.write( dataBuffer, 0, bytesRead);
                downloadedDataSize += bytesRead;
                setProgress( (int)(100 * downloadedDataSize / fileSize) );
            }
            return gstInstaller;
        } catch (IOException e) {
            // handle exception
        }

        return null;
    }

}

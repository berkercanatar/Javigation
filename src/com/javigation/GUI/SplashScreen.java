package com.javigation.GUI;

import javax.swing.*;
import java.awt.*;

public class SplashScreen {
    JFrame frame;
    JLabel image = new JLabel();
    JProgressBar progressBar = new JProgressBar();

    public SplashScreen(){
        createSplashScreen();
    }

    private void createSplashScreen(){
        frame = new JFrame();
        frame.getContentPane().setLayout(null);
        frame.setUndecorated(true);
        frame.setSize(273,392);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setVisible(true);

        image.setSize(273,392);//putting the image
        frame.add(image);

        progressBar.setBounds( 65,340,143,20);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.LIGHT_GRAY);
        progressBar.setForeground(Color.DARK_GRAY);
        progressBar.setValue(0);
        frame.add(progressBar);

        setProgressBar();
    }

    private void setProgressBar(){
        for( int i = 0; i <= 100; i++)
        {
            try{
                Thread.sleep(30);
                progressBar.setValue(i);
                if (i==100)
                    frame.dispose();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

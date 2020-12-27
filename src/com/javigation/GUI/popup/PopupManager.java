package com.javigation.GUI.popup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupManager {

    JWindow popup;
    private float alpha = 0f;
    private long start = -1;
    private long runtime = 2000;


    public PopupManager(String message, String type){

        popup = new JWindow();
        Color color = getType(type);
        Color colorText = getContrastColor( color );

        popup.setBackground(color);


        JPanel pnl = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {

                g.setColor( colorText );
                g.drawString( message, 30, 25);

            }
        };
        pnl.setPreferredSize( new Dimension(1000, 50));
        popup.add( pnl );
        popup.setLocation(450, 100);
        popup.pack();

        showIt();


    }

    public static void showAlert(String message){
        new PopupManager( message, "warning");
    }

    public static void showError(String message){
        new PopupManager( message, "error");
    }

    public static void showInfo(String message){
        new PopupManager( message, "info");
    }

    public static void showComplete(String message){
        new PopupManager( message, "completed");
    }

    private void showIt() {
        popup.setVisible(true);

        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( ((Timer) e.getSource()).getDelay() == 1000) {
                    popup.setVisible(false);
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        t.start();
    }

    private Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }

    private Color getType(String type) {

        type = type.toLowerCase();

        if ( type.equals("error") )
            return Color.red;
        else if ( type.equals("warning") )
            return Color.yellow;
        else if ( type.equals("completed") )
            return Color.green;
        else
            return Color.blue;
    }
}

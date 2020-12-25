package com.javigation.GUI.popup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupManager {

    JWindow popup;


    public PopupManager(String message, String type){

        popup = new JWindow();
        Color color = getType(type);
        Color colorText = getContrastColor( color );

        popup.setBackground(color);


        JPanel pnl = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {

                int width = g.getFontMetrics().stringWidth(message);
                int hei = 35;

                g.setColor( color );
                g.fillRect(10, 10, width + 30, hei + 10);
                g.setColor( colorText );
                g.drawString( message, 150 - width, 17);

            }
        };
        pnl.setPreferredSize( new Dimension(300, 35));
        popup.add( pnl );
        popup.setLocation(900, 100);
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

    private void showIt() {
        popup.setOpacity(1);
        popup.setVisible(true);

        Timer t = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (((Timer) e.getSource()).getDelay() == 3000) {
                    for (double d = 1.00; d > 0.0002; d -= 0.0001) {
                        //Thread.sleep(100);
                        popup.setOpacity((float)d);
                    }
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
        else
            return Color.blue;
    }
}

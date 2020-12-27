package com.javigation.GUI.popup;

import com.javigation.GUI.Containers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupManager {

    JWindow popup;
    private float alpha = 0f;
    private long start = -1;
    private long runtime = 2000;

    private static JLabel messageLabel;

    public static void setup() {
        //CREATE POPUP PANEL or COMPONENT

        messageLabel = new JLabel();
        //messageLabel.setFont();

        Containers.popupContainer.add(messageLabel);

        Containers.popupContainer.add(new JButton());
        Containers.popupContainer.setVisible(false);
    }

    public enum PopupType {
        WARNING,
        ERROR,
        INFO,
        SUCCESS
    }


    public PopupManager(String message, PopupType type){

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
        //new PopupManager( message, PopupType.WARNING);

        //SET BACKGROUNDS
        messageLabel.setText(message);
        Containers.popupContainer.setVisible(true);
        //START TIMER
    }

    public static void showError(String message){
        new PopupManager( message, PopupType.ERROR);
    }

    public static void showInfo(String message){
        new PopupManager( message, PopupType.INFO);
    }

    public static void showComplete(String message){
        new PopupManager( message, PopupType.SUCCESS);
    }

    private void showIt() {
        popup.setVisible(true);

        Timer t = new Timer(1000, e -> {
            if ( ((Timer) e.getSource()).getDelay() == 1000) {
                popup.setVisible(false);
                ((Timer) e.getSource()).stop();
            }
        });

        t.start();
    }

    private Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }

    private Color getType(PopupType type) {

        Color color = switch (type) {
            case ERROR -> Color.red;
            case WARNING -> Color.yellow;
            case SUCCESS -> Color.green;
            default -> Color.blue;
        };

        return color;
    }
}

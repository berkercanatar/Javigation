package com.javigation.GUI.popup;

import com.javigation.GUI.Containers;
import com.javigation.GUI.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PopupManager {



    private static JLabel messageLabel;
    private static JPanel panel;


    public static void setup() {
        //CREATE POPUP PANEL or COMPONENT

        panel = new JPanel();
        messageLabel = new JLabel();
        messageLabel.setVerticalAlignment(SwingConstants.TOP);
        messageLabel.setHorizontalAlignment( SwingConstants.CENTER );
        panel.setPreferredSize(new Dimension(800, 74));
        messageLabel.setPreferredSize( new Dimension( 600, 30));
        panel.add( messageLabel);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Containers.popupContainer.setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        Containers.popupContainer.setBackground( panel.getBackground() );
        //panel.setBorder( new RoundedBorder( panel.getBackground(), 2, 10 ) );

        Containers.popupContainer.add(panel);
        //Containers.popupContainer.setBorder( new RoundedBorder( panel.getBackground(), 1, 10));
        Containers.popupContainer.setVisible(false);

    }

    public enum PopupType {
        WARNING,
        ERROR,
        INFO,
        SUCCESS
    }

    public static void showAlert(String message){

        panel.setBackground( getColor( PopupType.WARNING ) );
        messageLabel.setForeground( getContrastColor( panel.getBackground() ));
        messageLabel.setFont(new Font("TimesRoman", Font.ITALIC, 20));
        Containers.popupContainer.setBackground( panel.getBackground() );
        messageLabel.setText(message);
        //START TIMER
        showIt();
    }

    public static void showError(String message){
        panel.setBackground( getColor( PopupType.ERROR ) );
        messageLabel.setForeground( getContrastColor( panel.getBackground() ));
        messageLabel.setFont( new Font("TimesRoman", Font.BOLD, 20) );
        Containers.popupContainer.setBackground( panel.getBackground() );
        messageLabel.setText(message);
        //START TIMER
        showIt();
    }

    public static void showInfo(String message){
        panel.setBackground( getColor( PopupType.INFO ) );
        messageLabel.setForeground( getContrastColor( panel.getBackground() ));
        messageLabel.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        Containers.popupContainer.setBackground( panel.getBackground() );
        messageLabel.setText(message);
        //START TIMER
        showIt();
    }

    public static void showSuccess(String message){
        panel.setBackground( getColor( PopupType.SUCCESS ) );
        messageLabel.setForeground( getContrastColor( panel.getBackground() ));
        Containers.popupContainer.setBackground( panel.getBackground() );
        messageLabel.setFont(new Font("TimesRoman", Font.ITALIC, 15));
        messageLabel.setText(message);
        //START TIMER
        showIt();
    }

    private static Timer t = new Timer(5000, e -> {
        if ( ((Timer) e.getSource()).getDelay() == 5000) {
            Containers.popupContainer.setVisible(false);
            ((Timer) e.getSource()).stop();
        }
    });

    private static void showIt() {
        Containers.popupContainer.setVisible(true);
        t.stop();
        t.start();
    }

    private static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }

    private static Color getColor(PopupType type) {

        Color color = switch (type) {
            case ERROR -> Color.red;
            case WARNING -> Color.yellow;
            case SUCCESS -> Color.green;
            default -> Color.blue;
        };

        return color;
    }
}

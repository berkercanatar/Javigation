package com.javigation.GUI.popup;

import com.javigation.GUI.Containers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupManager {


    private float alpha = 0f;
    private long start = -1;
    private long runtime = 2000;

    private static JLabel messageLabel;
    private static JPanel panel;

    public static void setup() {
        //CREATE POPUP PANEL or COMPONENT
        panel = new JPanel();
        messageLabel = new JLabel();
        messageLabel.setVerticalAlignment(SwingConstants.TOP);
        panel.setLayout( new BorderLayout() );
        panel.setPreferredSize(new Dimension(800, 100));
        panel.add( messageLabel, BorderLayout.WEST );
        Containers.popupContainer.setBackground( panel.getBackground() );
        Containers.popupContainer.add(panel);
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
        messageLabel.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        messageLabel.setText(message);
        //START TIMER
        showIt();
    }

    private static void showIt() {
        Containers.popupContainer.setVisible(true);

        Timer t = new Timer(1000, e -> {
            if ( ((Timer) e.getSource()).getDelay() == 1000) {
                Containers.popupContainer.setVisible(false);
                ((Timer) e.getSource()).stop();
            }
        });

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

package com.javigation.GUI.popup;

import com.javigation.flight.CommandChain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Slider extends JPanel {

    private CommandChain action;

    public static Slider INSTANCE;

    public JLabel actionNameLabel;

    public Slider() {
        INSTANCE = this;

        try {
            for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(laf.getName())) {
                    UIManager.setLookAndFeel(laf.getClassName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UIDefaults slide = new UIDefaults();
        slide.put("Slider.thumbHeight", 75);
        slide.put("Slider.thumbWidth", 75);

        JSlider slider = new JSlider();
        slider.setValue(0);
        setVisible(false);
        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (100 * slider.getValue() / slider.getMaximum() > 95) {
                    setVisible(false);
                    action.Perform();
                    slider.setValue(0);
                } else {
                    slider.setValue(0);
                }
            }
        });

        slider.putClientProperty("Nimbus.Overrides", slide);
        JButton button = new JButton("Exit");
        actionNameLabel = new JLabel("Confirm");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        JPanel labelP = new JPanel();
        actionNameLabel.setHorizontalAlignment( SwingConstants.CENTER );
        labelP.setLayout( new BorderLayout());
        labelP.add(actionNameLabel, BorderLayout.CENTER);
        labelP.add( button, BorderLayout.EAST);

        setLayout( new BorderLayout() );
        add(labelP, BorderLayout.NORTH);
        setBackground(Color.DARK_GRAY);
        add(slider, BorderLayout.CENTER);

    }

    public static void launchSlider(String text, CommandChain action) {
        INSTANCE.action = action;
        INSTANCE.actionNameLabel.setText(text);
        INSTANCE.setVisible(true);
    }
}
package com.javigation.GUI.popup;

import com.javigation.Utils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Slider {

    public void makeUI() {
        JFrame f = new JFrame();


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
        slide.put("Slider.thumbHeight", 100);
        slide.put("Slider.thumbWidth", 50);

        JSlider slider = new JSlider();
        slider.setValue(0);
        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (100 * slider.getValue() / slider.getMaximum() > 85) {
                    f.setVisible(false);
                    f.dispose();
                } else {
                    slider.setValue(0);
                }
            }
        });

        slider.putClientProperty("Nimbus.Overrides", slide);
        JButton button = new JButton("Exit");
        JLabel label = new JLabel("Confirmation");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
            }
        });
        JPanel p = new JPanel();
        JPanel labelP = new JPanel();
        label.setHorizontalAlignment( SwingConstants.CENTER );
        labelP.setLayout( new BorderLayout());
        labelP.add(label, BorderLayout.CENTER);
        labelP.add( button, BorderLayout.EAST);

        p.setLayout( new BorderLayout() );
        p.add(labelP, BorderLayout.NORTH);
        p.setBackground(Color.DARK_GRAY);
        p.add(slider, BorderLayout.CENTER);

        f.setUndecorated(true);
        f.getContentPane().add(p);
        f.setResizable(false);
        f.setSize(320, 240);
        f.setLocation(800, 800);
        f.setVisible(true);

    }
    public static void launchSlider() {

            new Slider().makeUI();

    }
}
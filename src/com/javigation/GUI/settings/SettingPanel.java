package com.javigation.GUI.settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingPanel extends JPanel {

    JPanel adjustColor, adjustLooking, colorinSide;
    JTextField colorCode;
    Color color = new Color( 255, 0, 100);


    public SettingPanel(){
        this.setBackground(Color.GREEN );
        this.setLayout( new GridLayout( 5,1 ) );
        setPanels();
    }

    private void setPanels() {
        adjustColor = new JPanel();
        adjustLooking = new JPanel();
        colorCode = new JTextField();
        colorinSide = new JPanel();
        colorinSide.setLayout( new BorderLayout() );
        colorinSide.add( new JLabel("Choose a color" ), BorderLayout.CENTER );
        adjustColor.setLayout( new GridLayout(1,2, 80, 40) );
        adjustLooking.setLayout( new GridLayout(1,2) );
        adjustColor.setBackground( color );
        adjustLooking.setBackground( color );
        adjustColor.add( colorinSide );
        adjustColor.add( colorCode );
        colorCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Entered");
                int code = Integer.parseInt(( (JTextField) e.getSource()).getText());
                System.out.println(code);
                color = new Color( 155, 0, code);
                adjustColor.setBackground( color);
                adjustLooking.setBackground( color );
            }
        });
        this.add( adjustColor);
        this.add( adjustLooking);
    }

}

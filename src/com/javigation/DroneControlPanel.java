package com.javigation;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DroneControlPanel extends JPanel {

    public DroneControlPanel() {
        setPreferredSize(new Dimension(50,200));
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 5, 20, new Color(GUIManager.COLOR_PURPLE.getRed(), GUIManager.COLOR_PURPLE.getGreen(), GUIManager.COLOR_PURPLE.getBlue(), 200)));
        add(new JButton("test"));
    }

}

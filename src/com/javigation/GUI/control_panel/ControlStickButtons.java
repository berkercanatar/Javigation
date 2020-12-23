package com.javigation.GUI.control_panel;

import com.javigation.GUI.GUIManager;

import javax.swing.*;
import java.awt.*;

public class ControlStickButtons extends JPanel {

    public ControlStickButtons(Component leftComponent, Component topComponent, Component bottomComponent, Component rightComponent) {
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 1.0;
        c.gridx=0;
        c.gridy=0;
        c.insets = new Insets( 3, 3, 3, 3);
        c.anchor = GridBagConstraints.CENTER;
        add(leftComponent, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 0.5;
        add(topComponent, c);
        c.anchor = GridBagConstraints.SOUTH;
        add(bottomComponent, c);
        c.gridx=2;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 1;
        add(rightComponent, c);
    }

}

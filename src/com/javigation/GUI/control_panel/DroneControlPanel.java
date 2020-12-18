package com.javigation.GUI.control_panel;

import com.javigation.GUI.GUIManager;
import com.javigation.GUI.RoundedBorder;
import com.javigation.Utils;
import com.javigation.flight.Command;
import com.javigation.flight.CommandChain;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class DroneControlPanel extends JPanel {

    public DroneControlPanel() {
        setPreferredSize(new Dimension(400,400));
        setLayout(new GridLayout(4,2));
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 5, 20, Utils.colorWithAlpha(GUIManager.COLOR_PURPLE, 0.25f)));

        generateButtons();

    }

    private void generateButtons() {
        for ( Command.CommandType buttonType : Command.CommandType.values() ) {
           add(new DroneControlPanelButton(this, buttonType));
        }

    }

}

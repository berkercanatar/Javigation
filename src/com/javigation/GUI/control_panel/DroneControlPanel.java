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
        setPreferredSize(new Dimension(400,180));
        //setLayout(new GridLayout(4,2));
        //setLayout(new GridBagLayout());
        setLayout(null);
        setBackground(GUIManager.COLOR_TRANSPARENT);
        setBorder(new RoundedBorder(Color.BLACK, 5, 20, Utils.colorWithAlpha(GUIManager.COLOR_PURPLE, 0.25f)));

        generateButtons();

    }

    private void generateButtons() {
        /*for (Command.CommandType buttonType : Command.CommandType.values()) {
            add(new DroneControlPanelButton(this, buttonType));
        }

         */
        /*for (Command. CommandType buttonType : Command.CommandType.values()) {

            String button =
            JButton button_ = new DroneControlPanelButton(this,buttonType);
        }

         */


        JButton ascend = new DroneControlPanelButton(this,Command.CommandType.ASCEND);
        JButton descend = new DroneControlPanelButton(this,Command.CommandType.DESCEND);
        JButton yaw_ccw = new DroneControlPanelButton(this,Command.CommandType.YAW_CCW);
        JButton yaw_cw = new DroneControlPanelButton(this,Command.CommandType.YAW_CW);

        JButton pitch_down = new DroneControlPanelButton(this,Command.CommandType.PITCH_DOWN);
        JButton pitch_up = new DroneControlPanelButton(this,Command.CommandType.PITCH_UP);
        JButton roll_left = new DroneControlPanelButton(this,Command.CommandType.ROLL_LEFT);
        JButton roll_right = new DroneControlPanelButton(this,Command.CommandType.ROLL_RIGHT);

       /* GridBagConstraints c = new GridBagConstraints();
        //c.gridheight = 10;
        //c.gridwidth = 10;


        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.BASELINE_LEADING; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 1;       //aligned with button 2
        c.gridwidth = 2;   //2 columns wide
        c.gridy = 2;       //third row
        add(ascend,c);
        c.gridx = 3;       //aligned with button 2
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        add(descend ,c);
        c.gridx = 0;       //aligned with button 2
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        add(yaw_ccw,c);

        */
        add(ascend);
        add(descend);
        add(yaw_ccw);
        add(yaw_cw);

        add(pitch_down);
        add(pitch_up);
        add(roll_left);
        add(roll_right);

        Insets insets = getInsets();
        Dimension size = ascend.getPreferredSize();

        ascend.setBounds(56 + insets.left, 0 + insets.top,
                size.width, size.height);

        descend.setBounds(56 + insets.left, 70 + insets.top,
                size.width, size.height);

        yaw_cw.setBounds(107 + insets.left, 35 + insets.top,
                size.width, size.height);

        yaw_ccw.setBounds(5 + insets.left, 35 + insets.top,
                size.width, size.height);

        pitch_up.setBounds(246 + insets.left, 0 + insets.top,
                size.width, size.height);

        pitch_down.setBounds(246 + insets.left, 70 + insets.top,
                size.width, size.height);

        roll_right.setBounds(297 + insets.left, 35 + insets.top,
                size.width, size.height);

        roll_left.setBounds(195 + insets.left, 35 + insets.top,
                size.width, size.height);
    }



}

package com.javigation.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TutorialPanel extends JPanel {

    public static JTabbedPane pane;
    public static final Color TAB_UNSELECTED_COLOR = new Color(21, 53, 68);
    public static final Color TAB_BAR_COLOR = new Color(46, 91, 114);


    public TutorialPanel(){
        setGUI();
    }

    public void setGUI(){
        this.setBackground(TAB_BAR_COLOR);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
        Dimension a = new Dimension(1000,550);
        tabbedPane.setPreferredSize(a);
        JPanel helpTab1, helpTab2, helpTab3, helpTab4;
        ControlPanelTutorial controlPanelTutorial = new ControlPanelTutorial();
        helpTab1 = new JPanel();
        helpTab2 = new JPanel();
        helpTab3 = new JPanel();

        tabbedPane.addTab("Connect to the drone  ", new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/javigation.png")),helpTab1);
        tabbedPane.addTab("Get ready for take-off",new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/droneSettings.png")),helpTab2);
        tabbedPane.addTab("Learn to fly          ",new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/flightPlan.png")),controlPanelTutorial);
        tabbedPane.addTab("Eye in the sky        ",new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/camera.png")),helpTab3);
        tabbedPane.setFont(new Font( "Tahoma", Font.BOLD, 16 ));

        add(tabbedPane);
        setSize(1000,450);
        setVisible(true);
    }

}
class ControlPanelTutorial extends JPanel{
    BufferedImage flightControl;
    JButton change;
    JPanel firstPanel;
    JPanel secondPanel;
    JPanel selectedPanel;
    BufferedImage ascend,descend,land,missionAbort,missionPause,missionResume,missionStart,pitchDown,pitchUp,rollLeft,rollRight,rtl,takeOff,yawCcw,yawCw;

    public ControlPanelTutorial(){
        change = new JButton();
        firstPanel = new JPanel();
        secondPanel = new JPanel();
        firstPanel.setBackground(TutorialPanel.TAB_UNSELECTED_COLOR);

        this.setBackground(TutorialPanel.TAB_UNSELECTED_COLOR);
        selectedPanel = firstPanel;

        try{
            flightControl = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/tutorialPanel/controlPanel.png").getPath()));
            ascend = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/ascend.png").getPath()));
            descend = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/descend.png").getPath()));
            land = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/land.png").getPath()));
            missionAbort = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/mission_abort.png").getPath()));
            missionPause = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/mission_pause.png").getPath()));
            missionResume = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/mission_resume.png").getPath()));
            missionStart = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/mission_start.png").getPath()));
            pitchDown = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/pitch_down.png").getPath()));
            pitchUp = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/pitch_up.png").getPath()));
            rollLeft = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/roll_left.png").getPath()));
            rollRight = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/roll_right.png").getPath()));
            rtl = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/rtl.png").getPath()));
            takeOff = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/takeOff.png").getPath()));
            yawCcw = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/yaw_ccw.png").getPath()));
            yawCw = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/controlPanel/yaw_cw.png").getPath()));

        } catch (IOException e){
            System.out.println("Could not find the image");
        }


        change.setText("Next");
        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedPanel==firstPanel) {
                    selectedPanel = secondPanel;
                }
                else {
                    selectedPanel = firstPanel;
                }
                repaint();
            }
        });
        this.add(selectedPanel);
        this.add(change);

    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if( selectedPanel==firstPanel) {
            g.drawImage(flightControl, 273, 120, null);
            g.drawImage(ascend,250,40,null);
            g.drawImage(descend,200,330,null);
            g.drawImage(yawCcw,190,180,null);
            g.drawImage(yawCw,370,390,null);
            g.drawImage(pitchDown,690,45,null);
            g.drawImage(pitchUp,735,335,null);
            g.drawImage(rollLeft,600-8,392,null);
            g.drawImage(rollRight,762,182,null);
            g.setColor(Color.WHITE);
            g.drawLine(290,79,375,190);
            g.drawLine(240,210,320,240);
            g.drawLine(245,350,375,290);
            g.drawLine(400,390,456,268);
            g.drawLine(612,405,550,268);
            g.drawLine(758,350,630,290);
            g.drawLine(769,210,685,240);
            g.drawLine(712,75,628,188);
            g.setFont(new Font( "Tahoma", Font.BOLD, 16 ));
            g.drawString("Ascend",185,70);
            g.drawString("Yaw Counterclockwise",5,210);
            g.drawString("Descend",125,361);
            g.drawString("Yaw Clockwise",243,420);
            g.drawString("Move Left",640,420);
            g.drawString("Move Backward",779,361);
            g.drawString("Move Right",815,210);
            g.drawString("Move Forward",735,70);

            change.setText("Next");
        }
        if( selectedPanel==secondPanel) {
            g.drawImage(flightControl, 0, 10, null);
            change.setText("Back");
        }
    }
}

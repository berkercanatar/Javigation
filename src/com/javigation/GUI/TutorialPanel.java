package com.javigation.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TutorialPanel extends JPanel {

    public static JTabbedPane pane;
    private static final Color TAB_UNSELECTED_COLOR = new Color(21, 53, 68);
    private static final Color TAB_BAR_COLOR = new Color(46, 91, 114);


    public TutorialPanel(){
        setGUI();

    }

    public void setGUI(){
        this.setBackground(TAB_UNSELECTED_COLOR);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
        Dimension a = new Dimension(1000,1000);
        tabbedPane.setPreferredSize(a);
        JPanel helpTab1, helpTab2, helpTab3, helpTab4;
        ControlPanelTutorial controlPanelTutorial = new ControlPanelTutorial();
        helpTab1 = new JPanel();
        helpTab2 = new JPanel();
        helpTab3 = new JPanel();
        helpTab4 = new JPanel();

        tabbedPane.setBackground(Color.yellow);
        tabbedPane.setForeground(Color.black);

        tabbedPane.addTab("  Connecting to drone", new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/javigation.png")),helpTab1);
        tabbedPane.addTab("Controlling the drone",new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/flightPlan.png")),controlPanelTutorial);

        helpTab2.add( new JLabel("Controlling The Drone",new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/flightPlan.png")),JLabel.LEADING ));







        add(tabbedPane);
        setSize(2000,2000);
        setVisible(true);
    }

}
class ControlPanelTutorial extends JPanel{
    BufferedImage flightControl;

    public ControlPanelTutorial(){
        try{
            flightControl = ImageIO.read( new File(GUIManager.class.getClassLoader().getResource("images/tutorialPanel/controlPanel.png").getPath()));
        } catch (IOException e){ }
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(flightControl,273,410,null);
    }
}

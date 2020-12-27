package com.javigation.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class TabController extends JPanel {
    private JTabbedPane tabControl = new JTabbedPane();

    private static final int TOP_PANEL_HEIGHT = 70;
    private JPanel tabBarPanel = new JPanel(new BorderLayout(0,0));
    private JPanel tabBarTabsPanel = new JPanel(new GridBagLayout());//new FlowLayout(FlowLayout.LEADING, 5, 0));
    public JPanel tabBarStatusPanel = new JPanel(new BorderLayout());



    private static final Color TAB_SELECTED_COLOR = new Color(37, 101, 74);
    private static final Color TAB_UNSELECTED_COLOR = new Color(21, 53, 68);
    private static final Color TAB_TITLE_COLOR = Color.WHITE;
    private static final Color TAB_BAR_COLOR = new Color(46, 91, 114);

    private Map<JPanel, JLabel> panelToHeader = new LinkedHashMap<JPanel, JLabel>() {
    };

    public JPanel tabGuiSettings = new JPanel(new BorderLayout());
    public JPanel tabFlightPlan = new JPanel(new BorderLayout());
    public JPanel tabCameraView = new JPanel(new BorderLayout());
    public JPanel tabDroneSettings = new JPanel(new BorderLayout());
    public JPanel tabTutorial = new JPanel(new BorderLayout());
    public TutorialPanel tutorial = new TutorialPanel();

    public TabController() {
        setLayout(new BorderLayout());

        Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        insets.top = -1;
        UIManager.put("TabbedPane.contentBorderInsets", insets);

        tabControl.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tab_placement, int run_count, int max_tab_height) {
                return 0;
            }
        }); //Disable native tabbar drawing

        panelToHeader.put(tabGuiSettings, new JLabel("Javigation", new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/javigation.png")), JLabel.LEFT));
        panelToHeader.put(tabFlightPlan, new JLabel("Flight Plan", new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/flightplan.png")), JLabel.LEFT));
        panelToHeader.put(tabCameraView, new JLabel("Camera View", new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/camera.png")), JLabel.LEFT));
        panelToHeader.put(tabDroneSettings, new JLabel("Drone Settings", new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/dronesettings.png")), JLabel.LEFT));
        panelToHeader.put(tabTutorial, new JLabel( new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/tutorial.png")),JLabel.LEFT));

        Border border = new RoundedBorder(Color.BLACK,1,16);
        GridBagConstraints gridBagConst = new GridBagConstraints();
        gridBagConst.insets.left = 5;
        gridBagConst.insets.right = 5;
        for( Map.Entry<JPanel, JLabel> entry : panelToHeader.entrySet()) {
            JPanel pnl = entry.getKey();
            JLabel lbl = entry.getValue();
            tabControl.addTab(null, pnl);
            lbl.setForeground(TAB_TITLE_COLOR);
            lbl.setFont( new Font( "Tahoma", Font.BOLD, 16 ) );
            lbl.setBorder(border);
            lbl.setOpaque(true);
            lbl.setPreferredSize(new Dimension(lbl.getPreferredSize().width, TOP_PANEL_HEIGHT - 10));
            lbl.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if( pnl == tabTutorial){
                        lbl.setBackground(TAB_SELECTED_COLOR);
                        //JOptionPane help = new JOptionPane(null);
                        //help.add(tutorial);

                        UIManager.put("OptionPane.border",new LineBorder(TAB_BAR_COLOR));
                        UIManager.put("OptionPane.okButtonText","Done");
                        //JDialog dialog = help.createDialog(null,"Help");
                        //((Frame)dialog.getParent()).setIconImage(new ImageIcon(GUIManager.class.getClassLoader().getResource("images/tabIcons/tutorial.png")).getImage());
                        //dialog.setResizable(true);
                        //dialog.setVisible(true);

                        JOptionPane.showMessageDialog(null,tutorial,"Help",JOptionPane.PLAIN_MESSAGE);
                        lbl.setBackground(TAB_UNSELECTED_COLOR);
                    }
                    else {
                        if (pnl == tabCameraView) {
                            GUIManager.vc.getParent().remove(GUIManager.vc);
                            tabCameraView.add(GUIManager.vc);
                        } else if (pnl == tabFlightPlan) {
                            //GUIManager.vc.getParent().remove(GUIManager.vc);
                            //GUIManager.gstPanel.add(GUIManager.vc, BorderLayout.CENTER);
                            GUIManager.containers.refreshCameraContainer();
                        }

                        tabControl.setSelectedComponent(pnl);
                        resetTabColors();
                        lbl.setBackground(TAB_SELECTED_COLOR);
                    }
                }
            });
            tabBarTabsPanel.add(lbl, gridBagConst);
        }
        tabBarStatusPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        tabBarPanel.add(tabBarTabsPanel, BorderLayout.LINE_START);
        tabBarPanel.add(tabBarStatusPanel, BorderLayout.CENTER);
        tabBarStatusPanel.setBackground(Color.black);

        add(tabBarPanel, BorderLayout.NORTH);
        add(tabControl, BorderLayout.CENTER);

        tabBarPanel.setPreferredSize(new Dimension(tabBarPanel.getPreferredSize().width, TOP_PANEL_HEIGHT));
        tabBarTabsPanel.setPreferredSize(new Dimension(tabBarTabsPanel.getPreferredSize().width, TOP_PANEL_HEIGHT));

        //tabControl.addTab(null, tabGuiSettings);
        //tabControl.addTab(null, tabDroneSettings);
        tabGuiSettings.setBackground(Color.black);
        tabDroneSettings.setBackground(Color.red);


        tabControl.setSelectedComponent(tabFlightPlan);
        resetTabColors();
        panelToHeader.get(tabControl.getSelectedComponent()).setBackground(TAB_SELECTED_COLOR);

        tabBarTabsPanel.setBackground(TAB_BAR_COLOR);

    }

    private void resetTabColors() {
        for (Component c : tabBarTabsPanel.getComponents()) {
            if (c instanceof JLabel) {
                JLabel lbl = (JLabel)c;
                lbl.setBackground(TAB_UNSELECTED_COLOR);
            }
        }
    }

}

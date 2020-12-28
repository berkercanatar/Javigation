package com.javigation.GUI;

import com.javigation.Statics;
import com.javigation.Utils;
import com.javigation.drone_link.DroneConnection;
import org.freedesktop.gstreamer.swing.GstVideoComponent;
import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Containers {

    public JLayeredPane MainContainer;
    public JLayeredPane PreviewContainer;
    public ContentType MainContent;

    public JXMapViewer MapContent;
    public GstVideoComponent CameraContent;

    private JPanel mainContentContainer;
    private JPanel previewContentContainer;
    private JPanel previewOverlay;

    public static JPanel popupContainer;
    public static JPanel connectedDronesContainer;

    public enum ContentType {
        MAP,
        CAMERA
    }

    public Containers (JXMapViewer mapContent, GstVideoComponent cameraContent) {
        MapContent = mapContent;
        CameraContent = cameraContent;
        MainContent = ContentType.MAP;

        MainContainer = new JLayeredPane() {
            @Override
            public void doLayout() {
                synchronized (getTreeLock()) {
                    for ( int i = 0 ; i < getComponentCount() ; i++ ) {
                        Component comp = getComponent(i);
                        switch (comp.getName()) {
                            case "MainContentContainer":
                                comp.setBounds(0, 0, getWidth(), getHeight());
                                break;
                            case "PreviewContainer":
                                comp.setBounds(getWidth() - 550,getHeight() - 350, 500,300);
                                break;
                            case "PopupContainer":
                                comp.setBounds(getWidth() / 2 - 400,20, 800,75);
                                break;
                            case "ConnectedDronesContainer":
                                comp.setBounds(20,getHeight() / 2 - 300, 220,600);
                                break;
                        }
                    }
                }
            }
        };
        mainContentContainer = new JPanel(new BorderLayout());
        mainContentContainer.add(MapContent);
        mainContentContainer.setName("MainContentContainer");
        MainContainer.add(mainContentContainer, JLayeredPane.DEFAULT_LAYER);

        PreviewContainer = new JLayeredPane(){
            @Override
            public void doLayout() {
                synchronized (getTreeLock()) {
                    for ( Component comp : getComponents() )
                        comp.setBounds(0, 0, getWidth(), getHeight());
                }
            }
        };
        //PreviewContainer.setLayout(new OverlayLayout(PreviewContainer));
        previewContentContainer = new JPanel(new BorderLayout());
        previewContentContainer.add(CameraContent);
        PreviewContainer.add(previewContentContainer, JLayeredPane.DEFAULT_LAYER);
        PreviewContainer.setName("PreviewContainer");
        MainContainer.add(PreviewContainer, JLayeredPane.MODAL_LAYER);

        popupContainer = new JPanel();
        popupContainer.setName("PopupContainer");
        MainContainer.add(popupContainer, JLayeredPane.POPUP_LAYER);

        connectedDronesContainer = new JPanel();
        connectedDronesContainer.setLayout(new BoxLayout(connectedDronesContainer, BoxLayout.Y_AXIS));
        connectedDronesContainer.setName("ConnectedDronesContainer");
        connectedDronesContainer.setMaximumSize(new Dimension(220, 600));
        connectedDronesContainer.setBackground(ConnectedDronePanel.MAIN_COLOR);
        final JScrollPane sp = new JScrollPane(connectedDronesContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setName("ConnectedDronesContainer");
        MainContainer.add(sp, JLayeredPane.POPUP_LAYER);

        previewOverlay = new JPanel();
        previewOverlay.setAlignmentX(1);
        previewOverlay.setAlignmentY(1);
        previewOverlay.setOpaque(false);
        previewOverlay.setBackground(GUIManager.COLOR_TRANSPARENT);
        PreviewContainer.add(previewOverlay, JLayeredPane.POPUP_LAYER);




        previewOverlay.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) { }
            public void mousePressed(MouseEvent e) {
                System.out.println("OVERLAY CLICKED");
                Switch();
            }
            public void mouseReleased(MouseEvent e) { }
            public void mouseEntered(MouseEvent e) { }
            public void mouseExited(MouseEvent e) { }
        });
    }

    public void refreshCameraContainer() {
        ( MainContent == ContentType.MAP ? previewContentContainer : mainContentContainer).add(CameraContent);
        MainContainer.revalidate();
        MainContainer.repaint();
    }

    public void Switch() {
        mainContentContainer.add( MainContent == ContentType.MAP ? CameraContent : MapContent );
        previewContentContainer.add( MainContent == ContentType.MAP ? MapContent : CameraContent );
        MainContent = MainContent == ContentType.MAP ? ContentType.CAMERA : ContentType.MAP;
        GUIManager.toggleOverMapTools(MainContent == ContentType.MAP);
        MainContainer.revalidate();
        MainContainer.repaint();
    }

}

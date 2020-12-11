package com.javigation;

import javax.swing.*;

public class MainForm extends JFrame {

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
    }

    public MainForm() {
        super("Javigation");
        GUIManager.setupGUI(this);

        DroneConnection.Get(14540);
    }

}

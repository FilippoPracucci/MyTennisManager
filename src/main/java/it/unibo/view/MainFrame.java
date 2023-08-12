package it.unibo.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.controller.db.QueryManager;

public class MainFrame extends JFrame {
    private static final String FRAME_NAME = "MyTennisManager";

    private final StartMenu menu;

    public MainFrame(final QueryManager qM) {
        super(FRAME_NAME);
        menu = new StartMenu(this, qM);
        this.add(menu);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void changePanel(final JPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    /*public static void main(String[] args) {
        new MainFrame();
    }*/
}
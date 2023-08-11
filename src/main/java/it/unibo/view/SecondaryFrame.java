package it.unibo.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SecondaryFrame extends JFrame {

    private static final String FRAME_NAME = "MyTennisManager";

    public SecondaryFrame() {
        super(FRAME_NAME);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void changePanel(final JPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}

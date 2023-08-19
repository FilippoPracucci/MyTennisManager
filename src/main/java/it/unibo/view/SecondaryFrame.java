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
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.toFront();
    }

    public void changePanel(final JPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void startFrame(final JPanel panel) {
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }

    public void closeFrame() {
        this.dispose();
    }
}

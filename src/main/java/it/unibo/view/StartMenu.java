package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.controller.db.QueryManagerImpl;

public class StartMenu extends JPanel {
    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;
    private static final int FONT_SIZE = 30;
    private static final String TITLE = "MyTennisManager";
    private static final String SIGN_IN = "ACCEDI";
    private static final String SIGN_UP = "ISCRIVITI";
    private static final String STATISTICS = "STATISTICHE";
    private static final String INFO = "INFO";

    private final Dimension dimension;
    private final JPanel northPanel;
    private final JPanel centerPanel;
    private final JPanel southPanel;

    public StartMenu(final MainFrame frame, final QueryManagerImpl queryManager) {
        final JButton signIn = new JButton(SIGN_IN);
        final JButton signUp = new JButton(SIGN_UP);
        final JButton stats = new JButton(STATISTICS);
        final JButton info = new JButton(INFO);
        final JLabel label = new JLabel(TITLE);
        final BorderLayout layout = new BorderLayout(50, 150);

        this.centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.setLayout(layout);

        this.dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension(Double.valueOf(this.dimension.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(this.dimension.getHeight() * HEIGHT_PERC).intValue()));

        this.centerPanel.add(signIn);
        this.centerPanel.add(signUp);
        this.centerPanel.add(stats);
        this.add(centerPanel, BorderLayout.CENTER);

        label.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        this.northPanel = new JPanel();
        this.northPanel.add(label);
        this.add(this.northPanel, BorderLayout.NORTH);

        this.southPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        this.southPanel.add(info);
        this.add(this.southPanel, BorderLayout.SOUTH);

        signIn.addActionListener(e -> new SignInMenu(new SecondaryFrame(), dimension, queryManager));

        signUp.addActionListener(e -> {
            final String[] options = { "Giocatore", "Organizzatore" };
            final int result = JOptionPane.showOptionDialog(this,
                    null,
                    null,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    options,
                    options[1]);
            if (result == 0) {
                new SignUpGiocatore(new SecondaryFrame(), dimension, queryManager);
            } else if (result == 1) {
                new SignUpOrganizzatore(new SecondaryFrame(), dimension, queryManager);
            } else {
                frame.dispose();
            }
        });

        stats.addActionListener(e -> new StatisticheMenu(new SecondaryFrame(), dimension, queryManager));

        info.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Registrare prima l'organizzatore e il corrispettivo circolo.\n" +
                    "Registrare un nuovo giocatore solo se il suo circolo e' gia' stato registrato.");
        });
    }
}

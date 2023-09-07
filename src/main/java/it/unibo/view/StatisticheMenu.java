package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import it.unibo.controller.db.QueryManagerImpl;

public class StatisticheMenu extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String CLUB_WITH_MORE_TOURNAMENT = "3 circoli con piu' tornei organizzati";
    private static final String PLAYER_WITH_MORE_TOURNAMENT = "5 giocatori che hanno disputato piu' tornei";

    private final JTabbedPane pane;
    private final JPanel clubPanel;
    private final JPanel playerPanel;

    public StatisticheMenu(final SecondaryFrame frame,
        final Dimension dim,
        final QueryManagerImpl queryManager) {

        this.pane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue())
        );
        frame.startFrame(this);

        this.clubPanel = new StatisticheCircoliPanel(frame, dim, queryManager);
        this.playerPanel = new StatisticheGiocatoriPanel(frame, dim, queryManager);

        this.pane.add(CLUB_WITH_MORE_TOURNAMENT, this.clubPanel);
        this.pane.add(PLAYER_WITH_MORE_TOURNAMENT, this.playerPanel);

        this.add(this.pane);
    }
}

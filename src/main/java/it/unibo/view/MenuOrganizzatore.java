package it.unibo.view;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.Dimension;
import java.awt.FlowLayout;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class MenuOrganizzatore extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String NEW_TOURNAMENT_EDITION = "Nuova edizione torneo";
    private static final String NEW_TOURNAMENT = "Nuovo torneo";
    private static final String ALL_TOURNAMENTS = "Lista tornei";

    private final JTabbedPane pane;
    private final JPanel newTournamentEdition;
    private final JPanel newTournament;
    private final JPanel allTournaments;

    public MenuOrganizzatore(final SecondaryFrame frame, final Dimension dim, final QueryManager queryManager, final Pair<String, String> credentials) {
        this.pane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        this.newTournamentEdition = new AddEdizioneTorneoPanel(frame, dim, queryManager, credentials);
        this.newTournament = new AddTorneoPanel(frame, dim, queryManager);
        this.allTournaments = new JPanel();

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.pane.add(NEW_TOURNAMENT_EDITION, this.newTournamentEdition);
        this.pane.add(NEW_TOURNAMENT, this.newTournament);
        this.pane.add(ALL_TOURNAMENTS, this.allTournaments);
        this.add(this.pane);
    }
}

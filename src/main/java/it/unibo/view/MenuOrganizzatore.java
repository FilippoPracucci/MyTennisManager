package it.unibo.view;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.util.Optional;

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
    private static final String DELETE_TOURNAMENT = "Elimina torneo";
    private static final String DELETE_TOURNAMENT_EDITION = "Elimina edizione torneo";

    private final JTabbedPane pane;
    private final JPanel newTournamentEdition;
    private final JPanel newTournament;
    private final JPanel allTournaments;
    private final JPanel deleteTournamentPanel;
    private final JPanel deleteEditionPanel;

    public MenuOrganizzatore(final SecondaryFrame frame, final Dimension dim, final QueryManager queryManager, final Pair<String, String> credentials) {
        this.pane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        this.newTournament = new AddTorneoPanel(frame, dim, queryManager, credentials);
        this.newTournamentEdition = new AddEdizioneTorneoPanel(frame, dim, queryManager, credentials, Optional.empty());
        this.allTournaments = new AllTournamentsPanel(frame, dim, queryManager, credentials);
        this.deleteTournamentPanel = new DeleteTournamentPanel(frame, dim, queryManager, credentials);
        this.deleteEditionPanel = new DeleteEditionPanel(frame, dim, queryManager, credentials);
        

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.pane.add(NEW_TOURNAMENT, this.newTournament);
        this.pane.add(NEW_TOURNAMENT_EDITION, this.newTournamentEdition);
        this.pane.add(ALL_TOURNAMENTS, this.allTournaments);
        this.pane.add(DELETE_TOURNAMENT, this.deleteTournamentPanel);
        this.pane.add(DELETE_TOURNAMENT_EDITION, this.deleteEditionPanel);
        this.add(this.pane);
    }
}

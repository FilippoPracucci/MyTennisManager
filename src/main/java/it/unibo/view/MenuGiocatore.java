package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class MenuGiocatore extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String REGISTRATION =  "Iscrizione";
    private static final String TOURNAMENTS = "Lista tornei idonei";
    private static final String TOURNAMENTS_FILTERED = "Lista tornei con filtro";
    private static final String COUPLE_MENU = "Menù coppie";

    private final JTabbedPane pane;
    private final JPanel registrationPanel;
    private final JPanel tournamentsPanel;
    private final JPanel tournamentsFilteredPanel;
    private final JPanel couplePanel;

    public MenuGiocatore(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {

        this.pane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        this.registrationPanel = new RegistrationPanel(frame, dim, queryManager, credentials, true, Optional.empty());
        this.tournamentsPanel = new TournamentsEligiblePanel(frame, dim, queryManager, credentials, Optional.empty());
        this.tournamentsFilteredPanel = new TournamentsFiltered(frame, dim, queryManager, credentials);
        this.couplePanel = new MenuCoppia(frame, dim, queryManager, credentials);

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.pane.add(REGISTRATION, this.registrationPanel);
        this.pane.add(TOURNAMENTS, this.tournamentsPanel);
        this.pane.add(TOURNAMENTS_FILTERED, this.tournamentsFilteredPanel);
        this.pane.add(COUPLE_MENU, this.couplePanel);
        this.add(this.pane);
    }
}

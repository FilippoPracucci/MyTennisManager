package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import it.unibo.controller.db.QueryManagerImpl;
import it.unibo.utils.Pair;

public class MenuGiocatore extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String TOURNAMENTS = "Lista tornei idonei";
    private static final String TOURNAMENTS_FILTERED = "Lista tornei con filtro";
    private static final String ALL_REGISTRATIONS = "Lista iscrizioni";
    private static final String COUPLE_MENU = "Menu' coppie";
    private static final String MY_PROFILE = "Profilo";

    private final JTabbedPane pane;
    private final JPanel tournamentsPanel;
    private final JPanel tournamentsFilteredPanel;
    private final JPanel registrationsPanel;
    private final JPanel couplePanel;
    private final JPanel profilePanel;

    public MenuGiocatore(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManagerImpl queryManager,
            final Pair<String, String> credentials) {

        this.pane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        this.tournamentsPanel = new TorneiEligiblePanel(frame, dim, queryManager, credentials, true, Optional.empty());
        this.tournamentsFilteredPanel = new TorneiFiltered(frame, dim, queryManager, credentials);
        this.registrationsPanel = new AllIscrizioni(frame, dim, queryManager, credentials, Optional.empty());
        this.couplePanel = new MenuCoppia(frame, dim, queryManager, credentials);
        this.profilePanel = new MyProfilePanel(frame, dim, queryManager, credentials, true);

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.pane.add(TOURNAMENTS, this.tournamentsPanel);
        this.pane.add(TOURNAMENTS_FILTERED, this.tournamentsFilteredPanel);
        this.pane.add(ALL_REGISTRATIONS, this.registrationsPanel);
        this.pane.add(COUPLE_MENU, this.couplePanel);
        this.pane.add(MY_PROFILE, this.profilePanel);
        this.add(this.pane);
    }
}

package it.unibo.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class AllTournamentsPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private final List<String> columns;

    public AllTournamentsPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.setLayout(new GridLayout(100, 8));

        this.columns = List.of("Id_Torneo",
                "Numero_Edizione",
                "Tipo",
                "Data_Inizio",
                "Data_Fine",
                "Limite_Categoria",
                "Limite_Eta",
                "Montepremi");
        queryManager.findAllTorneoByCircolo(queryManager.findCircoloByOrganizzatore(
            queryManager.findOrganizzatoreByCredentials(credentials.getX(), credentials.getY())
            .get()
            .getId()
        ).get());

        
    }
}

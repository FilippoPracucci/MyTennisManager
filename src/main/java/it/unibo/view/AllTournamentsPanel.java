package it.unibo.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class AllTournamentsPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private final List<String> columns;

    private final JTable table;

    public AllTournamentsPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {

        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.columns = List.of("Id_Torneo",
                "Numero_Edizione",
                "Tipo",
                "Data_Inizio",
                "Data_Fine",
                "Limite_Categoria",
                "Limite_Eta",
                "Montepremi");

        this.table = new JTable(
            queryManager.listTorneiToMatrix(
                queryManager.findAllEdizioniByCircolo(
                    queryManager.findCircolo(
                        queryManager.findOrganizzatoreByCredentials(credentials.getX(), credentials.getY()).get().getId()
                    ).get()
                ),
            this.columns.size()),
        this.columns.toArray());
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(), Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.add(scrollPane);
    }
}

package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.unibo.controller.db.QueryManagerImpl;
import it.unibo.utils.Pair;

public class AllTorneiPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String PLAYERS_REGISTERED = "Giocatori iscritti";

    private final List<String> columns;

    private final JTable table;
    private final JButton playersButton;
    private final JPanel buttonPanel;
    Pair<Integer, Integer> edition = new Pair<>(0, 0);
    String tipo = "Singolare maschile";

    public AllTorneiPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManagerImpl queryManager,
            final Pair<String, String> credentials) {


        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.playersButton = new JButton(PLAYERS_REGISTERED);
        this.playersButton.setEnabled(false);
        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        this.buttonPanel.add(this.playersButton);
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
                ), this.columns.size()),
            this.columns.toArray());
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.table.setDragEnabled(false);
        this.table.getTableHeader().setReorderingAllowed(false);

        this.table.setRowSelectionAllowed(true);
        ListSelectionModel listSelectionModel = this.table.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                final int row = table.getSelectedRow();
                playersButton.setEnabled(true);
                if (!e.getValueIsAdjusting() && row >= 0) {
                    edition = new Pair<>(
                        (Integer) table.getModel().getValueAt(row, 0),
                        (Integer) table.getModel().getValueAt(row, 1));
                    tipo = Objects.toString(table.getModel().getValueAt(row, 2));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(), Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.playersButton.addActionListener(e -> {
            if (tipo.equalsIgnoreCase("Singolare_maschile") || tipo.equalsIgnoreCase("Singolare_femminile")) {
                new AllIscrittiPanel(new SecondaryFrame(), dim, queryManager, edition, true);
            } else {
                new AllIscrittiPanel(new SecondaryFrame(), dim, queryManager, edition, false);
            }
        });

        this.setLayout(new BorderLayout());
        this.add(this.buttonPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}

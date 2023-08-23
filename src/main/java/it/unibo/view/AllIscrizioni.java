package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class AllIscrizioni extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String DELETE = "Cancella";

    private final List<String> columns;

    private final JTable table;
    private DefaultTableModel model;
    private final JPanel buttonPanel;
    private final JButton delete;
    Pair<Integer, Integer> edition;

    public AllIscrizioni(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials,
            final Optional<Integer> couple) {

        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
            Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        frame.startFrame(this);

        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        this.delete = new JButton(DELETE);
        this.delete.setEnabled(false);
        this.buttonPanel.add(this.delete);

        this.columns = List.of("Id_Torneo",
            "Numero_Edizione",
            "Tipo",
            "Data_Inizio",
            "Data_Fine",
            "Circolo",
            "Limite_Categoria",
            "Limite_Eta",
            "Montepremi",
            "Preferenza orario");

        this.table = new JTable();
        if (!couple.isPresent()) {
            this.model = new DefaultTableModel(queryManager.listIscrizioniToMatrix(
                        queryManager.findAllIscrizioniByGiocatore(
                            queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                        ), this.columns.size()),
                this.columns.toArray());
        } else {
            this.model = new DefaultTableModel(queryManager.listIscrizioniToMatrix(
                    queryManager.findAllIscrizioniByCoppia(
                        queryManager.findCoppia(couple.get()).get()
                    ), this.columns.size()),
                this.columns.toArray());
        }
        this.table.setModel(this.model);
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
                delete.setEnabled(true);
                if (!e.getValueIsAdjusting() && row >= 0) {
                    edition = new Pair<>(
                        (Integer) table.getModel().getValueAt(row, 0),
                        (Integer) table.getModel().getValueAt(row, 1)
                    );
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(
            Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
            Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue())
        );

        this.delete.addActionListener(e -> {
            queryManager.deleteIscrizioneByEdizione(edition,
                couple.isPresent() ?
                    Optional.empty() :
                    Optional.of(queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get().getId()),
                couple);
            if (!couple.isPresent()) {
                this.model = new DefaultTableModel(queryManager.listIscrizioniToMatrix(
                            queryManager.findAllIscrizioniByGiocatore(
                                queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                            ), this.columns.size()),
                    this.columns.toArray());
            } else {
                this.model = new DefaultTableModel(queryManager.listIscrizioniToMatrix(
                        queryManager.findAllIscrizioniByCoppia(
                            queryManager.findCoppia(couple.get()).get()
                        ), this.columns.size()),
                    this.columns.toArray());
            }
            this.table.setModel(this.model);
            this.delete.setEnabled(false);
        });

        this.setLayout(new BorderLayout());
        this.add(this.buttonPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}

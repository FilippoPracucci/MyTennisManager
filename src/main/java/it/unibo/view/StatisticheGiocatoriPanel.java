package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import it.unibo.controller.db.QueryManager;

public class StatisticheGiocatoriPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String START_YEAR = "Anno inizio intervallo:";
    private static final String END_YEAR = "Anno fine intervallo:";
    private static final String SEARCH = "Cerca";

    private final List<String> columns;

    private final JTable table;
    private DefaultTableModel model;
    private final JPanel buttonPanel;
    private final JComboBox<Integer> startYear;
    private final JComboBox<Integer> endYear;
    private final JButton search;
    private final JLabel sYearLabel;
    private final JLabel eYearLabel;

    public StatisticheGiocatoriPanel (final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager) {

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
            Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue())
        );

        this.columns = List.of("Id_Giocatore",
            "Nome",
            "Cognome",
            "Email",
            "Tessera",
            "Classifica",
            "Età",
            "Sesso",
            "Telefono",
            "Id_Circolo"
        );

        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        this.startYear = new JComboBox<>();
        this.createYearList(this.startYear, 2010, LocalDateTime.now().getYear());
        this.endYear = new JComboBox<>();
        this.createYearList(this.endYear, 2010, LocalDateTime.now().getYear());
        this.search = new JButton(SEARCH);
        this.search.setEnabled(false);
        this.table = new JTable();
        this.model = new DefaultTableModel();
        this.sYearLabel = new JLabel(START_YEAR);
        this.eYearLabel = new JLabel(END_YEAR);

        this.startYear.addItemListener(e -> {
            this.endYear.removeAllItems();
            this.createYearList(this.endYear, (Integer) this.startYear.getSelectedItem(), LocalDateTime.now().getYear());
        });

        this.endYear.addItemListener(e -> {
            this.search.setEnabled(true);
        });

        this.search.addActionListener(e -> {
            this.model = new DefaultTableModel(queryManager.listGiocatoriToMatrix(
                    queryManager.findTopGiocatori(
                        (Integer) this.startYear.getSelectedItem(), 
                        (Integer) this.endYear.getSelectedItem()
                    ), this.columns.size()
                ), this.columns.toArray());
            this.table.setModel(this.model);
        });

        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.table.setDragEnabled(false);
        this.table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(), Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.buttonPanel.add(this.sYearLabel);
        this.buttonPanel.add(this.startYear);
        this.buttonPanel.add(this.eYearLabel);
        this.buttonPanel.add(this.endYear);
        this.buttonPanel.add(this.search);

        this.add(this.buttonPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void createYearList(final JComboBox<Integer> box, final int min, final int max) {
        for (int i = min; i <= max; i++) {
            box.addItem(i);
        }
    }
}

package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class TournamentsFiltered extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String CATEGORY_FILTER_LABEL = "Categoria";
    private static final String AGE_FILTER_LABEL = "Età massima";
    private static final String YEAR_FILTER_LABEL = "Anno";
    private static final String RESET = "Reset";

    private final List<String> columns;

    private JTable table;
    private TableModel model;
    private JScrollPane scrollPane;
    private final JPanel filterPanel;
    private final JComboBox<Integer> categoryFilter;
    private final JComboBox<Integer> ageFilter;
    private final JComboBox<Integer> yearFilter;
    private final JLabel categoryLabel;
    private final JLabel ageLabel;
    private final JLabel yearLabel;
    private final JButton reset;

    public TournamentsFiltered(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.columns = List.of("Id_Torneo",
                "Numero_Edizione",
                "Tipo",
                "Data_Inizio",
                "Data_Fine",
                "Limite_Categoria",
                "Limite_Eta",
                "Montepremi",
                "Id_Circolo");

        this.categoryLabel = new JLabel(CATEGORY_FILTER_LABEL);
        this.ageLabel = new JLabel(AGE_FILTER_LABEL);
        this.yearLabel = new JLabel(YEAR_FILTER_LABEL);
        this.reset = new JButton(RESET);

        this.filterPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        this.categoryFilter = new JComboBox<>();
        this.createList(2, 4, this.categoryFilter);
        this.ageFilter = new JComboBox<>();
        this.createAgeList(this.ageFilter);
        this.yearFilter = new JComboBox<>();
        this.createList(2010, LocalDateTime.now().getYear(), this.yearFilter);

        this.model = new DefaultTableModel(queryManager.listTorneiToMatrix(
                queryManager.findAllEligibleByPlayer(
                    queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                ), this.columns.size()),
            this.columns.toArray());

        this.table = new JTable(this.model);
        this.scrollPane = new JScrollPane(this.table);
        this.scrollPane.setPreferredSize(new Dimension(
            Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
            Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.categoryFilter.addItemListener(e -> {
            this.updateTable(this.categoryFilter,
                this.ageFilter,
                this.yearFilter,
                queryManager,
                credentials);
        });

        this.ageFilter.addItemListener(e -> {
            this.updateTable(this.categoryFilter,
                this.ageFilter,
                this.yearFilter,
                queryManager,
                credentials);
        });

        this.yearFilter.addItemListener(e -> {
            this.updateTable(this.categoryFilter,
                this.ageFilter,
                this.yearFilter,
                queryManager,
                credentials);
        });

        this.reset.addActionListener(e -> {
            this.categoryFilter.setSelectedIndex(0);
            this.ageFilter.setSelectedIndex(0);
            this.yearFilter.setSelectedIndex(0);
            this.model = new DefaultTableModel(queryManager.listTorneiToMatrix(
                queryManager.findAllEligibleByPlayer(
                    queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                ), this.columns.size()),
            this.columns.toArray());
            this.table.setModel(this.model);
        });

        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        this.filterPanel.add(this.categoryLabel);
        this.filterPanel.add(this.categoryFilter);
        this.filterPanel.add(this.ageLabel);
        this.filterPanel.add(this.ageFilter);
        this.filterPanel.add(this.yearLabel);
        this.filterPanel.add(this.yearFilter);
        this.filterPanel.add(this.reset);

        this.add(this.filterPanel, BorderLayout.NORTH);
        this.add(this.scrollPane, BorderLayout.CENTER);
    }

    private void createList(final int min, final int max, final JComboBox<Integer> box) {
        box.addItem(Types.NULL);
        for (int i = min; i <= max; i++) {
            box.addItem(i);
        }
    }

    private void createAgeList(final JComboBox<Integer> box) {
        box.addItem(Types.NULL);
        for (int i = 10; i <= 18; i+=2) {
            box.addItem(i);
        }
    }

    private void updateTable(final JComboBox<Integer> catBox,
            final JComboBox<Integer> etaBox,
            final JComboBox<Integer> annoBox,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {
        final Optional<Integer> cat;
            final Optional<Integer> eta;
            final Optional<String> anno;
            if (this.categoryFilter.getSelectedItem().equals(this.categoryFilter.getItemAt(0))) {
                cat = Optional.empty();
            } else {
                cat = Optional.of((Integer) this.categoryFilter.getSelectedItem());
            }
            if (this.ageFilter.getSelectedItem().equals(this.ageFilter.getItemAt(0))) {
                eta = Optional.empty();
            } else {
                eta = Optional.of((Integer) this.ageFilter.getSelectedItem());
            }
            if (this.yearFilter.getSelectedItem().equals(this.yearFilter.getItemAt(0))) {
                anno = Optional.empty();
            } else {
                anno = Optional.of(Objects.toString(this.yearFilter.getSelectedItem()));
            }
            this.model = new DefaultTableModel(queryManager.listTorneiToMatrix(
                queryManager.findAllFiltered(
                    queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get(),
                    cat,
                    eta,
                    anno),
                this.columns.size()),
            this.columns.toArray());
            this.table.setModel(this.model);
    }
}

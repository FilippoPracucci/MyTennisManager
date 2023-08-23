package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class AllRegisteredPanel extends JPanel{

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String TIME_PREFERENCE = "Preferenza_Orario";
    private static final String RESET = "Reset";

    private final List<String> columnsSingles;
    private final List<String> columnsDoubles;

    private final JTable table;
    private DefaultTableModel model;
    private final JPanel buttonPanel;
    private final JLabel timePreferenceLabel;
    private final JComboBox<String> timePreference;
    private final JButton reset;

    public AllRegisteredPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<Integer, Integer> edition,
            final boolean isPlayer) {

        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
            Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        frame.startFrame(this);

        this.columnsSingles = List.of("Id_Utente",
            "Nome",
            "Cognome",
            "Email",
            "Tessera",
            "Classifica",
            "Età",
            "Telefono",
            "Preferenza orario");
        this.columnsDoubles = List.of("Id_Coppia",
            "Giocatore 1",
            "Giocatore 2",
            "Tessera 1",
            "Tessera 2",
            "Classifica 1",
            "Classifica 2",
            "Telefono 1",
            "Telefono 2",
            "Preferenza Orario");

        this.table = new JTable();
        if (isPlayer) {
            this.model = new DefaultTableModel(queryManager.listGiocatoriIscrittiToMatrix(
                    queryManager.findAllGiocatoriIscrittiByEdizioneTorneo(
                        queryManager.findEdizioneByPrimaryKey(edition)
                    ), this.columnsSingles.size()),
                this.columnsSingles.toArray());
        } else {
            this.model = new DefaultTableModel(queryManager.listCoppieIscritteToMatrix(
                    queryManager.findAllCoppieIscritteByEdizioneTorneo(
                        queryManager.findEdizioneByPrimaryKey(edition)
                    ), this.columnsDoubles.size()),
                this.columnsDoubles.toArray());
        }
        this.table.setModel(this.model);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.table.setDragEnabled(false);
        this.table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(), Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        this.timePreferenceLabel = new JLabel(TIME_PREFERENCE);
        this.timePreference = new JComboBox<>();
        this.reset = new JButton(RESET);
        this.createTimeList(timePreference);
        this.buttonPanel.add(this.timePreferenceLabel);
        this.buttonPanel.add(this.timePreference);
        this.buttonPanel.add(this.reset);

        this.timePreference.addItemListener(e -> {
            if (isPlayer) {
                this.model = new DefaultTableModel(queryManager.listGiocatoriIscrittiToMatrix(
                    queryManager.findAllIscrittiByPreferenzaOrario(Objects.toString(this.timePreference.getSelectedItem())),
                    this.columnsSingles.size()
                ), this.columnsSingles.toArray());
            } else {
                this.model = new DefaultTableModel(queryManager.listCoppieIscritteToMatrix(
                    queryManager.findAllCoppieIscritteByPreferenzaOrario(Objects.toString(this.timePreference.getSelectedItem())),
                    this.columnsDoubles.size()
                ), this.columnsDoubles.toArray());
            }
            this.table.setModel(this.model);
        });

        this.reset.addActionListener(e -> this.timePreference.setSelectedIndex(0));

        this.setLayout(new BorderLayout());
        this.add(this.buttonPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void createTimeList(final JComboBox<String> box) {
        box.addItem("Nessuna");
        box.addItem("9:00-13:00");
        box.addItem("15:00-21:00");
    }
}

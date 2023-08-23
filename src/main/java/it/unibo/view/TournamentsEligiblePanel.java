package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class TournamentsEligiblePanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String TIME_PREFERENCE_LABEL = "Preferenza di orario *";
    private static final String SIGNUP = "Iscriviti";

    private final List<String> columns;

    private final JTable table;
    private final JPanel buttonPanel;
    private final JLabel timePreferenceLabel;
    private final JComboBox<String> timePreference;
    private final JButton signUp;
    Pair<Integer, Integer> edition = new Pair<>(0, 0);

    public TournamentsEligiblePanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials,
            final boolean isPlayer,
            final Optional<Integer> couple) {

        final Integer id;

        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.timePreferenceLabel = new JLabel(TIME_PREFERENCE_LABEL);
        this.signUp = new JButton(SIGNUP);
        this.signUp.setEnabled(false);
        this.timePreference = new JComboBox<>();
        this.timePreference.setEnabled(false);
        this.createTimeList(this.timePreference);
        if (isPlayer) {
            id = queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get().getId();
        } else {
            id = couple.get();
        }

        this.columns = List.of("Id_Torneo",
                "Numero_Edizione",
                "Tipo",
                "Data_Inizio",
                "Data_Fine",
                "Limite_Categoria",
                "Limite_Eta",
                "Montepremi",
                "Id_Circolo");

        if (!isPlayer) {
            frame.startFrame(this);
            this.table = new JTable(
                queryManager.listTorneiToMatrix(
                    queryManager.findAllDoppiEligibleByCoppia(
                        queryManager.findGiocatoriOfCoppia(queryManager.findCoppia(couple.get()).get()),
                        couple.get()
                    ), this.columns.size()),
                this.columns.toArray());
        } else {
            this.table = new JTable(
                queryManager.listTorneiToMatrix(
                    queryManager.findAllSingolariEligibleByPlayer(
                        queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                    ), this.columns.size()),
                this.columns.toArray());
        }

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
                timePreference.setEnabled(true);
                signUp.setEnabled(true);
                if (!e.getValueIsAdjusting() && row >= 0) {
                    edition = new Pair<>(
                        (Integer) table.getModel().getValueAt(row, 0),
                        (Integer) table.getModel().getValueAt(row, 1));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(), Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.signUp.addActionListener(e -> {
            Optional<String> tP;
            if (((String) this.timePreference.getModel().getSelectedItem()).equalsIgnoreCase("Nessuna")) {
                tP = Optional.empty();
            } else {
                tP = Optional.of((String) this.timePreference.getModel().getSelectedItem());
            }
            if (isPlayer) {
                queryManager.addIscrizione(
                    queryManager.createIscrizione(null,
                            tP,
                            edition.getX(),
                            edition.getY(),
                            Optional.of(id),
                            Optional.empty())
                );
            } else {
                queryManager.addIscrizione(
                    queryManager.createIscrizione(null,
                            tP,
                            edition.getX(),
                            edition.getY(),
                            Optional.empty(),
                            Optional.of(id))
                );
            }
            JOptionPane.showMessageDialog(this, "Iscrizione effettuata con successo!");
            frame.changePanel(new MenuGiocatore(frame, dim, queryManager, credentials));
        });

        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        this.buttonPanel.add(this.timePreferenceLabel);
        this.buttonPanel.add(this.timePreference);
        this.buttonPanel.add(this.signUp);
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

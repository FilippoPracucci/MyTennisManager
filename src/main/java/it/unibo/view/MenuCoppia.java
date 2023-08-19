package it.unibo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class MenuCoppia extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String NEW_COUPLE = "Nuova coppia";
    private static final String REGISTRATION = "Iscriviti a torneo di doppio";
    private static final String UNION = "Unisciti a coppia esistente";
    private static final String CHANGE_VIEW = "Cambia visione";
    private static final String MY_COUPLES = "LE TUE COPPIE";
    private static final String ELIGIBLE_COUPLES = "COPPIE ESISTENTI IDONEE";
    private static final String ID_COUPLE_REGISTRATION = "Id_Coppia iscrizione:";
    private static final String ID_COUPLE_UNION = "Id_Coppia unione:";

    private final List<String> columns;
    private boolean isMineUnions;

    private final JPanel buttonPanel;
    private JTable table;
    private TableModel model;
    private JScrollPane scrollPane;
    private final JButton newCoupleButton;
    private final JButton registrationButton;
    private final JButton unionButton;
    private final JButton changeView;
    private final JComboBox<Integer> myCoupleBox;
    private final JComboBox<Integer> coupleBox;
    private final JLabel info;
    private final JLabel idCoppiaIscrizione;
    private final JLabel idCoppiaUnione;

    public MenuCoppia(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.columns = List.of("Id_Coppia", "Id_Compagno", "Nome_Compagno", "Cognome_Compagno", "Classifica_Compagno", "Sesso_Compagno");

        this.newCoupleButton = new JButton(NEW_COUPLE);
        this.registrationButton = new JButton(REGISTRATION);
        this.unionButton = new JButton(UNION);
        this.changeView = new JButton(CHANGE_VIEW);
        this.myCoupleBox = new JComboBox<>();
        this.info = new JLabel(MY_COUPLES);
        this.idCoppiaIscrizione = new JLabel(ID_COUPLE_REGISTRATION);
        this.idCoppiaUnione = new JLabel(ID_COUPLE_UNION);
        this.createList(
            queryManager.findAllPlayerCouples(
                queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
            ), this.myCoupleBox);
        this.coupleBox = new JComboBox<>();
        this.createList(
            queryManager.findAllEligibleUnioni(
                queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
            ), this.coupleBox);

        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        this.model = new DefaultTableModel(queryManager.listUnioniToMatrix(
                queryManager.findAllUnioniByGiocatore(
                    queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                ), this.columns.size()),
            this.columns.toArray());
        this.isMineUnions = true;

        this.table = new JTable();
        this.table.setModel(this.model);
        this.scrollPane = new JScrollPane(this.table);
        this.scrollPane.setPreferredSize(new Dimension(
            Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
            Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));

        this.newCoupleButton.addActionListener(e -> {
            final Integer id = queryManager.addCoppia(queryManager.createCoppia(null));
            queryManager.addUnione(queryManager.createUnione(
                id,
                queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get().getId())
            );
            JOptionPane.showMessageDialog(this, "Creazione coppia effettuata con successo!");
            this.model = new DefaultTableModel(queryManager.listUnioniToMatrix(
                queryManager.findAllUnioniByGiocatore(
                    queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                ), this.columns.size()),
            this.columns.toArray());
            this.table.setModel(this.model);
            this.isMineUnions = true;
            this.info.setText(MY_COUPLES);
        });

        this.registrationButton.addActionListener(e -> {
            frame.changePanel(new RegistrationPanel(frame,
                dim,
                queryManager,
                credentials,
                false,
                Optional.of((Integer) this.myCoupleBox.getSelectedItem())));
            new TournamentsEligiblePanel(new SecondaryFrame(),
                dim,
                queryManager,
                credentials,
                Optional.of((Integer) this.myCoupleBox.getSelectedItem()));
        });

        this.unionButton.addActionListener(e -> {
            queryManager.addUnione(queryManager.createUnione(
                (Integer) this.coupleBox.getSelectedItem(),
                queryManager.getIdGiocatore(
                    queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                )
            ));
            JOptionPane.showMessageDialog(this, "Unione effettuata con successo!");
            this.model = new DefaultTableModel(queryManager.listUnioniToMatrix(
                queryManager.findAllUnioniByGiocatore(
                    queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                ), this.columns.size()),
            this.columns.toArray());
            this.table.setModel(this.model);
            this.isMineUnions = true;
            this.info.setText(MY_COUPLES);
            this.myCoupleBox.removeAllItems();
            this.createList(
                queryManager.findAllPlayerCouples(
                    queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                ), this.myCoupleBox);
        });

        this.changeView.addActionListener(e -> {
            if (this.isMineUnions) {
                this.model = new DefaultTableModel(queryManager.listUnioniToMatrix(
                    queryManager.findAllEligibleCompagniUnioneForGiocatore(
                        queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                    ), this.columns.size()),
                this.columns.toArray());
                this.table.setModel(this.model);
                this.isMineUnions = false;
                this.info.setText(ELIGIBLE_COUPLES);
            } else {
                this.model = new DefaultTableModel(queryManager.listUnioniToMatrix(
                    queryManager.findAllUnioniByGiocatore(
                        queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
                    ), this.columns.size()),
                this.columns.toArray());
                this.table.setModel(this.model);
                this.isMineUnions = true;
                this.info.setText(MY_COUPLES);
            }
        });

        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.buttonPanel.add(this.newCoupleButton);
        this.buttonPanel.add(this.idCoppiaIscrizione);
        this.buttonPanel.add(this.myCoupleBox);
        this.buttonPanel.add(this.registrationButton);
        this.buttonPanel.add(this.idCoppiaUnione);
        this.buttonPanel.add(this.coupleBox);
        this.buttonPanel.add(this.unionButton);
        this.buttonPanel.add(this.changeView);
        this.buttonPanel.add(this.info);
        this.add(this.buttonPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void createList(final List<Integer> list, final JComboBox<Integer> box) {
        box.addItem(Types.NULL);
        list.forEach(i -> box.addItem(i));
    }
}

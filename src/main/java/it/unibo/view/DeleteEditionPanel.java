package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class DeleteEditionPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String ID_TOURNAMENT_LABEL = "Id torneo";
    private static final String N_EDITION_LABEL = "Numero edizione";
    private static final String START_DATE_LABEL = "Data di inizio";
    private static final String END_DATE_LABEL = "Data di fine";
    private static final String DELETE = "Elimina";
    private static final String CANCEL = "Annulla";

    private final JLabel idTournamentLabel;
    private final JLabel nEditionLabel;
    private final JLabel startDateLabel;
    private final JLabel endDateLabel;
    private final JComboBox<Integer> idTournamentBox;
    private final JComboBox<Integer> nEditionBox;
    private final JLabel startDate;
    private final JLabel endDate;
    private final JButton delete;
    private final JButton cancel;

    private Optional<Integer> id;
    private Optional<Integer> nE;
    private Pair<Integer, Integer> key;
    private DateFormat dFormat;

    public DeleteEditionPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {

        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.idTournamentLabel = new JLabel(ID_TOURNAMENT_LABEL);
        this.nEditionLabel = new JLabel(N_EDITION_LABEL);
        this.startDateLabel = new JLabel(START_DATE_LABEL);
        this.endDateLabel = new JLabel(END_DATE_LABEL);

        this.idTournamentBox = new JComboBox<>();
        this.createList(queryManager.findAllTorneoByCircolo(
            queryManager.findCircoloByOrganizzatore(
                queryManager.findOrganizzatoreByCredentials(credentials.getX(), credentials.getY()).get().getId()
            ).get()
        ).stream().map(c -> c.getId()).toList(), idTournamentBox);

        if (this.idTournamentBox.getItemCount() == 0) {
            this.id = Optional.empty();
        } else {
            this.id = Optional.of((Integer) this.idTournamentBox.getModel().getSelectedItem());
        }

        this.nEditionBox = new JComboBox<>();
        if (this.id.isPresent()) {
            this.createList(queryManager.findAllEdizioneByTorneo(
                queryManager.findTorneo(this.id.get()).get()
            ).stream().map(et -> et.getNumeroEdizione()).toList(), nEditionBox);
            this.nE = Optional.of((Integer) this.nEditionBox.getModel().getSelectedItem());

            this.key = new Pair<>(this.id.get(), this.nE.get());
        }

        this.startDate = new JLabel();
        this.endDate = new JLabel();
        this.dFormat = new SimpleDateFormat("dd-MM-YYYY");
        if (this.id.isPresent() && this.nE.isPresent()) {
            this.startDate.setText(this.dFormat.format(queryManager.findEdizioneByPrimaryKey(this.key).getDataInizio()));
            this.endDate.setText(this.dFormat.format(queryManager.findEdizioneByPrimaryKey(this.key).getDataFine()));
        }

        this.idTournamentBox.addItemListener(e -> {
            this.id = Optional.of((Integer) this.idTournamentBox.getModel().getSelectedItem());
            this.nEditionBox.removeAllItems();
            this.createList(queryManager.findAllEdizioneByTorneo(
                queryManager.findTorneo(this.id.get()).get()
            ).stream().map(et -> et.getNumeroEdizione()).toList(), nEditionBox);
            this.nE = Optional.of((Integer) this.nEditionBox.getModel().getSelectedItem());
            this.key = new Pair<>(this.id.get(), this.nE.get());
            this.startDate.setText(this.dFormat.format(queryManager.findEdizioneByPrimaryKey(this.key).getDataInizio()));
            this.endDate.setText(this.dFormat.format(queryManager.findEdizioneByPrimaryKey(this.key).getDataFine()));
        });

        this.nEditionBox.addItemListener(e -> {
            this.nE = Optional.ofNullable((Integer) this.nEditionBox.getModel().getSelectedItem());
            if (this.nE.isPresent()) {
                this.key = new Pair<>(this.id.get(), this.nE.get());
                this.startDate.setText(this.dFormat.format(queryManager.findEdizioneByPrimaryKey(this.key).getDataInizio()));
                this.endDate.setText(this.dFormat.format(queryManager.findEdizioneByPrimaryKey(this.key).getDataFine()));
            }
        });

        this.delete = new JButton(DELETE);
        this.cancel = new JButton(CANCEL);

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        //startFrame(frame);
        cnst.gridy = 0;
        cnst.insets = insets;
        cnst.weighty = GridBagConstraints.CENTER;
        cnst.gridx = 0;
        this.addField(this.idTournamentLabel, this.idTournamentBox, cnst);
        this.addField(this.nEditionLabel, this.nEditionBox, cnst);
        this.addField(this.startDateLabel, this.startDate, cnst);
        this.addField(this.endDateLabel, this.endDate, cnst);
        southPanel.add(this.delete, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.delete.addActionListener(e -> {
            queryManager.deleteEdizioneTorneo(this.key);
            JOptionPane.showMessageDialog(this, "Edizione torneo eliminata con successo!");
            frame.changePanel(new MenuOrganizzatore(frame, dim, queryManager, credentials));
        });

        this.cancel.addActionListener(e -> {
            final String[] options = { "Sì", "No" };
            final int result = JOptionPane.showOptionDialog(this,
                    "Sei sicuro di voler annullare?",
                    "Uscita",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (result == 0) {
                frame.closeFrame();
            }
        });
    }

    

    private void addField(final JLabel label, final JComponent field, final GridBagConstraints cnst) {
        cnst.gridx = 0;
        this.add(label, cnst);
        cnst.gridx += 2;
        this.add(field, cnst);
        cnst.gridy += 2;
    }

    private void createList(final List<Integer> ids, final JComboBox<Integer> box) {
        ids.forEach(i -> box.addItem(i));
    }
}

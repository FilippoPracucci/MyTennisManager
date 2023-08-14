package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;
import it.unibo.utils.Utils;

public class AddEdizioneTorneoPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String ID_TORNEO_LABEL = "Id torneo";
    private static final String N_EDITION_LABEL = "Numero edizione";
    private static final String START_DATE_LABEL = "Data inizio";
    private static final String END_DATE_LABEL = "Data fine";
    private static final String CLUB_LABEL = "Circolo";
    private static final String SIGNUP = "Aggiungi";
    private static final String CANCEL = "Annulla";

    private final JLabel idTorneoLabel;
    private final JLabel nEditionLabel;
    private final JLabel startDateLabel;
    private final JLabel endDateLabel;
    private final JLabel clubLabel;
    private final JComboBox<Integer> idTorneo;
    private final JLabel nEdition;
    private final JComboBox<Integer> startYear;
    private final JComboBox<Integer> startMonth;
    private final JComboBox<Integer> startDay;
    private final JComboBox<Integer> endYear;
    private final JComboBox<Integer> endMonth;
    private final JComboBox<Integer> endDay;
    private final JLabel clubName;
    private final JButton signUp;
    private final JButton cancel;

    private int sYear;
    private int eYear;
    private int sMonth;
    private int eMonth;
    private int numEdizione;
    private final Integer IdClub;

    public AddEdizioneTorneoPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials,
            final Optional<Integer> idT) {
        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.idTorneoLabel = new JLabel(ID_TORNEO_LABEL);
        this.nEditionLabel = new JLabel(N_EDITION_LABEL);
        this.startDateLabel = new JLabel(START_DATE_LABEL);
        this.endDateLabel = new JLabel(END_DATE_LABEL);
        this.clubLabel = new JLabel(CLUB_LABEL);

        this.idTorneo = new JComboBox<>();
        this.nEdition = new JLabel();
        this.clubName = new JLabel();
        this.startYear = new JComboBox<>();
        this.endYear = new JComboBox<>();
        this.createList(2010, LocalDateTime.now().getYear(), this.startYear);
        this.createList(2010, LocalDateTime.now().getYear(), this.endYear);
        sYear = ((Integer) this.startYear.getModel().getSelectedItem()).intValue();
        eYear = ((Integer) this.endYear.getModel().getSelectedItem()).intValue();

        this.startMonth = new JComboBox<>();
        this.endMonth = new JComboBox<>();
        this.createList(1, 12, this.startMonth);
        this.createList(1, 12, this.endMonth);
        sMonth = ((Integer) this.startMonth.getModel().getSelectedItem()).intValue();
        eMonth = ((Integer) this.endMonth.getModel().getSelectedItem()).intValue();

        this.startDay = new JComboBox<>();
        this.endDay = new JComboBox<>();
        this.startYear.addItemListener(e -> {
            sYear = ((Integer) this.startYear.getModel().getSelectedItem()).intValue();
            this.endYear.setSelectedItem(sYear);
            this.startDay.removeAllItems();
            this.createDaysList(1, LocalDate.of(sYear, sMonth, 1).lengthOfMonth(), this.startDay);
        });
        this.endYear.addItemListener(e -> {
            eYear = ((Integer) this.endYear.getModel().getSelectedItem()).intValue();
            this.endDay.removeAllItems();
            this.createDaysList(1, LocalDate.of(eYear, eMonth, 1).lengthOfMonth(), this.endDay);
        });
        this.startMonth.addItemListener(e -> {
            sMonth = ((Integer) this.startMonth.getModel().getSelectedItem()).intValue();
            this.endMonth.setSelectedItem(sMonth);
            this.startDay.removeAllItems();
            this.createDaysList(1, LocalDate.of(sYear, sMonth, 1).lengthOfMonth(), this.startDay);
        });
        this.endMonth.addItemListener(e -> {
            eMonth = ((Integer) this.endMonth.getModel().getSelectedItem()).intValue();
            this.endDay.removeAllItems();
            this.createDaysList(1, LocalDate.of(eYear, eMonth, 1).lengthOfMonth(), this.endDay);
        });

        this.IdClub = queryManager
                .findCircoloByOrganizzatore(queryManager.findOrganizzatoreByCredentials(credentials.getX(), credentials.getY()).get().getId())
                .get().getId();

        this.clubName.setText(this.IdClub + " - " +
            queryManager.findCircoloByOrganizzatore(
                queryManager.findOrganizzatoreByCredentials(credentials.getX(), credentials.getY()).get().getId()
            ).get().getNome()
        );

        if (idT.isPresent()) {
            this.idTorneo.addItem(idT.get());
            this.numEdizione = 1;
            this.nEdition.setText(String.valueOf(this.numEdizione));
        } else {
            this.createTournamentsList(this.idTorneo, queryManager, this.IdClub);
            this.idTorneo.setSelectedItem(null);
        }

        this.idTorneo.addItemListener(e -> {
            this.numEdizione = queryManager.getNumeroEdizione(queryManager.findTorneo((Integer) this.idTorneo.getModel().getSelectedItem()).get()) + 1;
            this.nEdition.setText(String.valueOf(this.numEdizione));
        });

        this.signUp = new JButton(SIGNUP);
        this.cancel = new JButton(CANCEL);

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        frame.startFrame(this);
        cnst.gridy = 0;
        cnst.insets = insets;
        cnst.weighty = GridBagConstraints.CENTER;
        cnst.gridx = 0;
        this.addField(this.idTorneoLabel, this.idTorneo, cnst);
        this.addField(this.nEditionLabel, this.nEdition, cnst);
        cnst.gridx = 0;
        this.add(this.startDateLabel, cnst);
        cnst.gridx++;
        this.add(this.startYear, cnst);
        cnst.gridx++;
        this.add(this.startMonth, cnst);
        cnst.gridx++;
        this.add(this.startDay, cnst);
        cnst.gridy += 2;
        cnst.gridx = 0;
        this.add(this.endDateLabel, cnst);
        cnst.gridx++;
        this.add(this.endYear, cnst);
        cnst.gridx++;
        this.add(this.endMonth, cnst);
        cnst.gridx++;
        this.add(this.endDay, cnst);
        cnst.gridy += 2;
        addField(this.clubLabel, this.clubName, cnst);
        cnst.gridy += 2;

        southPanel.add(this.signUp, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.signUp.addActionListener(e -> {
            queryManager.addEdizioneTorneo(queryManager.createEdizioneTorneo(
                (Integer) this.idTorneo.getModel().getSelectedItem(),
                Integer.valueOf(this.numEdizione),
                Utils.buildDate(
                    ((Integer) startDay.getModel().getSelectedItem()).intValue(), 
                    ((Integer) startMonth.getModel().getSelectedItem()).intValue(),
                    ((Integer) startYear.getModel().getSelectedItem()).intValue()).get(),
                Utils.buildDate(
                    ((Integer) endDay.getModel().getSelectedItem()).intValue(), 
                    ((Integer) endMonth.getModel().getSelectedItem()).intValue(),
                    ((Integer) endYear.getModel().getSelectedItem()).intValue()).get(),
                this.IdClub)
            );
            JOptionPane.showMessageDialog(this, "Edizione inserita con successo!");
            frame.changePanel(new MenuOrganizzatore(frame, dim, queryManager, credentials));
        });

        this.cancel.addActionListener(e -> {
            final String[] options = { "Sì", "No" };
            final int result = JOptionPane.showOptionDialog(this,
                    "Sei sicuro di voler uscire?",
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

    /*private void startFrame(final SecondaryFrame frame) {
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void closeFrame(final SecondaryFrame frame) {
        frame.dispose();
    }*/

    private void addField(final JLabel label, final JComponent field, final GridBagConstraints cnst) {
        cnst.gridx = 0;
        this.add(label, cnst);
        cnst.gridx += 2;
        this.add(field, cnst);
        cnst.gridy += 2;
    }

    private void createList(final int min, final int max, final JComboBox<Integer> box) {
        for (int i = min; i <= max; i++) {
            box.addItem(i);
        }
    }

    private void createDaysList(final int min, final int max, final JComboBox<Integer> box) {
        for (int i = min; i <= max; i++) {
            box.addItem(i);
        }
    }

    private final void createTournamentsList(final JComboBox<Integer> box, final QueryManager qM, final Integer idCircolo) {
        qM.findAllTorneoByCircolo(qM.findCircolo(idCircolo).get()).forEach(t -> box.addItem(t.getId()));
    }
}

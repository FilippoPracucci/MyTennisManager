package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class RegistrationPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String ID_TOURNAMENT_LABEL = "Id torneo";
    private static final String N_EDITION_LABEL = "Numero edizione";
    private static final String TIME_PREFERENCE_LABEL = "Preferenza di orario *";
    private static final String ID_USER_LABEL = "Id utente ";
    private static final String ID_COUPLE_LABEL = "Id coppia";
    private static final String INFO = "* opzionale";
    private static final String SIGNUP = "Iscriviti";
    private static final String CANCEL = "Annulla";

    private final JLabel idTournamentLabel;
    private final JLabel nEditionLabel;
    private final JLabel timePreferenceLabel;
    private final Optional<JLabel> idUserLabel;
    private final Optional<JLabel> idCoupleLabel;

    private final JComboBox<Integer> idTournament;
    private final JComboBox<Integer> nEdition;
    private final JComboBox<String> timePreference;
    private final Optional<JLabel> idUser;
    private final Optional<JLabel> idCouple;
    private final JLabel info;
    private final JButton signUp;
    private final JButton cancel;

    public RegistrationPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials,
            final boolean isPlayer,
            final Optional<Integer> couple) {

        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);
        final Integer id;

        this.idTournamentLabel = new JLabel(ID_TOURNAMENT_LABEL);
        this.nEditionLabel = new JLabel(N_EDITION_LABEL);
        this.timePreferenceLabel = new JLabel(TIME_PREFERENCE_LABEL);
        this.idUserLabel = Optional.of(new JLabel(ID_USER_LABEL));
        this.idCoupleLabel = Optional.of(new JLabel(ID_COUPLE_LABEL));
        this.info = new JLabel(INFO);
        this.signUp = new JButton(SIGNUP);
        this.cancel = new JButton(CANCEL);

        this.idTournament = new JComboBox<>();
        this.createTournamentsList(this.idTournament, queryManager, credentials, isPlayer, couple);
        this.nEdition = new JComboBox<>();
        this.createTournamentEditionsList(this.nEdition, queryManager, (Integer) this.idTournament.getModel().getSelectedItem());
        this.timePreference = new JComboBox<>();
        this.createTimeList(this.timePreference);
        if (isPlayer) {
            id = queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get().getId();
            this.idUser = Optional.of(new JLabel(id.toString()));
            this.idCouple = Optional.empty();
        } else {
            id = couple.get();
            this.idCouple = Optional.of(new JLabel(id.toString()));
            this.idUser = Optional.empty();
        }

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        cnst.gridy = 0;
        cnst.insets = insets;
        cnst.weighty = GridBagConstraints.CENTER;
        cnst.gridx = 0;
        this.addField(this.idTournamentLabel, this.idTournament, cnst);
        this.addField(this.nEditionLabel, this.nEdition, cnst);
        this.addField(this.timePreferenceLabel, this.timePreference, cnst);
        if (this.idUser.isPresent()) {
            this.addField(this.idUserLabel.get(), this.idUser.get(), cnst);
        } else {
            this.addField(this.idCoupleLabel.get(), this.idCouple.get(), cnst);
        }
        cnst.gridx = 0;
        this.add(this.info, cnst);
        cnst.gridy += 2;
        cnst.gridx += 2;
        southPanel.add(this.signUp, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.idTournament.addItemListener(e -> {
            this.createTournamentEditionsList(this.nEdition, queryManager, (Integer) this.idTournament.getModel().getSelectedItem());
        });

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
                            (Integer) this.idTournament.getModel().getSelectedItem(),
                            (Integer) this.nEdition.getModel().getSelectedItem(),
                            Optional.of(id),
                            Optional.empty())
                );
            } else {
                queryManager.addIscrizione(
                    queryManager.createIscrizione(null,
                            tP,
                            (Integer) this.idTournament.getModel().getSelectedItem(),
                            (Integer) this.nEdition.getModel().getSelectedItem(),
                            Optional.empty(),
                            Optional.of(id))
                );
            }
            JOptionPane.showMessageDialog(this, "Iscrizione effettuata con successo!");
            frame.changePanel(new MenuGiocatore(frame, dim, queryManager, credentials));
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
                if (couple.isPresent()) {
                    frame.changePanel(new MenuCoppia(frame, dim, queryManager, credentials));
                } else {
                    frame.closeFrame();
                }
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

    private void createTournamentsList(final JComboBox<Integer> box,
        final QueryManager qM,
        final Pair<String, String> credentials,
        final boolean isPlayer,
        final Optional<Integer> couple) {

        if (isPlayer) {
            qM.findAllTorneoSingolareEligible(qM.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()).forEach(t -> box.addItem(t.getId()));
        } else {
            qM.findAllTorneoDoppioEligible(qM.findGiocatoriOfCoppia(qM.findCoppia(couple.get()).get())).forEach(t -> box.addItem(t.getId()));
        }
    }

    private void createTournamentEditionsList(final JComboBox<Integer> box, final QueryManager qM, final Integer id) {
        qM.findAllEdizioneByTorneo(qM.findTorneo(id).get()).forEach(et -> box.addItem(et.getNumeroEdizione()));
    }

    private void createTimeList(final JComboBox<String> box) {
        box.addItem("Nessuna");
        box.addItem("9:00-13:00");
        box.addItem("15:00-21:00");
    }
}

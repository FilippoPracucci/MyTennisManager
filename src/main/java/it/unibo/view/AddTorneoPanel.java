package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.controller.db.QueryManagerImpl;
import it.unibo.model.Torneo.Tipo;
import it.unibo.utils.Pair;

public class AddTorneoPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String TYPE_LABEL = "Tipo";
    private static final String RANK_LIMIT_LABEL = "Limite di categoria *";
    private static final String AGE_LIMIT_LABEL = "Limite eta' *";
    private static final String PRIZE_LABEL = "Montepremi *";
    private static final String INFO = "* opzionale";
    private static final String SIGNUP = "Aggiungi";
    private static final String CANCEL = "Annulla";

    private final JLabel typeLabel;
    private final JLabel rankLimitLabel;
    private final JLabel ageLimitLabel;
    private final JLabel prizeLabel;
    private final JComboBox<String> type;
    private final JComboBox<Integer> rankLimit;
    private final JComboBox<Integer> ageLimit;
    private final JTextField prizeField;
    private final JLabel info;
    private final JButton signUp;
    private final JButton cancel;

    public AddTorneoPanel(final SecondaryFrame frame, final Dimension dim, final QueryManagerImpl queryManager, Pair<String, String> credentials) {
        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.typeLabel = new JLabel(TYPE_LABEL);
        this.rankLimitLabel = new JLabel(RANK_LIMIT_LABEL);
        this.ageLimitLabel = new JLabel(AGE_LIMIT_LABEL);
        this.prizeLabel = new JLabel(PRIZE_LABEL);

        this.type = new JComboBox<>();
        this.createTypeList(type);
        this.rankLimit = new JComboBox<>();
        this.createRankList(2, 4, rankLimit);
        this.ageLimit = new JComboBox<>();
        this.createAgeList(this.ageLimit);
        this.prizeField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.info = new JLabel(INFO);

        this.signUp = new JButton(SIGNUP);
        this.cancel = new JButton(CANCEL);

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        cnst.gridy = 0;
        cnst.insets = insets;
        cnst.weighty = GridBagConstraints.CENTER;
        cnst.gridx = 0;
        this.addField(this.typeLabel, this.type, cnst);
        this.addField(this.rankLimitLabel, this.rankLimit, cnst);
        this.addField(this.ageLimitLabel, this.ageLimit, cnst);
        this.addField(this.prizeLabel, this.prizeField, cnst);
        cnst.gridx = 0;
        this.add(this.info, cnst);
        cnst.gridy += 2;
        cnst.gridx += 2;
        southPanel.add(this.signUp, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.signUp.addActionListener(e -> {
            Optional<Integer> rankL;
            Optional<Integer> ageL;
            Optional<Integer> prize;
            if (((Integer) this.rankLimit.getModel().getSelectedItem()).intValue() == 0) {
                rankL = Optional.empty();
            } else {
                rankL = Optional.of((Integer) this.rankLimit.getModel().getSelectedItem());
            }
            if (this.ageLimit.getModel().getSelectedItem().equals(this.ageLimit.getItemAt(0))) {
                ageL = Optional.empty();
            } else {
                ageL = Optional.of(((Integer) this.ageLimit.getModel().getSelectedItem()).intValue());
            }
            if (this.prizeField.getText().isBlank()) {
                prize = Optional.empty();
            } else {
                prize = Optional.of(Integer.valueOf(this.prizeField.getText()).intValue());
            }
            queryManager.addTorneo(queryManager.createTorneo(
                    Tipo.getTipo(this.type.getModel().getSelectedItem().toString()),
                    rankL,
                    ageL,
                    prize)
            );
            JOptionPane.showMessageDialog(this, "Torneo inserito con successo!");
            frame.changePanel(new AddEdizioneTorneoPanel(frame, dim, queryManager, credentials, Optional.of(queryManager.getIdLastTorneo())));
        });

        this.cancel.addActionListener(e -> {
            final String[] options = { "Si'", "No" };
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

    private void createRankList(final int min, final int max, final JComboBox<Integer> box) {
        box.addItem(0);
        for (int i = min; i <= max; i++) {
            box.addItem(i);
        }
    }

    private void createTypeList(final JComboBox<String> box) {
        List.of(Tipo.values()).forEach(t -> box.addItem(t.getNome()));
    }

    private void createAgeList(final JComboBox<Integer> box) {
        box.addItem(0);
        for (int i = 10; i <= 18; i+=2) {
            box.addItem(i);
        }
    }
}

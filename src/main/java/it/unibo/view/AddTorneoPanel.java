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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.controller.db.QueryManager;
import it.unibo.model.Torneo.Tipo;

public class AddTorneoPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String TYPE_LABEL = "Tipo";
    private static final String RANK_LIMIT_LABEL = "Limite di categoria *";
    private static final String AGE_LIMIT_LABEL = "Limite età *";
    private static final String PRIZE_LABEL = "Montepremi *";
    private static final String INFO = "Campo con * = opzionale";
    private static final String SIGNUP = "Aggiungi";
    private static final String CANCEL = "Annulla";

    private final JLabel typeLabel;
    private final JLabel rankLimitLabel;
    private final JLabel ageLimitLabel;
    private final JLabel prizeLabel;
    private final JComboBox<Tipo> type;
    private final JComboBox<Integer> rankLimit;
    private final JTextField ageLimitField;
    private final JTextField prizeField;
    private final JLabel info;
    private final JButton signUp;
    private final JButton cancel;

    public AddTorneoPanel(final SecondaryFrame frame, final Dimension dim, final QueryManager queryManager) {
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
        createTypeList(type);
        this.rankLimit = new JComboBox<>();
        createRankList(2, 4, rankLimit);
        this.ageLimitField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.prizeField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.info = new JLabel(INFO);

        this.signUp = new JButton(SIGNUP);
        this.cancel = new JButton(CANCEL);

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        startFrame(frame);
        cnst.gridy = 0;
        cnst.insets = insets;
        cnst.weighty = GridBagConstraints.CENTER;
        cnst.gridx = 0;
        this.addField(this.typeLabel, this.type, cnst);
        this.addField(this.rankLimitLabel, this.rankLimit, cnst);
        this.addField(this.ageLimitLabel, this.ageLimitField, cnst);
        this.addField(this.prizeLabel, this.prizeField, cnst);
        cnst.gridx = 0;
        this.add(this.info, cnst);
        cnst.gridy += 2;
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
            if (this.ageLimitField.getText().isBlank()) {
                ageL = Optional.empty();
            } else {
                ageL = Optional.of(Integer.valueOf(this.ageLimitField.getText()).intValue());
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
        });

        this.cancel.addActionListener(e -> {
            final String[] options = { "Yes", "No" };
            final int result = JOptionPane.showOptionDialog(this,
                    "Do you really want to quit?",
                    "Quitting",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (result == 0) {
                closeFrame(frame);
            }
        });
    }

    private void startFrame(final SecondaryFrame frame) {
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
    }

    private void closeFrame(final SecondaryFrame frame) {
        frame.dispose();
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

    private void createTypeList(final JComboBox<Tipo> box) {
        List.of(Tipo.values()).forEach(t -> box.addItem(t));
    }
}

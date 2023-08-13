package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class DeleteTournamentPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String ID_TORNEO_LABEL = "Id_Torneo";
    private static final String TYPE_LABEL = "Tipo";
    private static final String RANK_LIMIT_LABEL = "Limite di categoria *";
    private static final String AGE_LIMIT_LABEL = "Limite età *";
    private static final String PRIZE_LABEL = "Montepremi *";
    private static final String DELETE = "Elimina";
    private static final String CANCEL = "Annulla";

    private final JLabel idTorneoLabel;
    private final JLabel typeLabel;
    private final JLabel rankLimitLabel;
    private final JLabel ageLimitLabel;
    private final JLabel prizeLabel;
    private final JComboBox<Integer> idTorneoBox;
    private final JLabel type;
    private final JLabel rankLimit;
    private final JLabel ageLimit;
    private final JLabel prize;
    private final JButton delete;
    private final JButton cancel;

    private int id;

    public DeleteTournamentPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {

        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.idTorneoLabel = new JLabel(ID_TORNEO_LABEL);
        this.typeLabel = new JLabel(TYPE_LABEL);
        this.rankLimitLabel = new JLabel(RANK_LIMIT_LABEL);
        this.ageLimitLabel = new JLabel(AGE_LIMIT_LABEL);
        this.prizeLabel = new JLabel(PRIZE_LABEL);

        this.idTorneoBox = new JComboBox<>();
        this.createList(queryManager.findAllTorneoByCircolo(
            queryManager.findCircoloByOrganizzatore(
                queryManager.findOrganizzatoreByCredentials(credentials.getX(), credentials.getY()).get().getId()
            ).get()
        ).stream().map(c -> c.getId()).toList(), idTorneoBox);

        this.id = this.idTorneoBox.getModel().getElementAt(0);
        
        this.type = new JLabel(queryManager.findTorneo(this.id).get().getTipo().getNome());
        this.rankLimit = new JLabel(queryManager.findTorneo(this.id).get().getLimiteCategoria().get().toString());
        this.ageLimit = new JLabel(queryManager.findTorneo(this.id).get().getLimiteEta().get().toString());
        this.prize = new JLabel(queryManager.findTorneo(this.id).get().getMontepremi().get().toString());

        this.idTorneoBox.addItemListener(e -> {
            this.id = ((Integer) this.idTorneoBox.getModel().getSelectedItem()).intValue();
            this.type.setText(queryManager.findTorneo(this.id).get().getTipo().getNome());
            this.rankLimit.setText(queryManager.findTorneo(this.id).get().getLimiteCategoria().get().toString());
            this.ageLimit.setText(queryManager.findTorneo(this.id).get().getLimiteEta().get().toString());
            this.prize.setText(queryManager.findTorneo(this.id).get().getMontepremi().get().toString());
        });

        this.delete = new JButton(DELETE);
        this.cancel = new JButton(CANCEL);

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        startFrame(frame);
        cnst.gridy = 0;
        cnst.insets = insets;
        cnst.weighty = GridBagConstraints.CENTER;
        cnst.gridx = 0;
        this.addField(this.idTorneoLabel, this.idTorneoBox, cnst);
        this.addField(this.typeLabel, this.type, cnst);
        this.addField(this.rankLimitLabel, this.rankLimit, cnst);
        this.addField(this.ageLimitLabel, this.ageLimit, cnst);
        this.addField(this.prizeLabel, this.prize, cnst);
        southPanel.add(this.delete, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.delete.addActionListener(e -> {
            queryManager.deleteTorneo(this.id);
            JOptionPane.showMessageDialog(this, "Torneo eliminato con successo!");
            frame.changePanel(new MenuOrganizzatore(frame, dim, queryManager, credentials));
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

    private void createList(final List<Integer> ids, final JComboBox<Integer> box) {
        ids.forEach(i -> box.addItem(i));
    }
}

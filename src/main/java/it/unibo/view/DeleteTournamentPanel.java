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

import it.unibo.controller.db.QueryManager;
import it.unibo.model.EdizioneTorneo;
import it.unibo.utils.Pair;

public class DeleteTournamentPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String ID_TOURNAMENT_LABEL = "Id torneo";
    private static final String TYPE_LABEL = "Tipo";
    private static final String RANK_LIMIT_LABEL = "Limite di categoria *";
    private static final String AGE_LIMIT_LABEL = "Limite età *";
    private static final String PRIZE_LABEL = "Montepremi *";
    private static final String DELETE = "Elimina";
    private static final String CANCEL = "Annulla";

    private final JLabel idTournamentLabel;
    private final JLabel typeLabel;
    private final JLabel rankLimitLabel;
    private final JLabel ageLimitLabel;
    private final JLabel prizeLabel;
    private final JComboBox<Integer> idTournamentBox;
    private final JLabel type;
    private final JLabel rankLimit;
    private final JLabel ageLimit;
    private final JLabel prize;
    private final JButton delete;
    private final JButton cancel;

    private Optional<Integer> id;

    public DeleteTournamentPanel(final SecondaryFrame frame,
            final Dimension dim,
            final QueryManager queryManager,
            final Pair<String, String> credentials) {

        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.idTournamentLabel = new JLabel(ID_TOURNAMENT_LABEL);
        this.typeLabel = new JLabel(TYPE_LABEL);
        this.rankLimitLabel = new JLabel(RANK_LIMIT_LABEL);
        this.ageLimitLabel = new JLabel(AGE_LIMIT_LABEL);
        this.prizeLabel = new JLabel(PRIZE_LABEL);

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
    
        this.type = new JLabel();
        this.rankLimit = new JLabel();
        this.ageLimit = new JLabel();
        this.prize = new JLabel();
        if (this.id.isPresent()) {
            this.type.setText(queryManager.findTorneo(this.id.get()).get().getTipo().getNome());
            this.rankLimit.setText(queryManager.findTorneo(this.id.get()).get().getLimiteCategoria().get().toString());
            this.ageLimit.setText(queryManager.findTorneo(this.id.get()).get().getLimiteEta().get().toString());
            this.prize.setText(queryManager.findTorneo(this.id.get()).get().getMontepremi().get().toString());
        }

        this.idTournamentBox.addItemListener(e -> {
            this.id = Optional.of((Integer) this.idTournamentBox.getModel().getSelectedItem());
            this.type.setText(queryManager.findTorneo(this.id.get()).get().getTipo().getNome());
            this.rankLimit.setText(queryManager.findTorneo(this.id.get()).get().getLimiteCategoria().get().toString());
            this.ageLimit.setText(queryManager.findTorneo(this.id.get()).get().getLimiteEta().get().toString());
            this.prize.setText(queryManager.findTorneo(this.id.get()).get().getMontepremi().get().toString());
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
        this.addField(this.typeLabel, this.type, cnst);
        this.addField(this.rankLimitLabel, this.rankLimit, cnst);
        this.addField(this.ageLimitLabel, this.ageLimit, cnst);
        this.addField(this.prizeLabel, this.prize, cnst);
        southPanel.add(this.delete, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        if (!this.id.isPresent()) {
            this.delete.setEnabled(false);
        }

        this.delete.addActionListener(e -> {
            List<EdizioneTorneo> list = queryManager.findAllEdizioneByTorneo(queryManager.findTorneo(this.id.get()).get());
            list.forEach(et -> queryManager.deleteEdizioneTorneo(new Pair<>(this.id.get(), et.getNumeroEdizione())));
            queryManager.deleteTorneo(this.id.get());
            final String[] options = { "Sì", "No" };
            final int result = JOptionPane.showOptionDialog(this,
                    "Verranno eliminate anche tutte le relative edizioni\n" +
                    "Continuare?",
                    "Informazione",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (result == 0) {
                JOptionPane.showMessageDialog(this, "Torneo eliminato con successo!");
                frame.changePanel(new MenuOrganizzatore(frame, dim, queryManager, credentials));
            }
        });

        this.cancel.addActionListener(e -> {
            final String[] options = { "Sì", "No" };
            final int result = JOptionPane.showOptionDialog(this,
                    "Sei sicuro di voler uscirannullaree?",
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

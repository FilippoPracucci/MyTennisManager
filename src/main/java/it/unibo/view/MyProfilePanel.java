package it.unibo.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.controller.db.QueryManagerImpl;
import it.unibo.utils.Pair;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

public class MyProfilePanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String NAME_LABEL = "Nome";
    private static final String SURNAME_LABEL = "Cognome";
    private static final String EMAIL_LABEL = "Email";
    private static final String PASSWORD_LABEL = "Password";
    private static final String BADGE_LABEL = "Tessera";
    private static final String RANKING_LABEL = "Classifica";
    private static final String AGE_LABEL = "Eta'";
    private static final String PHONE_LABEL = "Telefono";
    private static final String CLUB_LABEL = "Circolo";
    private static final String MODIFY = "Modifica";
    private static final String SAVE = "Salva";
    private static final String CANCEL = "Annulla";

    private final JLabel nameLabel;
    private final JLabel surnameLabel;
    private final JLabel emailLabel;
    private final JLabel passwordLabel;
    private final JLabel badgeLabel;
    private final JLabel rankingLabel;
    private final JLabel ageLabel;
    private final JLabel phoneLabel;
    private final JLabel clubLabel;
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField emailField;
    private final JTextField passwordField;
    private final JTextField badgeField;
    private final JTextField rankingField;
    private final JTextField ageField;
    private final JTextField phoneField;
    private final JComboBox<Integer> club;
    private final JLabel clubName;
    private final JButton modifyButton;
    private final JButton saveButton;
    private final JButton cancelButton;

    Pair<String, String> credUpdated;

    public MyProfilePanel(final SecondaryFrame frame,
        final Dimension dim,
        final QueryManagerImpl queryManager,
        final Pair<String, String> credentials,
        final boolean isPlayer) {

        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.nameLabel = new JLabel(NAME_LABEL);
        this.surnameLabel = new JLabel(SURNAME_LABEL);
        this.emailLabel = new JLabel(EMAIL_LABEL);
        this.passwordLabel = new JLabel(PASSWORD_LABEL);
        this.badgeLabel = new JLabel(BADGE_LABEL);
        this.rankingLabel = new JLabel(RANKING_LABEL);
        this.ageLabel = new JLabel(AGE_LABEL);
        this.phoneLabel = new JLabel(PHONE_LABEL);
        this.clubLabel = new JLabel(CLUB_LABEL);

        this.nameField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.nameField.setEnabled(false);
        this.surnameField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.surnameField.setEnabled(false);
        this.emailField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.emailField.setEnabled(false);
        this.passwordField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.passwordField.setEnabled(false);
        this.badgeField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.badgeField.setEnabled(false);
        this.rankingField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.rankingField.setEnabled(false);
        this.ageField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.ageField.setEnabled(false);
        this.phoneField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.phoneField.setEnabled(false);
        this.club = new JComboBox<>();
        this.club.setEnabled(false);
        this.clubName = new JLabel();

        final Integer id;
        if (isPlayer) {
            id = queryManager.getIdGiocatore(
                queryManager.findGiocatoreByCredentials(credentials.getX(), credentials.getY()).get()
            );
            this.nameField.setText(queryManager.findGiocatore(id).get().getNome());
            this.surnameField.setText(queryManager.findGiocatore(id).get().getCognome());
            this.emailField.setText(queryManager.findGiocatore(id).get().getEmail());
            this.passwordField.setText(queryManager.findGiocatore(id).get().getPassword());
            this.badgeField.setText(queryManager.findGiocatore(id).get().getTessera());
            this.rankingField.setText(queryManager.findGiocatore(id).get().getClassifica());
            this.ageField.setText(String.valueOf(queryManager.findGiocatore(id).get().getEta()));
            this.phoneField.setText(queryManager.findGiocatore(id).get().getTelefono());
        } else {
            id = queryManager.getIdOrganizzatore(
                queryManager.findOrganizzatoreByCredentials(credentials.getX(), credentials.getY()).get()
            );
            this.nameField.setText(queryManager.findOrganizzatore(id).get().getNome());
            this.surnameField.setText(queryManager.findOrganizzatore(id).get().getCognome());
            this.emailField.setText(queryManager.findOrganizzatore(id).get().getEmail());
            this.passwordField.setText(queryManager.findOrganizzatore(id).get().getPassword());
        }

        this.modifyButton = new JButton(MODIFY);
        this.saveButton = new JButton(SAVE);
        this.cancelButton = new JButton(CANCEL);
        this.cancelButton.setEnabled(false);

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue())
        );

        cnst.gridy = 0;
        cnst.insets = insets;
        cnst.weighty = GridBagConstraints.CENTER;
        cnst.gridx = 0;
        addField(this.nameLabel, this.nameField, cnst);
        addField(this.surnameLabel, this.surnameField, cnst);
        addField(this.emailLabel, this.emailField, cnst);
        addField(this.passwordLabel, this.passwordField, cnst);
        if (isPlayer) {
            addField(this.badgeLabel, this.badgeField, cnst);
            addField(this.rankingLabel, this.rankingField, cnst);
            addField(this.ageLabel, this.ageField, cnst);
            addField(this.phoneLabel, this.phoneField, cnst);
            addField(this.clubLabel, this.club, cnst);
            cnst.gridy -= 2;
            cnst.gridx++;
            this.add(this.clubName, cnst);
            cnst.gridy += 2;

            createList(this.club, queryManager);
            this.clubName.setText(
                queryManager.findCircolo((Integer) this.club.getItemAt(0)).get().getNome()
            );
            this.club.addItemListener(e -> {
                this.clubName.setText(
                    queryManager.findCircolo((Integer) this.club.getModel().getSelectedItem()).get().getNome()
                );
            });
        }

        
        southPanel.add(this.modifyButton);
        southPanel.add(this.cancelButton);
        cnst.gridy--;
        cnst.gridx = 2;
        this.add(southPanel, cnst);

        this.modifyButton.addActionListener(e -> {
            southPanel.remove(this.modifyButton);
            southPanel.add(this.saveButton, 0);
            cancelButton.setEnabled(true);
            Arrays.asList(this.getComponents()).forEach(c -> c.setEnabled(true));
            this.updateUI();
        });

        this.credUpdated = credentials;

        this.saveButton.addActionListener(e -> {
            if (isPlayer) {
                queryManager.updateGiocatore(
                    id,
                    this.nameField.getText(),
                    this.surnameField.getText(),
                    this.emailField.getText(),
                    this.passwordField.getText(),
                    this.badgeField.getText(),
                    this.rankingField.getText(),
                    Integer.valueOf(this.ageField.getText()).intValue(),
                    queryManager.findGiocatore(id).get().getSesso(),
                    this.phoneField.getText(),
                    (Integer) this.club.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this, "Giocatore aggiornato con successo!");
                credUpdated = new Pair<>(this.emailField.getText(), this.passwordField.getText());
                frame.changePanel(new MenuGiocatore(frame, dim, queryManager, credUpdated));
            } else {
                queryManager.updateOrganizzatore(
                    id,
                    this.nameField.getText(),
                    this.surnameField.getText(),
                    this.emailField.getText(),
                    this.passwordField.getText()
                );
                JOptionPane.showMessageDialog(this, "Organizzatore aggiornato con successo!");
                credUpdated = new Pair<>(this.emailField.getText(), this.passwordField.getText());
                frame.changePanel(new MenuOrganizzatore(frame, dim, queryManager, credUpdated));
            }
        });

        this.cancelButton.addActionListener(e -> {
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
                if (isPlayer) {
                    frame.changePanel(new MenuGiocatore(frame, dim, queryManager, credUpdated));
                } else {
                    frame.changePanel(new MenuOrganizzatore(frame, dim, queryManager, credUpdated));
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

    private void createList(final JComboBox<Integer> box, final QueryManagerImpl qM) {
        qM.findAllCircolo().forEach(c -> box.addItem(c.getId()));
    }
}

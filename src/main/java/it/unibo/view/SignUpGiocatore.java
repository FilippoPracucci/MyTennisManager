package it.unibo.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

import it.unibo.controller.db.QueryManager;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;

public class SignUpGiocatore extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String NAME_LABEL = "Nome";
    private static final String SURNAME_LABEL = "Cognome";
    private static final String EMAIL_LABEL = "Email";
    private static final String PASSWORD_LABEL = "Password";
    private static final String CARD_LABEL = "Tessera";
    private static final String RANKING_LABEL = "Classifica";
    private static final String AGE_LABEL = "Eta'";
    private static final String GENDER_LABEL = "Sesso";
    private static final String PHONE_LABEL = "Telefono";
    private static final String CLUB_LABEL = "Circolo";
    private static final String SIGNUP = "Registrati";
    private static final String CANCEL = "Annulla";

    private final JLabel nameLabel;
    private final JLabel surnameLabel;
    private final JLabel emailLabel;
    private final JLabel passwordLabel;
    private final JLabel cardLabel;
    private final JLabel rankingLabel;
    private final JLabel ageLabel;
    private final JLabel genderLabel;
    private final JLabel phoneLabel;
    private final JLabel clubLabel;
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField emailField;
    private final JTextField passwordField;
    private final JTextField cardField;
    private final JTextField rankingField;
    private final JTextField ageField;
    private final JTextField phoneField;
    private final JComboBox<Integer> club;
    private final JLabel clubName;
    private final JButton signUp;
    private final JButton cancel;

    public SignUpGiocatore(final SecondaryFrame frame, final Dimension dim, final QueryManager queryManager) {
        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);
        final JRadioButtonMenuItem male = new JRadioButtonMenuItem("M");
        final JRadioButtonMenuItem female = new JRadioButtonMenuItem("F");

        this.nameLabel = new JLabel(NAME_LABEL);
        this.surnameLabel = new JLabel(SURNAME_LABEL);
        this.emailLabel = new JLabel(EMAIL_LABEL);
        this.passwordLabel = new JLabel(PASSWORD_LABEL);
        this.cardLabel = new JLabel(CARD_LABEL);
        this.rankingLabel = new JLabel(RANKING_LABEL);
        this.ageLabel = new JLabel(AGE_LABEL);
        this.genderLabel = new JLabel(GENDER_LABEL);
        this.phoneLabel = new JLabel(PHONE_LABEL);
        this.clubLabel = new JLabel(CLUB_LABEL);
        
        this.nameField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.surnameField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.emailField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.passwordField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.cardField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.rankingField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.ageField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.phoneField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.club = new JComboBox<>();
        this.clubName = new JLabel();
        
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
        addField(this.nameLabel, this.nameField, cnst);
        addField(this.surnameLabel, this.surnameField, cnst);
        addField(this.emailLabel, this.emailField, cnst);
        addField(this.passwordLabel, this.passwordField, cnst);
        addField(this.cardLabel, this.cardField, cnst);
        addField(this.rankingLabel, this.rankingField, cnst);
        addField(this.ageLabel, this.ageField, cnst);
        cnst.gridx = 0;
        this.add(this.genderLabel, cnst);
        cnst.gridx += 2;
        this.add(male, cnst);
        cnst.gridy++;
        this.add(female, cnst);
        cnst.gridy += 2;
        addField(this.phoneLabel, this.phoneField, cnst);
        addField(this.clubLabel, this.club, cnst);
        cnst.gridy -= 2;
        cnst.gridx++;
        this.add(this.clubName, cnst);
        cnst.gridy += 2;

        createList(this.club, queryManager);
        this.club.addItemListener(e -> {
            this.clubName.setText(queryManager.findCircolo((Integer) this.club.getModel().getSelectedItem()).get().getNome());
        });

        
        southPanel.add(this.signUp, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.signUp.addActionListener(e -> {
            String gender = male.isSelected() ? male.getText() : female.getText();
            queryManager.addGiocatore(queryManager.createGiocatore(
                    this.nameField.getText(),
                    this.surnameField.getText(),
                    this.emailField.getText(),
                    this.passwordField.getText(),
                    this.cardField.getText(),
                    this.rankingField.getText(),
                    Integer.valueOf(this.ageField.getText()).intValue(),
                    gender,
                    this.phoneField.getText(),
                    ((Integer) this.club.getModel().getSelectedItem())
            ));
            JOptionPane.showMessageDialog(this, "Giocatore inserito con successo!");
            frame.closeFrame();
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
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

    private void createList(final JComboBox<Integer> box, final QueryManager qM) {
        qM.findAllCircolo().forEach(c -> box.addItem(c.getId()));
    }
}


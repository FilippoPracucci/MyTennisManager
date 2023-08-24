package it.unibo.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.controller.db.QueryManagerImpl;
import it.unibo.utils.Pair;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;

public class SignUpOrganizzatore extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String NAME_LABEL = "Nome";
    private static final String SURNAME_LABEL = "Cognome";
    private static final String EMAIL_LABEL = "Email";
    private static final String PASSWORD_LABEL = "Password";
    private static final String SIGNUP = "Registrati e aggiungi il suo circolo";
    private static final String CANCEL = "Annulla";

    private final JLabel nameLabel;
    private final JLabel surnameLabel;
    private final JLabel emailLabel;
    private final JLabel passwordLabel;
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField emailField;
    private final JTextField passwordField;
    private final JButton signUp;
    private final JButton cancel;

    public SignUpOrganizzatore(final SecondaryFrame frame, final Dimension dim, final QueryManagerImpl queryManager) {
        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.nameLabel = new JLabel(NAME_LABEL);
        this.surnameLabel = new JLabel(SURNAME_LABEL);
        this.emailLabel = new JLabel(EMAIL_LABEL);
        this.passwordLabel = new JLabel(PASSWORD_LABEL);

        this.nameField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.surnameField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.emailField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.passwordField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());

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

        
        southPanel.add(this.signUp, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.signUp.addActionListener(e -> {
            queryManager.addOrganizzatore(queryManager.createOrganizzatore(
                    this.nameField.getText(),
                    this.surnameField.getText(),
                    this.emailField.getText(),
                    this.passwordField.getText()
            ));
            frame.changePanel(new AddCircoloPanel(frame, dim, queryManager, new Pair<>(this.emailField.getText(), this.passwordField.getText())));
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

    private void addField(final JLabel label, final JTextField field, final GridBagConstraints cnst) {
        cnst.gridx = 0;
        this.add(label, cnst);
        cnst.gridx += 2;
        this.add(field, cnst);
        cnst.gridy += 2;
    }
}

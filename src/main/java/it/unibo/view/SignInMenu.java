package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class SignInMenu extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String EMAIL_LABEL = "Email";
    private static final String PASSWORD_LABEL = "Password";
    private static final String SIGNIN = "Accedi";
    private static final String CANCEL = "Annulla";

    private final JLabel emailLabel;
    private final JLabel passwordLabel;
    private final JTextField emailField;
    private final JTextField passwordField;
    private final JButton signIn;
    private final JButton cancel;

    public SignInMenu(final SecondaryFrame frame, final Dimension dim, final QueryManager queryManager) {
        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.emailLabel = new JLabel(EMAIL_LABEL);
        this.passwordLabel = new JLabel(PASSWORD_LABEL);

        this.emailField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.passwordField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());

        this.signIn = new JButton(SIGNIN);
        this.cancel = new JButton(CANCEL);

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(Double.valueOf(dim.getWidth() * WIDTH_PERC).intValue(),
                Double.valueOf(dim.getHeight() * HEIGHT_PERC).intValue()));
        frame.startFrame(this);
        cnst.gridy = 0;
        cnst.insets = insets;
        cnst.weighty = GridBagConstraints.CENTER;
        cnst.gridx = 0;
        addField(this.emailLabel, this.emailField, cnst);
        addField(this.passwordLabel, this.passwordField, cnst);

        
        southPanel.add(this.signIn, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.signIn.addActionListener(e -> {
            final String[] options = { "Giocatore", "Organizzatore" };
            final int result = JOptionPane.showOptionDialog(this,
                    null,
                    null,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    options,
                    options[1]);
            if (result == 0) {
                frame.changePanel(new MenuGiocatore(frame, dim, queryManager, new Pair<>(this.emailField.getText(), this.passwordField.getText())));
            } else if (result == 1) {
                frame.changePanel(new MenuOrganizzatore(frame, dim, queryManager, new Pair<>(this.emailField.getText(), this.passwordField.getText())));
            } else {
                frame.closeFrame();
            }
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

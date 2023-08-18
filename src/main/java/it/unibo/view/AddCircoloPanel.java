package it.unibo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.controller.db.QueryManager;
import it.unibo.utils.Pair;

public class AddCircoloPanel extends JPanel {

    private static final double WIDTH_PERC = 0.6;
    private static final double HEIGHT_PERC = 0.5;

    private static final String NAME_LABEL = "Nome";
    private static final String CITY_LABEL = "Citta'";
    private static final String ADDRESS_LABEL = "Indirizzo";
    private static final String PHONE_LABEL = "Telefono";
    private static final String SIGNUP = "Aggiungi";
    private static final String CANCEL = "Annulla";

    private final JLabel nameLabel;
    private final JLabel cityLabel;
    private final JLabel addressLabel;
    private final JLabel phoneLabel;
    private final JTextField nameField;
    private final JTextField cityField;
    private final JTextField addressField;
    private final JTextField phoneField;
    private final JButton signUp;
    private final JButton cancel;

    public AddCircoloPanel(final SecondaryFrame frame, final Dimension dim, final QueryManager queryManager, Pair<String, String> credentials) {
        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints cnst = new GridBagConstraints();
        final int n = (int) (dim.getHeight() * 0.01);
        final Insets insets = new Insets(n, n, n, n);

        this.nameLabel = new JLabel(NAME_LABEL);
        this.cityLabel = new JLabel(CITY_LABEL);
        this.addressLabel = new JLabel(ADDRESS_LABEL);
        this.phoneLabel = new JLabel(PHONE_LABEL);

        this.nameField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.cityField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.addressField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());
        this.phoneField = new JTextField(Double.valueOf(dim.getWidth() * 0.025).intValue());

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
        addField(nameLabel, nameField, cnst);
        addField(cityLabel, cityField, cnst);
        addField(addressLabel, addressField, cnst);
        addField(phoneLabel, phoneField, cnst);

        
        southPanel.add(this.signUp, cnst);
        southPanel.add(this.cancel, cnst);
        this.add(southPanel, cnst);

        this.signUp.addActionListener(e -> {
            queryManager.addCircolo(queryManager.createCircolo(
                    queryManager.getIdOrganizzatore(queryManager.findOrganizzatoreByCredentials(credentials.getX(), credentials.getY()).get()),
                    this.nameField.getText(),
                    this.cityField.getText(),
                    this.addressField.getText(),
                    this.phoneField.getText())
            );
            JOptionPane.showMessageDialog(this, "Circolo inserito con successo!");
            frame.closeFrame();
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

    private void addField(final JLabel label, final JComponent field, final GridBagConstraints cnst) {
        cnst.gridx = 0;
        this.add(label, cnst);
        cnst.gridx += 2;
        this.add(field, cnst);
        cnst.gridy += 2;
    }
}

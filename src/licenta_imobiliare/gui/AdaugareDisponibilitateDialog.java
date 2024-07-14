package licenta_imobiliare.gui;

import licenta_imobiliare.dao.AngajatDAO;
import licenta_imobiliare.model.Angajat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class AdaugareDisponibilitateDialog extends JDialog {
    private JSpinner dataSpinner;
    private JCheckBox[] oreCheckBoxes;
    private Map<Date, List<String>> disponibilitate;
    private JComboBox<String> angajatiComboBox;
    private Map<String, String> angajatNumeIdMap;

    public AdaugareDisponibilitateDialog(JFrame parent) {
        super(parent, "Adaugă Disponibilitate", true);
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(parent);

        disponibilitate = new HashMap<>();
        angajatNumeIdMap = new HashMap<>();

        PanouGradient mainPanel = new PanouGradient(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;

        JLabel angajatLabel = new JLabel("Angajat:");
        angajatLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(angajatLabel, gbc);

        angajatiComboBox = new JComboBox<>(incarcaAngajati());
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(angajatiComboBox, gbc);


        JLabel dataLabel = new JLabel("Data:");
        dataLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(dataLabel, gbc);

        dataSpinner = new JSpinner(new SpinnerDateModel());
        dataSpinner.setEditor(new JSpinner.DateEditor(dataSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(dataSpinner, gbc);


        oreCheckBoxes = new JCheckBox[8];
        for (int i = 0; i < 8; i++) {
            oreCheckBoxes[i] = new JCheckBox("Ora " + (9 + i));
            oreCheckBoxes[i].setOpaque(false);
            oreCheckBoxes[i].setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = 2 + i;
            gbc.gridwidth = 2;
            mainPanel.add(oreCheckBoxes[i], gbc);
        }

        JButton adaugaButton = new JButton();
        ImageIcon addIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/plus.png"));
        Image scaledAddImage = addIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        adaugaButton.setIcon(new ImageIcon(scaledAddImage));
        adaugaButton.setContentAreaFilled(false);
        adaugaButton.setBorderPainted(false);
        adaugaButton.setFocusPainted(false);
        adaugaButton.setPreferredSize(new Dimension(30, 30));
        adaugaButton.addActionListener(this::adaugaDisponibilitate);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(adaugaButton, gbc);

        JButton inapoiButton = new JButton();
        ImageIcon backIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"));
        Image scaledBackImage = backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        inapoiButton.setIcon(new ImageIcon(scaledBackImage));
        inapoiButton.setContentAreaFilled(false);
        inapoiButton.setBorderPainted(false);
        inapoiButton.setFocusPainted(false);
        inapoiButton.setPreferredSize(new Dimension(30, 30));
        inapoiButton.addActionListener(e -> dispose());
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(inapoiButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private String[] incarcaAngajati() {
        AngajatDAO angajatDAO = new AngajatDAO();
        List<Angajat> angajatiList = angajatDAO.getAllAngajati();
        String[] angajatiNume = new String[angajatiList.size()];

        for (int i = 0; i < angajatiList.size(); i++) {
            Angajat angajat = angajatiList.get(i);
            String numeComplet = angajat.getNumeAngajat() + " " + angajat.getPrenumeAngajat();
            angajatiNume[i] = numeComplet;
            angajatNumeIdMap.put(numeComplet, angajat.getIdAngajat());
        }

        return angajatiNume;
    }

    private void adaugaDisponibilitate(ActionEvent e) {
        Date data = (Date) dataSpinner.getValue();
        String numeAngajatSelectat = (String) angajatiComboBox.getSelectedItem();
        if (numeAngajatSelectat == null) {
            JOptionPane.showMessageDialog(this, "Te rog să selectezi un angajat.");
            return;
        }

        String idAngajat = angajatNumeIdMap.get(numeAngajatSelectat);

        for (JCheckBox checkBox : oreCheckBoxes) {
            if (checkBox.isSelected()) {
                String ora = checkBox.getText().replace("Ora ", "") + ":00:00";
                disponibilitate.computeIfAbsent(data, k -> new ArrayList<>()).add(ora);
            }
        }

        AngajatDAO angajatDAO = new AngajatDAO();
        for (Map.Entry<Date, List<String>> entry : disponibilitate.entrySet()) {
            for (String ora : entry.getValue()) {
                angajatDAO.adaugaDisponibilitate(idAngajat, entry.getKey(), ora);
            }
        }

        JOptionPane.showMessageDialog(this, "Disponibilitate adăugată cu succes!");
        dispose();
    }

    public Map<Date, List<String>> getDisponibilitate() {
        return disponibilitate;
    }

    class PanouGradient extends JPanel {
        public PanouGradient(LayoutManager layout) {
            super(layout);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = new Color(123, 104, 238);
            Color color2 = new Color(72, 209, 204);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }
}

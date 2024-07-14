package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ProiectDAO;
import licenta_imobiliare.model.Proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaugaProiectDialog extends JDialog {
    private JTextField numeProiectField;
    private JTextField adresaProiectField;
    private JButton adaugaButton;
    private JButton anuleazaButton;

    public AdaugaProiectDialog(JFrame parent) {
        super(parent, "Adaugă Proiect", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        PanouGradient mainPanel = new PanouGradient();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel numeProiectLabel = new JLabel("Nume Proiect:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(numeProiectLabel, gbc);

        numeProiectField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(numeProiectField, gbc);

        JLabel adresaProiectLabel = new JLabel("Adresa Proiect:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(adresaProiectLabel, gbc);

        adresaProiectField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(adresaProiectField, gbc);

        adaugaButton = new JButton("Adaugă");
        adaugaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adaugaProiect();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(adaugaButton, gbc);

        anuleazaButton = new JButton("Anulează");
        anuleazaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(anuleazaButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void adaugaProiect() {
        String numeProiect = numeProiectField.getText().trim();
        String adresaProiect = adresaProiectField.getText().trim();

        if (numeProiect.isEmpty() || adresaProiect.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog să completezi toate câmpurile.", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Proiect proiect = new Proiect();
        proiect.setNumeProiect(numeProiect);
        proiect.setAdresaProiect(adresaProiect);

        ProiectDAO proiectDAO = new ProiectDAO();
        proiectDAO.adaugaProiect(proiect);

        JOptionPane.showMessageDialog(this, "Proiect adăugat cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    class PanouGradient extends JPanel {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            AdaugaProiectDialog dialog = new AdaugaProiectDialog(frame);
            dialog.setVisible(true);
        });
    }
}

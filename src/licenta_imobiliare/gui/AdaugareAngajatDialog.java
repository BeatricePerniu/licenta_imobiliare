package licenta_imobiliare.gui;

import licenta_imobiliare.dao.AngajatDAO;
import licenta_imobiliare.model.Angajat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Date;

public class AdaugareAngajatDialog extends JDialog {
    private JTextField numeField;
    private JTextField prenumeField;
    private JTextField cnpField;
    private JTextField telefonField;
    private JTextField emailField;
    private JSpinner dataNastereSpinner;
    private JLabel logoLabel;

    public AdaugareAngajatDialog(JFrame parent) {
        super(parent, "Adaugă Angajat Nou", true);
        setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new PanouGradient(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        logoLabel = new JLabel();
        logoLabel.setPreferredSize(new Dimension(100, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(logoLabel, gbc);
        gbc.gridwidth = 1;

        JLabel numeLabel = new JLabel("Nume:");
        numeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(numeLabel, gbc);
        numeField = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(numeField, gbc);

        JLabel prenumeLabel = new JLabel("Prenume:");
        prenumeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(prenumeLabel, gbc);
        prenumeField = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(prenumeField, gbc);

        JLabel cnpLabel = new JLabel("CNP:");
        cnpLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(cnpLabel, gbc);
        cnpField = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(cnpField, gbc);

        JLabel telefonLabel = new JLabel("Telefon:");
        telefonLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(telefonLabel, gbc);
        telefonField = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(telefonField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(emailLabel, gbc);
        emailField = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        JLabel dataNastereLabel = new JLabel("Data nașterii:");
        dataNastereLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(dataNastereLabel, gbc);
        dataNastereSpinner = new JSpinner(new SpinnerDateModel());
        dataNastereSpinner.setEditor(new JSpinner.DateEditor(dataNastereSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        mainPanel.add(dataNastereSpinner, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        JButton adaugaButton = new JButton(incarcaIconita("resources/images/plus.png", 30, 30));
        adaugaButton.setContentAreaFilled(false);
        adaugaButton.setBorderPainted(false);
        adaugaButton.setFocusPainted(false);
        adaugaButton.setPreferredSize(new Dimension(40, 40));
        adaugaButton.addActionListener(e -> adaugaAngajat());
        buttonPanel.add(adaugaButton);

        JButton inapoiButton = new JButton(incarcaIconita("resources/images/back.png", 30, 30));
        inapoiButton.setContentAreaFilled(false);
        inapoiButton.setBorderPainted(false);
        inapoiButton.setFocusPainted(false);
        inapoiButton.setPreferredSize(new Dimension(40, 40));
        inapoiButton.addActionListener(e -> dispose());
        buttonPanel.add(inapoiButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente(); // Initial resize

        setVisible(true);
    }

    private void adaugaAngajat() {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String cnp = cnpField.getText();
        String email = emailField.getText();
        Date dataNastere = (Date) dataNastereSpinner.getValue();
        int telefon;
        try {
            telefon = Integer.parseInt(telefonField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Te rog să introduci un număr de telefon valid.");
            return;
        }

        Angajat angajat = new Angajat();
        angajat.setNumeAngajat(nume);
        angajat.setPrenumeAngajat(prenume);
        angajat.setCnpAngajat(cnp);
        angajat.setEmailAngajat(email);
        angajat.setDataNastereAngajat(dataNastere);
        angajat.setNrTelAngajat(telefon);

        AngajatDAO angajatDAO = new AngajatDAO();
        angajatDAO.adaugaAngajat(angajat);

        JOptionPane.showMessageDialog(this, "Angajat adăugat cu succes!");
        dispose();
    }

    private void redimensioneazaComponente() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            int logoWidth = width / 5;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));

            int buttonSize = Math.min(width / 20, height / 20);
        }
    }

    private ImageIcon incarcaIconita(String cale, int latime, int inaltime) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(cale);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            if (icon.getImage() != null) {
                Image img = icon.getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } else {
            System.err.println("Couldn't find file: " + cale);
        }
        return null;
    }

    class PanouGradient extends JPanel {
        public PanouGradient() {
            super();
        }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Adaugă Angajat");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            new AdaugareAngajatDialog(frame);
            frame.setVisible(true);
        });
    }
}

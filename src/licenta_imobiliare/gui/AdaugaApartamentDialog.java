package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ApartamentDAO;
import licenta_imobiliare.dao.ProiectDAO;
import licenta_imobiliare.model.Apartament;
import licenta_imobiliare.model.Proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdaugaApartamentDialog extends JDialog {
    private JTextField pretField;
    private JTextField suprafataUtilaField;
    private JTextField suprafataTotalaField;
    private JTextField etajField;
    private JTextField holField;
    private JTextField camereField;
    private JTextField baieField;
    private JTextField bucatarieField;
    private JCheckBox chirieCheckBox;
    private JComboBox<Proiect> proiectComboBox;
    private JButton saveButton;
    private JButton cancelButton;
    private Apartament apartament;
    private Runnable refreshCallback;

    public AdaugaApartamentDialog(JFrame parent, Runnable refreshCallback) {
        this(parent, null, refreshCallback);
    }

    public AdaugaApartamentDialog(JFrame parent, Apartament apartament, Runnable refreshCallback) {
        super(parent, "Adaugă/Actualizează Apartament", true);
        this.apartament = apartament;
        this.refreshCallback = refreshCallback;
        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(parent);

        PanouGradient mainPanel = new PanouGradient();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        mainPanel.add(new JLabel("Preț:"), gbc);
        pretField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(pretField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Suprafață Utilă(m2):"), gbc);
        suprafataUtilaField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(suprafataUtilaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Suprafață Totală(m2):"), gbc);
        suprafataTotalaField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(suprafataTotalaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Etaj:"), gbc);
        etajField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(etajField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Hol(m2):"), gbc);
        holField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(holField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Număr Camere:"), gbc);
        camereField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(camereField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Baie(m2):"), gbc);
        baieField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(baieField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(new JLabel("Bucătărie(m2):"), gbc);
        bucatarieField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(bucatarieField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        mainPanel.add(new JLabel("Chirie:"), gbc);
        chirieCheckBox = new JCheckBox();
        gbc.gridx = 1;
        mainPanel.add(chirieCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        mainPanel.add(new JLabel("Proiect:"), gbc);
        proiectComboBox = new JComboBox<>();
        incarcaProiecte();
        gbc.gridx = 1;
        mainPanel.add(proiectComboBox, gbc);

        if (apartament != null) {
            pretField.setText(String.valueOf(apartament.getPret()));
            suprafataUtilaField.setText(String.valueOf(apartament.getSuprafataUtila()));
            suprafataTotalaField.setText(String.valueOf(apartament.getSuprafataTotala()));
            etajField.setText(String.valueOf(apartament.getEtaj()));
            holField.setText(String.valueOf(apartament.getHol()));
            camereField.setText(String.valueOf(apartament.getCamere()));
            baieField.setText(String.valueOf(apartament.getBaie()));
            bucatarieField.setText(String.valueOf(apartament.getBucatarie()));
            chirieCheckBox.setSelected(apartament.isChirie());
            proiectComboBox.setSelectedItem(apartament.getProiect());
        }

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new PanouGradient();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        saveButton = createGradientButton("Salvează");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveApartament();
            }
        });
        buttonPanel.add(saveButton);

        cancelButton = createGradientButton("Anulează");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void incarcaProiecte() {
        ProiectDAO proiectDAO = new ProiectDAO();
        List<Proiect> proiecte = proiectDAO.getToateProiectele();
        for (Proiect proiect : proiecte) {
            proiectComboBox.addItem(proiect);
        }
    }

    private Proiect getSelectedProiect() {
        return (Proiect) proiectComboBox.getSelectedItem();
    }

    private void saveApartament() {
        if (apartament == null) {
            apartament = new Apartament();
        }

        apartament.setPret(Integer.parseInt(pretField.getText()));
        apartament.setSuprafataUtila(Integer.parseInt(suprafataUtilaField.getText()));
        apartament.setSuprafataTotala(Integer.parseInt(suprafataTotalaField.getText()));
        apartament.setEtaj(Integer.parseInt(etajField.getText()));
        apartament.setHol(Integer.parseInt(holField.getText()));
        apartament.setCamere(Integer.parseInt(camereField.getText()));
        apartament.setBaie(Integer.parseInt(baieField.getText()));
        apartament.setBucatarie(Integer.parseInt(bucatarieField.getText()));
        apartament.setChirie(chirieCheckBox.isSelected());
        apartament.setProiect(getSelectedProiect());

        ApartamentDAO apartamentDAO = new ApartamentDAO();
        if (apartament.getIdApartament() == null) {
            apartamentDAO.adaugaApartament(apartament);
        } else {
            apartamentDAO.actualizeazaApartament(apartament);
        }

        JOptionPane.showMessageDialog(this, "Apartament salvat cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        refreshCallback.run();
        dispose();
    }

    private JButton createGradientButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground() instanceof Color) {
                    Color color1 = new Color(123, 104, 238);
                    Color color2 = new Color(72, 209, 204);
                    Graphics2D g2d = (Graphics2D) g;
                    int width = getWidth();
                    int height = getHeight();
                    GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, width, height);
                }
                super.paintComponent(g);
            }
        };
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        return button;
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
            AdaugaApartamentDialog dialog = new AdaugaApartamentDialog(frame, () -> {});
            dialog.setVisible(true);
        });
    }
}

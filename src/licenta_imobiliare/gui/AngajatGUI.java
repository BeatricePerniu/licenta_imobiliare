package licenta_imobiliare.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class AngajatGUI extends JPanel {
    private JFrame frame;
    private JLabel logoLabel;
    private JButton adaugaDisponibilitateButton;
    private JButton adaugaAngajatButton;
    private JButton afiseazaDisponibilitatiButton;
    private JButton inapoiButton;

    public AngajatGUI(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());


        PanouGradient panouGradient = new PanouGradient();
        panouGradient.setLayout(new BorderLayout());


        JPanel antet = new JPanel();
        antet.setOpaque(false);
        antet.setLayout(new BorderLayout());
        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        antet.add(logoLabel, BorderLayout.WEST);
        antet.setPreferredSize(new Dimension(800, 150));
        panouGradient.add(antet, BorderLayout.NORTH);

        JPanel panouPrincipal = new JPanel();
        panouPrincipal.setOpaque(false);
        panouPrincipal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        buttonPanel.setOpaque(false);

        adaugaDisponibilitateButton = creeazaButon("Adaugă Disponibilitate Angajat", "resources/images/plus.png");
        adaugaDisponibilitateButton.addActionListener(this::deschideFereastraDisponibilitate);
        buttonPanel.add(adaugaDisponibilitateButton);

        adaugaAngajatButton = creeazaButon("Adaugă Angajat Nou", "resources/images/plus.png");
        adaugaAngajatButton.addActionListener(this::deschideFereastraAdaugareAngajat);
        buttonPanel.add(adaugaAngajatButton);

        afiseazaDisponibilitatiButton = creeazaButon("Lista Disponibilități Angajați", "resources/images/document.png");
        afiseazaDisponibilitatiButton.addActionListener(this::deschideFereastraAfisareDisponibilitati);
        buttonPanel.add(afiseazaDisponibilitatiButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panouPrincipal.add(buttonPanel, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);

        inapoiButton = new JButton();
        inapoiButton.setContentAreaFilled(false);
        inapoiButton.setBorderPainted(false);
        inapoiButton.addActionListener(e -> {
            frame.setContentPane(new RezervareGUI(frame));
            frame.invalidate();
            frame.validate();
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(inapoiButton);
        panouGradient.add(backButtonPanel, BorderLayout.SOUTH);

        add(panouGradient, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();
    }

    private JButton creeazaButon(String text, String caleIconita) {
        JButton buton = new JButton();
        buton.setText(text);
        if (caleIconita != null) {
            buton.setIcon(incarcaIconita(caleIconita, 100, 100));
        }
        buton.setHorizontalTextPosition(SwingConstants.CENTER);
        buton.setVerticalTextPosition(SwingConstants.BOTTOM);
        buton.setBackground(new Color(0, 102, 204));
        buton.setForeground(Color.WHITE);
        buton.setBorderPainted(false);
        buton.setFocusPainted(false);
        buton.setContentAreaFilled(false);
        return buton;
    }

    private void redimensioneazaComponente() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {

            int logoWidth = width / 5;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));


            int buttonSize = Math.min(width / 6, height / 6);
            if (adaugaDisponibilitateButton.getIcon() != null) {
                adaugaDisponibilitateButton.setIcon(incarcaIconita("resources/images/plus.png", buttonSize, buttonSize));
            }
            if (adaugaAngajatButton.getIcon() != null) {
                adaugaAngajatButton.setIcon(incarcaIconita("resources/images/plus.png", buttonSize, buttonSize));
            }
            if (afiseazaDisponibilitatiButton.getIcon() != null) {
                afiseazaDisponibilitatiButton.setIcon(incarcaIconita("resources/images/document.png", buttonSize, buttonSize));
            }


            int backButtonSize = Math.min(width / 30, height / 30);
            inapoiButton.setIcon(incarcaIconita("resources/images/back.png", backButtonSize, backButtonSize));
        }
    }

    private ImageIcon incarcaIconita(String cale, int latime, int inaltime) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(cale));
        if (icon.getImage() != null && latime > 0 && inaltime > 0) {
            Image img = icon.getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    private void deschideFereastraDisponibilitate(ActionEvent e) {
        new AdaugareDisponibilitateDialog(frame).setVisible(true);
    }

    private void deschideFereastraAdaugareAngajat(ActionEvent e) {
        new AdaugareAngajatDialog(frame).setVisible(true);
    }

    private void deschideFereastraAfisareDisponibilitati(ActionEvent e) {
        new AfisareDisponibilitatiDialog(frame).setVisible(true);
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
            JFrame frame = new JFrame("Angajat GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new AngajatGUI(frame));
            frame.setVisible(true);
        });
    }
}

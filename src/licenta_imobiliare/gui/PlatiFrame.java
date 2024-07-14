package licenta_imobiliare.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;

public class PlatiFrame extends JPanel {
    private JFrame frame;
    private JLabel logoLabel;
    private JButton inregistrarePlataButton;
    private JButton raportPlatiButton;
    private JButton backButton;

    public PlatiFrame(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());


        PanouGradient panouGradient = new PanouGradient();
        panouGradient.setLayout(new BorderLayout());

        JPanel antet = new JPanel();
        antet.setOpaque(false);
        antet.setLayout(new BorderLayout());
        ImageIcon logoIcon = incarcaIconita("resources/images/logomic.png", 100, 50);
        logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        antet.add(logoLabel, BorderLayout.WEST);
        antet.setPreferredSize(new Dimension(800, 150));
        panouGradient.add(antet, BorderLayout.NORTH);


        JPanel panouPrincipal = new JPanel();
        panouPrincipal.setOpaque(false);
        panouPrincipal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        inregistrarePlataButton = creeazaButon("Inregistrare Plata", "resources/images/plus.png");
        raportPlatiButton = creeazaButon("Raport Plati", "resources/images/raport.png");


        gbc.gridx = 0;
        gbc.gridy = 0;
        panouPrincipal.add(inregistrarePlataButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panouPrincipal.add(raportPlatiButton, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);


        backButton = new JButton();
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            frame.setContentPane(new PlatiGUI(frame));
            frame.invalidate();
            frame.validate();
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);
        panouGradient.add(backButtonPanel, BorderLayout.SOUTH);

        add(panouGradient, BorderLayout.CENTER);
        // Listener for resizing icons
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();


        inregistrarePlataButton.addActionListener(e -> {
            try {
                frame.setContentPane(new InregistrarePlataFrame(frame));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            frame.invalidate();
            frame.validate();
        });

        raportPlatiButton.addActionListener(e -> {
            frame.setContentPane(new RaportPlatiFrame(frame));
            frame.invalidate();
            frame.validate();
        });
    }

    private JButton creeazaButon(String text, String caleIconita) {
        JButton buton = new JButton();
        buton.setText(text);
        buton.setIcon(incarcaIconita(caleIconita, 100, 100)); // Set initial icon size
        buton.setHorizontalTextPosition(SwingConstants.CENTER);
        buton.setVerticalTextPosition(SwingConstants.BOTTOM);
        buton.setBackground(new Color(0, 102, 204));
        buton.setForeground(Color.WHITE);
        buton.setBorderPainted(false);
        buton.setFocusPainted(false);
        buton.setContentAreaFilled(false); // Remove button background
        return buton;
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

    private void redimensioneazaComponente() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {

            int logoWidth = width / 5;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));


            int buttonSize = Math.min(width / 6, height / 6);
            inregistrarePlataButton.setIcon(incarcaIconita("resources/images/plus.png", buttonSize, buttonSize));
            raportPlatiButton.setIcon(incarcaIconita("resources/images/raport.png", buttonSize, buttonSize));

            int backButtonSize = Math.min(width / 20, height / 20);
            backButton.setIcon(incarcaIconita("resources/images/back.png", backButtonSize, backButtonSize));
        }
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
            JFrame frame = new JFrame("Plăți Frame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new PlatiFrame(frame));
            frame.setVisible(true);
        });
    }
}

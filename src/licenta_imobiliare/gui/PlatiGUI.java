package licenta_imobiliare.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PlatiGUI extends JPanel {
    private JFrame frame;
    private JLabel logoLabel;
    private JButton facturiButton;
    private JButton platiButton;
    private JButton backButton;

    public PlatiGUI(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        PanouGradient panouGradient = new PanouGradient();
        panouGradient.setLayout(new BorderLayout());

        JPanel antet = new JPanel();
        antet.setOpaque(false);
        antet.setLayout(new BorderLayout());
        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/logomic.png"));
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


        facturiButton = creeazaButon("Facturi", "resources/images/factura.png");
        platiButton = creeazaButon("Plăți", "resources/images/plata.png");


        gbc.gridx = 0;
        gbc.gridy = 0;
        panouPrincipal.add(facturiButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panouPrincipal.add(platiButton, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);


        backButton = new JButton();
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            frame.setContentPane(new MeniuPrincipal(frame));
            frame.invalidate();
            frame.validate();
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);
        panouGradient.add(backButtonPanel, BorderLayout.SOUTH);

        add(panouGradient, BorderLayout.CENTER);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente(); // Redimensionare inițială


        facturiButton.addActionListener(e -> {
            frame.setContentPane(new FacturiGUI(frame));
            frame.invalidate();
            frame.validate();
        });

        platiButton.addActionListener(e -> {
            frame.setContentPane(new PlatiFrame(frame));
            frame.invalidate();
            frame.validate();
        });
    }

    private JButton creeazaButon(String text, String caleIconita) {
        JButton buton = new JButton();
        buton.setText(text);
        buton.setIcon(incarcaIconita(caleIconita, 100, 100));
        buton.setHorizontalTextPosition(SwingConstants.CENTER);
        buton.setVerticalTextPosition(SwingConstants.BOTTOM);
        buton.setForeground(Color.WHITE);
        buton.setBorderPainted(false);
        buton.setFocusPainted(false);
        buton.setContentAreaFilled(false);
        return buton;
    }

    private ImageIcon incarcaIconita(String cale, int latime, int inaltime) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(cale));
        if (icon.getImage() != null && latime > 0 && inaltime > 0) {
            Image img = icon.getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    private void redimensioneazaComponente() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            // Redimensioneaz logo-ul
            int logoWidth = width / 5;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));

            int buttonSize = Math.min(width / 6, height / 6);
            facturiButton.setIcon(incarcaIconita("resources/images/factura.png", buttonSize, buttonSize));
            platiButton.setIcon(incarcaIconita("resources/images/plata.png", buttonSize, buttonSize));


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
            JFrame frame = new JFrame("Plăți GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new PlatiGUI(frame));
            frame.setVisible(true);
        });
    }
}

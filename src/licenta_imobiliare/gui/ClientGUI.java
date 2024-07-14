package licenta_imobiliare.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ClientGUI extends JPanel {
    private JFrame frame;
    private JPanel panouPrincipal;
    private JLabel logoLabel;
    private JButton butonClientExistent;
    private JButton butonClientNou;
    private JButton butonInapoi;

    public ClientGUI(JFrame frame) {
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


        panouPrincipal = new JPanel();
        panouPrincipal.setOpaque(false);
        panouPrincipal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        butonClientExistent = creeazaButon("resources/images/clientexistent.png");
        butonClientNou = creeazaButon("resources/images/client.png");


        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panouPrincipal.add(butonClientExistent, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panouPrincipal.add(butonClientNou, gbc);


        JPanel panouInapoi = new JPanel();
        panouInapoi.setOpaque(false);
        panouInapoi.setLayout(new BorderLayout());
        butonInapoi = creeazaButon("resources/images/back.png");
        panouInapoi.add(butonInapoi, BorderLayout.SOUTH);
        panouGradient.add(panouInapoi, BorderLayout.SOUTH);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);
        add(panouGradient, BorderLayout.CENTER);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();


        butonClientExistent.addActionListener(e -> {
            frame.setContentPane(new ClientExistentGUI(frame));
            frame.invalidate();
            frame.validate();
        });


        butonClientNou.addActionListener(e -> {
            frame.setContentPane(new ClientNouGUI(frame));
            frame.invalidate();
            frame.validate();
        });


        butonInapoi.addActionListener(e -> {
            frame.setContentPane(new MeniuPrincipal(frame));
            frame.invalidate();
            frame.validate();
        });
    }

    private JButton creeazaButon(String caleIconita) {
        JButton buton = new JButton();
        buton.setIcon(incarcaIconita(caleIconita, 100, 100));
        buton.setBorderPainted(false);
        buton.setBorderPainted(false);
        buton.setFocusPainted(false);
        buton.setContentAreaFilled(false);
        return buton;
    }

    private void redimensioneazaComponente() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            int iconSize = Math.min(width / 5, height / 5);
            butonClientExistent.setIcon(incarcaIconita("resources/images/clientexistent.png", iconSize, iconSize));
            butonClientNou.setIcon(incarcaIconita("resources/images/client.png", iconSize, iconSize));
            butonInapoi.setIcon(incarcaIconita("resources/images/back.png", iconSize / 2, iconSize / 2));

            // Redimensionăm logo-ul pentru a fi mai mic
            int logoWidth = width / 6;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));
        }
    }

    private ImageIcon incarcaIconita(String cale, int latime, int inaltime) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(cale));
        if (icon.getImage() != null) {
            Image img = icon.getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sky Real Estate - Client");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ClientGUI(frame));
            frame.setVisible(true);
        });
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
}

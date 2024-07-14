package licenta_imobiliare.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MeniuPrincipal extends JPanel {
    private JFrame frame;
    private JPanel panouPrincipal;
    private JLabel logoLabel;
    private JButton butonClienti;
    private JButton butonProiecte;
    private JButton butonDocumente;
    private JButton butonPlati;
    private JButton butonRezervari;

    public MeniuPrincipal(JFrame frame) {
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

        butonClienti = creeazaButon("", "resources/images/client.png");
        butonProiecte = creeazaButon("", "resources/images/proiect.png");
        butonDocumente = creeazaButon("", "resources/images/doc.png");
        butonPlati = creeazaButon("", "resources/images/plati.png");
        butonRezervari = creeazaButon("", "resources/images/rezervare.png");

        JPanel panouButoane = new JPanel(new GridLayout(1, 5, 20, 0));
        panouButoane.setOpaque(false);
        panouButoane.add(butonClienti);
        panouButoane.add(butonProiecte);
        panouButoane.add(butonDocumente);
        panouButoane.add(butonPlati);
        panouButoane.add(butonRezervari);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        panouPrincipal.add(panouButoane, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);
        add(panouGradient, BorderLayout.CENTER);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaIconite();
            }
        });

        redimensioneazaIconite();


        butonClienti.addActionListener(e -> {
            frame.setContentPane(new ClientGUI(frame));
            frame.invalidate();
            frame.validate();
        });


        butonProiecte.addActionListener(e -> {
            frame.setContentPane(new ProiecteGUI(frame));
            frame.invalidate();
            frame.validate();
        });


        butonDocumente.addActionListener(e -> {
            frame.setContentPane(new DocumenteGUI(frame));
            frame.invalidate();
            frame.validate();
        });


        butonRezervari.addActionListener(e -> {
            frame.setContentPane(new RezervareGUI(frame));
            frame.invalidate();
            frame.validate();
        });


        butonPlati.addActionListener(e -> {
            frame.setContentPane(new PlatiGUI(frame));
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
        buton.setBackground(new Color(0, 102, 204));
        buton.setForeground(Color.WHITE);
        buton.setBorderPainted(false);
        buton.setFocusPainted(false);
        buton.setContentAreaFilled(false);
        return buton;
    }

    private void redimensioneazaIconite() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            int iconSize = Math.min(width / 6, height / 6); // Calcul dimensiunea icon in fct de dim ferestrei
            butonClienti.setIcon(incarcaIconita("resources/images/client.png", iconSize, iconSize));
            butonProiecte.setIcon(incarcaIconita("resources/images/proiect.png", iconSize, iconSize));
            butonDocumente.setIcon(incarcaIconita("resources/images/doc.png", iconSize, iconSize));
            butonPlati.setIcon(incarcaIconita("resources/images/plati.png", iconSize, iconSize));
            butonRezervari.setIcon(incarcaIconita("resources/images/rezervare.png", iconSize, iconSize));

            //dim logomic
            int logoWidth = width / 5;
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
            JFrame frame = new JFrame("Sky Real Estate - Meniu Principal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new MeniuPrincipal(frame));
            frame.setVisible(true);
        });
    }

    // Panou de fundal gradient
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

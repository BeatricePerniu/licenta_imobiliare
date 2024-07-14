package licenta_imobiliare.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ProiecteGUI extends JPanel {
    private JFrame frame;
    private JPanel panouPrincipal;
    private JLabel logoLabel;
    private JButton butonVanzare;
    private JButton butonChirie;
    private JButton butonInapoi;

    public ProiecteGUI(JFrame frame) {
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

        JPanel panouButoane = new JPanel(new GridBagLayout());
        panouButoane.setOpaque(false);


        butonVanzare = creeazaButon("Vânzare", "resources/images/vanzare.png", 100, 100);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        panouButoane.add(butonVanzare, gbc);

        butonChirie = creeazaButon("Chirie", "resources/images/chirie.png", 100, 100);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panouButoane.add(butonChirie, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panouPrincipal.add(panouButoane, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);

        butonInapoi = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        butonInapoi.setContentAreaFilled(false);
        butonInapoi.setBorderPainted(false);
        JPanel panouInapoi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panouInapoi.setOpaque(false);
        panouInapoi.add(butonInapoi);
        panouGradient.add(panouInapoi, BorderLayout.SOUTH);

        add(panouGradient, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();

        butonVanzare.addActionListener(e -> {
            frame.setContentPane(new DisponibilitateApartamenteVanzareGUI(frame));
            frame.invalidate();
            frame.validate();
        });

        butonChirie.addActionListener(e -> {
            frame.setContentPane(new DisponibilitateApartamenteInchiriereGUI(frame));
            frame.invalidate();
            frame.validate();
        });

        butonInapoi.addActionListener(e -> {
            frame.setContentPane(new MeniuPrincipal(frame));
            frame.invalidate();
            frame.validate();
        });
    }

    private JButton creeazaButon(String text, String caleIconita, int latime, int inaltime) {
        ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(caleIconita))
                .getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH));
        JButton buton = new JButton(text, icon);
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
        int width = frame.getWidth();
        int height = frame.getHeight();


        int logoWidth = width / 5;
        int logoHeight = height / 10;
        logoLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/logomic.png"))
                .getImage().getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH)));


        int buttonSize = Math.min(width / 5, height / 5);
        butonVanzare.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/vanzare.png"))
                .getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        butonChirie.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/chirie.png"))
                .getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));

        int backSize = Math.min(width / 20, height / 20);
        butonInapoi.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(backSize, backSize, Image.SCALE_SMOOTH)));
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
            JFrame frame = new JFrame("Proiecte");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ProiecteGUI(frame));
            frame.setVisible(true);
        });
    }
}

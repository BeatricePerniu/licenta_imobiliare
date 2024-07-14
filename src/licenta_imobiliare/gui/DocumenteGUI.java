package licenta_imobiliare.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DocumenteGUI extends JPanel {

    private JFrame parentFrame;
    private JLabel logoLabel;
    private JButton backButton;

    public DocumenteGUI(JFrame parentFrame) {
        this.parentFrame = parentFrame;
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


        JButton contractNouButton = creeazaButon("Contract Nou", "resources/images/docnou.png");
        JButton contracteExistenteButton = creeazaButon("Contract Existent", "resources/images/document.png");


        JPanel panouButoane = new JPanel(new GridLayout(1, 2, 20, 0));
        panouButoane.setOpaque(false);
        panouButoane.add(contractNouButton);
        panouButoane.add(contracteExistenteButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        panouPrincipal.add(panouButoane, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);
        add(panouGradient, BorderLayout.CENTER);


        backButton = new JButton();
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);


        JPanel backButtonPanel = new PanouGradient();
        backButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.SOUTH);


        contractNouButton.addActionListener(e -> new ContractNouDialog(parentFrame));
        contracteExistenteButton.addActionListener(e -> {
            ContracteExistenteFrame frame = new ContracteExistenteFrame(parentFrame);
            frame.setSize(parentFrame.getSize());
            frame.setLocationRelativeTo(parentFrame);
            frame.setVisible(true);
        });

        backButton.addActionListener(e -> {
            parentFrame.setContentPane(new MeniuPrincipal(parentFrame));
            parentFrame.invalidate();
            parentFrame.validate();
        });


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

            int logoWidth = width / 5;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));


            int buttonSize = Math.min(width / 20, height / 20);
            backButton.setIcon(incarcaIconita("resources/images/back.png", buttonSize, buttonSize));
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
            JFrame frame = new JFrame("Documente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new DocumenteGUI(frame));
            frame.setVisible(true);
        });
    }
}

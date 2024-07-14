package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ClientDAO;
import licenta_imobiliare.model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientNouGUI extends JPanel {
    private JFrame frame;
    private JPanel panouPrincipal;
    private JLabel logoLabel;
    private JTextField textFieldNume, textFieldPrenume, textFieldCNP, textFieldSerie, textFieldNrBuletin, textFieldDomiciliu, textFieldEmail, textFieldTelefon, textFieldDataNastere;
    private JButton butonAdauga, butonInapoi;

    public ClientNouGUI(JFrame frame) {
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

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel labelNume = new JLabel("Nume:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panouPrincipal.add(labelNume, gbc);

        textFieldNume = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panouPrincipal.add(textFieldNume, gbc);

        JLabel labelPrenume = new JLabel("Prenume:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panouPrincipal.add(labelPrenume, gbc);

        textFieldPrenume = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panouPrincipal.add(textFieldPrenume, gbc);

        JLabel labelCNP = new JLabel("CNP:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panouPrincipal.add(labelCNP, gbc);

        textFieldCNP = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panouPrincipal.add(textFieldCNP, gbc);

        JLabel labelSerie = new JLabel("Serie:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panouPrincipal.add(labelSerie, gbc);

        textFieldSerie = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panouPrincipal.add(textFieldSerie, gbc);

        JLabel labelNrBuletin = new JLabel("Nr Buletin:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panouPrincipal.add(labelNrBuletin, gbc);

        textFieldNrBuletin = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panouPrincipal.add(textFieldNrBuletin, gbc);

        JLabel labelDataNastere = new JLabel("Data Nastere (yyyy-MM-dd):");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panouPrincipal.add(labelDataNastere, gbc);

        textFieldDataNastere = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        panouPrincipal.add(textFieldDataNastere, gbc);

        JLabel labelDomiciliu = new JLabel("Domiciliu:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panouPrincipal.add(labelDomiciliu, gbc);

        textFieldDomiciliu = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        panouPrincipal.add(textFieldDomiciliu, gbc);

        JLabel labelEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        panouPrincipal.add(labelEmail, gbc);

        textFieldEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 8;
        panouPrincipal.add(textFieldEmail, gbc);

        JLabel labelTelefon = new JLabel("Telefon:");
        gbc.gridx = 0;
        gbc.gridy = 9;
        panouPrincipal.add(labelTelefon, gbc);

        textFieldTelefon = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 9;
        panouPrincipal.add(textFieldTelefon, gbc);


        butonAdauga = new JButton("Adauga Client");
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panouPrincipal.add(butonAdauga, gbc);


        butonInapoi = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        butonInapoi.setContentAreaFilled(false);
        butonInapoi.setBorderPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        panouPrincipal.add(butonInapoi, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);
        add(panouGradient, BorderLayout.CENTER);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();


        butonInapoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new ClientGUI(frame));
                frame.invalidate();
                frame.validate();
            }
        });


        butonAdauga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nume = textFieldNume.getText();
                    String prenume = textFieldPrenume.getText();
                    String cnp = textFieldCNP.getText();
                    String serie = textFieldSerie.getText();
                    int nrBuletin = Integer.parseInt(textFieldNrBuletin.getText());
                    String domiciliu = textFieldDomiciliu.getText();
                    String email = textFieldEmail.getText();
                    int telefon = Integer.parseInt(textFieldTelefon.getText());
                    String dataNastereStr = textFieldDataNastere.getText();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date dataNastere = dateFormat.parse(dataNastereStr);

                    Client client = new Client(null, nume, prenume, cnp, serie, nrBuletin, dataNastere, domiciliu, email, telefon);

                    ClientDAO clientDAO = new ClientDAO();
                    clientDAO.adaugaClient(client);

                    JOptionPane.showMessageDialog(frame, "Clientul a fost adăugat cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Număr de buletin sau telefon invalid.", "Eroare", JOptionPane.ERROR_MESSAGE);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(frame, "Formatul datei de naștere este invalid.", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void redimensioneazaComponente() {
        int width = frame.getWidth();
        int height = frame.getHeight();


        int logoWidth = width / 5;
        int logoHeight = height / 10;
        logoLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/logomic.png"))
                .getImage().getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH)));


        int buttonWidth = width / 20;
        int buttonHeight = height / 20;
        butonInapoi.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(buttonWidth / 2, buttonHeight / 2, Image.SCALE_SMOOTH)));
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
            JFrame frame = new JFrame("Sky Real Estate - Client Nou");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ClientNouGUI(frame));
            frame.setVisible(true);
        });
    }
}

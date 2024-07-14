package licenta_imobiliare.gui;

import licenta_imobiliare.dao.UtilizatorDAO;
import licenta_imobiliare.model.Utilizator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PanouAutentificare extends JFrame {
    private JPanel mainPanel;
    private JLabel logoLabel;
    private JLabel userLabel;
    private JTextField userTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;

    public PanouAutentificare() {
        setTitle("Sky Real Estate - Autentificare");
        setSize(800, 600); // Dimensiunea ferestrei
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("images/logomare.png"));
        logoLabel = new JLabel(logoIcon);
        mainPanel.add(logoLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;

        ImageIcon userIcon = new ImageIcon(getClass().getClassLoader().getResource("images/utilizator.png"));
        userLabel = new JLabel(userIcon);
        mainPanel.add(userLabel, gbc);

        gbc.gridx++;
        userTextField = new JTextField(20);
        mainPanel.add(userTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;


        ImageIcon passwordIcon = new ImageIcon(getClass().getClassLoader().getResource("images/parola.png"));
        passwordLabel = new JLabel(passwordIcon);
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx++;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        loginButton = new JButton("LOG IN");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autentificare();
            }
        });

        add(mainPanel);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaIconite();
            }
        });

        redimensioneazaIconite();
    }

    private void autentificare() {
        String utilizator = userTextField.getText();
        String parola = new String(passwordField.getPassword());

        // Validare utilizator dinbaza de date
        if (validareUtilizator(utilizator, parola)) {
            JOptionPane.showMessageDialog(this, "Autentificare reușită!");
            // Deschide fereastra principală a aplicației
            setContentPane(new MeniuPrincipal(this));
            invalidate();
            validate();
        } else {
            JOptionPane.showMessageDialog(this, "Nume utilizator sau parolă incorectă.", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validareUtilizator(String utilizator, String parola) {
        UtilizatorDAO utilizatorDAO = new UtilizatorDAO();
        Utilizator utilizatorObiect = utilizatorDAO.getUtilizatorByNume(utilizator);

        if (utilizatorObiect != null && utilizatorObiect.getParolaUtilizator().equals(parola)) {
            return true;
        }
        return false;
    }

    private void redimensioneazaIconite() {
        if (getWidth() > 0 && getHeight() > 0) {
            int iconSizeLogo = Math.min(getWidth() / 4, getHeight() / 4); // Dimensiunea logo-ului
            int iconSizeUserPass = Math.min(getWidth() / 20, getHeight() / 20); // Dimensiunea iconițelor pentru utilizator și parola

            logoLabel.setIcon(incarcaIconita("images/logomare.png", iconSizeLogo * 2, iconSizeLogo));
            userLabel.setIcon(incarcaIconita("images/utilizator.png", iconSizeUserPass, iconSizeUserPass));
            passwordLabel.setIcon(incarcaIconita("images/parola.png", iconSizeUserPass, iconSizeUserPass));
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
            PanouAutentificare frame = new PanouAutentificare();
            frame.setVisible(true);
        });
    }


    class GradientPanel extends JPanel {
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

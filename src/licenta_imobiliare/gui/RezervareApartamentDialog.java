package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ApartamentDAO;
import licenta_imobiliare.dao.ClientDAO;
import licenta_imobiliare.dao.RezervareDAO;
import licenta_imobiliare.model.Apartament;
import licenta_imobiliare.model.Client;
import licenta_imobiliare.model.Rezervare;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RezervareApartamentDialog extends JDialog {
    private JComboBox<String> apartamentComboBox;
    private JComboBox<String> clientComboBox;
    private JButton rezervaButton;
    private JButton inapoiButton;
    private JButton exportaPDFButton;
    private JLabel logoLabel;
    private Rezervare rezervare;

    private ApartamentDAO apartamentDAO = new ApartamentDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private RezervareDAO rezervareDAO = new RezervareDAO();

    public RezervareApartamentDialog(JFrame parent) {
        super(parent, "Rezervare Apartament", true);
        setLayout(new BorderLayout());
        setSize(600, 400); // Increased window size
        setLocationRelativeTo(parent);

        PanouGradient panouGradient = new PanouGradient(getLayout());
        panouGradient.setLayout(new BorderLayout());

        JPanel antet = new JPanel();
        antet.setOpaque(false);
        antet.setLayout(new BorderLayout());
        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        antet.add(logoLabel, BorderLayout.WEST);
        antet.setPreferredSize(new Dimension(600, 100));
        panouGradient.add(antet, BorderLayout.NORTH);

        JPanel mainPanel = new PanouGradient(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Increased padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // set id
        JLabel apartamentLabel = new JLabel("ID Apartament:");
        apartamentLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(apartamentLabel, gbc);

        apartamentComboBox = new JComboBox<>(incarcaApartamente());
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(apartamentComboBox, gbc);


        JLabel clientLabel = new JLabel("Client:");
        clientLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(clientLabel, gbc);

        clientComboBox = new JComboBox<>(incarcaClienti());
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(clientComboBox, gbc);

        // Btn rezerva
        rezervaButton = createButtonWithIcon("resources/images/plus.png", 30, 30);
        rezervaButton.addActionListener(this::rezervaApartament);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(rezervaButton, gbc);

        // buton back
        inapoiButton = createButtonWithIcon("resources/images/back.png", 30, 30);
        inapoiButton.addActionListener(e -> dispose());
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(inapoiButton, gbc);

        // buton export PDF
        exportaPDFButton = createButtonWithIcon("resources/images/pdf.png", 30, 30);
        exportaPDFButton.addActionListener(this::exportaInPDF);
        exportaPDFButton.setVisible(false);
        gbc.gridx = 2;
        gbc.gridy = 2;
        mainPanel.add(exportaPDFButton, gbc);

        panouGradient.add(mainPanel, BorderLayout.CENTER);

        add(panouGradient, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();

        setVisible(true);
    }

    private JButton createButtonWithIcon(String iconPath, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(iconPath))
                .getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }

    private String[] incarcaApartamente() {
        List<Apartament> apartamenteList = apartamentDAO.getToateApartamentele();
        return apartamenteList.stream()
                .map(Apartament::getIdApartament)
                .toArray(String[]::new);
    }

    private String[] incarcaClienti() {
        List<Client> clientList = clientDAO.getAllClients();
        return clientList.stream()
                .map(client -> client.getNume() + " " + client.getPrenume())
                .toArray(String[]::new);
    }

    private void rezervaApartament(ActionEvent e) {
        String idApartament = (String) apartamentComboBox.getSelectedItem();
        String numeClientSelectat = (String) clientComboBox.getSelectedItem();
        if (idApartament == null || numeClientSelectat == null) {
            JOptionPane.showMessageDialog(this, "Te rog să selectezi un apartament și un client.");
            return;
        }

        String[] clientNumePrenume = numeClientSelectat.split(" ");
        if (clientNumePrenume.length < 2) {
            JOptionPane.showMessageDialog(this, "Numele clientului nu este valid.");
            return;
        }

        String numeClient = clientNumePrenume[0];
        String prenumeClient = clientNumePrenume[1];

        Client client = clientDAO.getClientByNume(numeClient, prenumeClient);
        if (client == null) {
            JOptionPane.showMessageDialog(this, "Clientul selectat nu a fost găsit.");
            return;
        }


        rezervare = new Rezervare();
        rezervare.setApartament(apartamentDAO.getApartament(idApartament));
        rezervare.setClient(client);

        rezervareDAO.adaugaRezervare(rezervare);

        JOptionPane.showMessageDialog(this, "Apartament rezervat cu succes!");

        exportaPDFButton.setVisible(true);
    }

    private void exportaInPDF(ActionEvent e) {
        if (rezervare == null) {
            JOptionPane.showMessageDialog(this, "Nu există nicio rezervare de exportat.");
            return;
        }

        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf"); // Adjust path as necessary

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, fontFile);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add logo
            File logoFile = new File("C:\\Users\\beatr\\OneDrive\\Desktop\\licenta_imobiliare\\src\\resources\\images\\logomic.png"); // Adjust path as necessary
            PDImageXObject logo = PDImageXObject.createFromFileByExtension(logoFile, document);
            contentStream.drawImage(logo, 50, 700, 100, 100);

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(200, 750);
            contentStream.showText("Detalii Rezervare");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Client: " + rezervare.getClient().getNume() + " " + rezervare.getClient().getPrenume());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Apartament: " + rezervare.getApartament().getIdApartament());
            contentStream.endText();

            contentStream.close();

            String pdfFileName = "Rezervare_" + rezervare.getClient().getNume() + "_" + rezervare.getApartament().getIdApartament() + ".pdf";
            document.save(pdfFileName);
            JOptionPane.showMessageDialog(this, "PDF generat cu succes: " + pdfFileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la generarea PDF-ului: " + ex.getMessage());
        }
    }

    private void redimensioneazaComponente() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            // Resize logo
            int logoWidth = width / 5;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));
        }
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

    class PanouGradient extends JPanel {
        public PanouGradient(LayoutManager layout) {
            super(layout);
        }

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
        SwingUtilities.invokeLater(() -> new RezervareApartamentDialog(null).setVisible(true));
    }
}

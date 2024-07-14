package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ClientDAO;
import licenta_imobiliare.dao.FacturaDAO;
import licenta_imobiliare.dao.PlataDAO;
import licenta_imobiliare.model.Client;
import licenta_imobiliare.model.Factura;
import licenta_imobiliare.model.Plata;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class InregistrarePlataFrame extends JPanel {
    private JFrame frame;
    private JComboBox<Client> clientComboBox;
    private JComboBox<Factura> facturaComboBox;
    private JCheckBox plataInregistrataCheckBox;
    private JButton buttonOK;
    private JButton pdfButton;
    private JSpinner dateSpinner;
    private JLabel logoLabel;
    private JButton buttonCancel; // Moved declaration here
    private JPanel contentPane;

    private ClientDAO clientDAO;
    private FacturaDAO facturaDAO;
    private PlataDAO plataDAO;

    public InregistrarePlataFrame(JFrame frame) throws SQLException {
        this.frame = frame;
        clientDAO = new ClientDAO();
        facturaDAO = new FacturaDAO();
        plataDAO = new PlataDAO();

        setLayout(new BorderLayout());


        JPanel panelGradient = new PanouGradient();
        panelGradient.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Smaller insets for more compact layout
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;


        logoLabel = new JLabel();
        logoLabel.setPreferredSize(new Dimension(100, 50)); // Set initial preferred size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelGradient.add(logoLabel, gbc);
        gbc.gridwidth = 1;

        clientComboBox = new JComboBox<>();
        facturaComboBox = new JComboBox<>();
        incarcaClienti();

        clientComboBox.addActionListener(e -> incarcaFacturi());

        plataInregistrataCheckBox = new JCheckBox("Plata Inregistrata");

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelGradient.add(new JLabel("Selecteaza Client:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelGradient.add(clientComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelGradient.add(new JLabel("Selecteaza Factura:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelGradient.add(facturaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelGradient.add(new JLabel("Selecteaza Data:"), gbc);
        dateSpinner = new JSpinner(new SpinnerDateModel());
        gbc.gridx = 1;
        gbc.gridy = 3;
        panelGradient.add(dateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelGradient.add(plataInregistrataCheckBox, gbc);

        buttonOK = new JButton("Inregistreaza Plata");
        buttonOK.addActionListener(e -> inregistreazaPlata());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelGradient.add(buttonOK, gbc);


        pdfButton = new JButton();
        pdfButton.setIcon(incarcaIconita("resources/images/pdf.png", 50, 50)); // Set larger icon
        pdfButton.setContentAreaFilled(false); // Remove background
        pdfButton.setBorderPainted(false); // Remove border
        pdfButton.addActionListener(e -> genereazaPDF());

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelGradient.add(pdfButton, gbc);


        add(panelGradient, BorderLayout.CENTER);


        JPanel footerPanel = new PanouGradient();
        footerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setOpaque(false); // Ensure transparency for gradient background

        buttonCancel = new JButton();
        buttonCancel.setIcon(incarcaIconita("resources/images/back.png", 20, 20)); // Set icon
        buttonCancel.setContentAreaFilled(false); // Remove background
        buttonCancel.setBorderPainted(false); // Remove border
        buttonCancel.addActionListener(e -> {
            frame.setContentPane(new PlatiFrame(frame));
            frame.invalidate();
            frame.validate();
        });

        footerPanel.add(buttonCancel);
        add(footerPanel, BorderLayout.SOUTH);



        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();
    }

    private void incarcaClienti() {
        List<Client> clienti = clientDAO.getAllClients();
        for (Client client : clienti) {
            clientComboBox.addItem(client);
        }
    }

    private void incarcaFacturi() {
        Client client = (Client) clientComboBox.getSelectedItem();
        if (client != null) {
            List<Factura> facturi = facturaDAO.getFacturiByClientId(client.getIdClient());
            facturaComboBox.removeAllItems();
            for (Factura factura : facturi) {
                facturaComboBox.addItem(factura);
            }
        }
    }

    private void inregistreazaPlata() {
        Client client = (Client) clientComboBox.getSelectedItem();
        Factura factura = (Factura) facturaComboBox.getSelectedItem();
        if (client != null && factura != null && plataInregistrataCheckBox.isSelected()) {
            Plata plata = new Plata();
            plata.setClient(client);
            plata.setFactura(factura);
            plata.setSuma(factura.getTotal());
            plata.setData((java.util.Date) dateSpinner.getValue());

            plataDAO.adaugaPlata(plata);
            JOptionPane.showMessageDialog(this, "Plata a fost inregistrata cu succes!");
        } else {
            JOptionPane.showMessageDialog(this, "Va rugam sa completati toate campurile si sa bifati 'Plata Inregistrata'.", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void redimensioneazaComponente() {
        int width = frame.getWidth();
        int height = frame.getHeight();

        if (width > 0 && height > 0) {

            int logoWidth = width / 15 ;
            int logoHeight = height / 20;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));

            int buttonSize = Math.min(width / 30, height / 30);
            buttonCancel.setPreferredSize(new Dimension(buttonSize * 2, buttonSize * 2));
            pdfButton.setPreferredSize(new Dimension(buttonSize * 3, buttonSize * 3));
            buttonOK.setPreferredSize(new Dimension(buttonSize * 6, buttonSize * 2));
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

    private void genereazaPDF() {
        Client client = (Client) clientComboBox.getSelectedItem();
        Factura factura = (Factura) facturaComboBox.getSelectedItem();
        if (client != null && factura != null && plataInregistrataCheckBox.isSelected()) {
            File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf"); // Adjust path as necessary

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())); // Landscape orientation
                document.addPage(page);

                PDType0Font font = PDType0Font.load(document, fontFile);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                File logoFile = new File("C:\\Users\\beatr\\OneDrive\\Desktop\\licenta_imobiliare\\src\\resources\\images\\logomic.png"); // Adjust path as necessary
                PDImageXObject logo = PDImageXObject.createFromFileByExtension(logoFile, document);
                contentStream.drawImage(logo, 50, 500, 100, 100);

                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(200, 550);
                contentStream.showText("Confirmare Plata");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Client: " + client.getNume() + " " + client.getPrenume());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Apartament: " + factura.getApartament().getIdApartament());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Pret: " + factura.getPretApartament());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Data Emiterii: " + factura.getDataEmiterii());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Data Scadenta: " + factura.getDataScadenta());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Suma Platita: " + factura.getTotal());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Data Plata: " + dateSpinner.getValue().toString());
                contentStream.endText();

                contentStream.close();

                String pdfFileName = "Confirmare_Plata_" + client.getIdClient() + "_" + factura.getIdFactura() + ".pdf";
                document.save(pdfFileName);
                JOptionPane.showMessageDialog(this, "PDF generat cu succes: " + pdfFileName);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la generarea PDF-ului: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Va rugam sa selectati un client si o factura, si sa bifati 'Plata Inregistrata'.", "Eroare", JOptionPane.ERROR_MESSAGE);
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
            JFrame frame = new JFrame("Inregistrare Plata");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            try {
                frame.setContentPane(new InregistrarePlataFrame(frame));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            frame.setVisible(true);
        });
    }
}

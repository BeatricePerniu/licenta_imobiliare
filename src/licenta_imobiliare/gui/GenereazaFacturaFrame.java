package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ClientDAO;
import licenta_imobiliare.dao.ContractDAO;
import licenta_imobiliare.dao.FacturaDAO;
import licenta_imobiliare.model.Apartament;
import licenta_imobiliare.model.Client;
import licenta_imobiliare.model.Factura;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GenereazaFacturaFrame extends JPanel {
    private JFrame frame;
    private JComboBox<Client> clientComboBox;
    private JComboBox<Apartament> apartamentComboBox;
    private JSpinner dateSpinner;
    private JButton veziFacturaButton;
    private ClientDAO clientDAO;
    private ContractDAO contractDAO;
    private Factura facturaGenerata;

    public GenereazaFacturaFrame(JFrame frame) {
        this.frame = frame;
        clientDAO = new ClientDAO();
        contractDAO = new ContractDAO();

        setLayout(new BorderLayout());

        PanouGradient panouGradient = new PanouGradient();
        panouGradient.setLayout(new BorderLayout());
        add(panouGradient, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        clientComboBox = new JComboBox<>();
        apartamentComboBox = new JComboBox<>();
        incarcaClienti();

        clientComboBox.addActionListener(e -> incarcaApartamente());

        dateSpinner = new JSpinner(new SpinnerDateModel());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Selecteaza Client:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(clientComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Selecteaza Apartament:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(apartamentComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Selecteaza Data:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(dateSpinner, gbc);

        JButton genereazaButton = new JButton("Genereaza Factura");
        genereazaButton.addActionListener(e -> {
            try {
                genereazaFactura();
                incarcaClienti();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(genereazaButton, gbc);

        veziFacturaButton = new JButton("Vezi Factura Generata");
        veziFacturaButton.setEnabled(false);
        veziFacturaButton.addActionListener(e -> afiseazaFactura(facturaGenerata));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(veziFacturaButton, gbc);

        JButton backButton = new JButton("Inapoi");
        backButton.addActionListener(e -> {
            frame.setContentPane(new FacturiGUI(frame));
            frame.invalidate();
            frame.validate();
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(backButton, gbc);

        panouGradient.add(panel, BorderLayout.CENTER);
    }

    private void incarcaClienti() {
        clientComboBox.removeAllItems();
        List<Client> clienti = clientDAO.getClientsWithContracts();
        for (Client client : clienti) {
            clientComboBox.addItem(client);
        }
    }

    private void incarcaApartamente() {
        Client client = (Client) clientComboBox.getSelectedItem();
        if (client != null) {
            List<Apartament> apartamente = contractDAO.getApartmentsForClient(client.getIdClient());
            apartamentComboBox.removeAllItems();
            for (Apartament apartament : apartamente) {
                apartamentComboBox.addItem(apartament);
            }
        }
    }

    private void genereazaFactura() throws SQLException {
        Client client = (Client) clientComboBox.getSelectedItem();
        Apartament apartament = (Apartament) apartamentComboBox.getSelectedItem();
        if (client != null && apartament != null) {
            FacturaDAO facturaDAO = new FacturaDAO();
            Factura factura = new Factura();
            factura.setClient(client);
            factura.setApartament(apartament);
            factura.setPretApartament(apartament.getPret());
            factura.setDataEmiterii((java.util.Date) dateSpinner.getValue());
            factura.setDataScadenta(new java.util.Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)));
            facturaDAO.adaugaFactura(factura);
            this.facturaGenerata = factura;
            veziFacturaButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Factura generata cu succes!");


            genereazaFacturaPDF(factura);
        }
    }



    private void genereazaFacturaPDF(Factura factura) {
        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf");
        File logoFile = new File("C:\\Users\\beatr\\OneDrive\\Desktop\\licenta_imobiliare\\src\\resources\\images\\logomic.png"); // Change this path as needed

        if (!logoFile.exists()) {
            JOptionPane.showMessageDialog(this, "Logo file not found: " + logoFile.getAbsolutePath());
            return;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, fontFile);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            drawInvoiceTemplate(contentStream, document, logoFile);

            contentStream.setFont(font, 10);
            contentStream.setLeading(12.5f);

            // Info Companie
            contentStream.beginText();
            contentStream.newLineAtOffset(72, 750); // Adjusted coordinates
            contentStream.showText("SC SKY REAL ESTATE SRL");
            contentStream.newLine();
            contentStream.showText("Nr. Reg. com.: J40/16212/2017");
            contentStream.newLine();
            contentStream.showText("CIF: 38248640");
            contentStream.newLine();
            contentStream.showText("Adresa: CALEA VITAN 211");
            contentStream.newLine();
            contentStream.showText("Email: SKIA@GMAIL.RO");
            contentStream.newLine();
            contentStream.showText("Tel: 0747037306");
            contentStream.newLine();
            contentStream.showText("Banca: BCR");
            contentStream.newLine();
            contentStream.showText("Cont: RO12BCR1234567890123456");
            contentStream.endText();

            // Info Clienti
            contentStream.beginText();
            contentStream.newLineAtOffset(330, 720);
            contentStream.showText("Client: " + factura.getClient().getNume() + " " + factura.getClient().getPrenume());
            contentStream.newLine();
            contentStream.showText("CNP: " + factura.getClient().getCnp());
            contentStream.newLine();
            contentStream.showText("Adresa: " + factura.getClient().getDomiciliu());
            contentStream.endText();

            // Gestioneaza detalii Factura
            contentStream.beginText();
            contentStream.newLineAtOffset(80, 620);
            contentStream.showText("Factura seria: F Nr: " + factura.getIdFactura() + " Data: " + factura.getDataEmiterii());
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(450, 620);
            contentStream.showText("Cota TVA: 20%");
            contentStream.endText();

            // Deseneaza header tabel
            contentStream.beginText();
            contentStream.newLineAtOffset(80, 551);
            contentStream.showText("Denumire produse sau servicii");
            contentStream.newLineAtOffset(215, 1);
            contentStream.showText("U.M.");
            contentStream.newLineAtOffset(30, 0);
            contentStream.showText("Cant.");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("Pret EUR");
            contentStream.newLineAtOffset(70, 0);
            contentStream.showText("TVA");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("Total");
            contentStream.endText();


            contentStream.beginText();
            contentStream.newLineAtOffset(80, 521);
            contentStream.showText(factura.getApartament().getIdApartament());
            contentStream.newLineAtOffset(215, 1);
            contentStream.showText("buc");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("1");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText(String.valueOf(factura.getApartament().getPret()));
            contentStream.newLineAtOffset(60, 0);
            contentStream.showText(String.valueOf(factura.getApartament().getPret() * 0.2));
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText(String.valueOf(factura.getApartament().getPret() * 1.2));
            contentStream.endText();

            // Tabel Plati
            contentStream.beginText();
            contentStream.newLineAtOffset(352, 140);
            contentStream.showText("Pret fara TVA: " + factura.getApartament().getPret());
            contentStream.newLine();
            contentStream.showText("Valoare TVA: " + (factura.getApartament().getPret() * 0.2));
            contentStream.newLine();
            contentStream.showText("Total de plata: " + (factura.getApartament().getPret() * 1.2));
            contentStream.endText();

            // TVA
            contentStream.beginText();
            contentStream.newLineAtOffset(80, 110);
            contentStream.showText("TVA la incasare:"+(factura.getApartament().getPret() * 0.2));
            contentStream.endText();

            contentStream.close();

            String pdfFileName = "factura_" + factura.getClient().getNume() + "_" + factura.getClient().getPrenume() + ".pdf";
            document.save(pdfFileName);
            System.out.println("PDF salvat cu succes: " + pdfFileName);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare generare PDF: " + e.getMessage());
        }
    }

    private void drawInvoiceTemplate(PDPageContentStream contentStream, PDDocument document, File logoFile) throws IOException {
        contentStream.setLineWidth(1.0f);
        contentStream.setStrokingColor(Color.BLACK);

        PDImageXObject logo = PDImageXObject.createFromFile(logoFile.getAbsolutePath(), document);
        contentStream.drawImage(logo, 430, 750, 85, 60);

        // chenar companie client
        contentStream.addRect(70, 635, 200, 150);
        contentStream.stroke();
        contentStream.addRect(320, 680, 200, 60);
        contentStream.stroke();

        // chenar detalii factura
        contentStream.addRect(70, 605, 450, 30);
        contentStream.stroke();

        // chenar header tabel
        contentStream.addRect(70, 550, 450, 30);
        contentStream.stroke();

        // linii tabel
        int numberOfRows = 4;
        for (int i = 0; i < numberOfRows; i++) {
            contentStream.addRect(70, 520 - (i * 30), 450, 30);
            contentStream.stroke();
        }

        // chenar plati
        contentStream.addRect(350, 100, 160, 60);
        contentStream.stroke();
        // chenar tva
        contentStream.addRect(70, 100, 150, 30);
        contentStream.stroke();
    }



    private void afiseazaFactura(Factura factura) {
        if (factura != null) {
            JFrame facturaFrame = new JFrame("Factura Generata");
            facturaFrame.setSize(400, 300);
            facturaFrame.setLocationRelativeTo(null);

            JTextArea facturaTextArea = new JTextArea(10, 30);
            facturaTextArea.setText(factura.genereazaTextFactura());
            facturaTextArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(facturaTextArea);
            facturaFrame.add(scrollPane, BorderLayout.CENTER);

            facturaFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Nu exista factura generata de afisat.", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Gradient background panel
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

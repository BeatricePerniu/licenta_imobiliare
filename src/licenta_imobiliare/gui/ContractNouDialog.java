package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ClientDAO;
import licenta_imobiliare.dao.ApartamentDAO;
import licenta_imobiliare.dao.ProiectDAO;
import licenta_imobiliare.dao.ContractDAO;
import licenta_imobiliare.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class ContractNouDialog extends JDialog {
    private JComboBox<Client> clientComboBox;
    private JComboBox<Apartament> apartamentComboBox;
    private JComboBox<Proiect> proiectComboBox;
    private JComboBox<String> tipContractComboBox;
    private JButton genereazaButton;
    private JButton inapoiButton;
    private JButton exportaButton;
    private ContractDAO contractDAO;

    public ContractNouDialog(JFrame parent) {
        super(parent, "Contract Nou", true);
        contractDAO = new ContractDAO();
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(null);


        JLabel logoLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("resources/images/logomic.png")));
        logoLabel.setBounds(10, 10, 100, 50);
        mainPanel.add(logoLabel);


        JLabel clientLabel = new JLabel("Selectează clientul");
        clientLabel.setBounds(100, 150, 200, 30);
        mainPanel.add(clientLabel);

        clientComboBox = new JComboBox<>();
        clientComboBox.setBounds(100, 180, 200, 30);
        mainPanel.add(clientComboBox);


        JLabel apartamentLabel = new JLabel("Selectează apartamentul");
        apartamentLabel.setBounds(100, 220, 200, 30);
        mainPanel.add(apartamentLabel);

        apartamentComboBox = new JComboBox<>();
        apartamentComboBox.setBounds(100, 250, 200, 30);
        mainPanel.add(apartamentComboBox);


        JLabel proiectLabel = new JLabel("Selectează proiectul");
        proiectLabel.setBounds(100, 290, 200, 30);
        mainPanel.add(proiectLabel);

        proiectComboBox = new JComboBox<>();
        proiectComboBox.setBounds(100, 320, 200, 30);
        mainPanel.add(proiectComboBox);


        JLabel tipContractLabel = new JLabel("Selectează tipul de contract (vânzare/închiriere)");
        tipContractLabel.setBounds(400, 150, 300, 30);
        mainPanel.add(tipContractLabel);

        tipContractComboBox = new JComboBox<>(new String[]{"Închiriere", "Vânzare"});
        tipContractComboBox.setBounds(400, 180, 200, 30);
        mainPanel.add(tipContractComboBox);


        genereazaButton = new JButton("Generează Contract");
        genereazaButton.setBounds(300, 400, 200, 50);
        mainPanel.add(genereazaButton);

        genereazaButton.addActionListener(e -> genereazaContract());


        exportaButton = new JButton("Exportă Contract");
        exportaButton.setBounds(300, 460, 200, 50);
        mainPanel.add(exportaButton);

        exportaButton.addActionListener(e -> exportaContract());


        inapoiButton = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        inapoiButton.setContentAreaFilled(false);
        inapoiButton.setBorderPainted(false);
        inapoiButton.setBounds(10, 520, 50, 50);
        inapoiButton.addActionListener(e -> {
            parent.setContentPane(new DocumenteGUI(parent));
            parent.invalidate();
            parent.validate();
            dispose();
        });
        mainPanel.add(inapoiButton);


        incarcaDateClientSiApartament();

        tipContractComboBox.addActionListener(e -> filtreazaApartamente());
        proiectComboBox.addActionListener(e -> filtreazaApartamente());

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void incarcaDateClientSiApartament() {
        ClientDAO clientDAO = new ClientDAO();
        ApartamentDAO apartamentDAO = new ApartamentDAO();
        ProiectDAO proiectDAO = new ProiectDAO();

        List<Client> clienti = clientDAO.getAllClients();
        List<Proiect> proiecte = proiectDAO.getToateProiectele();

        clientComboBox.removeAllItems();
        for (Client client : clienti) {
            clientComboBox.addItem(client);
        }

        proiectComboBox.removeAllItems();
        for (Proiect proiect : proiecte) {
            proiectComboBox.addItem(proiect);
        }

        filtreazaApartamente();
    }

    private void filtreazaApartamente() {
        String tipContract = (String) tipContractComboBox.getSelectedItem();
        Proiect proiectSelectat = (Proiect) proiectComboBox.getSelectedItem();
        apartamentComboBox.removeAllItems();

        if (proiectSelectat == null) return;

        ApartamentDAO apartamentDAO = new ApartamentDAO();
        List<Apartament> apartamente = apartamentDAO.getApartamenteByProiect(proiectSelectat.getIdProiect());

        for (Apartament apartament : apartamente) {
            if ("Închiriere".equals(tipContract) && apartament.isChirie()) {
                apartamentComboBox.addItem(apartament);
            } else if ("Vânzare".equals(tipContract) && !apartament.isChirie()) {
                apartamentComboBox.addItem(apartament);
            }
        }
    }

    private void genereazaContract() {
        Client client = (Client) clientComboBox.getSelectedItem();
        Apartament apartament = (Apartament) apartamentComboBox.getSelectedItem();
        Proiect proiect = (Proiect) proiectComboBox.getSelectedItem();
        String tipContract = (String) tipContractComboBox.getSelectedItem();

        if (client == null || apartament == null || proiect == null || tipContract == null) {
            JOptionPane.showMessageDialog(this, "Toate câmpurile trebuie completate.");
            return;
        }

        Contract contract;
        if ("Închiriere".equals(tipContract)) {
            contract = new ContractDeInchiriere(null, new java.util.Date(), client, apartament, apartament.getPret(), proiect);
        } else {
            contract = new ContractDeVanzare(null, new java.util.Date(), client, apartament, apartament.getPret(), proiect);
        }


        contractDAO.adaugaContract(contract);

        JTextArea contractTextArea = new JTextArea(contract.genereazaTextContract());
        contractTextArea.setWrapStyleWord(true);
        contractTextArea.setLineWrap(true);
        contractTextArea.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(contractTextArea), "Contract Generat", JOptionPane.INFORMATION_MESSAGE);
    }


    private void exportaContract() {
        Client client = (Client) clientComboBox.getSelectedItem();
        Apartament apartament = (Apartament) apartamentComboBox.getSelectedItem();
        Proiect proiect = (Proiect) proiectComboBox.getSelectedItem();
        String tipContract = (String) tipContractComboBox.getSelectedItem();

        if (client == null || apartament == null || proiect == null || tipContract == null) {
            JOptionPane.showMessageDialog(this, "Toate câmpurile trebuie completate.");
            return;
        }

        Contract contract;
        if ("Închiriere".equals(tipContract)) {
            contract = new ContractDeInchiriere(null, new Date(), client, apartament, apartament.getPret(), proiect);
        } else {
            contract = new ContractDeVanzare(null, new Date(), client, apartament, apartament.getPret(), proiect);
        }

        try {

            String fileName = "contract_" + apartament.getIdApartament() + ".pdf";
            exportaContractToPDF(contract, fileName);
            JOptionPane.showMessageDialog(this, "Contractul a fost exportat cu succes.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "A apărut o eroare la exportarea contractului: " + e.getMessage());
        }
    }

    private void exportaContractToPDF(Contract contract, String fileName) throws IOException {
        String contractText = contract.genereazaTextContract();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);


            File fontFileRegular = new File("C:\\Windows\\Fonts\\ARIAL.TTF");
            PDType0Font fontRegular = PDType0Font.load(document, fontFileRegular);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            addHeader(contentStream, document, fontRegular);
            contentStream.setFont(fontRegular, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(25, 700);


            String[] lines = contractText.split("\n");
            float yPosition = 700;
            float leading = 14.5f;

            for (String line : lines) {
                if (yPosition < 50) {
                    contentStream.endText();
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(fontRegular, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(25, 700);
                    yPosition = 700;
                }
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
                yPosition -= leading;
            }

            contentStream.endText();
            contentStream.close();

            document.save(fileName);
            System.out.println("PDF saved successfully to: " + fileName);
        }
    }

    private static void addHeader(PDPageContentStream contentStream, PDDocument document, PDType0Font font) throws IOException {

        URL logoUrl = ContractNouDialog.class.getClassLoader().getResource("resources/images/logomic.png");
        BufferedImage bufferedImage = ImageIO.read(logoUrl);
        PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
        contentStream.drawImage(pdImage, 50, 750, 70, 70);


        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.newLineAtOffset(130, 780);
        contentStream.showText("SKY REAL ESTATE SRL");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Calea Floreasca, Nr. 211, Bucuresti");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("office@sky.ro");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("J40/12345/2021");
        contentStream.endText();
    }

    private static void addContractDetails(PDPageContentStream contentStream, PDType0Font font, Contract contract) throws IOException {
        contentStream.setFont(font, 12);
        contentStream.setLeading(14.5f);
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 700);

        contentStream.showText("ID Contract: " + contract.getIdContract());
        contentStream.newLine();
        contentStream.showText("Data: " + contract.getDataInceput());
        contentStream.newLine();
        contentStream.showText("Client: " + contract.getClient().getNume());
        contentStream.newLine();
        contentStream.showText("Apartament: " + contract.getApartament().getIdApartament());
        contentStream.newLine();
        contentStream.showText("Proiect: " + contract.getProiect().getNumeProiect());
        contentStream.newLine();

        if (contract instanceof ContractDeInchiriere) {
            contentStream.showText("Data Început: " + ((ContractDeInchiriere) contract).getDataInceput());
            contentStream.newLine();
        }


        Apartament apartament = contract.getApartament();
        contentStream.showText("Preț: " + apartament.getPret());
        contentStream.newLine();

        contentStream.endText();
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

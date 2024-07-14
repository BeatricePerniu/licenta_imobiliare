package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ContractDAO;
import licenta_imobiliare.model.Apartament;
import licenta_imobiliare.model.Contract;
import licenta_imobiliare.model.ContractDeInchiriere;
import licenta_imobiliare.model.ContractDeVanzare;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.awt.image.BufferedImage;

public class ContracteExistenteFrame extends JFrame {
    private JTable contractTable;
    private ContractTabelModel contractTableModel;
    private JButton inapoiButton;
    private JLabel logoLabel;

    public ContracteExistenteFrame(JFrame parent) {
        super("Contracte Existente");
        setLayout(new BorderLayout());
        if (parent != null) {
            setSize(parent.getSize());
            setLocationRelativeTo(parent);
        } else {
            setSize(800, 600);
            setLocationRelativeTo(null);
        }

        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());


        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Align to the left
        logoPanel.setOpaque(false);
        logoLabel = new JLabel();
        logoPanel.add(logoLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        contractTableModel = new ContractTabelModel();
        contractTable = new JTable(contractTableModel);
        contractTable.setOpaque(false);
        ((DefaultTableCellRenderer) contractTable.getDefaultRenderer(Object.class)).setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(contractTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        inapoiButton = new JButton();
        inapoiButton.setContentAreaFilled(false);
        inapoiButton.setBorderPainted(false);
        inapoiButton.setFocusPainted(false);  // Eliminăm focusul butonului
        inapoiButton.addActionListener(e -> {
            if (parent != null) {
                parent.setVisible(true);
            }
            dispose();
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(inapoiButton);
        mainPanel.add(backButtonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        refreshContractTable();


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });


        redimensioneazaComponente();

        setVisible(true);


        TableColumnModel columnModel = contractTable.getColumnModel();
        columnModel.getColumn(5).setCellRenderer(new ButtonRenderer());
        columnModel.getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void refreshContractTable() {
        List<Contract> contracte = new ContractDAO().getToateContractele();
        contractTableModel.setContracte(contracte);
    }

    private void redimensioneazaComponente() {
        int width = getWidth();
        int height = getHeight();


        int logoWidth = width / 5;
        int logoHeight = height / 10;
        logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));

        int buttonSize = Math.min(width / 20, height / 20);
        inapoiButton.setIcon(incarcaIconita("resources/images/back.png", buttonSize, buttonSize));
    }

    private ImageIcon incarcaIconita(String cale, int latime, int inaltime) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(cale));
        if (icon.getImage() != null && latime > 0 && inaltime > 0) {
            Image img = icon.getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
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

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int selectedRow = contractTable.getSelectedRow();
                Contract contract = contractTableModel.getContracte().get(selectedRow);
                try {
                    String fileName = "contract_" + contract.getApartament().getIdApartament();
                    if (contract.getProiect() != null && contract.getProiect().getNumeProiect() != null && !contract.getProiect().getNumeProiect().isEmpty()) {
                        fileName += "_" + contract.getProiect().getNumeProiect();
                    }
                    fileName += ".pdf";
                    exportaContractToPDF(contract, fileName);
                    JOptionPane.showMessageDialog(null, "PDF generated successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error generating PDF: " + e.getMessage());
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    private void exportaContractToPDF(Contract contract, String fileName) throws IOException {
        String contractText = contract.genereazaTextContract(); // Obțineți textul din contract

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
            System.out.println("PDF salvat cu seucces: " + fileName);
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
        if (contract.getProiect() != null) {
            contentStream.showText("Proiect: " + contract.getProiect().getNumeProiect());
        } else {
            contentStream.showText("Proiect: N/A");
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContracteExistenteFrame(null).setVisible(true));
    }
}

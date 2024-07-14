package licenta_imobiliare.gui;

import licenta_imobiliare.dao.FacturaDAO;
import licenta_imobiliare.model.Factura;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RaportFacturiFrame extends JPanel {
    private JFrame frame;
    private JTable facturiTable;
    private JComboBox<String> monthFilterComboBox;
    private JComboBox<String> yearFilterComboBox;
    private JTextField searchTextField;
    private FacturaDAO facturaDAO;
    private JLabel logoLabel;
    private JButton backButton;
    private JButton pdfButton;

    public RaportFacturiFrame(JFrame frame) throws SQLException {
        this.frame = frame;
        facturaDAO = new FacturaDAO();

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

        JPanel filterPanel = new JPanel();
        filterPanel.setOpaque(false);
        filterPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        monthFilterComboBox = new JComboBox<>(new String[]{"All", "Ultima luna", "Ultimile 3 luni", "Ultimile 6 luni"});
        monthFilterComboBox.addActionListener(this::onFilterChanged);

        yearFilterComboBox = new JComboBox<>(new String[]{"All", "2024", "2023", "2022", "2021", "2020", "2019"});
        yearFilterComboBox.addActionListener(this::onFilterChanged);

        searchTextField = new JTextField(10);
        searchTextField.addActionListener(this::onFilterChanged);

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Selecteaza luna:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        filterPanel.add(monthFilterComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(new JLabel("Selecteaza anul:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        filterPanel.add(yearFilterComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        filterPanel.add(new JLabel("Cauta ID Factura:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        filterPanel.add(searchTextField, gbc);

        panouGradient.add(filterPanel, BorderLayout.NORTH);

        facturiTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(facturiTable);
        panouGradient.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        backButton = new JButton();
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            frame.setContentPane(new FacturiGUI(frame));
            frame.invalidate();
            frame.validate();
        });

        pdfButton = new JButton();
        pdfButton.setContentAreaFilled(false);
        pdfButton.setBorderPainted(false);
        pdfButton.addActionListener(e -> genereazaRaportPDF());

        buttonPanel.add(backButton);
        buttonPanel.add(pdfButton);
        panouGradient.add(buttonPanel, BorderLayout.SOUTH);

        add(panouGradient, BorderLayout.CENTER);

        incarcaFacturi();


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();
    }

    private void onFilterChanged(ActionEvent e) {
        incarcaFacturi();
    }

    private void incarcaFacturi() {
        String selectedMonthFilter = (String) monthFilterComboBox.getSelectedItem();
        String selectedYearFilter = (String) yearFilterComboBox.getSelectedItem();
        String searchID = searchTextField.getText().trim();
        List<Factura> facturi;

        if (!searchID.isEmpty()) {
            facturi = facturaDAO.getFacturiById(searchID); // ar trebui să returneze o listă de facturi
        } else if (!"All".equals(selectedMonthFilter) || !"All".equals(selectedYearFilter)) {
            Calendar cal = Calendar.getInstance();
            java.util.Date endDate = cal.getTime();
            java.util.Date startDate = null;

            if (!"All".equals(selectedMonthFilter)) {
                int monthsOffset = getMonthOffset(selectedMonthFilter);
                cal.add(Calendar.MONTH, -monthsOffset);
                startDate = cal.getTime();
            }

            if (!"All".equals(selectedYearFilter)) {
                int year = Integer.parseInt(selectedYearFilter);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.DAY_OF_YEAR, 1);
                java.util.Date yearStartDate = cal.getTime();
                cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
                java.util.Date yearEndDate = cal.getTime();

                if (startDate == null || startDate.before(yearStartDate)) {
                    startDate = yearStartDate;
                }
                if (endDate.after(yearEndDate)) {
                    endDate = yearEndDate;
                }
            }

            facturi = facturaDAO.getFacturiByDateRange(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
        } else {
            facturi = facturaDAO.getToateFacturile();
        }

        String[] columnNames = {"ID Factura", "Data Emiterii", "Client", "Apartament", "Pret", "TVA", "Total", "Descarca"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Factura factura : facturi) {
            Object[] rowData = {
                    factura.getIdFactura(),
                    factura.getDataEmiterii(),
                    factura.getClient() != null ? factura.getClient().getNume() : "N/A",
                    factura.getApartament() != null ? factura.getApartament().getIdApartament() : "N/A",
                    factura.getPretApartament(),
                    factura.getTva(),
                    factura.getPretApartament() + factura.getTva(),
                    "Descarca"
            };
            model.addRow(rowData);
        }

        facturiTable.setModel(model);

        facturiTable.getColumn("Descarca").setCellRenderer(new ButtonRenderer());
        facturiTable.getColumn("Descarca").setCellEditor(new ButtonEditor(new JCheckBox(), this));
    }

    private int getMonthOffset(String filter) {
        switch (filter) {
            case "Ultima luna":
                return 1;
            case "Ultimile 3 luni":
                return 3;
            case "Ultimile 6 luni":
                return 6;
            default:
                return -1;
        }
    }

    private void redimensioneazaComponente() {
        int width = frame.getWidth();
        int height = frame.getHeight();

        if (width > 0 && height > 0) {
            int logoWidth = width / 5;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));

            int buttonSize = Math.min(width / 20, height / 20);
            backButton.setIcon(incarcaIconita("resources/images/back.png", buttonSize, buttonSize));
            pdfButton.setIcon(incarcaIconita("resources/images/pdf.png", buttonSize, buttonSize));
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

    private void genereazaFacturaPDF(Factura factura) {
        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf");
        File logoFile = new File("C:\\Users\\beatr\\OneDrive\\Desktop\\licenta_imobiliare\\src\\resources\\images\\logomic.png"); // Change this path as needed

        if (!logoFile.exists()) {
            JOptionPane.showMessageDialog(this, "Nu se gaseste logo: " + logoFile.getAbsolutePath());
            return;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, fontFile);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);


            drawInvoiceTemplate(contentStream, document, logoFile);


            contentStream.setFont(font, 10);
            contentStream.setLeading(14.5f);

            contentStream.beginText();
            contentStream.newLineAtOffset(60, 720);
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
            contentStream.showText("Banca: BT");
            contentStream.newLine();
            contentStream.showText("Cont: RO28BTRL0000222111111");
            contentStream.endText();


            contentStream.beginText();
            contentStream.newLineAtOffset(330, 700);
            contentStream.showText("Client: " + factura.getClient().getNume() + " " + factura.getClient().getPrenume());
            contentStream.newLine();
            contentStream.showText("CNP: " + factura.getClient().getCnp());
            contentStream.newLine();
            contentStream.showText("Adresa: " + factura.getClient().getDomiciliu());
            contentStream.endText();


            contentStream.beginText();
            contentStream.newLineAtOffset(60, 570);
            contentStream.showText("Factura seria: F Nr: " + factura.getIdFactura() + " Data: " + new SimpleDateFormat("dd.MM.yyyy").format(factura.getDataEmiterii()));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(450, 570);
            contentStream.showText("Cota TVA: 20%");
            contentStream.endText();


            contentStream.beginText();
            contentStream.newLineAtOffset(60, 510);
            contentStream.showText("Denumire produse sau servicii");
            contentStream.newLineAtOffset(270, 0);
            contentStream.showText("U.M.");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("Cant.");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("Pret MII EUR");
            contentStream.newLineAtOffset(70, 0);
            contentStream.showText("TVA");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("Total");
            contentStream.endText();


            double pretApartament = factura.getPretApartament();
            double valoareTVA = pretApartament * 0.2;
            double totalDePlata = pretApartament + valoareTVA;


            contentStream.beginText();
            contentStream.newLineAtOffset(60, 480);
            contentStream.showText(factura.getApartament().getIdApartament());
            contentStream.newLineAtOffset(270, 0);
            contentStream.showText("buc");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("1");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText(String.valueOf(pretApartament));
            contentStream.newLineAtOffset(70, 0);
            contentStream.showText(String.format("%.2f", valoareTVA));
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText(String.format("%.2f", totalDePlata));
            contentStream.endText();


            contentStream.beginText();
            contentStream.newLineAtOffset(330, 150);
            contentStream.showText("Pret fara TVA: " + String.format("%.2f", pretApartament));
            contentStream.newLine();
            contentStream.showText("Valoare TVA: " + String.format("%.2f", valoareTVA));
            contentStream.newLine();
            contentStream.showText("Total de plata: " + String.format("%.2f", totalDePlata));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(60, 130);
            contentStream.showText("TVA la incasare.");
            contentStream.endText();

            contentStream.close();

            String pdfFileName = "factura_" + factura.getClient().getNume() + "_" + factura.getClient().getPrenume() + ".pdf";
            document.save(pdfFileName);
            System.out.println("PDF salvat: " + pdfFileName);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare PDF: " + e.getMessage());
        }
    }


    private void drawInvoiceTemplate(PDPageContentStream contentStream, PDDocument document, File logoFile) throws IOException {

        contentStream.setLineWidth(1.0f);
        contentStream.setStrokingColor(Color.BLACK);


        PDImageXObject logo = PDImageXObject.createFromFile(logoFile.getAbsolutePath(), document);
        contentStream.drawImage(logo, 50, 750, 60, 60); //


        contentStream.addRect(50, 600, 250, 150); // dreptunghi companie
        contentStream.stroke();
        contentStream.addRect(320, 620, 250, 100); // dreptunghi client
        contentStream.stroke();


        contentStream.addRect(50, 560, 520, 40); // dreptunghi  plata
        contentStream.stroke();


        contentStream.addRect(50, 500, 520, 30); // Tabel dreptunghi
        contentStream.stroke();


        int numberOfRows = 1;
        for (int i = 0; i < numberOfRows; i++) {
            contentStream.addRect(50, 470 - (i * 30), 520, 30);
        }

        // Draw footer rectangles
        contentStream.addRect(320, 120, 250, 60);
        contentStream.stroke();

        contentStream.addRect(50, 90, 200, 20);
        contentStream.stroke();
    }


    private void genereazaRaportPDF() {
        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf");
        File logoFile = new File("C:\\Users\\beatr\\OneDrive\\Desktop\\licenta_imobiliare\\src\\resources\\images\\logomic.png"); // Adjust path as necessary

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())); // Landscape orientation
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, fontFile);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);


            PDImageXObject logo = PDImageXObject.createFromFileByExtension(logoFile, document);
            contentStream.drawImage(logo, 50, 550, 60, 60);


            contentStream.beginText();
            contentStream.setFont(font, 16);
            contentStream.newLineAtOffset(320, 580);
            contentStream.showText("RAPORT FACTURI");
            contentStream.endText();


            contentStream.setFont(font, 12);
            contentStream.setLineWidth(1.0f);
            contentStream.setStrokingColor(Color.BLACK);

            float margin = 50;
            float yStart = 500;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float rowHeight = 20f;

            String[] columns = {"ID Factura", "Data Emiterii", "Client", "Apartament", "Pret", "TVA", "Total"};
            float[] columnWidths = {120, 100, 120, 150, 100, 70, 100};


            contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
            contentStream.addRect(margin, yPosition - rowHeight, tableWidth, rowHeight);
            contentStream.fill();
            contentStream.setNonStrokingColor(Color.BLACK);

            yPosition -= rowHeight;
            for (int i = 0; i < columns.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + i * columnWidths[i] + 5, yPosition + 5);
                contentStream.showText(columns[i]);
                contentStream.endText();
            }

            yPosition -= rowHeight;


            DefaultTableModel model = (DefaultTableModel) facturiTable.getModel();
            contentStream.setFont(font, 10);

            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < columns.length; col++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + col * columnWidths[col] + 5, yPosition + 5);
                    contentStream.showText(model.getValueAt(row, col).toString());
                    contentStream.endText();
                }
                yPosition -= rowHeight;
            }

            contentStream.close();

            String pdfFileName = "Raport_Facturi.pdf";
            document.save(pdfFileName);
            JOptionPane.showMessageDialog(this, "PDF generat cu succes: " + pdfFileName);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la generarea PDF-ului: " + e.getMessage());
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
        protected JButton button;
        private String label;
        private boolean isPushed;
        private RaportFacturiFrame parent;

        public ButtonEditor(JCheckBox checkBox, RaportFacturiFrame parent) {
            super(checkBox);
            this.parent = parent;
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

                int row = facturiTable.getSelectedRow();
                String idFactura = facturiTable.getValueAt(row, 0).toString();
                Factura factura = null;
                try {
                    factura = facturaDAO.getFacturaById(idFactura);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                parent.genereazaFacturaPDF(factura);
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
            JFrame frame = new JFrame("Raport Facturi");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            try {
                frame.setContentPane(new RaportFacturiFrame(frame));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            frame.setVisible(true);
        });
    }

}

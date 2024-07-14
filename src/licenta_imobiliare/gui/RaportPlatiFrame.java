package licenta_imobiliare.gui;

import licenta_imobiliare.dao.PlataDAO;
import licenta_imobiliare.model.Plata;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class RaportPlatiFrame extends JPanel {
    private JFrame frame;
    private JTable platiTable;
    private JComboBox<String> monthFilterComboBox;
    private JComboBox<String> yearFilterComboBox;
    private JTextField searchTextField;
    private JLabel totalLabel;
    private PlataDAO plataDAO;
    private JLabel logoLabel;
    private JButton backButton;
    private JButton pdfButton;

    public RaportPlatiFrame(JFrame frame) {
        this.frame = frame;
        plataDAO = new PlataDAO();

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

        // Main panel with filters and table
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        monthFilterComboBox = new JComboBox<>(new String[]{"Toate", "Ultima luna", "Ultimile 3 luni", "Ultimile 6 luni"});
        monthFilterComboBox.addActionListener(this::onFilterChanged);

        yearFilterComboBox = new JComboBox<>(new String[]{"Toate", "2024", "2023", "2022", "2021", "2020", "2019"});
        yearFilterComboBox.addActionListener(this::onFilterChanged);

        searchTextField = new JTextField(10);
        searchTextField.addActionListener(this::onFilterChanged);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Selecteaza luna:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(monthFilterComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Selecteaza anul:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(yearFilterComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Cauta ID Plata:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(searchTextField, gbc);

        panouGradient.add(panel, BorderLayout.NORTH);

        platiTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(platiTable);
        panouGradient.add(scrollPane, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        totalLabel = new JLabel("Total incasat MII EURO: 0");
        totalPanel.add(totalLabel, BorderLayout.WEST);

        backButton = new JButton();
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            frame.setContentPane(new PlatiFrame(frame));
            frame.invalidate();
            frame.validate();
        });

        pdfButton = new JButton();
        pdfButton.setContentAreaFilled(false);
        pdfButton.setBorderPainted(false);
        pdfButton.addActionListener(e -> genereazaRaportPDF());

        totalPanel.add(backButton, BorderLayout.EAST);
        totalPanel.add(pdfButton, BorderLayout.CENTER);
        panouGradient.add(totalPanel, BorderLayout.SOUTH);

        add(panouGradient, BorderLayout.CENTER);

        incarcaPlati();


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();
    }

    private void onFilterChanged(ActionEvent e) {
        incarcaPlati();
    }

    private void incarcaPlati() {
        String selectedMonthFilter = (String) monthFilterComboBox.getSelectedItem();
        String selectedYearFilter = (String) yearFilterComboBox.getSelectedItem();
        String searchID = searchTextField.getText().trim();
        List<Plata> plati;

        if (!searchID.isEmpty()) {
            plati = plataDAO.getPlataById(searchID);
        } else if (!"Toate".equals(selectedMonthFilter) || !"Toate".equals(selectedYearFilter)) {
            Calendar cal = Calendar.getInstance();
            java.util.Date endDate = cal.getTime();
            java.util.Date startDate = null;

            if (!"Toate".equals(selectedMonthFilter)) {
                int monthsOffset = getMonthOffset(selectedMonthFilter);
                cal.add(Calendar.MONTH, -monthsOffset);
                startDate = cal.getTime();
            }

            if (!"Toate".equals(selectedYearFilter)) {
                int year = Integer.parseInt(selectedYearFilter);
                cal.setTime(endDate);
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

            if (startDate == null) {
                cal.set(Calendar.DAY_OF_YEAR, 1);
                startDate = cal.getTime();
            }

            plati = plataDAO.getPlatiByDateRange(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
        } else {
            plati = plataDAO.getToatePlatile();
        }

        String[] columnNames = {"ID Plata", "Suma", "Data", "Client", "Factura"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        double totalAmount = 0.0;
        for (Plata plata : plati) {
            Object[] rowData = {
                    plata.getIdPlata(),
                    plata.getSuma(),
                    plata.getData(),
                    plata.getClient() != null ? plata.getClient().getNume() : "N/A",
                    plata.getFactura() != null ? plata.getFactura().getIdFactura() : "N/A"
            };
            model.addRow(rowData);
            totalAmount += plata.getSuma();
        }

        platiTable.setModel(model);
        totalLabel.setText("Total incasat: " + totalAmount);
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
        int width = getWidth();
        int height = getHeight();

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
            System.err.println("Nu s-a gasit fiserul: " + cale);
        }
        return null;
    }

    private void genereazaRaportPDF() {
        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf"); // Adjust path as necessary

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())); // Landscape orientation
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, fontFile);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add logo
            File logoFile = new File("C:\\Users\\beatr\\OneDrive\\Desktop\\licenta_imobiliare\\src\\resources\\images\\logomic.png"); // Adjust path as necessary
            PDImageXObject logo = PDImageXObject.createFromFileByExtension(logoFile, document);
            contentStream.drawImage(logo, 50, 500, 100, 100);

            contentStream.beginText();
            contentStream.setFont(font, 18);
            contentStream.newLineAtOffset(200, 550);
            contentStream.showText("Raport Plati");
            contentStream.endText();

            // factabel
            contentStream.setLineWidth(1.0f);
            contentStream.setStrokingColor(Color.BLACK);

            float margin = 50;
            float yStart = 450;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            String[] columns = {"ID Plata", "Suma", "Data", "Client", "Factura"};
            float[] columnWidths = {100, 100, 100, 100, 100};

            // inserez coloane
            for (int i = 0; i < columns.length; i++) {
                contentStream.beginText();
                contentStream.setFont(font, 10);
                contentStream.newLineAtOffset(margin + i * columnWidths[i], yPosition);
                contentStream.showText(columns[i]);
                contentStream.endText();
            }

            yPosition -= 15;

            //inserez randuri
            DefaultTableModel model = (DefaultTableModel) platiTable.getModel();
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    contentStream.beginText();
                    contentStream.setFont(font, 10);
                    contentStream.newLineAtOffset(margin + col * columnWidths[col], yPosition);
                    contentStream.showText(model.getValueAt(row, col).toString());
                    contentStream.endText();
                }
                yPosition -= 15;
            }

            // Add total amount
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(margin, yPosition - 30);
            contentStream.showText(totalLabel.getText());
            contentStream.endText();

            contentStream.close();

            String pdfFileName = "Raport_Plati.pdf";
            document.save(pdfFileName);
            JOptionPane.showMessageDialog(this, "PDF generat cu succes: " + pdfFileName);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la generarea PDF-ului: " + e.getMessage());
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
            JFrame frame = new JFrame("Raport Plati");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new RaportPlatiFrame(frame));
            frame.setVisible(true);
        });
    }
}

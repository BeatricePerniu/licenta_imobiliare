package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ApartamentDAO;
import licenta_imobiliare.dao.ProiectDAO;
import licenta_imobiliare.model.Apartament;
import licenta_imobiliare.model.Proiect;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class DisponibilitateApartamenteInchiriereGUI extends JPanel {
    private JFrame frame;
    private JComboBox<Proiect> proiecteComboBox;
    private JComboBox<Integer> camereComboBox;
    private JComboBox<String> sortarePretComboBox;
    private JTable apartamenteTable;
    private JLabel logoLabel;

    public DisponibilitateApartamenteInchiriereGUI(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());


        PanouGradient mainPanel = new PanouGradient();
        mainPanel.setLayout(new BorderLayout());

        logoLabel = new JLabel();
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        JPanel filtrePanel = new JPanel(new GridBagLayout());
        filtrePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        JLabel proiecteLabel = new JLabel("Selectează Proiectul:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        filtrePanel.add(proiecteLabel, gbc);

        proiecteComboBox = new JComboBox<>();
        incarcaProiecte();
        proiecteComboBox.addActionListener(e -> incarcaApartamente());
        gbc.gridx = 1;
        gbc.gridy = 0;
        filtrePanel.add(proiecteComboBox, gbc);


        JButton adaugaProiectButton = creeazaButon("Adaugă Proiect Nou", "resources/images/plus.png", 30, 30);
        adaugaProiectButton.setForeground(Color.BLACK);

        adaugaProiectButton.addActionListener(e -> {
            AdaugaProiectDialog adaugaProiectDialog = new AdaugaProiectDialog(frame);
            adaugaProiectDialog.setVisible(true);
            incarcaProiecte();
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        filtrePanel.add(adaugaProiectButton, gbc);


        JButton adaugaApartamentButton = creeazaButon("Adaugă Apartament", "resources/images/plus.png", 30, 30);
        adaugaApartamentButton.setForeground(Color.BLACK);

        adaugaApartamentButton.addActionListener(e -> {
            new AdaugaApartamentDialog(frame, this::incarcaApartamente).setVisible(true);
        });
        gbc.gridx = 3;
        gbc.gridy = 0;
        filtrePanel.add(adaugaApartamentButton, gbc);


        JLabel camereLabel = new JLabel("Selectează Numărul de Camere:");
        gbc.gridx = 4;
        gbc.gridy = 0;
        filtrePanel.add(camereLabel, gbc);

        camereComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5});
        camereComboBox.addActionListener(e -> incarcaApartamente());
        gbc.gridx = 5;
        gbc.gridy = 0;
        filtrePanel.add(camereComboBox, gbc);


        JLabel sortarePretLabel = new JLabel("Sortează după Preț:");
        gbc.gridx = 6;
        gbc.gridy = 0;
        filtrePanel.add(sortarePretLabel, gbc);

        sortarePretComboBox = new JComboBox<>(new String[]{"Nesortat", "Crescător", "Descrescător"});
        sortarePretComboBox.addActionListener(e -> incarcaApartamente());
        gbc.gridx = 7;
        gbc.gridy = 0;
        filtrePanel.add(sortarePretComboBox, gbc);

        mainPanel.add(filtrePanel, BorderLayout.CENTER);


        apartamenteTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(apartamenteTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);


        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);


        JButton butonInapoi = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH))); // Scale the image
        butonInapoi.setContentAreaFilled(false);
        butonInapoi.setBorderPainted(false);
        butonInapoi.addActionListener(e -> {
            frame.setContentPane(new ProiecteGUI(frame));
            frame.invalidate();
            frame.validate();
        });
        JPanel inapoiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inapoiPanel.setOpaque(false);
        inapoiPanel.add(butonInapoi);


        JButton exportPDFButton = creeazaButon("Export PDF", "resources/images/pdf.png", 30, 30);
        exportPDFButton.addActionListener(e -> exportToPDF());
        gbc.gridx = 8;
        gbc.gridy = 0;
        filtrePanel.add(exportPDFButton, gbc);


        JPanel adaugaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adaugaPanel.setOpaque(false);
        adaugaPanel.add(adaugaProiectButton);
        adaugaPanel.add(adaugaApartamentButton);

        buttonPanel.add(inapoiPanel, BorderLayout.WEST);
        buttonPanel.add(adaugaPanel, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente(logoLabel, butonInapoi, adaugaProiectButton, adaugaApartamentButton, exportPDFButton);
            }
        });

        redimensioneazaComponente(logoLabel, butonInapoi, adaugaProiectButton, adaugaApartamentButton, exportPDFButton);

        incarcaApartamente();
    }

    private void incarcaProiecte() {
        ProiectDAO proiectDAO = new ProiectDAO();
        List<Proiect> proiecte = proiectDAO.getToateProiectele();
        proiecteComboBox.removeAllItems();
        for (Proiect proiect : proiecte) {
            proiecteComboBox.addItem(proiect);
        }
        proiecteComboBox.setSelectedIndex(-1);
    }

    private void incarcaApartamente() {
        Proiect proiectSelectat = (Proiect) proiecteComboBox.getSelectedItem();
        int numarCamere = (Integer) camereComboBox.getSelectedItem();
        String sortarePret = (String) sortarePretComboBox.getSelectedItem();

        ApartamentDAO apartamentDAO = new ApartamentDAO();
        List<Apartament> apartamente;
        if (proiectSelectat != null) {
            apartamente = apartamentDAO.getApartamenteByProiectSiCamereSiPret(proiectSelectat.getIdProiect(), numarCamere, sortarePret);
        } else {
            apartamente = apartamentDAO.getToateApartamentele();
        }


        apartamente = apartamente.stream()
                .filter(Apartament::isChirie)
                .collect(Collectors.toList());

        afiseazaApartamente(apartamente);
    }

    private void afiseazaApartamente(List<Apartament> apartamente) {
        String[] columnNames = {"ID", "Preț", "Suprafață Utilă", "Suprafață Totală", "Camere", "Vezi Detalii", "Șterge", "Actualizează", "Exportă PDF"};
        Object[][] data = new Object[apartamente.size()][9];

        for (int i = 0; i < apartamente.size(); i++) {
            Apartament apartament = apartamente.get(i);
            data[i][0] = apartament.getIdApartament();
            data[i][1] = apartament.getPret();
            data[i][2] = apartament.getSuprafataUtila();
            data[i][3] = apartament.getSuprafataTotala();
            data[i][4] = apartament.getCamere();
            data[i][5] = "Vezi Detalii"; // Add text for detail button
            data[i][6] = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/sterge.png")).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
            data[i][7] = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/update.png")).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
            data[i][8] = "Exportă PDF";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6 || column == 7 || column == 8;
            }
        };

        apartamenteTable.setModel(model);
        apartamenteTable.setOpaque(false);
        ((DefaultTableCellRenderer) apartamenteTable.getDefaultRenderer(Object.class)).setOpaque(false);
        apartamenteTable.getColumn("Vezi Detalii").setCellRenderer(new ButtonRenderer());
        apartamenteTable.getColumn("Vezi Detalii").setCellEditor(new ButtonEditor(new JCheckBox(), apartamente));
        apartamenteTable.getColumn("Șterge").setCellRenderer(new IconRenderer());
        apartamenteTable.getColumn("Șterge").setCellEditor(new IconEditor(new JCheckBox(), apartamente, "delete"));
        apartamenteTable.getColumn("Actualizează").setCellRenderer(new IconRenderer());
        apartamenteTable.getColumn("Actualizează").setCellEditor(new IconEditor(new JCheckBox(), apartamente, "update"));
        apartamenteTable.getColumn("Exportă PDF").setCellRenderer(new ButtonRenderer());
        apartamenteTable.getColumn("Exportă PDF").setCellEditor(new ButtonEditor(new JCheckBox(), apartamente));
    }

    private void redimensioneazaComponente(JLabel logoLabel, JButton butonInapoi, JButton adaugaProiectButton, JButton adaugaApartamentButton, JButton exportPDFButton) {
        int width = frame.getWidth();
        int height = frame.getHeight();


        int logoWidth = width / 5;
        int logoHeight = height / 10;
        logoLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/logomic.png"))
                .getImage().getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH)));


        int buttonSize = Math.min(width / 20, height / 20);
        butonInapoi.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        adaugaProiectButton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/plus.png"))
                .getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        adaugaApartamentButton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/plus.png"))
                .getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        exportPDFButton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/pdf.png"))
                .getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
    }

    private JButton creeazaButon(String text, String caleIconita, int latime, int inaltime) {
        ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(caleIconita))
                .getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH));
        JButton buton = new JButton(text, icon);
        buton.setHorizontalTextPosition(SwingConstants.RIGHT);
        buton.setVerticalTextPosition(SwingConstants.CENTER);
        buton.setBackground(new Color(0, 102, 204));
        buton.setForeground(Color.WHITE);
        buton.setBorderPainted(false);
        buton.setFocusPainted(false);
        buton.setContentAreaFilled(false) ;
        return buton;
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
        private List<Apartament> apartamente;
        private JButton button;

        public ButtonEditor(JCheckBox checkBox, List<Apartament> apartamente) {
            super(checkBox);
            this.apartamente = apartamente;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
            int selectedRow = apartamenteTable.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }
            Apartament apartament = apartamente.get(selectedRow);
            if ("Vezi Detalii".equals(label)) {
                new DetaliiApartamentDialog(frame, apartament);
            } else if ("Exportă PDF".equals(label)) {
                exportApartamentToPDF(apartament);
            }
        }
    }

    class IconRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel((Icon) value);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    }

    class IconEditor extends DefaultCellEditor {
        private JLabel label;
        private List<Apartament> apartamente;
        private String action;

        public IconEditor(JCheckBox checkBox, List<Apartament> apartamente, String action) {
            super(checkBox);
            this.apartamente = apartamente;
            this.action = action;
            label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label.setIcon((Icon) value);
            return label;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
            int selectedRow = apartamenteTable.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }
            Apartament apartament = apartamente.get(selectedRow);

            if ("delete".equals(action)) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Sigur doriți să ștergeți acest apartament?", "Confirmare ștergere", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ApartamentDAO apartamentDAO = new ApartamentDAO();
                    apartamentDAO.stergeApartament(apartament.getIdApartament());
                    incarcaApartamente();
                }
            } else if ("update".equals(action)) {
                new AdaugaApartamentDialog(frame, apartament, DisponibilitateApartamenteInchiriereGUI.this::incarcaApartamente).setVisible(true);
                incarcaApartamente();
            }
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

    private void exportApartamentToPDF(Apartament apartament) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Ensure the project is correctly initialized
            ProiectDAO proiectDAO = new ProiectDAO();
            Proiect proiect = proiectDAO.getProiect(apartament.getIdProiect());

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Load fonts
                File fontFileRegular = new File("C:\\Windows\\Fonts\\ARIAL.TTF");
                PDType0Font fontRegular = PDType0Font.load(document, fontFileRegular);

                File fontFileBold = new File("C:\\Windows\\Fonts\\ARIALBD.TTF");
                PDType0Font fontBold = PDType0Font.load(document, fontFileBold);

                addHeader(contentStream, document, fontRegular);

                contentStream.beginText();
                contentStream.setFont(fontBold, 14);
                contentStream.setNonStrokingColor(new Color(128, 0, 128)); // Purple color for the title
                contentStream.newLineAtOffset(25, 700);
                contentStream.setLeading(20f); // Increased leading for more space between lines
                contentStream.showText("Detalii Apartament pentru Închiriere");
                contentStream.newLine();
                contentStream.newLine();

                addDetailRow(contentStream, fontBold, fontRegular, "Proiect: ", proiect != null ? proiect.getNumeProiect() : "N/A");
                addDetailRow(contentStream, fontBold, fontRegular, "ID: ", apartament.getIdApartament());
                addDetailRow(contentStream, fontBold, fontRegular, "Preț: ", String.valueOf(apartament.getPret()));
                addDetailRow(contentStream, fontBold, fontRegular, "Suprafață Totală: ", apartament.getSuprafataTotala() + " m2");
                addDetailRow(contentStream, fontBold, fontRegular, "Suprafață Utilă: ", apartament.getSuprafataUtila() + " m2");
                addDetailRow(contentStream, fontBold, fontRegular, "Camere: ", String.valueOf(apartament.getCamere()));
                addDetailRow(contentStream, fontBold, fontRegular, "Etaj: ", String.valueOf(apartament.getEtaj()));
                addDetailRow(contentStream, fontBold, fontRegular, "Hol: ", apartament.getHol() + " m2");
                addDetailRow(contentStream, fontBold, fontRegular, "Baie: ", apartament.getBaie() + " m2");
                addDetailRow(contentStream, fontBold, fontRegular, "Bucătărie: ", apartament.getBucatarie() + " m2");

                contentStream.endText();
            }

            document.save("detalii_apartament_inchiriere_" + apartament.getIdApartament() + ".pdf");
            JOptionPane.showMessageDialog(frame, "PDF generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error generating PDF: " + e.getMessage());
        }
    }

    private void addDetailRow(PDPageContentStream contentStream, PDType0Font fontBold, PDType0Font fontRegular, String label, String value) throws IOException {
        contentStream.setFont(fontBold, 12);
        contentStream.showText(label);
        contentStream.setFont(fontRegular, 12);
        contentStream.showText(value);
        contentStream.newLine();
        contentStream.newLine();
    }

    private void exportToPDF() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            Proiect proiect = (Proiect) proiecteComboBox.getSelectedItem();

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Load a font from the system or bundled with the application
                File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf"); // Change this path as needed
                PDType0Font font = PDType0Font.load(document, fontFile);

                addHeader(contentStream, document, font);

                contentStream.beginText();
                contentStream.setFont(font, 16);
                contentStream.setNonStrokingColor(new Color(75, 0, 130));
                contentStream.newLineAtOffset(25, 700);
                contentStream.setLeading(14.5f);
                contentStream.showText("LISTA APARTAMENTE DISPONIBILE ONE FLOREASCA-CHIRIE");
                contentStream.endText();

                float margin = 25;
                float yStart = 650;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float bottomMargin = 50;
                int rowsPerPage = 20;
                int numColumns = 5;
                float rowHeight = 20;
                float tableHeight = rowHeight * rowsPerPage;
                float cellMargin = 5;

                // Table Header
                String[] headers = {"ID AP.", "PRET(EUR)", "SUP.UTILA M2", "SUP.TOTALA MP2", "NR CAMERE"};
                contentStream.setFont(font, 12);
                yPosition -= rowHeight;
                for (int i = 0; i < numColumns; i++) {
                    float cellWidth = tableWidth / numColumns;
                    contentStream.addRect(margin + i * cellWidth, yPosition, cellWidth, rowHeight);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + i * cellWidth + cellMargin, yPosition + cellMargin);
                    contentStream.showText(headers[i]);
                    contentStream.endText();
                }
                contentStream.stroke();

                // Table Data
                DefaultTableModel model = (DefaultTableModel) apartamenteTable.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    yPosition -= rowHeight;
                    for (int j = 0; j < numColumns; j++) {
                        float cellWidth = tableWidth / numColumns;
                        contentStream.addRect(margin + j * cellWidth, yPosition, cellWidth, rowHeight);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + j * cellWidth + cellMargin, yPosition + cellMargin);
                        contentStream.showText(model.getValueAt(i, j).toString());
                        contentStream.endText();
                    }
                    contentStream.stroke();
                }
            }

            document.save("lista_apartamente_inchiriere.pdf" + (proiect != null ? "_" + proiect.getNumeProiect() : ""));
            JOptionPane.showMessageDialog(frame, "PDF generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error generating PDF: " + e.getMessage());
        }
    }


    private float drawTableHeaders(PDPageContentStream contentStream, PDType0Font font, float margin, float yPosition, float[] columnWidths, String[] headers) throws IOException {
        contentStream.setFont(font, 12);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);

        for (int i = 0; i < headers.length; i++) {
            contentStream.showText(headers[i]);
            contentStream.newLineAtOffset(columnWidths[i], 0);
        }

        contentStream.endText();
        return yPosition - 20;
    }

    private float drawTableRow(PDPageContentStream contentStream, PDType0Font font, float margin, float yPosition, float[] columnWidths, String[] rowData) throws IOException {
        contentStream.setFont(font, 12);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);

        for (int i = 0; i < rowData.length; i++) {
            contentStream.showText(rowData[i]);
            contentStream.newLineAtOffset(columnWidths[i], 0);
        }

        contentStream.endText();
        return yPosition - 20;
    }

    private void addHeader(PDPageContentStream contentStream, PDDocument document, PDType0Font font) throws IOException {
        URL logoUrl = getClass().getClassLoader().getResource("resources/images/logomic.png");
        BufferedImage bufferedImage = ImageIO.read(logoUrl);
        PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
        contentStream.drawImage(pdImage, 50, 740, 70, 70);  // Increased size of the logo

        // Add company details
        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.newLineAtOffset(130, 780);  // Position the text to the right of the logo
        contentStream.showText("SKY REAL ESTATE SRL");
        contentStream.newLineAtOffset(0, -15);  // Move to the next line
        contentStream.showText("Calea Floreasca, Nr. 211, Bucuresti");
        contentStream.newLineAtOffset(0, -15);  // Move to the next line
        contentStream.showText("office@sky.ro");
        contentStream.newLineAtOffset(0, -15);  // Move to the next line
        contentStream.showText("J40/12345/2021");
        contentStream.endText();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Disponibilitate Apartamente pentru Închiriere");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new DisponibilitateApartamenteInchiriereGUI(frame));
            frame.setVisible(true);
        });
    }
}

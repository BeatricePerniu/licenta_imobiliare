package licenta_imobiliare.gui;

import licenta_imobiliare.dao.AngajatDAO;
import licenta_imobiliare.model.Angajat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AfisareDisponibilitatiDialog extends JDialog {
    private JTable disponibilitateTable;
    private JButton inapoiButton;
    private JButton exportPDFButton;
    private AngajatDAO angajatDAO;
    private DisponibilitateTableModel tableModel;

    public AfisareDisponibilitatiDialog(JFrame parent) {
        super(parent, "Lista Disponibilități Angajați", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);

        angajatDAO = new AngajatDAO();
        tableModel = new DisponibilitateTableModel(angajatDAO.getAllAngajati());

        disponibilitateTable = new JTable(tableModel);
        disponibilitateTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        disponibilitateTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(disponibilitateTable);

        GradientPanel mainPanel = new GradientPanel(new BorderLayout(), new Color(123, 104, 238), new Color(72, 209, 204));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        GradientPanel buttonPanel = new GradientPanel(new FlowLayout(FlowLayout.LEFT), new Color(123, 104, 238), new Color(72, 209, 204));
        inapoiButton = createIconButton("resources/images/back.png");
        inapoiButton.addActionListener(e -> dispose());
        buttonPanel.add(inapoiButton);

        exportPDFButton = createIconButton("resources/images/pdf.png");
        exportPDFButton.addActionListener(e -> exportaInPDF());
        buttonPanel.add(exportPDFButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JButton createIconButton(String path) {
        JButton button = new JButton();
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(path));
        Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(30, 30));
        return button;
    }

    private void exportaInPDF() {
        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, fontFile);
            PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);

            File logoFile = new File("C:\\Users\\beatr\\OneDrive\\Desktop\\licenta_imobiliare\\src\\resources\\images\\logomic.png");
            PDImageXObject logo = PDImageXObject.createFromFileByExtension(logoFile, document);
            contentStream.drawImage(logo, 50, 520, 60, 60);

            contentStream.beginText();
            contentStream.setFont(font, 16);
            contentStream.newLineAtOffset(250, 560);
            contentStream.showText("LISTA DISPONIBILITATE ANGAJATI");
            contentStream.endText();

            contentStream.setLineWidth(1.0f);
            contentStream.setStrokingColor(Color.BLACK);

            float margin = 50;
            float yStart = 500;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float cellHeight = 20f;
            float tableTop = yPosition + cellHeight;

            String[] columns = {"Angajat", "Data", "Ore"};
            float[] columnWidths = {200, 200, 300};

            contentStream.setFont(font, 12);
            contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
            contentStream.addRect(margin, yPosition - cellHeight, tableWidth, cellHeight);
            contentStream.fill();
            contentStream.setNonStrokingColor(Color.BLACK);
            for (int i = 0; i < columns.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + (i * columnWidths[i]), yPosition - 15);
                contentStream.showText(columns[i]);
                contentStream.endText();
            }
            yPosition -= cellHeight;


            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(margin + tableWidth, yPosition);
            contentStream.stroke();


            for (Angajat angajat : tableModel.getAngajati()) {
                for (Date data : angajat.getDisponibilitate().keySet()) {

                    contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
                    contentStream.addRect(margin, yPosition - cellHeight, tableWidth, cellHeight);
                    contentStream.fill();
                    contentStream.setNonStrokingColor(Color.BLACK);


                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition - 15);
                    contentStream.showText(angajat.getNumeAngajat() + " " + angajat.getPrenumeAngajat());
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + columnWidths[0], yPosition - 15);
                    contentStream.showText(data.toString());
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + columnWidths[0] + columnWidths[1], yPosition - 15);
                    contentStream.showText(angajat.getDisponibilitate().get(data).toString());
                    contentStream.endText();


                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(margin + tableWidth, yPosition);
                    contentStream.stroke();

                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(margin, yPosition - cellHeight);
                    contentStream.stroke();

                    contentStream.moveTo(margin + columnWidths[0], yPosition);
                    contentStream.lineTo(margin + columnWidths[0], yPosition - cellHeight);
                    contentStream.stroke();

                    contentStream.moveTo(margin + columnWidths[0] + columnWidths[1], yPosition);
                    contentStream.lineTo(margin + columnWidths[0] + columnWidths[1], yPosition - cellHeight);
                    contentStream.stroke();

                    contentStream.moveTo(margin + tableWidth, yPosition);
                    contentStream.lineTo(margin + tableWidth, yPosition - cellHeight);
                    contentStream.stroke();

                    yPosition -= cellHeight;
                }
            }

            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(margin + tableWidth, yPosition);
            contentStream.stroke();

            contentStream.close();

            String pdfFileName = "Disponibilitati_Angajati.pdf";
            document.save(pdfFileName);
            JOptionPane.showMessageDialog(this, "PDF generat cu succes: " + pdfFileName);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la generarea PDF-ului: " + e.getMessage());
        }
    }


    private class DisponibilitateTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Angajat", "Data", "Ore", "Actiuni"};
        private final List<Angajat> angajati;

        public DisponibilitateTableModel(List<Angajat> angajati) {
            this.angajati = angajati;
        }

        public List<Angajat> getAngajati() {
            return angajati;
        }

        @Override
        public int getRowCount() {
            return angajati.stream().mapToInt(a -> a.getDisponibilitate().size()).sum();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            int row = -1;
            for (Angajat angajat : angajati) {
                for (Map.Entry<Date, List<String>> entry : angajat.getDisponibilitate().entrySet()) {
                    row++;
                    if (row == rowIndex) {
                        switch (columnIndex) {
                            case 0:
                                return angajat.getNumeAngajat() + " " + angajat.getPrenumeAngajat();
                            case 1:
                                return entry.getKey();
                            case 2:
                                return entry.getValue();
                            case 3:
                                return "Șterge";
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3;
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
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
                int row = disponibilitateTable.getSelectedRow();
                int modelRow = disponibilitateTable.convertRowIndexToModel(row);
                String angajatName = (String) tableModel.getValueAt(modelRow, 0);
                Date date = (Date) tableModel.getValueAt(modelRow, 1);
                Angajat angajatToUpdate = null;
                for (Angajat angajat : tableModel.getAngajati()) {
                    if ((angajat.getNumeAngajat() + " " + angajat.getPrenumeAngajat()).equals(angajatName)) {
                        angajatToUpdate = angajat;
                        break;
                    }
                }
                if (angajatToUpdate != null) {
                    showDeleteDialog(angajatToUpdate, date);
                    tableModel.fireTableDataChanged();
                }
            }
            isPushed = false;
            return label;
        }

        private void showDeleteDialog(Angajat angajat, Date date) {
            List<String> ore = angajat.getDisponibilitate().get(date);
            if (ore != null) {
                GradientPanel panel = new GradientPanel(new BorderLayout(), new Color(123, 104, 238), new Color(72, 209, 204));
                JLabel label = new JLabel("Selectați orele de șters:");
                label.setForeground(Color.WHITE);
                panel.add(label, BorderLayout.NORTH);

                JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1));
                checkBoxPanel.setOpaque(false);
                JCheckBox[] oraCheckBoxes = new JCheckBox[ore.size()];
                for (int i = 0; i < ore.size(); i++) {
                    oraCheckBoxes[i] = new JCheckBox(ore.get(i));
                    oraCheckBoxes[i].setOpaque(false);
                    oraCheckBoxes[i].setForeground(Color.WHITE);
                    checkBoxPanel.add(oraCheckBoxes[i]);
                }
                panel.add(checkBoxPanel, BorderLayout.CENTER);

                int result = JOptionPane.showConfirmDialog(null, panel, "Șterge Disponibilitate",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    for (JCheckBox oraCheckBox : oraCheckBoxes) {
                        if (oraCheckBox.isSelected()) {
                            angajatDAO.stergeDisponibilitate(angajat.getIdAngajat(), date, oraCheckBox.getText());
                        }
                    }
                    angajat.getDisponibilitate().get(date).removeIf(ora -> {
                        for (JCheckBox oraCheckBox : oraCheckBoxes) {
                            if (oraCheckBox.isSelected() && oraCheckBox.getText().equals(ora)) {
                                return true;
                            }
                        }
                        return false;
                    });
                    if (angajat.getDisponibilitate().get(date).isEmpty()) {
                        angajat.getDisponibilitate().remove(date);
                    }
                    angajatDAO.actualizeazaAngajat(angajat);
                }
            }
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

    // Panou de fundal gradient
    class GradientPanel extends JPanel {
        private Color color1;
        private Color color2;

        public GradientPanel(LayoutManager layout, Color color1, Color color2) {
            super(layout);
            this.color1 = color1;
            this.color2 = color2;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AfisareDisponibilitatiDialog dialog = new AfisareDisponibilitatiDialog(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        });
    }
}

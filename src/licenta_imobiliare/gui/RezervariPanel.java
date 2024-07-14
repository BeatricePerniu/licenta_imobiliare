package licenta_imobiliare.gui;

import licenta_imobiliare.dao.RezervareDAO;
import licenta_imobiliare.model.Rezervare;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;


public class RezervariPanel extends PanouGradient {
    private JFrame parentFrame;
    private JTable rezervariTable;
    private RezervariTableModel rezervariTableModel;

    public RezervariPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());

        rezervariTableModel = new RezervariTableModel(new RezervareDAO().getToateRezervarile());
        rezervariTable = new JTable(rezervariTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3; // Make the delete and export button columns editable
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 2 || column == 3) {
                    return new ButtonRenderer();
                }
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 2 || column == 3) {
                    return new ButtonEditor(new JCheckBox());
                }
                return super.getCellEditor(row, column);
            }
        };
        JScrollPane scrollPane = new JScrollPane(rezervariTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        PanouGradient buttonPanel = new PanouGradient(new FlowLayout(FlowLayout.LEFT));

        JButton adaugaRezervareButton = createButtonWithIcon("resources/images/plus.png", 20, 20);
        adaugaRezervareButton.addActionListener(this::adaugaRezervare);
        buttonPanel.add(adaugaRezervareButton);

        JButton inapoiButton = createButtonWithIcon("resources/images/back.png", 20, 20);
        inapoiButton.addActionListener(e -> {
            parentFrame.setContentPane(new RezervareGUI(parentFrame));
            parentFrame.invalidate();
            parentFrame.validate();
        });
        buttonPanel.add(inapoiButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente(adaugaRezervareButton, inapoiButton);
            }
        });

        redimensioneazaComponente(adaugaRezervareButton, inapoiButton);

        incarcaRezervari();
    }

    private void redimensioneazaComponente(JButton adaugaRezervareButton, JButton inapoiButton) {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            int buttonSize = Math.min(width / 20, height / 20);
            adaugaRezervareButton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/plus.png"))
                    .getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
            inapoiButton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                    .getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        }
    }

    private void incarcaRezervari() {
        rezervariTableModel = new RezervariTableModel(new RezervareDAO().getToateRezervarile());
        rezervariTable.setModel(rezervariTableModel);
    }

    private void adaugaRezervare(ActionEvent e) {
        RezervareApartamentDialog dialog = new RezervareApartamentDialog(parentFrame);
        dialog.setVisible(true);


        incarcaRezervari();
    }

    private void stergeRezervare(Rezervare rezervare) {
        RezervareDAO rezervareDAO = new RezervareDAO();
        rezervareDAO.stergeRezervare(rezervare.getIdRezervare());
        incarcaRezervari();
    }

    private void exportaInPDF(Rezervare rezervare) {
        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf"); // Adjust path as necessary

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, fontFile);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);


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

    private JButton createButtonWithIcon(String iconPath, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(iconPath))
                .getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }

    // Custom button renderer
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 2) {
                setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/sterge.png"))
                        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))); // Very small icon
            } else if (column == 3) {
                setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/pdf.png"))
                        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))); // Very small icon
            }
            return this;
        }
    }


    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (column == 2) {
                button.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/sterge.png"))
                        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))); // Very small icon
            } else if (column == 3) {
                button.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/pdf.png"))
                        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))); // Very small icon
            }
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    Rezervare rezervare = rezervariTableModel.getRezervareAt(row);
                    if (column == 2) {
                        stergeRezervare(rezervare);
                    } else if (column == 3) {
                        exportaInPDF(rezervare);
                    }
                }
            });
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return button;
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rezervări Apartamente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new RezervariPanel(frame));
            frame.setVisible(true);
        });
    }
}

class PanouGradient extends JPanel {
    public PanouGradient() {
        super();
    }

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

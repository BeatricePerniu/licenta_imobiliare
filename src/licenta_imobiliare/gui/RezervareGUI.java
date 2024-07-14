package licenta_imobiliare.gui;

import licenta_imobiliare.dao.AngajatDAO;
import licenta_imobiliare.model.Angajat;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RezervareGUI extends JPanel {
    private JFrame frame;
    private JLabel logoLabel;
    private JButton angajatButton;
    private JButton genereazaSugestieButton;
    private JButton rezervareApartamentButton;
    private JButton inapoiButton;
    private JTextArea textAreaSugestie;
    private JButton exportPDFButton;

    public RezervareGUI(JFrame frame) {
        this.frame = frame;
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


        JPanel panouPrincipal = new JPanel();
        panouPrincipal.setOpaque(false);
        panouPrincipal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;


        angajatButton = creeazaButon("Angajat", "resources/images/angajat.png");
        genereazaSugestieButton = creeazaButon("Generează Sugestie Vizionare", "resources/images/document.png");
        rezervareApartamentButton = creeazaButon("Rezervare Apartament", "resources/images/rezervare.png");

        // ADAUGA BUTOANE IN PANEL CU CONSTR
        panouPrincipal.add(angajatButton, gbc);
        panouPrincipal.add(genereazaSugestieButton, gbc);
        panouPrincipal.add(rezervareApartamentButton, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);

        inapoiButton = new JButton();
        inapoiButton.setContentAreaFilled(false);
        inapoiButton.setBorderPainted(false);
        inapoiButton.addActionListener(e -> {
            frame.setContentPane(new MeniuPrincipal(frame));
            frame.invalidate();
            frame.validate();
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(inapoiButton);
        panouGradient.add(backButtonPanel, BorderLayout.SOUTH);

        add(panouGradient, BorderLayout.CENTER);

        // Listener pt redim componente
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente();


        angajatButton.addActionListener(this::deschideFereastraAngajat);
        genereazaSugestieButton.addActionListener(this::genereazaSugestieVizionare);
        rezervareApartamentButton.addActionListener(this::deschideFereastraRezervareApartament);
    }

    private JButton creeazaButon(String text, String caleIconita) {
        JButton buton = new JButton();
        buton.setText(text);
        if (caleIconita != null) {
            buton.setIcon(incarcaIconita(caleIconita, 100, 100)); // Set initial icon
        }
        buton.setHorizontalTextPosition(SwingConstants.CENTER);
        buton.setVerticalTextPosition(SwingConstants.BOTTOM);
        buton.setBackground(new Color(0, 102, 204));
        buton.setForeground(Color.WHITE);
        buton.setBorderPainted(false);
        buton.setFocusPainted(false);
        buton.setContentAreaFilled(false); // Remove button background
        return buton;
    }

    private ImageIcon incarcaIconita(String cale, int latime, int inaltime) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(cale));
        if (icon.getImage() != null && latime > 0 && inaltime > 0) {
            Image img = icon.getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    private void redimensioneazaComponente() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            // Redimensionez logo
            int logoWidth = width / 5;
            int logoHeight = height / 10;
            logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));

            // marimile butoanelor
            int buttonSize = Math.min(width / 6, height / 6);
            angajatButton.setIcon(incarcaIconita("resources/images/angajat.png", buttonSize, buttonSize));
            genereazaSugestieButton.setIcon(incarcaIconita("resources/images/document.png", buttonSize, buttonSize));
            rezervareApartamentButton.setIcon(incarcaIconita("resources/images/rezervare.png", buttonSize, buttonSize));

            int backButtonSize = Math.min(width / 20, height / 20);
            inapoiButton.setIcon(incarcaIconita("resources/images/back.png", backButtonSize, backButtonSize));
        }
    }

    private void deschideFereastraAngajat(ActionEvent e) {
        frame.setContentPane(new AngajatGUI(frame));
        frame.invalidate();
        frame.validate();
    }

    private void genereazaSugestieVizionare(ActionEvent e) {
        // dialog pt selectarea datei
        DateSelectorDialog dateSelectorDialog = new DateSelectorDialog(frame);
        Date dataSelectata = dateSelectorDialog.getSelectedDate();
        if (dataSelectata != null) {
            //verific disp pt angajati pt data
            AngajatDAO angajatDAO = new AngajatDAO();
            List<Angajat> angajati = angajatDAO.getAllAngajati();
            StringBuilder sb = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            boolean disponibilitateGasita = false;

            for (Angajat angajat : angajati) {
                Map<Date, List<String>> disponibilitate = angajat.getDisponibilitate();
                for (Date data : disponibilitate.keySet()) {
                    if (dateFormat.format(data).equals(dateFormat.format(dataSelectata))) {
                        disponibilitateGasita = true;
                        sb.append("Angajat: ").append(angajat.getNumeAngajat()).append(" ").append(angajat.getPrenumeAngajat()).append("\n");
                        sb.append("Ore: ").append(disponibilitate.get(data)).append("\n\n");
                    }
                }
            }

            if (disponibilitateGasita) {
                // afisez in fereastra noua
                textAreaSugestie = new JTextArea(sb.toString());
                textAreaSugestie.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textAreaSugestie);

                JDialog disponibilitatiDialog = new JDialog(frame, "Disponibilități Angajați", true);
                disponibilitatiDialog.setSize(500, 400);
                disponibilitatiDialog.setLocationRelativeTo(this);
                JPanel dialogPanel = new PanouGradient(new BorderLayout());
                dialogPanel.add(scrollPane, BorderLayout.CENTER);

                exportPDFButton = new JButton();
                exportPDFButton.setContentAreaFilled(false);
                exportPDFButton.setBorderPainted(false);
                exportPDFButton.addActionListener(this::exportaInPDF);
                exportPDFButton.setIcon(incarcaIconita("resources/images/pdf.png", 30, 30));
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.setOpaque(false);
                buttonPanel.add(exportPDFButton);
                dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

                disponibilitatiDialog.add(dialogPanel);
                disponibilitatiDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Nu există angajați disponibili pentru data selectată.", "Informație", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void deschideFereastraRezervareApartament(ActionEvent e) {
        frame.setContentPane(new RezervariPanel(frame));
        frame.invalidate();
        frame.validate();
    }
    private void exportaInPDF(ActionEvent e) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            //incarc logo
            PDImageXObject logo = PDImageXObject.createFromFile("C:\\Users\\beatr\\OneDrive\\Desktop\\licenta_imobiliare\\src\\resources\\images\\logomic.png", document);
            contentStream.drawImage(logo, 50, 750, 60, 60);

            PDType0Font font = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\Arial.ttf"));

            // titlu
            contentStream.beginText();
            contentStream.setFont(font, 14); // Smaller font size for the title
            contentStream.newLineAtOffset(50, 700); // Adjusted position for the title
            contentStream.showText("SUGESTIE VIZIONARE");
            contentStream.endText();

            String[] lines = textAreaSugestie.getText().split("\n");

            contentStream.setFont(font, 12);
            float yPosition = 670;
            for (String line : lines) {
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                if (line.startsWith("Angajat:")) {
                    contentStream.setFont(font, 12);
                    contentStream.showText(line);
                    yPosition -= 20;
                } else if (line.startsWith("Ore:")) {
                    contentStream.setFont(font, 10);
                    contentStream.showText(line);
                    yPosition -= 20;
                } else {
                    contentStream.setFont(font, 12);
                    contentStream.showText(line);
                    yPosition -= 15;
                }
                contentStream.endText();
            }

            contentStream.close();

            document.save("SugestieVizionare.pdf");
            document.close();
            JOptionPane.showMessageDialog(this, "PDF generat cu succes!");

        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la generarea PDF-ului: " + ioException.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
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

    class DateSelectorDialog extends JDialog {
        private JSpinner dateSpinner;
        private JButton okButton;
        private JButton cancelButton;
        private Date selectedDate;

        public DateSelectorDialog(JFrame parent) {
            super(parent, "Selectează Data", true);
            setLayout(new BorderLayout());
            setSize(300, 150);
            setLocationRelativeTo(parent);

            JPanel mainPanel = new PanouGradient(new GridLayout(2, 2, 10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel dateLabel = new JLabel("Data:");
            mainPanel.add(dateLabel);

            dateSpinner = new JSpinner(new SpinnerDateModel());
            dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
            mainPanel.add(dateSpinner);

            okButton = new JButton("OK");
            okButton.addActionListener(e -> {
                selectedDate = (Date) dateSpinner.getValue();
                dispose();
            });
            mainPanel.add(okButton);

            cancelButton = new JButton("Anulează");
            cancelButton.addActionListener(e -> {
                selectedDate = null;
                dispose();
            });
            mainPanel.add(cancelButton);

            add(mainPanel, BorderLayout.CENTER);
            setVisible(true);
        }

        public Date getSelectedDate() {
            return selectedDate;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestionare Rezervări");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new RezervareGUI(frame));
            frame.setVisible(true);
        });
    }
}

package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ClientDAO;
import licenta_imobiliare.model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClientExistentGUI extends JPanel {
    private JFrame frame;
    private JPanel panouPrincipal;
    private JLabel logoLabel;
    private JTextField textFieldCautaClient;
    private JButton butonCauta;
    private JButton butonInapoi;
    private JTable tabelClienti;
    private JScrollPane scrollPane;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ClientExistentGUI(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());


        PanouGradient panouGradient = new PanouGradient();
        panouGradient.setLayout(new BorderLayout());


        JPanel antet = new JPanel();
        antet.setOpaque(false);
        antet.setLayout(new BorderLayout());

        logoLabel = new JLabel();
        logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", 100, 100));
        antet.add(logoLabel, BorderLayout.WEST);

        antet.setPreferredSize(new Dimension(800, 100));
        panouGradient.add(antet, BorderLayout.NORTH);

        panouPrincipal = new JPanel();
        panouPrincipal.setOpaque(false);
        panouPrincipal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        textFieldCautaClient = new JTextField(30);
        butonCauta = new JButton("CAUTA");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(textFieldCautaClient);
        searchPanel.add(butonCauta);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        panouPrincipal.add(searchPanel, gbc);

        String[] coloane = {"idClient", "nume", "prenume", "cnp", "serie", "nrBuletin", "email", "dataNastere", "telefon", "domiciliu", "Șterge", "Actualizează"};
        DefaultTableModel model = new DefaultTableModel(coloane, 0);
        tabelClienti = new JTable(model);
        tabelClienti.setOpaque(false);
        ((DefaultTableCellRenderer) tabelClienti.getDefaultRenderer(Object.class)).setOpaque(false);
        scrollPane = new JScrollPane(tabelClienti);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);


        TableColumn stergeColumn = tabelClienti.getColumnModel().getColumn(10);
        stergeColumn.setCellRenderer(new ButtonRenderer("resources/images/sterge.png"));
        stergeColumn.setCellEditor(new ButtonEditor(new JCheckBox(), "resources/images/sterge.png", this::stergeClient));

        TableColumn actualizeazaColumn = tabelClienti.getColumnModel().getColumn(11);
        actualizeazaColumn.setCellRenderer(new ButtonRenderer("resources/images/update.png"));
        actualizeazaColumn.setCellEditor(new ButtonEditor(new JCheckBox(), "resources/images/update.png", this::actualizeazaClient));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 20, 0, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panouPrincipal.add(scrollPane, gbc);


        butonInapoi = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        butonInapoi.setContentAreaFilled(false);
        butonInapoi.setBorderPainted(false);
        butonInapoi.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        panouPrincipal.add(butonInapoi, gbc);

        panouGradient.add(panouPrincipal, BorderLayout.CENTER);

        add(panouGradient, BorderLayout.CENTER);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensioneazaComponente();
            }
        });

        redimensioneazaComponente(); // Redimensionare inițială


        butonInapoi.addActionListener(e -> {
            frame.setContentPane(new ClientGUI(frame));
            frame.invalidate();
            frame.validate();
        });


        butonCauta.addActionListener(this::cautaClient);

        incarcaTotiClientii();
    }

    private void redimensioneazaComponente() {
        int width = frame.getWidth();
        int height = frame.getHeight();


        int logoWidth = width / 12;
        int logoHeight = height / 12;
        logoLabel.setIcon(incarcaIconita("resources/images/logomic.png", logoWidth, logoHeight));


        scrollPane.setPreferredSize(new Dimension(width - 100, height / 2));
        tabelClienti.setRowHeight(height / 20);


        int backSize = Math.min(width / 20, height / 20);
        butonInapoi.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/images/back.png"))
                .getImage().getScaledInstance(backSize, backSize, Image.SCALE_SMOOTH)));
    }

    private void cautaClient(ActionEvent e) {
        String searchTerm = textFieldCautaClient.getText().trim();
        if (!searchTerm.isEmpty()) {
            ClientDAO clientDAO = new ClientDAO();
            List<Client> clienti = clientDAO.cautaClient(searchTerm);
            afiseazaClientiInTabel(clienti);
        } else {
            JOptionPane.showMessageDialog(this, "Introduceți un termen de căutare!", "Atenție", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void afiseazaClientiInTabel(List<Client> clienti) {
        DefaultTableModel model = (DefaultTableModel) tabelClienti.getModel();
        model.setRowCount(0);
        for (Client client : clienti) {
            model.addRow(new Object[]{
                    client.getIdClient(),
                    client.getNume(),
                    client.getPrenume(),
                    client.getCnp(),
                    client.getSerie(),
                    client.getNrBuletin(),
                    client.getEmail(),
                    dateFormat.format(client.getDataNastere()),
                    client.getTelefon(),
                    client.getDomiciliu(),
                    "Șterge",
                    "Actualizează"
            });
        }
    }

    private void incarcaTotiClientii() {
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clienti = clientDAO.getAllClients();
        afiseazaClientiInTabel(clienti);
    }

    private void stergeClient(int row) {
        DefaultTableModel model = (DefaultTableModel) tabelClienti.getModel();
        String idClient = (String) model.getValueAt(row, 0);
        ClientDAO clientDAO = new ClientDAO();

        if (clientDAO.hasContracts(idClient)) {
            JOptionPane.showMessageDialog(this, "Clientul are contracte și nu poate fi șters.", "Eroare", JOptionPane.ERROR_MESSAGE);
        } else {
            clientDAO.stergeClient(idClient);
            incarcaTotiClientii();
        }
    }

    private void actualizeazaClient(int row) {
        ClientDAO clientDAO = new ClientDAO();
        DefaultTableModel model = (DefaultTableModel) tabelClienti.getModel();
        String idClient = (String) model.getValueAt(row, 0);
        String nume = (String) model.getValueAt(row, 1);
        String prenume = (String) model.getValueAt(row, 2);
        String cnp = (String) model.getValueAt(row, 3);
        String serie = (String) model.getValueAt(row, 4);
        int nrBuletin = Integer.parseInt(model.getValueAt(row, 5).toString());
        String email = (String) model.getValueAt(row, 6);
        Date dataNastere = null;
        try {
            dataNastere = dateFormat.parse((String) model.getValueAt(row, 7));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int telefon = Integer.parseInt(model.getValueAt(row, 8).toString());
        String domiciliu = (String) model.getValueAt(row, 9);

        Client client = new Client(idClient, nume, prenume, cnp, serie, nrBuletin, dataNastere, domiciliu, email, telefon);

        UpdateClientDialog updateClientDialog = new UpdateClientDialog(frame, client);
        updateClientDialog.setVisible(true);

        if (updateClientDialog.isUpdated()) {
            clientDAO.actualizeazaClient(updateClientDialog.getClient());
            incarcaTotiClientii();
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

    private ImageIcon incarcaIconita(String cale, int latime, int inaltime) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(cale));
        if (icon.getImage() != null && latime > 0 && inaltime > 0) {
            Image img = icon.getImage().getScaledInstance(latime, inaltime, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String iconPath) {
            setIcon(incarcaIconita(iconPath, 20, 20));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String iconPath;
        private JButton button;
        private int row;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, String iconPath, ButtonAction action) {
            super(checkBox);
            this.iconPath = iconPath;
            button = new JButton();
            button.setOpaque(true);
            button.setIcon(incarcaIconita(iconPath, 20, 20));
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                if (isPushed) {
                    action.performAction(row);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return "";
        }
    }

    @FunctionalInterface
    interface ButtonAction {
        void performAction(int row);
    }

    class UpdateClientDialog extends JDialog {
        private JTextField numeField;
        private JTextField prenumeField;
        private JTextField cnpField;
        private JTextField serieField;
        private JTextField nrBuletinField;
        private JTextField emailField;
        private JTextField dataNastereField;
        private JTextField telefonField;
        private JTextField domiciliuField;
        private boolean updated;
        private Client client;

        public UpdateClientDialog(JFrame parent, Client client) {
            super(parent, "Actualizează Client", true);
            this.client = client;
            setLayout(new GridLayout(10, 2, 10, 10));
            setSize(400, 400);
            setLocationRelativeTo(parent);

            numeField = new JTextField(client.getNume());
            prenumeField = new JTextField(client.getPrenume());
            cnpField = new JTextField(client.getCnp());
            serieField = new JTextField(client.getSerie());
            nrBuletinField = new JTextField(String.valueOf(client.getNrBuletin()));
            emailField = new JTextField(client.getEmail());
            dataNastereField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(client.getDataNastere()));
            telefonField = new JTextField(String.valueOf(client.getTelefon()));
            domiciliuField = new JTextField(client.getDomiciliu());

            add(new JLabel("Nume:"));
            add(numeField);
            add(new JLabel("Prenume:"));
            add(prenumeField);
            add(new JLabel("CNP:"));
            add(cnpField);
            add(new JLabel("Serie:"));
            add(serieField);
            add(new JLabel("Nr Buletin:"));
            add(nrBuletinField);
            add(new JLabel("Email:"));
            add(emailField);
            add(new JLabel("Data Nașterii:"));
            add(dataNastereField);
            add(new JLabel("Telefon:"));
            add(telefonField);
            add(new JLabel("Domiciliu:"));
            add(domiciliuField);

            JButton updateButton = new JButton("Actualizează");
            JButton cancelButton = new JButton("Anulează");

            updateButton.addActionListener(e -> {
                updated = true;
                updateClientFromFields();
                dispose();
            });

            cancelButton.addActionListener(e -> dispose());

            add(updateButton);
            add(cancelButton);
        }

        private void updateClientFromFields() {
            client.setNume(numeField.getText());
            client.setPrenume(prenumeField.getText());
            client.setCnp(cnpField.getText());
            client.setSerie(serieField.getText());
            client.setNrBuletin(Integer.parseInt(nrBuletinField.getText()));
            client.setEmail(emailField.getText());
            try {
                client.setDataNastere(new SimpleDateFormat("yyyy-MM-dd").parse(dataNastereField.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            client.setTelefon(Integer.parseInt(telefonField.getText()));
            client.setDomiciliu(domiciliuField.getText());
        }

        public boolean isUpdated() {
            return updated;
        }

        public Client getClient() {
            return client;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sky Real Estate - Client Existent");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ClientExistentGUI(frame));
            frame.setVisible(true);
        });
    }
}

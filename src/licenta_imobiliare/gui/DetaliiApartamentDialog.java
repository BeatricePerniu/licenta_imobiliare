package licenta_imobiliare.gui;

import licenta_imobiliare.dao.ProiectDAO;
import licenta_imobiliare.model.Apartament;
import licenta_imobiliare.model.Proiect;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DetaliiApartamentDialog extends JDialog {
    private JComboBox<String> proiectComboBox;
    private ProiectDAO proiectDAO;
    private List<Proiect> proiecte;

    public DetaliiApartamentDialog(JFrame parent, Apartament apartament) {
        super(parent, "Detalii Apartament", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);

        proiectDAO = new ProiectDAO();
        proiecte = proiectDAO.getToateProiectele();

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID Apartament:"));
        panel.add(new JLabel(apartament.getIdApartament()));
        panel.add(new JLabel("Chirie:"));
        panel.add(new JLabel(apartament.isChirie() ? "Da" : "Nu"));
        panel.add(new JLabel("Numar Camere:"));
        panel.add(new JLabel(String.valueOf(apartament.getCamere())));
        panel.add(new JLabel("Suprafață Utilă m2:"));
        panel.add(new JLabel(String.valueOf(apartament.getSuprafataUtila())));

        panel.add(new JLabel("Suprafață Totală m2:"));
        panel.add(new JLabel(String.valueOf(apartament.getSuprafataTotala())));

        panel.add(new JLabel("Preț:"));
        panel.add(new JLabel(String.valueOf(apartament.getPret())));

        panel.add(new JLabel("Etaj:"));
        panel.add(new JLabel(String.valueOf(apartament.getEtaj())));

        panel.add(new JLabel("Hol m2:"));
        panel.add(new JLabel(String.valueOf(apartament.getHol())));

        panel.add(new JLabel("Baie m2:"));
        panel.add(new JLabel(String.valueOf(apartament.getBaie())));

        panel.add(new JLabel("Bucătărie m2:"));
        panel.add(new JLabel(String.valueOf(apartament.getBucatarie())));


        panel.add(new JLabel("Proiect:"));
        proiectComboBox = new JComboBox<>();
        incarcaProiecte(apartament.getProiect());
        panel.add(proiectComboBox);

        add(panel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void incarcaProiecte(Proiect proiectCurent) {
        for (Proiect proiect : proiecte) {
            proiectComboBox.addItem(proiect.getNumeProiect());
        }
        if (proiectCurent != null) {
            proiectComboBox.setSelectedItem(proiectCurent.getNumeProiect());
        }
    }

    public Proiect getSelectedProiect() {
        String numeProiectSelectat = (String) proiectComboBox.getSelectedItem();
        for (Proiect proiect : proiecte) {
            if (proiect.getNumeProiect().equals(numeProiectSelectat)) {
                return proiect;
            }
        }
        return null;
    }
}

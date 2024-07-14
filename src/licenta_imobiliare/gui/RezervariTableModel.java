package licenta_imobiliare.gui;


import licenta_imobiliare.model.Rezervare;
import javax.swing.table.AbstractTableModel;
import java.util.List;


class RezervariTableModel extends AbstractTableModel {
    private final List<Rezervare> rezervari;
    private final String[] columnNames = {"Nume Client", "ID Apartament", "Șterge", "Exportă PDF"};

    public RezervariTableModel(List<Rezervare> rezervari) {
        this.rezervari = rezervari;
    }

    @Override
    public int getRowCount() {
        return rezervari.size();
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
        Rezervare rezervare = rezervari.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rezervare.getClient().getNume() + " " + rezervare.getClient().getPrenume();
            case 1:
                return rezervare.getApartament().getIdApartament();
            case 2:
            case 3:
                return "Button";
        }
        return null;
    }

    public Rezervare getRezervareAt(int rowIndex) {
        return rezervari.get(rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2 || columnIndex == 3;
    }
}
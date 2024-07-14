package licenta_imobiliare.gui;

import licenta_imobiliare.model.Contract;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ContractTabelModel extends AbstractTableModel {
    private List<Contract> contracte;
    private final String[] columnNames = {"ID Contract", "Data", "Client", "Apartament", "Proiect", "Exportă PDF"};

    public ContractTabelModel() {
        this.contracte = new ArrayList<>();
    }

    public void setContracte(List<Contract> contracte) {
        this.contracte = contracte;
        fireTableDataChanged();
    }

    public List<Contract> getContracte() {
        return contracte;
    }

    @Override
    public int getRowCount() {
        return contracte.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contract contract = contracte.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return contract.getIdContract();
            case 1:
                return contract.getDataInceput();
            case 2:
                return contract.getClient().getNume() + " " + contract.getClient().getPrenume();
            case 3:
                return contract.getApartament().getIdApartament();
            case 4:
                return contract.getProiect() != null ? contract.getProiect().getNumeProiect() : "N/A";
            case 5:
                return "Exportă PDF";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5;
    }
}

package licenta_imobiliare.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Rezervare {
    private String idRezervare;
    private Client client;
    private Apartament apartament;
    private Angajat angajat;
    private Date dataVizionare;
    private String oraVizionare;

    // Constructor implicit
    public Rezervare() {
    }

    // Constructor cu parametri
    public Rezervare(String idRezervare, Client client, Apartament apartament, Angajat angajat, Date dataVizionare, String oraVizionare) {
        this.idRezervare = idRezervare;
        this.client = client;
        this.apartament = apartament;
        this.angajat = angajat;
        this.dataVizionare = dataVizionare;
        this.oraVizionare = oraVizionare;
    }

    // Getters and Setters
    public String getIdRezervare() {
        return idRezervare;
    }

    public void setIdRezervare(String idRezervare) {
        this.idRezervare = idRezervare;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Apartament getApartament() {
        return apartament;
    }

    public void setApartament(Apartament apartament) {
        this.apartament = apartament;
    }

    public Angajat getAngajat() {
        return angajat;
    }

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
    }

    public Date getDataVizionare() {
        return dataVizionare;
    }

    public void setDataVizionare(Date dataVizionare) {
        this.dataVizionare = dataVizionare;
    }

    public String getOraVizionare() {
        return oraVizionare;
    }

    public void setOraVizionare(String oraVizionare) {
        this.oraVizionare = oraVizionare;
    }

    // Metodă pentru sugerarea unei date și ore disponibile pentru vizionare
    public void sugereazaDataSiOraVizionare(Angajat angajat) {
        for (Map.Entry<Date, List<String>> entry : this.angajat.getDisponibilitate().entrySet()) {
            Date data = entry.getKey();
            List<String> ore = entry.getValue();
            if (!ore.isEmpty()) {
                this.dataVizionare = data;
                this.oraVizionare = ore.get(0);
                this.angajat.eliminaDisponibilitate(data, this.oraVizionare);
                return;
            }
        }
        this.dataVizionare = null;
        this.oraVizionare = null;
    }

    // Metodă pentru generarea mesajului de vizionare
    public String genereazaMesajVizionare() {
        if (dataVizionare != null && oraVizionare != null) {
            return "Angajatul " + angajat.getNumeAngajat() + " " + angajat.getPrenumeAngajat() + " este disponibil pentru vizionare la data și ora: " + dataVizionare + " " + oraVizionare;
        } else {
            return "Nu există niciun loc disponibil pentru vizionare.";
        }
    }
}

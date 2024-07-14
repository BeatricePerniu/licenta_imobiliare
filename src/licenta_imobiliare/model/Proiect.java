package licenta_imobiliare.model;

import java.util.ArrayList;
import java.util.List;

public class Proiect {
    private String idProiect;
    private String numeProiect;
    private String adresaProiect;
    private List<Apartament> apartamente;
    private List<Parcare> parcari;

    // Constructor implicit
    public Proiect() {
        this.apartamente = new ArrayList<>();
        this.parcari = new ArrayList<>();
    }

    // Constructor cu parametri
    public Proiect(String idProiect, String numeProiect, String adresaProiect) {
        this.idProiect = idProiect;
        this.numeProiect = numeProiect;
        this.adresaProiect = adresaProiect;
        this.apartamente = new ArrayList<>();
        this.parcari = new ArrayList<>();
    }

    // Getters and Setters
    public String getIdProiect() {
        return idProiect;
    }

    public void setIdProiect(String idProiect) {
        this.idProiect = idProiect;
    }

    public String getNumeProiect() {
        return numeProiect;
    }

    public void setNumeProiect(String numeProiect) {
        this.numeProiect = numeProiect;
    }

    public String getAdresaProiect() {
        return adresaProiect;
    }

    public void setAdresaProiect(String adresaProiect) {
        this.adresaProiect = adresaProiect;
    }

    public List<Apartament> getApartamente() {
        return apartamente;
    }

    public void setApartamente(List<Apartament> apartamente) {
        this.apartamente = apartamente;
    }

    public List<Parcare> getParcari() {
        return parcari;
    }

    public void setParcari(List<Parcare> parcari) {
        this.parcari = parcari;
    }



    @Override
    public String toString() {
        return numeProiect; // Pentru afișarea în JComboBox
    }
}

package licenta_imobiliare.model;

import licenta_imobiliare.util.IDGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Angajat {
    private String idAngajat;
    private String numeAngajat;
    private String prenumeAngajat;
    private String cnpAngajat;
    private int nrTelAngajat;
    private Date dataNastereAngajat;
    private String emailAngajat;
    private String sectorAngajat;
    private HashMap<Date, List<String>> disponibilitate; // Date și ore disponibile


    public Angajat() {
        this.disponibilitate = new HashMap<>();
    }


    public Angajat(String numeAngajat, String prenumeAngajat) {
        this.numeAngajat = numeAngajat;
        this.prenumeAngajat = prenumeAngajat;
        this.idAngajat = generateIdAngajat(numeAngajat, prenumeAngajat);
        this.disponibilitate = new HashMap<>();
    }

    // Getters and Setters
    public String getIdAngajat() {
        return idAngajat;
    }

    public void setIdAngajat(String idAngajat) {
        this.idAngajat = idAngajat;
    }

    public String getNumeAngajat() {
        return numeAngajat;
    }

    public void setNumeAngajat(String numeAngajat) {
        this.numeAngajat = numeAngajat;
    }

    public String getPrenumeAngajat() {
        return prenumeAngajat;
    }

    public void setPrenumeAngajat(String prenumeAngajat) {
        this.prenumeAngajat = prenumeAngajat;
    }

    public String getCnpAngajat() {
        return cnpAngajat;
    }

    public void setCnpAngajat(String cnpAngajat) {
        this.cnpAngajat = cnpAngajat;
    }

    public int getNrTelAngajat() {
        return nrTelAngajat;
    }

    public void setNrTelAngajat(int nrTelAngajat) {
        this.nrTelAngajat = nrTelAngajat;
    }

    public Date getDataNastereAngajat() {
        return dataNastereAngajat;
    }

    public void setDataNastereAngajat(Date dataNastereAngajat) {
        this.dataNastereAngajat = dataNastereAngajat;
    }

    public String getEmailAngajat() {
        return emailAngajat;
    }

    public void setEmailAngajat(String emailAngajat) {
        this.emailAngajat = emailAngajat;
    }

    public String getSectorAngajat() {
        return sectorAngajat;
    }

    public void setSectorAngajat(String sectorAngajat) {
        this.sectorAngajat = sectorAngajat;
    }

    public HashMap<Date, List<String>> getDisponibilitate() {
        return disponibilitate;
    }

    public void setDisponibilitate(HashMap<Date, List<String>> disponibilitate) {
        this.disponibilitate = disponibilitate;
    }



    // Eliminarea disponibilității pentru o anumită dată și oră
    public void eliminaDisponibilitate(Date data, String ora) {
        if (disponibilitate.containsKey(data)) {
            disponibilitate.get(data).remove(ora);
            if (disponibilitate.get(data).isEmpty()) {
                disponibilitate.remove(data);
            }
        }
    }

    // Metodă pentru generarea ID-ului
    private String generateIdAngajat(String numeAngajat, String prenumeAngajat) {
        String uniqueCode = IDGenerator.generateUniqueId().substring(0, 8);
        return numeAngajat + "_" + prenumeAngajat + "_" + uniqueCode;
    }

    @Override
    public String toString() {
        return numeAngajat + " " + prenumeAngajat;
    }
}

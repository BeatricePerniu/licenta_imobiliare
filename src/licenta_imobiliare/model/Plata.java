package licenta_imobiliare.model;

import java.util.Date;

public class Plata {
    private String idPlata;
    private double suma;
    private Date data;
    private Client client;
    private Factura factura;

    // Constructori
    public Plata() {}

    public Plata(String idPlata, double suma, Date data, Client client, Factura factura) {
        this.idPlata = idPlata;
        this.suma = suma;
        this.data = data;
        this.client = client;
        this.factura = factura;
    }

    // Getters și Setters
    public String getIdPlata() {
        return idPlata;
    }

    public void setIdPlata(String idPlata) {
        this.idPlata = idPlata;
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }


    public void inregistreazaPlata(double sumaPlatita) {
        this.suma = sumaPlatita;
        if (sumaPlatita >= (factura.getPretApartament() + factura.getTva())) {
            factura.setEstePlatita(true);
            System.out.println("Plata înregistrată cu succes.");
        } else {
            System.out.println("Suma plătită este insuficientă.");
        }
    }




}

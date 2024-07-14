package licenta_imobiliare.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Factura {
    private String idFactura;
    private Date dataEmiterii;
    private Client client;
    private Apartament apartament;
    private String adresaFirma = "Calea Floreasca, Nr. 211, Bucuresti";
    private String numeFirma = "SC SKY REAL ESTATE SRL";
    private String regComFirma = "J40/12345/2021";
    private String cifFirma = "RO12345678";
    private String emailFirma = "office@skyrealestate.ro";
    private String telFirma = "021-123-4567";
    private String bancaFirma = "BCR";
    private String contFirma = "RO12BCR1234567890123456";
    private double pretApartament;
    private double tva;
    private boolean estePlatita;
    private Date dataScadenta;
    private List<Plata> plati;

    // Constructor implicit
    public Factura() {
        this.plati = new ArrayList<>();
        this.estePlatita = false;
    }

    // Constructor cu parametri
    public Factura(String idFactura, Date dataEmiterii, Client client, Apartament apartament, double pretApartament, Date dataScadenta) {
        this.idFactura = idFactura;
        this.dataEmiterii = dataEmiterii;
        this.client = client;
        this.apartament = apartament;
        this.pretApartament = pretApartament;
        this.tva = calculeazaTVA(pretApartament);
        this.estePlatita = false;
        this.dataScadenta = dataScadenta;
        this.plati = new ArrayList<>();
    }

    // Getters și Setters
    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public Date getDataEmiterii() {
        return dataEmiterii;
    }

    public void setDataEmiterii(Date dataEmiterii) {
        this.dataEmiterii = dataEmiterii;
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

    public String getAdresaFirma() {
        return adresaFirma;
    }

    public String getNumeFirma() {
        return numeFirma;
    }

    public String getRegComFirma() {
        return regComFirma;
    }

    public String getCifFirma() {
        return cifFirma;
    }

    public String getEmailFirma() {
        return emailFirma;
    }

    public String getTelFirma() {
        return telFirma;
    }

    public String getBancaFirma() {
        return bancaFirma;
    }

    public String getContFirma() {
        return contFirma;
    }

    public double getPretApartament() {
        return pretApartament;
    }

    public void setPretApartament(double pretApartament) {
        this.pretApartament = pretApartament;
        this.tva = calculeazaTVA(pretApartament);
    }

    public double getTva() {
        return tva;
    }
    public void setTva(double tva)
    {
        this.tva=tva;
    }

    public boolean isEstePlatita() {
        return estePlatita;
    }

    public void setEstePlatita(boolean estePlatita) {
        this.estePlatita = estePlatita;
    }

    public Date getDataScadenta() {
        return dataScadenta;
    }

    public void setDataScadenta(Date dataScadenta) {
        this.dataScadenta = dataScadenta;
    }

    public List<Plata> getPlati() {
        return plati;
    }

    public void setPlati(List<Plata> plati) {
        this.plati = plati;
    }

    // Metodă pentru calcularea TVA-ului
    private double calculeazaTVA(double pret) {
        return pret * 0.20;
    }



    // Metodă pentru generarea textului facturii
    public String genereazaTextFactura() {
        return "SC " + getNumeFirma() + "\t\t\tClient: " + client.getNume() + " " + client.getPrenume() + "\n" +
                "Nr. Reg. com.: " + getRegComFirma() + "\t\t\tCNP: " + client.getCnp() + "\n" +
                "CIF: " + getCifFirma() + "\n" +
                "Adresa: " + getAdresaFirma() + "\t\t\tAdresa: " + client.getDomiciliu() + "\n" +
                "Email: " + getEmailFirma() + "\n" +
                "Tel: " + getTelFirma() + "\tBanca: " + getBancaFirma() + "\n" +
                "Banca: " + getBancaFirma() + "\tCont: " + getContFirma() + "\n\n" +
                "Factura seria: ____ Nr.: " + getIdFactura() + " Data: " + getDataEmiterii() + "\tCota TVA: 20%\n\n" +
                "Denumire produse sau servicii\t\tU.M.\tCant.\tPret EUR\t\tTVA\tTotal\n\n" +
                "Apartament " + apartament.getIdApartament() + "\tbuc\t\t1\t" + getPretApartament() + "\t" + getTva() + "\t" + (getPretApartament() + getTva()) + "\n\n" +
                "Pret fara TVA\t\t\t\t" + getPretApartament() + "\n" +
                "Valoare TVA\t\t\t\t" + getTva() + "\n" +
                "Total de plata\t\t\t\t" + (getPretApartament() + getTva()) + "\n" +
                "TVA la incasare.\n";
    }
    public double getTotal() {
        return pretApartament + tva;
    }

    @Override
    public String toString() {
        return "Factura{idFactura='" + idFactura + "'}";
    }
}

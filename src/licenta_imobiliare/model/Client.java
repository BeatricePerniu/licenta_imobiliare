package licenta_imobiliare.model;

import java.util.Date;

public class Client {
    private String idClient;
    private String nume;
    private String prenume;
    private String cnp;
    private String serie;
    private int nrBuletin;
    private Date dataNastere;
    private String domiciliu;
    private String email;
    private int telefon;

    public Client() {}

    public Client(String idClient, String nume, String prenume, String cnp, String serie, int nrBuletin, Date dataNastere, String domiciliu, String email, int telefon) {
        this.idClient = idClient;
        this.nume = nume;
        this.prenume = prenume;
        this.cnp = cnp;
        this.serie = serie;
        this.nrBuletin = nrBuletin;
        this.dataNastere = dataNastere;
        this.domiciliu = domiciliu;
        this.email = email;
        this.telefon = telefon;
    }
    @Override
    public String toString() {
        return nume + " " + prenume;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }



    public void setNrBuletin(int nrBuletin) {
        this.nrBuletin = nrBuletin;
    }

    public Date getDataNastere() {
        return dataNastere;
    }

    public void setDataNastere(Date dataNastere) {
        this.dataNastere = dataNastere;
    }

    public String getDomiciliu() {
        return domiciliu;
    }

    public void setDomiciliu(String domiciliu) {
        this.domiciliu = domiciliu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefon() {
        return telefon;
    }

    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }

   public int getNrBuletin()
   {
       return nrBuletin;
   }
}

package licenta_imobiliare.model;

public class Apartament {
    private String idApartament;
    private int suprafataUtila;
    private int suprafataTotala;
    private int pret;
    private int etaj;
    private boolean chirie;
    private String idProiect;
    private int hol;
    private int camere;
    private int baie;
    private int bucatarie;
    private Proiect proiect;



    public Apartament() {}
    public Apartament(String idApartament, int suprafataUtila, int suprafataTotala, int pret, int etaj, boolean chirie, String idProiect, int hol, int camere, int baie, int bucatarie) {
        this.idApartament = idApartament;
        this.suprafataUtila = suprafataUtila;
        this.suprafataTotala = suprafataTotala;
        this.pret = pret;
        this.etaj = etaj;
        this.chirie = chirie;
        this.idProiect = idProiect;
        this.hol = hol;
        this.camere = camere;
        this.baie = baie;
        this.bucatarie = bucatarie;
    }
    @Override
    public String toString() {
        return idApartament;
    }


    public String getIdApartament() {
        return idApartament;
    }

    public void setIdApartament(String idApartament) {
        this.idApartament = idApartament;
    }

    public int getSuprafataUtila() {
        return suprafataUtila;
    }

    public void setSuprafataUtila(int suprafataUtila) {
        this.suprafataUtila = suprafataUtila;
    }

    public int getSuprafataTotala() {
        return suprafataTotala;
    }

    public void setSuprafataTotala(int suprafataTotala) {
        this.suprafataTotala = suprafataTotala;
    }

    public int getPret() {
        return pret;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    public int getEtaj() {
        return etaj;
    }

    public void setEtaj(int etaj) {
        this.etaj = etaj;
    }

    public boolean isChirie() {
        return chirie;
    }

    public void setChirie(boolean chirie) {
        this.chirie = chirie;
    }

    public String getIdProiect() {
        return idProiect;
    }

    public void setIdProiect(String idProiect) {
        this.idProiect = idProiect;
    }

    public int getHol() {
        return hol;
    }

    public void setHol(int hol) {
        this.hol = hol;
    }

    public int getCamere() {
        return camere;
    }

    public void setCamere(int camere) {
        this.camere = camere;
    }

    public int getBaie() {
        return baie;
    }

    public void setBaie(int baie) {
        this.baie = baie;
    }

    public int getBucatarie() {
        return bucatarie;
    }

    public void setBucatarie(int bucatarie) {
        this.bucatarie = bucatarie;
    }
    public Proiect getProiect() { // Add this getter
        return proiect;
    }

    public void setProiect(Proiect proiect) { // Add this setter
        this.proiect = proiect;
    }
}

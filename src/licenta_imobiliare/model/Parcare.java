package licenta_imobiliare.model;

public class Parcare {
    private String idParcare;
    private int numarLoc;
    private double pretVanzare;
    private double pretChirie;
    private String idProiect;

    // Constructor implicit
    public Parcare() {
    }

    // Constructor cu parametri
    public Parcare(String idParcare, int numarLoc, double pretVanzare, double pretChirie, String idProiect) {
        this.idParcare = idParcare;
        this.numarLoc = numarLoc;
        this.pretVanzare = pretVanzare;
        this.pretChirie = pretChirie;
        this.idProiect = idProiect;
    }

    // Getters and Setters
    public String getIdParcare() {
        return idParcare;
    }

    public void setIdParcare(String idParcare) {
        this.idParcare = idParcare;
    }

    public int getNumarLoc() {
        return numarLoc;
    }

    public void setNumarLoc(int numarLoc) {
        this.numarLoc = numarLoc;
    }

    public double getPretVanzare() {
        return pretVanzare;
    }

    public void setPretVanzare(double pretVanzare) {
        this.pretVanzare = pretVanzare;
    }

    public double getPretChirie() {
        return pretChirie;
    }

    public void setPretChirie(double pretChirie) {
        this.pretChirie = pretChirie;
    }

    public String getIdProiect() {
        return idProiect;
    }

    public void setIdProiect(String idProiect) {
        this.idProiect = idProiect;
    }
}

package licenta_imobiliare.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public abstract class Contract {
    private String idContract;
    private Date dataInceput;
    private String adresaFirma = "Calea Floreasca, Nr. 211, Bucuresti";
    private String numeFirma = "SKY REAL ESTATE SRL";
    private String regComFirma = "J40/12345/2021";
    private Client client;
    private Proiect proiect;
public Contract()
{

}

    public Contract(String idContract, Date dataInceput, Client client) {
        this.idContract = idContract;
        this.dataInceput = dataInceput;
        this.client = client;
    }


    public Contract(String idContract, Date dataInceput, Client client, Proiect proiect) {
        this.idContract = idContract;
        this.dataInceput = dataInceput;
        this.client = client;
        this.proiect = proiect;
    }

    // Getters and Setters
    public String getIdContract() {
        return idContract;
    }

    public void setIdContract(String idContract) {
        this.idContract = idContract;
    }

    public Date getDataInceput() {
        return dataInceput;
    }

    public void setDataInceput(Date dataInceput) {
        this.dataInceput = dataInceput;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Proiect getProiect() {
        return proiect;
    }

    public void setProiect(Proiect proiect) {
        this.proiect = proiect;
    }


    public abstract Apartament getApartament();


    public abstract String genereazaTextContract();

    public abstract void exportaPdf(String fileName) throws IOException;


    protected void addTextToPDF(PDDocument document, String text, File fontFile) throws IOException {
        PDType0Font font = PDType0Font.load(document, fontFile);
        float fontSize = 12;
        float leading = 14.5f;

        PDPageContentStream contentStream = new PDPageContentStream(document, new PDPage());
        contentStream.setFont(font, fontSize);
        contentStream.setLeading(leading);
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 750);

        String[] lines = text.split("\n");
        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLine();
        }

        contentStream.endText();
        contentStream.close();
    }
}

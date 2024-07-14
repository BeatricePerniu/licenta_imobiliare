package licenta_imobiliare.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ContractDeInchiriere extends Contract {
    private Apartament apartament;
    private double chirieLunara;

    public ContractDeInchiriere() {}

    public ContractDeInchiriere(String idContract, Date dataInceput, Client client, Apartament apartament, double chirieLunara, Proiect proiect) {
        super(idContract, dataInceput, client, proiect);
        this.apartament = apartament;
        this.chirieLunara = chirieLunara;
    }

    public Apartament getApartament() {
        return apartament;
    }

    public void setApartament(Apartament apartament) {
        this.apartament = apartament;
    }

    public double getChirieLunara() {
        return chirieLunara;
    }

    public void setChirieLunara(double chirieLunara) {
        this.chirieLunara = chirieLunara;
    }

    @Override
    public String genereazaTextContract() {
        Client client = getClient();
        Apartament apartament = getApartament();
        Proiect proiect = getProiect();
        return "Contract de închiriere\n" +
                "(variantă)\n" +
                "Încheiat astăzi " + getDataInceput() + "\n" +
                "la .............................................\n" +
                "I. PĂRŢILE CONTRACTANTE\n" +
                "1.1. " + client.getNume() + " " + client.getPrenume() + ", cu domiciliul în " + client.getDomiciliu() + ", posesor al CI seria " + client.getSerie() + " nr. " + client.getNrBuletin() + " CNP" + client.getCnp() + ", în calitate de locatar și\n" +
                "S.C. " + getNumeFirma() + " S.R.L. cu sediul social în " + getAdresaFirma() + ", având codul unic de înregistrare nr. " + getRegComFirma() + ", reprezentată prin ........................, în calitate de locator,\n" +
                "au convenit să încheie prezentul contract de închiriere, cu respectarea dispoziţiilor art. 1777-1835 Cod civil şi a următoarelor clauze:\n" +
                "II. OBIECTUL CONTRACTULUI\n" +
                "2.1. Obiectul contractului îl constituie cedarea folosinţei bunului imobil apartament nr. " + apartament.getIdApartament() + " situat în " + proiect.getNumeProiect() + "," + proiect.getAdresaProiect() + " cu suprafata de " + apartament.getSuprafataTotala() + " mp.\n" +
                "2.2. Predarea-primirea bunului care face obiectul prezentului contract de închiriere este consemnată în procesul-verbal anexa ................ .\n" +
                "III. TERMENUL CONTRACTULUI\n" +
                "3.1. Termenul contractului este de ....... luni/ani cu începere de la data procesului-verbal de predare-primire.\n" +
                "IV. PREŢUL CONTRACTULUI\n" +
                "4.1. Preţul închirierii – chiria – pentru folosirea bunului care face obiectul închirierii este de " + chirieLunara + " LEI/EURO/USD pe lună.\n" +
                "4.2. Plata chiriei se face lunar până cel mai târziu în ultima zi a lunii pentru care se face plata.\n" +
                "4.3. Plata chiriei se face în următoarele conturi:\n" +
                "a) contul locatorului nr. ............................ deschis la Banca .......................................... ;\n" +
                "b) contul locatarului nr. ............................ deschis la Banca .......................................... .\n" +
                "V. OBLIGAŢIILE PĂRŢILOR\n" +
                "5.1. Locatorul se obligă:\n" +
                "a) să predea bunul cu toate accesoriile sale în starea corespunzătoare folosinţei pentru care a fost încheiat şi să-l întreţină în această stare pe toată durata închirierii;\n" +
                "b) să execute la timp şi în bune condiţii toate lucrările de reparaţii care nu sunt în sarcina locatarului;\n" +
                "c) să asigure folosinţa bunului închiriat pe toată durata contractului, garantând pe locatar contra pierderii totale sau parţiale a bunului, contra viciilor ascunse ori contra tulburării folosinţei bunului.\n" +
                "5.2. Obligaţiile locatarului sunt următoarele:\n" +
                "a) să folosească bunul închiriat potrivit destinaţiei rezultate din prezentul contract;\n" +
                "b) să execute la timp şi în bune condiţii lucrările de întreţinere şi reparaţii ce îi revin;\n" +
                "c) să plătească chiria în cuantumul şi la termenele stabilite prin prezentul contract;\n" +
                "d) la încheierea duratei contractului, să restituie bunul închiriat în starea în care l-a primit, adică în stare bună.\n" +
                "5.3. Locatorul are obligaţia de a suporta toate sarcinile şi impozitele datorate pentru bunul închiriat.\n" +
                "5.4. Taxele comunale precum şi utilităţile consumate sunt în sarcina locatarului.\n" +
                "VI. SUBÎNCHIRIEREA ŞI CESIUNEA CONTRACTULUI\n" +
                "6.1. Subînchirierea în tot sau în parte a bunului închiriat sau cesiunea contractului de închiriere unui terţ este permisă numai cu acordul prealabil dat în scris de locator şi cu respectarea condiţiilor şi aprobărilor cerute pentru închiriere.\n" +
                "VII. RĂSPUNDEREA CONTRACTUALĂ\n" +
                "7.2. Pentru neexecutarea sau executarea necorespunzătoare a obligaţiilor contractuale, părţile datorează daune-interese.\n" +
                "7.3. Neplata chiriei la termen îl autorizează pe locator să rezilieze contractul şi să ceară daune-interese.\n" +
                "VIII. FORŢA MAJORĂ\n" +
                "8.1. Nici una dintre părţile contractante nu răspunde de neexecutarea la termen sau/şi de executarea în mod necorespunzător – total sau parţial – a oricărei obligaţii care îi revine în baza prezentului contract, dacă neexecutarea sau/şi executarea obligaţiei respective a fost cauzată de forţa majoră aşa cum este definită de lege.\n" +
                "8.2. Partea care invocă forţa majoră este obligată să notifice celeilalte părţi, în termen de ................, producerea evenimentului şi să ia toate măsurile posibile în vederea limitării consecinţelor lui.\n" +
                "8.3. Dacă în termen de ...................... de la producere, evenimentul respectiv nu încetează, părţile au dreptul să-şi notifice încetarea de plin drept a prezentului contract, fără ca vreuna dintre ele să pretindă daune-interese.\n" +
                "IX. NOTIFICĂRILE ÎNTRE PĂRŢI\n" +
                "9.1. În accepţiunea părţilor contractante, orice notificare adresată de una dintre acestea celeilalte este valabil îndeplinită dacă va fi transmisă la adresa/sediul prevăzut în partea introductivă a prezentului contract.\n" +
                "9.2. În cazul în care notificarea se face pe cale poştală, ea va fi transmisă prin scrisoare recomandată, cu confirmare de primire (A.R.) şi se consideră primită de destinatar la data menţionată de oficiul poştal primitor pe această confirmare.\n" +
                "9.3. Dacă notificarea se trimite prin telefax, ea se consideră primită în prima zi lucrătoare după cea în care a fost expediată.\n" +
                "9.4. Notificările verbale nu se iau în considerare de nici una dintre părţi, dacă nu sunt confirmate prin intermediul uneia dintre modalităţile prevăzute la alineatele precedente.\n" +
                "X. SOLUŢIONAREA LITIGIILOR\n" +
                "10.1. Orice litigiu decurgând din sau în legătură cu prezentul contract, inclusiv referitor la încheierea, executarea ori desfiinţarea lui, dacă nu se poate rezolva pe cale amiabilă de reprezentanţii părţilor se va soluţiona prin arbitrajul Curţii de Arbitraj Comercial Internaţional de pe lângă Camera de Comerţ şi Industrie a României, în conformitate cu Regulile de procedură arbitrală ale acestei Curţi.\n" +
                "10.2. Hotărârea arbitrală este definitivă şi obligatorie.\n" +
                "sau\n" +
                "10.1. În cazul în care eventualele neînţelegeri privind validitatea prezentului contract sau rezultate din interpretarea, executarea sau încetarea lui nu se vor putea rezolva pe cale amiabilă, părţile au convenit să se adreseze instanţelor judecătoreşti competente.\n" +
                "XI. CLAUZE GENERALE REFERITOARE LA PROTECȚIA DATELOR CU CARACTER PERSONAL\n" +
                "11.1. Părțile colectează și prelucrează datele cu caracter personal înscrise în prezentul contract în conformitate cu legislația în vigoare, în modalități care asigură confidențialitatea și securitatea adecvată a acestor date, în vederea asigurării protecției împotriva prelucrării neautorizate sau ilegale și împotriva pierderii, a distrugerii sau a deteriorării accidentale.\n" +
                "11.2. În procesul de prelucrare a datelor cu caracter personal, părțile aplică prevederile Regulamentului (UE) 2016/679 al Parlamentului European și al Consiliului din 27 aprilie 2016 privind protecția persoanelor fizice în ceea ce privește prelucrarea datelor cu caracter personal și privind libera circulație a acestor date și de abrogare a Directivei 95/46/CE (regulamentul general privind protecția datelor) și ale legislației naționale.\n" +
                "11.3. Datele cu caracter personal comunicate în cadrul prezentului contract, vor fi prelucrate în scopul executării prezentului contract.\n" +
                "11.4. Datele cu caracter personal colectate și prelucrate în vederea executării prezentului contract sunt următoarele: nume și prenume, adresă, serie și număr carte de identitate, cod numeric personal, număr de telefon/fax, adresă de poștă electronică, cod bancar.\n" +
                "11.5. Datele personale, comunicate în cadrul prezentului contract, pot fi comunicate instituțiilor publice, în conformitate cu prevederile legale in vigoare.\n" +
                "11.6. În situația în care este necesară prelucrarea datelor personale în alte scopuri decât cele prevăzute la pct. 11.3, partea care realizează prelucrarea va informa cealaltă parte și îi va solicita acordul scris cu privire la prelucrarea datelor cu caracter personal, în conformitate cu prevederile legislației în vigoare.\n" +
                "11.7. Părțile își garantează reciproc dreptul la informare și acces la datele cu caracter personal, dreptul la rectificare, actualizare, portabilitate, ștergere, la restricționare și opoziție în conformitate cu prevederile legislației în vigoare.\n" +
                "11.8. Datele personale înscrise în prezentul contract sunt păstrate pe întreaga perioadă de executare a contractului și ulterior încetării acestuia, în conformitate cu prevederile legale referitoare la arhivarea documentelor.\n" +
                "11.9. În contextul Regulamentului (UE) 2016/679 al Parlamentului European și al Consiliului din 27 aprilie 2016 privind protecția persoanelor fizice în ceea ce privește prelucrarea datelor cu caracter personal și privind libera circulație a acestor date, prelucrare înseamnă orice operațiune sau set de operațiuni efectuate asupra datelor cu caracter personal sau asupra seturilor de date cu caracter personal, cu sau fără utilizarea de mijloace automatizate, cum ar fi colectarea, înregistrarea, organizarea, structurarea, stocarea, adaptarea sau modificarea, extragerea, consultarea, utilizarea, divulgarea prin transmitere, diseminarea sau punerea la dispoziție în orice alt mod, alinierea sau combinarea, restricționarea, ștergerea sau distrugerea.\n" +
                "XII. CLAUZE SPECIALE\n" +
                "12.1. ..................................................................................................................................................... .\n" +
                "12.2. ..................................................................................................................................................... .\n" +
                "XIII. CLAUZE FINALE\n" +
                "13.1. Modificarea prezentului contract se face numai prin act adiţional încheiat între părţile contractante.\n" +
                "13.2. Prezentul contract, împreună cu anexele sale care fac parte integrantă din cuprinsul său, reprezintă voinţa părţilor şi înlătură orice altă înţelegere verbală dintre acestea, anterioară sau ulterioară încheierii lui.\n" +
                "13.3. Prezentul contract s-a încheiat într-un număr de ........... exemplare, din care ............................ .\n" +
                "LOCATOR\n" +
                getNumeFirma() + "\n" +
                "LOCATAR\n" +
                client.getNume() + " " + client.getPrenume();
    }

    @Override
    public void exportaPdf(String filePath) throws IOException {
        String text = genereazaTextContract();
        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf"); // Change this path as needed

        try (PDDocument document = new PDDocument()) {
            addTextToPDF(document, text, fontFile);
            document.save(filePath);
            System.out.println("PDF saved successfully to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while generating PDF: " + e.getMessage());
        }
    }

    public void addTextToPDF(PDDocument document, String text, File fontFile) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);

        PDType0Font font = PDType0Font.load(document, fontFile);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 700);

        String[] lines = text.split("\n");
        float yPosition = 700;
        float leading = 14.5f;

        for (String line : lines) {
            if (yPosition < 50) {
                contentStream.endText();
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(25, 700);
                yPosition = 700;
            }
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -leading);
            yPosition -= leading;
        }

        contentStream.endText();
        contentStream.close();
    }
}

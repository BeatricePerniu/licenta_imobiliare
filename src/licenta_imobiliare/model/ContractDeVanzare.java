package licenta_imobiliare.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ContractDeVanzare extends Contract {
    private Apartament apartament;
    private double pretVanzare;

    public ContractDeVanzare() {}

    public ContractDeVanzare(String idContract, Date dataInceput, Client client, Apartament apartament, double pretVanzare) {
        super(idContract, dataInceput, client);
        this.apartament = apartament;
        this.pretVanzare = pretVanzare;
    }

    public ContractDeVanzare(String idContract, Date dataInceput, Client client, Apartament apartament, double pretVanzare, Proiect proiect) {
        super(idContract, dataInceput, client, proiect);
        this.apartament = apartament;
        this.pretVanzare = pretVanzare;
    }

    public Apartament getApartament() {
        return apartament;
    }

    public void setApartament(Apartament apartament) {
        this.apartament = apartament;
    }

    public double getPretVanzare() {
        return pretVanzare;
    }

    public void setPretVanzare(double pretVanzare) {
        this.pretVanzare = pretVanzare;
    }

    @Override
    public String genereazaTextContract() {
        Client client = getClient();
        Apartament apartament = getApartament();
        Proiect proiect = getProiect();
        return "C O N T R A C T D E V Â N Z A R E\n\n" +
                "Între:\n" +
                getNumeFirma() + " S.R.L. cu sediul social în " + getAdresaFirma() + ", având codul unic de înregistrare nr. " + getRegComFirma() +
                " în calitate de vânzător, pe de o parte și\n" +
                client.getNume() + " " + client.getPrenume() + ", cetățean român, domiciliat în " + client.getDomiciliu() + ", posesor al CI seria " + client.getSerie() + " nr. " + client.getNrBuletin() + "\n" +
                "CNP " + client.getCnp() + ",\n" +
                "în calitate de cumpărător, pe de altă parte,\n\n" +
                "a intervenit prezentul CONTRACT DE VÂNZARE, în următoarele condiții:\n\n" +
                "Eu, " + client.getNume() + " " + client.getPrenume() + ", vând _________, dreptul de proprietate asupra apartamentului nr. " + apartament.getIdApartament() + "\n" +
                "situat în " + proiect.getNumeProiect() + ", " + proiect.getAdresaProiect() + "\n" +
                "din " + apartament.getEtaj() + " camere și dependințe, respectiv: " + apartament.getCamere() + ", având o suprafață utilă de " + apartament.getSuprafataUtila() + " mp rezultată în urma\n" +
                "măsurătorilor cadastrale, rezultând o suprafață totală de " + apartament.getSuprafataTotala() + " mp, împreună cu cota parte de ____% aferentă\n" +
                "apartamentului din dreptul de proprietate asupra părților și dependințelor comune ale imobilului bloc care, prin\n" +
                "natura și destinația lor sunt în proprietate comună, forțată și perpetuă a tuturor proprietarilor, precum și cota\n" +
                "parte de teren aferentă apartamentului, reprezentând suprafața de ____ mp, atribuit în folosință pe durata\n" +
                "existenței construcției, care urmează regimul juridic prevăzut de art. 36 din Legea nr. 18/1991, republicată cu\n" +
                "modificările și completările ulterioare, denumit în continuare \"Imobilul\" " + proiect.getNumeProiect() + ".\n" +
                "Imobilul are numărul cadastral _______ și este intabulat în Cartea Funciară nr.________, a\n" +
                "localității_______, fiind intabulat pe numele vânzătorului conform încheierii nr. _____ din _____, emisă de\n" +
                "OCPI _____- BCPI –______.\n" +
                "Subsemnatul " + getNumeFirma() + ", declar că am dobândit dreptul de proprietate asupra imobilului\n" +
                "astfel_________.\n" +
                "Imobilul ce face obiectul prezentului contract, astfel cum a fost descris mai sus, se află în circuitul\n" +
                "civil de la data dobândirii în proprietate, nu a fost naționalizat, nu a fost înstrăinat sub nicio formă, nu\n" +
                "constituie aport în natură sau punct de lucru în cadrul vreunei societăți comerciale, asociații sau fundații,\n" +
                "nu a fost promis spre vânzare sau orice alt act de înstrăinare și nici spre ipotecare, nu a făcut obiectul\n" +
                "unui contract de închiriere, comodat sau orice alt act de transmitere a dreptului de folosință asupra imobilului,\n" +
                "nu există niciun litigiu pe rolul instanțelor judecătorești cu privire la acesta, nu este revendicat, nu există cereri\n" +
                "depuse în baza Legii nr. 10/2001, a Legii nr. 112/1995, a Legii nr. 247/2005 sau litigii de orice natură în\n" +
                "legătură cu acesta, fiind liber de orice sarcini sau servituți, astfel cum rezultă din extrasul de carte funciară pentru\n" +
                "autentificare nr. ______din _____ emis de Oficiul de Cadastru și Publicitate Imobiliară_______– Biroul de\n" +
                "Cadastru și Publicitate Imobiliară______.\n" +
                "Impozitele și taxele de orice natură sunt în sarcina vânzătorului până astăzi, data autentificării\n" +
                "contractului de vânzare și sunt achitate la zi, astfel cum rezultă din certificatul de atestare fiscală privind\n" +
                "impozitele și taxele locale nr. _____ din _____eliberat de Primăria_______, dată de la care acestea trec\n" +
                "asupra cumpărătoarei care suportă și taxele de autentificare ale prezentului înscris.\n" +
                "În conformitate cu dispozițiile art. 33, alineat 2, din Legea nr. 196/2018, apartamentul are achitate\n" +
                "la zi cotele de contribuție la cheltuielile asociației de proprietari, astfel cum rezultă din Adeverința nr. ____,\n" +
                "din _______eliberată de Asociația de Proprietari, orice alte datorii față de asociația de proprietari scadente\n" +
                "până la data autentificării, urmând a le suporta vânzătorul.\n" +
                "Toate cheltuielile privind întreținerea, consumul de energie electrică, gaze naturale, apă, sau orice alte\n" +
                "plăți curente privind folosința imobilului ce se înstrăinează, au fost achitate la zi de mine, vânzătorul, conform\n" +
                "facturilor existente pe care le pun la dispoziția cumpărătorului împreună cu dovada plăților efectuate. Mă oblig\n" +
                "să achit integral toate datoriile referitoare la folosința imobilului până în momentul eliberării lui efective, pe\n" +
                "baza facturilor ce îmi vor fi remise de cumpărător în momentul comunicării lor de către emitenți.\n" +
                "Subsemnatul " + client.getNume() + " " + client.getPrenume() + ", în calitate de cumpărător, declar că astăzi, data autentificării\n" +
                "contractului de vânzare, având ca obiect un imobil cu destinația de locuință, vânzătorul, " + client.getNume() + ", mi-a\n" +
                "înmânat în original certificatul de performanță energetică a imobilului, înregistrat în registrul auditorului cu\n" +
                "nr. ______din data de ______, certificat emis de inginer ______, auditor energetic grad ______atestat conform\n" +
                "certificatului de atestare auditor energetic pentru clădiri seria ____ nr. _____, și am luat cunoștință de datele\n" +
                "menționate în acesta, fiind de acord să dobândesc în aceste condiții, cu respectarea dispozițiilor Legii nr.\n" +
                "372/2005 privind performanța energetică a clădirilor, cu modificările și completările ulterioare.\n" +
                "Eu, " + getNumeFirma() + ", vânzător, înțeleg ca, în conformitate cu prevederile art. 1.672 pct. 3 Codul\n" +
                "Civil și în condițiile prevăzute de art. 1.695 Codul Civil să îl garantez pe cumpărător împotriva oricăror\n" +
                "evicțiuni.\n" +
                "De asemenea, în conformitate cu prevederile art. 1.707 Codul Civil, eu, " + getNumeFirma() + ", vânzător,\n" +
                "înțeleg să îl garantez pe cumpărător împotriva oricăror vicii ascunse care ar face bunul impropriu întrebuințării\n" +
                "sale conform scopului comunicat vânzătoarei de către cumpărător sau care i-ar micșora în asemenea măsură\n" +
                "întrebuințarea sau valoarea încât, dacă le-ar fi cunoscut, cumpărătorul nu ar mai fi cumpărat sau ar fi dat un\n" +
                "preț mai mic.\n" +
                "În conformitate cu prevederile art. 1.672 pct.1 Codul Civil și în condițiile prevăzute de art. 1.673\n" +
                "Codul Civil, eu, " + getNumeFirma() + ", vânzător, transmit azi, data autentificării, cumpărătorului " + client.getNume() + " " + client.getPrenume() + ",\n" +
                "dreptul de proprietate asupra apartamentului nr. " + apartament.getIdApartament() + " situat în municipiul " + proiect.getAdresaProiect() + "\n" +
                "compus din " + apartament.getCamere() + " camere și dependințe, având o\n" +
                "suprafață utilă de " + apartament.getSuprafataUtila() + " mp rezultată în urma măsurătorilor cadastrale, rezultând o suprafață totală de ______\n" +
                "mp, împreună cu cota parte de ____% aferentă apartamentului din dreptul de proprietate asupra părților și\n" +
                "dependințelor comune ale imobilului bloc care, prin natura și destinația lor sunt în proprietate comună, forțată\n" +
                "și perpetuă a tuturor proprietarilor, precum și cota parte de teren aferentă apartamentului, reprezentând\n" +
                "suprafața de ____ mp, atribuit în folosință pe durata existenței construcției.\n" +
                "De asemenea, în conformitate cu prevederile art. 1.672 pct.2 Codul Civil și în condițiile prevăzute de art.\n" +
                "1.685 Codul Civil, eu, " + proiect.getNumeProiect() + ", vânzător voi preda imobilul, astfel cum a fost descris mai sus,\n" +
                "cumpărătorului " + client.getNume() + ", la data:........, în sensul că imobilul va fi pus la dispoziția acestuia,\n" +
                "împreună cu tot ceea ce este necesar pentru exercitarea liberă și neîngrădită a proprietății și posesiei.\n" +
                "Predarea imobilului se efectuează la data de ______, prin punerea acestuia la dispoziția cumpărătorului\n" +
                "liber de orice bunuri ale vânzătoarei, în conformitate cu prevederile art. 1.687 Codul Civil.\n" +
                "Până la data predării imobilului vânzătorul va suporta plata tuturor utilităților.\n" +
                "Totodată, în conformitate cu prevederile art. 1.686 alin. 2 Codul Civil, eu, " + proiect.getNumeProiect() + ", în\n" +
                "calitate de vânzător, am predat cumpărătorului " + client.getNume() + ", toate titlurile și documentele privitoare la\n" +
                "proprietatea bunului, astfel cum acestea au fost enunțate în cuprinsul prezentului înscris.\n" +
                "Prețul acestei vânzări este " + getPretVanzare() + " EURO (_____euro), echivalenți cu ________lei, calcul efectuat la\n" +
                "cursul BNR de azi data autentificării în valoare de ______ lei/1 Euro, în vederea taxării, care s-a achitat azi,\n" +
                "data autentificarii prezentului contract de vânzare, în moneda euro, prin transfer bancar în contul nr.\n" +
                "_________deschis la ______pe numele vânzătorului " + client.getNume() + ". Documentele bancare (ordinul de plată\n" +
                "și extrasul de cont al vânzătorului) constituie chitanță liberatorie de plată pentru cumpărător, în conformitate\n" +
                "cu prevederile art. 1.500 NCC.\n" +
                "În cazul în care contul vânzătoarei " + getNumeFirma() + " nu va fi creditat in 3 zile lucrătoare de la data\n" +
                "autentificării cu prețul vânzării, vânzătorul va putea solicita rezoluțiunea (desființarea) contractului de vânzare,\n" +
                "în condițiile art. 1.757 alin. 1 Codul Civil.\n" +
                "Eu, " + client.getNume() + ", am cumpărat de la " + getNumeFirma() + ", dreptul de proprietate asupra\n" +
                "apartamentului nr. " + apartament.getIdApartament() + " situat în municipiul Bucuresti, " + proiect.getNumeProiect() + " et. " + apartament.getEtaj() + ",\n" +
                "sector ____, compus din " + apartament.getEtaj() + " camere și dependințe, respectiv:________, având o suprafață utilă de " + apartament.getSuprafataUtila() + "\n" +
                "mp rezultată în urma măsurătorilor cadastrale, rezultând o suprafață totală de " + apartament.getSuprafataTotala() + " mp, împreună cu cota\n" +
                "parte de ____% aferentă apartamentului din dreptul de proprietate asupra părților și dependințelor comune ale\n" +
                "imobilului bloc care, prin natura și destinația lor sunt în proprietate comună, forțată și perpetuă a tuturor\n" +
                "proprietarilor, precum și cota parte de teren aferentă apartamentului, reprezentând suprafața de ____ mp,\n" +
                "atribuit în folosință pe durata existenței construcției, care urmează regimul juridic prevăzut de art. 36 din\n" +
                "Legea nr. 18/1991, republicată cu modificările și completările ulterioare, denumit în continuare \"Imobilul\".\n" +
                "Totodată, eu, " + client.getNume() + " " + client.getPrenume() + ", declar că mi-am îndeplinit obligațiile ce îmi revin în conformitate cu\n" +
                "prevederile art. 1.719 Codul Civil, în sensul că:\n" +
                "- voi prelua de la vânzătoare imobilul ce mi-a fost vândut;\n" +
                "- am verificat starea acestuia, potrivit prevederilor art. 1.690 Codul Civil, pentru a descoperi viciile\n" +
                "aparente, vicii pe care nu le-am sesizat;\n" +
                "- am plătit prețul vânzării în modalitatea și în condițiile expuse în prezentul act și mă declar\n" +
                "întrutotul de acord cu conținutul prezentului înscris.\n" +
                "Eu, " + client.getNume() + " " + client.getPrenume() + ", în calitate de cumpărător, am preluat de la vânzătorul " + getNumeFirma() + ", toate\n" +
                "titlurile și documentele privitoare la proprietatea și folosința imobilului.\n" +
                "Subsemnatul " + client.getNume() + " " + client.getPrenume() + ", în calitate de cumpărător, declar pe proprie răspundere, cunoscând\n" +
                "prevederile art. 326 Cod Penal privind falsul în declarații, că sunt necăsătorit, imobilul dobândit prin\n" +
                "prezentul contract de vânzare, fiind bun propriu.\n" +
                "Lucrările de publicitate imobiliară sunt în sarcina notarului public, conform Legii nr. 7/1996\n" +
                "modificată, care prevede obligativitatea notarului public care a întocmit actul prin care se constituie, se\n" +
                "modifică, se transmite sau se stinge un drept real imobiliar, de a cere din oficiu înscrierea sa în cartea funciară,\n" +
                "în ziua întocmirii lui, sau cel mai târziu a doua zi la biroul de carte funciară în a cărui rază de activitate se află\n" +
                "imobilul.\n" +
                "Înscrierile în cartea funciară vor fi efectuate în baza prezentului contract de vânzare încheiat în formă\n" +
                "autentică, în conformitate cu prevederile art. 888 Codul Civil.\n" +
                "Noi, părțile contractante, am luat cunoștință de prevederile Codului Fiscal, ale art. 1.660 din Codul\n" +
                "Civil privind condițiile prețului vânzării (în sensul că acesta constă într-o sumă de bani, este serios și\n" +
                "determinat), ale legii privind prevenirea și combaterea evaziunii fiscale, precum și de dispozițiile Legii pentru\n" +
                "prevenirea și sancționarea spălării banilor, cu modificările ulterioare.\n" +
                "În condițiile art. 111 alin.1 din Codul Fiscal, astfel cum a fost modificat prin O.U.G. 3/2017, prezentul\n" +
                "contract de vânzare este scutit de plata impozitului datorat pe venitul rezultat din transferul dreptului de\n" +
                "proprietate din patrimoniul personal al persoanelor fizice, prețul vânzării în sumă de _________lei încadrându-se\n" +
                "în limita de 450.000 lei, ce constituie plafonul neimpozabil stabilit de lege.\n" +
                "Taxa A.N.C.P.I. pentru îndeplinirea formalităților de publicitate imobiliară și onorariul notarial au fost\n" +
                "calculate la valoarea declarată de părți de ________. Valoarea din Ghidul privind valorile minime imobiliare\n" +
                "________pentru anul 2020 este de________, conform Zona_____, .\n" +
                "Eu, " + client.getNume() + " " + client.getPrenume() + ", în calitate de cumpărător al dreptului de proprietate asupra Imobilului susmenționat,\n" +
                "mă oblig ca în termen de 30 de zile de la autentificarea prezentului contract să mă prezint la\n" +
                "Direcția de Impozite și Taxe Locale competentă, în vederea schimbării rolului fiscal, cu respectarea\n" +
                "prevederilor legale.\n" +
                "În conformitate cu dispozițiile art. 33, alineat 6, din Legea nr. 196/2018, subsemnatul\n" +
                client.getNume() + " " + client.getPrenume() + ", în calitate de cumpărător al dreptului de proprietate asupra imobilului sus-menționat, mă\n" +
                "oblig ca în termen de 10 zile lucrătoare de la autentificarea prezentului contract să mă prezint la Președintele\n" +
                "Asociației de Proprietari/Locatari cu informațiile necesare în vederea calculării cotelor de contribuție în cadrul\n" +
                "Asociației de Proprietari/Locatari.\n" +
                "În conformitate cu prevederile art. 1.666 alin. 1 Codul Civil, cheltuielile pentru încheierea prezentului\n" +
                "contract de vânzare sunt în sarcina cumpărătorului.\n" +
                "Noi, " + proiect.getNumeProiect() + ", în calitate de vânzător pe de o parte, și eu, " + client.getNume() + " " + client.getPrenume() + ", în calitate de\n" +
                "cumpărător, pe de altă parte, declarăm că am luat cunoștință de conținutul prezentului contract de vânzare pe\n" +
                "care l-am înțeles în întregime și cu care suntem de acord și declarăm că cele consemnate în cuprinsul acestuia\n" +
                "sunt adevărate, sub sancțiunile prevăzute de legea penală pentru falsul în declarații. Constatând că acest\n" +
                "contract corespunde întocmai voinței noastre, am semnat unicul exemplar al acestuia.\n" +
                "VÂNZĂTOR\n" +
                getNumeFirma() + "\n" +
                "CUMPĂRĂTOR\n" +
                client.getNume() + " " + client.getPrenume();
    }

    @Override
    public void exportaPdf(String filePath) throws IOException {
        String text = genereazaTextContract();
        File fontFile = new File("C:\\Windows\\Fonts\\Arial.ttf"); // Change this path as needed
        String finalFileName = filePath;

        // Append project name if it's not null
        if (getProiect() != null && getProiect().getNumeProiect() != null && !getProiect().getNumeProiect().isEmpty()) {
            finalFileName += "_" + getProiect().getNumeProiect();
        }
        finalFileName += ".pdf";

        try (PDDocument document = new PDDocument()) {
            addTextToPDF(document, text, fontFile);
            document.save(finalFileName);
            System.out.println("PDF saved successfully to: " + finalFileName);
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

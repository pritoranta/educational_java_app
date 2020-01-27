package viihde;

import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Esine edustaa yht� viihdep�iv�kirjan kirjausta.
 * Se voi olla esim. elokuva, peli, tai vaikkapa musiikki kappale
 * Esine osaa mm. tiet�� omat tietokentt�ns� (nimi, tyyppi, tagit, jne.) ja tarkistaa niiden oikeellisuuden
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 21 Mar 2019
 *
 */
public class Esine implements Cloneable{
    
    /**
     * Esineiden vertailija
     * @author Iisakki S�kkinen, Pietari Ritoranta
     * @version 25 Apr 2019
     *
     */
    public static class Vertailija implements Comparator<Esine> {
        private String ehto = "";
        /**
         * Alustaa vertailija olion antamalla sille lajitteluehdon merkkijonona
         * @param ehto Annettu lajitteluehto
         */
        public Vertailija(String ehto) {
            if (ehto == null) return;
            this.ehto = ehto;
        }
        
        /**
         * Vertailee kahta esinetta toisiinsa.
         * Vertailun kohde perustuu ehto muuttujan arvosta
         * @param e1 Ensimmainen esine
         * @param e2 Toinen esine
         * @return Joko negatiivisen tai positiivisen luvun tai 0. Arvot riippuvat vertailun tuloksesta: Jos e1 on isompi vertailussa, palautetaan 1, jos pienempi niin -1, jos sama niin 0.
         */
        @Override public int compare(Esine e1, Esine e2) {
            if (ehto.equalsIgnoreCase("nimi nouseva"))
                return e1.getNimi().compareToIgnoreCase(e2.getNimi());
            if (ehto.equalsIgnoreCase("nimi laskeva")) {
                return -1*(e1.getNimi().compareToIgnoreCase(e2.getNimi()));
            }
            return e1.getNimi().compareToIgnoreCase(e2.getNimi());
        }
    }

    private final static int TAG_MAX_LKM=4;
    
    private int         esineID;
    private String      nimi        = "";
    private String      pvm         = "";
    private String      kommentti   = "";
    private int         tyyppi      = 0;
    private int[]       tagit       = new int[TAG_MAX_LKM]; // Yhdell� esineell� voi olla vain 4 tagia maksimissaan
    private double      arvosana    = 0.0;
    
    private static int  seuraavaID  = 1;
    
    /**
     * Alustaa esineen kaikilla tiedoilla
     * @param nimi Esineen nimi
     * @param tyyppi Esineen tyyppi
     * @param pvm Esineen pvm
     * @param arvosana Esineen arvosana
     * @param kommentti Esineen kommentti
     * @param tagit Esineen tagit
     */
    public Esine(String nimi, int tyyppi, String pvm, double arvosana, String kommentti, int[] tagit) {
        this.nimi = nimi;
        this.pvm = pvm;
        this.kommentti = kommentti;
        this.tyyppi = tyyppi;
        this.tagit = tagit;
        this.arvosana = arvosana;
    }
    
    /**
     * Alustaa esineen ilman mit��n tietoja
     */
    public Esine() {}
    
    /**
     * Palauttaa esineelle annetun nimen
     * @return Esineen nimen
     * <pre name="test">
     *      Esine e1 = new Esine();
     *      e1.taytaAvengers();
     *      e1.getNimi() =R= "Avengers: Infinity War .*";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }
    
    /**
     * Toimii kehitysvaiheessa nopeana tapana luoda t�ytt�� esineen tiedot oikeellisella datalla
     */
    public void taytaAvengers() {
        nimi = "Avengers: Infinity War " + rand(1000,9999);
        pvm = "2019-12-1";
        kommentti = "thanos = thicc";
        tyyppi = 0;
        arvosana = rand(0.0,10.0);

        tagit[0] = -1;
        tagit[1] = -1;
        tagit[2] = -1; // -1 vastaa null-arvoa, jottei tarvita Integer-oliota
        tagit[3] = -1;
    }
    
    /**
     * Kloonaa esineen
     * <pre name="test">
     *  Esine e1 = new Esine();
     *  Esine e2 = e1.clone();
     *  (e1 == e2) === false;
     * </pre>
     */
    @Override public Esine clone() throws CloneNotSupportedException {
        Esine uusi;
        uusi = (Esine) super.clone();
        return uusi;
    }
    
    /**
     * Palauttaa olion tunnusnumeron
     * @return Olion tunnusnumero
     */
    public int getID() {
        return esineID;
    }
    
    /**
     * @return tyyppi
     */
    public int getTyyppi() {
        return tyyppi;
    }

    /**
     * @param tyyppi tyyppi, joka asetetaan
     */
    public void setTyyppi(int tyyppi) {
        this.tyyppi=tyyppi;
    }

    
    /**
     * Rekister�i olion antamalla sille oman uniikin tunnusnumeron.
     * @return Olion juuri saaman uuden tunnusnumeron
     * <pre name="test"
     *      Esine e1 = new Esine();
     *      Esine e2 = new Esine();
     *      e1.rekisteroi();
     *      e2.rekisteroi();
     *      e1.getID()+1 === e2.getID();
     * </pre>
     */
    public int rekisteroi() {
        if (esineID>0) return esineID;
        esineID = seuraavaID;
        seuraavaID++;
        return esineID;
    }
    
    /**
     * Tulostaa olion tiedot annettuun ulostuloon
     * @param out Annettu ulostulo, johon tiedot tulostetaan
     */
    public void tulosta(PrintStream out) {
        var tulostus = new StringBuilder();
        tulostus.append(String.format("%04d", esineID));
        tulostus.append('\t');
        tulostus.append(nimi);
        tulostus.append('\t');
        tulostus.append(pvm);
        tulostus.append("\nKommentti:\t");
        tulostus.append(kommentti);
        tulostus.append("\nArvosana:\t");
        tulostus.append(String.format("%2.2f", arvosana));
        tulostus.append('\n');
        
        out.println(tulostus.toString());
    }
    
    /**
     * Palauttaa esineen tiedot siin� muodossa, miss� ne ovat tallennustiedostossakin
     * @return Esineen tiedot tallennusmuodossa
     */
    public String annaTiedot() {
        var tulostus = new StringBuilder();
        tulostus.append(String.format("%d\t|", esineID));
        tulostus.append(String.format("%s\t|", nimi));
        tulostus.append(String.format("%d\t|", tyyppi));
        tulostus.append(String.format("%s\t|", pvm));
        tulostus.append(arvosana+"\t|");
        tulostus.append(String.format("%s\t|", kommentti));
        for (int i = 0; i < tagit.length; ++i)
            tulostus.append(String.format("%d\t|", tagit[i]));
        tulostus.append("\n");
        return tulostus.toString();
    }
    
    @Override public String toString() {
        var tulostus = new StringBuilder();
        tulostus.append(String.format("%04d", esineID));
        tulostus.append('\t');
        tulostus.append(nimi);
        tulostus.append('\t');
        tulostus.append(pvm);
        tulostus.append("\nKommentti:\t");
        tulostus.append(kommentti);
        tulostus.append("\nArvosana:\t");
        tulostus.append(String.format("%2.2f", arvosana));
        tulostus.append('\n');
        return tulostus.toString();
    }
    
    /**
     * Antaa esineelle tagit,
     * huomioi että TAG_MAX_LKM määrää kuinka laitetaan
     * @param tagit tagien id:t taulukossa
     * @return virheilmoituksen jos jotain menee pieleen, muuten null
     */
    public String setTagit(int[] tagit) {
        for (int i=0; i<TAG_MAX_LKM; i++) {
            if (i < tagit.length) this.tagit[i]=tagit[i];
            else this.tagit[i] = -1;
        }
        return null;
    }
    
    /**
     * @return tagien id:t taulukossa
     */
    public int[] getTagit() {
        return tagit;
    }

    
    /**
     * Apumetodi jolla saadaan nopeasti tietylt� alueelta satunnainen luku
     * @param ala Minimi arvo mik� voidaan satunnaisesti palauttaa
     * @param yla Maksimi arvo mik� voidaan satunnaisesti palauttaa
     * @return Satunnainen luku v�lilt� [ala,yla]
     */
    private int rand(int ala, int yla) {
        return (int)((yla-ala)*Math.random() + ala);
    }
    
    /**
     * Apumetodi jolla saadaan nopeasti tietylt� alueelta satunnainen luku
     * T�m� versio palauttaa reaaliarvon
     * @param ala Minimi arvo mik� voidaan satunnaisesti palauttaa
     * @param yla Maksimi arvo mik� voidaan satunnaisesti palauttaa
     * @return Satunnainen luku v�lilt� [ala,yla]
     */
    private double rand(double ala, double yla) {
        return (yla-ala)*Math.random() + ala;
    }
    


    /**
     * Asettaa ID:n esineelle
     * @param esineID Annettava ID
     */
    public void setID(int esineID) {
        this.esineID = esineID;
        if (esineID >= seuraavaID) {
            seuraavaID = esineID + 1;
        }
    }

    /**
     * Erottaa ja asettaa esineelle annetusta merkkijonosta esineen tiedot.
     * @param tiedot Annetut tiedot merkkijonona
     * <pre name="test">
     *  Esine e = new Esine();
     *  e.parse("86  |Avengers: Infinity War  |0     |2019-12-1 |0.1     |thanos = thicc      |0   |7   |null|null|");
     *  e.toString().startsWith("0086\tAvengers: Infinity War\t2019-12-1");
     *  e.getID() === 86;
     * </pre>
     */
    public void parse(String tiedot) {
        String[] palat = tiedot.split("[( )\t]*\\|");
        this.setID(Mjonot.erotaInt(palat[0], -1));
        this.nimi = palat[1];
        this.tyyppi = Mjonot.erotaInt(palat[2],0);
        this.pvm = palat[3];
        this.arvosana = Mjonot.erotaDouble(palat[4], 0.0);
        this.kommentti = palat[5];
        for (int i = 0; i < tagit.length; ++i) {
            tagit[i] = Mjonot.erotaInt(palat[6+i], -1);
        }
        
    }
    
    @Override public boolean equals(Object esine) {
        if ( esine == null ) return false;
        return this.toString().equals(esine.toString());
    }
    
    @Override public int hashCode() {
        return esineID;
    }

    /**
     * Testaa Esine-luokan toimivuutta
     * @param args Ei k�yt�ss�
     */
    public static void main(String[] args) {
        Esine e1 = new Esine();
        e1.taytaAvengers();
        e1.tulosta(System.out);
        System.out.println(e1);
        System.out.println(e1.annaTiedot());
    }

    /**
     * Palauttaa esineen arvosanan
     * @return Esineen arvosana
     */
    public double getArvosana() {
        return arvosana;
    }

    /**
     * Palauttaa esineen kommentin
     * @return Esineen kommentti
     */
    public String getKommentti() {
        return kommentti;
    }

    /**
     * Vaihtaa esineen nimen
     * @param s Annettu uusi nimi
     * @return virheilmoitus jos jotain menee pieleen
     */
    public String setNimi(String s) {
        if (s == null||s.trim().contentEquals("")) {
            return "Virheellinen nimi! Nimi ei saa olla tyhjä.";
        }
        nimi = s;
        return null;
    }

    /**
     * Vaihtaa esineen arvosanan erottamalla merkkijonosta arvosanan
     * @param s Annettu merkkijono
     * @return virheilmoitus jos jotain menee pieleen
     * 
     */
    public String setArvosana(String s) {
        if (s == null)
            return "Virheellinen arvosana annettu! Null merkkijono viite!";
        if (!s.matches("[0-9]*([\\,\\.][0-9]*)?")) return "Virheellinen arvosana annettu! Ei ole desimaaliluku!";
        
        arvosana = Mjonot.erotaDouble(s, arvosana);
        return null;
    }

    /**
     * Muuttaa esineen paivamaarasta paivaa
     * @param s Annettu paiva merkkijonona
     * @return Virheilmoitus jos jotain menee pieleen, null muuten
     */
    public String setPaiva(String s) {
        if (s == null || s.isEmpty()) return "Virhe! Paiva ei voi olla tyhja!";
        if (!s.matches("[0-3]?[0-9]")) return "Virheellinen arvo paivalle! " + s;
        if (Mjonot.erotaInt(s, -1) <= 0 || Mjonot.erotaInt(s, -1) > 31) return "Virheellinen arvo paivalle! " + s;
        var pvmOsat = pvm.split("[-/]");
        pvmOsat[2] = s;
        pvm = pvmOsat[0] + "-" + pvmOsat[1] + "-" + pvmOsat[2];
        return null;
    }

    /**
     * Muuttaa esineen paivamaarasta kuukautta
     * @param s Annettu kk merkkijonona
     * @return Virheilmoitus jos jotain menee pieleen, null muuten
     */
    public String setKuukausi(String s) {
        if (s == null || s.isEmpty()) return "Virhe! Kuukausi ei voi olla tyhja!";
        if (Mjonot.erotaInt(s, -1) <= 0 || Mjonot.erotaInt(s, -1) > 12) return "Virheellinen arvo kuukaudelle! " + s;
        var pvmOsat = pvm.split("[-/]");
        pvmOsat[1] = s;
        pvm = pvmOsat[0] + "-" + pvmOsat[1] + "-" + pvmOsat[2];
        return null;
    }

    /**
     * Palauttaa esineen paivamaaran
     * @return Esineen paivamaara
     */
    public String getPvm() {
        return pvm+"///";
    }

    /**
     * Muuttaa esineen paivamaarasta vuotta
     * @param s vuosi merkkijonona
     * @return Virheilmoitus jos jotain menee pieleen, null muuten
     */
    public String setVuosi(String s) {
        if (s == null || s.isEmpty()) return "Virhe! Vuosi ei voi olla tyhja!";
        if (Mjonot.erotaInt(s, -1) < 0) return "Virheellinen arvo vuodelle! " + s;
        var pvmOsat = pvm.split("[-/]");
        pvmOsat[0] = s;
        pvm = pvmOsat[0] + "-" + pvmOsat[1] + "-" + pvmOsat[2];
        return null;
    }

    /**
     * @return kuinka monta tagia esineelle voi antaa
     */
    public static int getMaxTagit() {
        return TAG_MAX_LKM;
    }

    /**
     * Asettaa esineelle kommentin
     * @param s kommentti
     * @return virhe,jos sellainen tuli, muuten null
     */
    public String setKommentti(String s) {
        kommentti=s;
        return null;
    }

}
 
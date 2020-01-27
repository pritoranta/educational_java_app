package viihde;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 20.3.2019
 * Hoitaa tyyppien alustamisen ja tarkistamisen. Koska tyyppejä ei voi itse lisätä, ei lisääminen ole julkinen metodi
 */
public class Tyypit {

    private Tyyppi[] tyypit;
    private int lkm=0;
    
    /**
     * Alustaa Tyypit-olion neljän kokoiseksi
     */
    public Tyypit() {
        tyypit=new Tyyppi[4];
    }
    
    /**
     * Alustaa Tyypit-olion halutun kokoiseksi
     * @param koko Tyypit-olion koko
     */
    public Tyypit(int koko) {
        tyypit=new Tyyppi[koko];
    }
    
    /**
     * Alustaa Tyypit oletuksilla: elokuva:0, peli:1, kirja:2, sarja:3
     * @example
     * <pre name="test">
     * Tyypit s=new Tyypit();
     * s.alustaPerus();
     * s.getId("o")===-1;
     * int rek=s.getId("elokuva");
     * s.getNimi(rek)==="elokuva";
     * s.getId("peli")===rek+1;
     * s.getNimi(-1)==="";
     * </pre>
     */
    public void alustaPerus() {
        String[] perusTyypit=new String[] {
                "elokuva",
                "peli",
                "kirja",
                "sarja"
        };
        for (int i=0; i<perusTyypit.length && i<tyypit.length; i++) {
            Tyyppi t=new Tyyppi(perusTyypit[i]);
            t.rekisteroi();
            lisaa(t);
        }
    }
    
    /**
     * Lisää halutun Tyyppi-olion Tyypit-taulukkoon, kasvattaa taulukkoa jos liian pieni
     * @param t Tyyppi-olio
     */
    private Tyyppi lisaa(Tyyppi t) {
        if (lkm>=tyypit.length) kasvata();
        
        if (haeNimella(t.getNimi())!=null)
                return haeNimella(t.getNimi());
        
        tyypit[lkm]=t;
        lkm++;
        return t;
    }
    
    /**
     * Luo ja lisää Tyyppi-olion Tyypit-taulukkoon, paitsi jos merkkijono on tyhjä.
     * Jos Tyyppi on jo olemassa palauttaa sen sijaan sen viitteen.
     * @param s tyypin nimi
     * @return Tyyppi, joka lisättiin
     * @example
     * <pre name="test">
     * Tyypit t=new Tyypit();
     * t.lisaa("");
     * t.getLkm()===0;
     * t.lisaa("auto");
     * t.getLkm()===1;
     * t.lisaa("auto");
     * t.getLkm()===1;
     * t.lisaa("AUTO");
     * t.getLkm()===2;
     * t.lisaa("Auto");
     * t.getLkm()===3;
     * t.lisaa("autO");
     * t.getLkm()===4;
     * </pre>
     */
    public Tyyppi lisaa(String s) {
        s.trim();
        if (s.length()<1) return null;
        if (onkoOlemassa(s)) return haeNimella(s);
        Tyyppi t=new Tyyppi(s);
        lisaa(t);
        return t;
    }
    
    /**
     * Hakee Tyyppiä sen nimellä
     * @param nimi haettavan tyypin nimi
     * @return Tyyppi, joka vastaa nimeä, null jos sellaista ei ole
     */
    public Tyyppi haeNimella(String nimi) {
        nimi.trim();
        for (int i=0; i<lkm; i++) {
            if (tyypit[i].getNimi().contentEquals(nimi)) return tyypit[i];
        }
        return null;
    }
    
    /**
     * Kertoo, loytyyko merkkijonoa vastaava tyyppi tyyppien joukosta
     * @param s Annettu merkkijono
     * @return true jos loytyy, muulloin false
     */
    private boolean onkoOlemassa(String s) {
        for (int i=0; i<lkm; i++) {
            if (s.contentEquals(tyypit[i].getNimi())) return true;
        }
        return false;
    }
        
    /**
     * Kasvattaa tyyppilistan maksimikokoa kaksinkertaistamalla sen nykyisen koon
     */
    private void kasvata() {
        Tyyppi[] apu=new Tyyppi[lkm*2];
        for (int i=0; i<lkm; i++) {
            apu[i]=tyypit[i];
        }
        tyypit=apu;
    }
    
    /**
     * Palauttaa id:tä vastaavan Tyypin nimen, jos sellainen on. Muuten palauttaa tyhjän jonon
     * @param id etsittävän Tyypin id
     * @return id:tä vastaava Tyypin nimi, jos löytyy. Jos ei löydy, palautetaan ""
     */
    public String getNimi(int id) {
        for (int i=0; i<lkm; i++) {
            if (tyypit[i].getId()==id) return tyypit[i].getNimi();
        }
        return "";
    }
    
    /**
     * Hakee Tyyppiä nimen perusteella ja palauttaa sen id:n
     * @param nimi haettavan Tyypin nimi
     * @return Tyypin id
     */
    public int getId(String nimi) {
        for (int i=0; i<lkm; i++) {
            if (tyypit[i].getNimi().contentEquals(nimi)) return tyypit[i].getId();
        }
        return -1;
    }
    
    /**
     * Hakee Tyyppeja nimen perusteella ja palauttaa listan jossa on loydettyjen tyyppien ID:t
     * @param nimet Lista haettavia tyyppeja
     * @return Tyypin id
     */
    public ArrayList<Integer> getId(String[] nimet) {
        var tulos = new ArrayList<Integer>();
        for (int i=0; i<lkm; i++) {
            for (var nimi : nimet) {
                if (tyypit[i].getNimi().contentEquals(nimi)) {
                    tulos.add(tyypit[i].getId());
                    break;
                }
            }
        }
        return tulos;
    }
    
    /**
     * Palauttaa Tyyppi-olioiden lukumäärän kyseisessä Tyypit-oliossa
     * @return lkm
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Lukee Tyypit tiedostosta
     * @param hakemisto hakemisto, josta löytyy "tyypit.dat"
     * @throws SailoException jos lukeminen ei onnistu
     * @example
     * <pre name="test">
     *  #THROWS SailoException
     *  #THROWS IndexOutOfBoundsException
     *  #import java.io.File;
     *  
     *  Tyypit t = new Tyypit();

     *  String hakemisto = "testidata";
     *  String tiedNimi = hakemisto+"/tyypit";
     *  File file = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  file.delete();
     *  
     *  t.lueTiedostosta(hakemisto); #THROWS SailoException
     *  Tyyppi t1=t.lisaa("auto");
     *  Tyyppi t2=t.lisaa("mopo");
     *  t.kirjoitaTiedostoon(hakemisto);
     *  
     *  t = new Tyypit();
     *  t.lueTiedostosta(hakemisto);
     *  t.haeNimella(t1.getNimi()).getNimi() === t1.getNimi();
     *  t.haeNimella(t2.getNimi()).getNimi() === t2.getNimi();
     *  t.getLkm()===2;
     *  t.kirjoitaTiedostoon(hakemisto);
     *  file.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta (String hakemisto) throws SailoException {
        String tiedostonNimi = hakemisto + "/tyypit.dat";
        try (var br = new BufferedReader(new FileReader(tiedostonNimi))) {
            String rivi = br.readLine();
            while (rivi != null) {
                if (Character.isDigit(rivi.charAt(0))) {
                    Tyyppi t=parseTyyppi(rivi);
                    if (t==null) throw new SailoException("tyypit.dat viallinen!");
                    lisaa(t);
                }
                rivi = br.readLine();
            }
            
        } catch (FileNotFoundException e) {
            throw new SailoException("Tyypit: Tiedostoa ei l�ytynyt!");
        } catch (IOException e) {
            throw new SailoException("IO Virhe!");
        }
    }
    
    /**
     * Parsii merkkijonosta tiedot Tyyppi-luokan tuntemassa muodossa
     * @param tiedot tiedot merkkijonona
     * @return luotu Tyyppi-olio
     */
    public Tyyppi parseTyyppi(String tiedot) {
        return Tyyppi.parse(tiedot);
    }
    
    /**
     * Kirjoittaa annetussa hakemistossa sijaitsevaan "tyypit.dat"-tiedostoon
     * omat tietonsa.
     * @param hakemisto hakemisto, josta löytyy "tyypit.dat"
     * @throws SailoException jos kirjoittaminen ei onnistu
     */
    public void kirjoitaTiedostoon(String hakemisto) throws SailoException {
        String tiedostonNimi = hakemisto + "/tyypit.dat";
        String otsake = "tpid|tyyppi";
        StringBuilder data = new StringBuilder(otsake+"\n");

        try (var fo = new PrintStream(new FileOutputStream(tiedostonNimi))) {
            for (int i = 0; i < lkm; ++i) {
                data.append(tyypit[i].annaTiedot());
                data.append("\n");
            }
            System.out.println(data); // Debuggausta varten
            fo.printf(data.toString());
            } catch (FileNotFoundException e) {
            throw new SailoException("Tyypit: Tiedostoa ei l�ytynyt!");
        }

    }
    
    
    /**
     * @param args ei käytössä
     * Testipääohjelma
     */
    public static void main(String[] args) {
        Tyypit t=new Tyypit();
        Tyyppi a=t.lisaa("auto");
        a.rekisteroi(); 
        System.out.println(t.getLkm()+", "+a.getId()); //1, 1
        a=t.lisaa("auto");
        a.rekisteroi(); 
        System.out.println(t.getLkm()+", "+a.getId()); //1, 1
        a=t.lisaa("AUTO");
        a.rekisteroi(); 
        System.out.println(t.getLkm()+", "+a.getId()); //2, 2
        a=t.lisaa("Auto");
        a.rekisteroi(); 
        System.out.println(t.getLkm()+", "+a.getId()); //3, 3
        a=t.lisaa("autO");
        a.rekisteroi(); 
        System.out.println(t.getLkm()+", "+a.getId()); //4, 4
    }

    /**
     * Palauttaa kaikkien olemassa olevien tyyppien nimet
     * @return tyyppien nimet String-taulukossa
     */
    public String[] annaTyypit() {
        String[] apu=new String[lkm];
        for (int i=0; i<apu.length; i++) {
            apu[i]=tyypit[i].getNimi();
        }
        return apu;
    }

    /**
     * Antaa halutusta indeksistä tyypin
     * @param i indeksi
     * @return tyyppi indeksistä
     * @throws IndexOutOfBoundsException jos indeksi ei ole sopiva, eli yli lkm tai alle 0
     */
    public Tyyppi anna(int i)  throws IndexOutOfBoundsException{
        if (i >= lkm || i < 0) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return tyypit[i];
    }

    /**
     * Poistaa halutusta indeksistä tyypin
     * @param i indeksi
     * @throws IndexOutOfBoundsException jos indeksi ei ole sopiva, eli yli lkm tai alle 0
     */
    public void poista(int i)  throws IndexOutOfBoundsException{
        if (i >= lkm || i < 0) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        
        for (int j=i; j<lkm-1; j++) {
            tyypit[j]=tyypit[j+1];
        }
        lkm--;
    }

}

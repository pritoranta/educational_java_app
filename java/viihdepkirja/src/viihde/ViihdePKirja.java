package viihde;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 21.3.2019
 * 
 */
public class ViihdePKirja {
    
    private Tyypit tyypit=new Tyypit();
    private Esineet esineet=new Esineet();
    private Tagit tagit=new Tagit();
    private String hakemisto = "";
    
    /**
     * Alustaa ViihdePKirja olion tyypit: elokuva, kirja, sarja, peli
     */
    public void alustaTyypit() {
        tyypit.alustaPerus();
    }
    
    /**
     * Lisää Esineen Esineisiin
     * @param e Esine, joka halutaan lisätä
     * @example
     * <pre name="test">
     * ViihdePKirja vpk=new ViihdePKirja();
     * Esine avengers1=new Esine(), avengers2=new Esine();
     * vpk.getEsineita()===0;
     * avengers1.rekisteroi(); 
     * vpk.getEsineita()===0;                           
     * avengers1.taytaAvengers();                         
     * avengers2.rekisteroi();                            
     * avengers2.taytaAvengers();                         
     *                                                    
     * vpk.lisaa(avengers1);
     * vpk.getEsineita()===1;                              
     * vpk.lisaa(avengers2); 
     * vpk.getEsineita()===2;                             
     * </pre>
     */
    public void lisaa(Esine e) {
        esineet.lisaa(e);
    }
    
    /**
     * Poistaa esineen joka vastaa annetun esineen IDn
     * @param e Annettu esine joka halutaan poistaa
     */
    public void poista(Esine e) {
        esineet.poista(e);
        
    }
    
    /**
     * Korvaa tai lis�� esineen tietorakenteeseen.
     * @param e Esine jolla korvataan/joka lis�t��n tietorakenteeseen
     */
    public void korvaaTaiLisaa(Esine e){
        
        esineet.korvaaTaiLisaa(e);
    }
    
    /**
     * Asettaa esineen tyypin id:n tyypin nimen perusteella
     * @param e esine
     * @param tyyppi tyypin nimi
     */
    public void setTyyppi(Esine e, String tyyppi) {
        if (tyyppi == null) return;
        var uusi = (lisaaTyyppi(tyyppi));
        if (uusi == null) return;
        e.setTyyppi(uusi.rekisteroi());
    }
    
    /**
     * Lisää halutun nimisen tyypin ja palauttaa sen
     * @param nimi tyypin nimi
     * @return lisätty tyyppi
     */
    public Tyyppi lisaaTyyppi(String nimi) {
        return tyypit.lisaa(nimi);
    }
    

    
    /**
     * Lisää esineelle tagit
     * @param e esine
     * @param tagit tagit merkkijonoina
     * @return virheen kuvaus, jos sellainen tapahtuu, muuten null
     */
    public String setTagit(Esine e, String... tagit) {
        if (tagit.length>Esine.getMaxTagit()) return "Virhe! Liikaa tageja, maksimissaan: "+Esine.getMaxTagit();
        
        int[] apu=new int[tagit.length];
        for (int i=0; i<tagit.length; i++) {
            if (this.tagit.lisaa(tagit[i])==null) {apu[i]=-1; continue;}
            apu[i]=this.tagit.lisaa(tagit[i]).rekisteroi();
        }
        e.setTagit(apu);
        
        return null;
    }
    
    /**
     * Kertoo käykö tagit, ja jos eivät käy palauttaa virheilmoituksen
     * Ei kuitenkaan lisää tageja vaikka ne kävisivät
     * @param tagit1 lisättävät tagit
     * @return virheilmoitus, jos tagit eivät käy, muuten null
     */
    public String kaykoTagit(String... tagit1) {
        if (tagit1.length>Esine.getMaxTagit()) return "Virhe! Liikaa tageja, maksimissaan: "+Esine.getMaxTagit();
        
        for (var tagi : tagit1) {
            if (tagi.length() > 20) return "Virhe! Liian pitka tagi! " + tagi;
            if (!tagi.matches("^(\\s)*[a-zA-Z]+(\\s)*")) return "Virhe! Tagi ei saa sisaltaa erikoismerkkeja! " + tagi + " sisaltaa erikoismerkkeja!";
        }
        
        return null;
    }
    
    /**
     * Lisää väliaikaisen täyttöesineen, täyttää sen ja rekisteröi sen
     * sekä sen tarvitsema tyypin ja tagit
     * @return olio, joka lisättiin
     */
    public Esine lisaaTayttoEsine() {
        Esine e=new Esine();
        e.taytaAvengers();
        e.setTagit(tayteTagit());
        e.setTyyppi(tyypit.lisaa("elokuva").rekisteroi());
        e.rekisteroi();
        esineet.lisaa(e);
        return e;
    }
    
    /**
     * Tallentaa oletushakemistoon p�iv�kirjan tiedot: esineet, tyypit ja tagit
     * @throws SailoException jos tallentamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        File dir = new File(hakemisto);
        dir.mkdirs();
        
        try {
            esineet.kirjoitaTiedostoon(hakemisto);
        } catch (SailoException e) {
            throw new SailoException("Virhe! Tallennus ep�onnistui! " + e.getMessage());
        }
        try {
            poistaOrvotTagit();
            tagit.kirjoitaTiedostoon(hakemisto);
        } catch (SailoException e) {
            throw new SailoException("Virhe! Tallennus ep�onnistui! " + e.getMessage());
        }
        try {
            poistaOrvotTyypit();
            tyypit.kirjoitaTiedostoon(hakemisto);
        } catch (SailoException e) {
            throw new SailoException("Virhe! Tallennus ep�onnistui! " + e.getMessage());
        }
    }
    
    /**
     * @return Esineiden lkm
     */
    public int getEsineita() {
        return esineet.getLkm();
    }
    
    /**
     * @return tagien lkm
     */
    public int getTageja() {
        return tagit.getLkm();
    }
    
    /**
     * @return tyyppien lkm
     */
    public int getTyyppeja() {
        return tyypit.getLkm();
    }
    
    /**
     * palauttaa halutun Esineen
     * @param i halutun esineen indeksi
     * @return Esine i 
     */
    public Esine annaEsine(int i) {
        return esineet.anna(i);
    }
    
    /**
     * Muodostaa ja palauttaa täytteeksi tarkoitetut tagit, rekisteröi ne
     * @return taytteeksi tarkoitettujen tagien id:t
     */
    public int[] tayteTagit() {
        String[] tags=new String[] {
                "huh",
                "hah",
                "hei",
                "iii"
        };
        Tag[] t= tagit.lisaa(tags);
        int[] tagnumerot=new int[t.length];
        for (int i=0;i<t.length;i++) {
            tagnumerot[i]=t[i].rekisteroi();
        }
        return tagnumerot;
    }
    
    /**
     * Antaa kaikki esineeseen liittyvät tiedot merkkijonossa,
     * ensin Esine, sitten Tyyppi, sitten Tagit
     * @param e Esine
     * @return Esineen tiedot ja siihen liitetyt tiedot merkkijonona
     */
    public String annaTiedot(Esine e) {
        StringBuilder sb=new StringBuilder();
        sb.append(e.annaTiedot());
        sb.append("\n");
        
        sb.append(tyypit.getNimi(e.getTyyppi()));
        sb.append("\n");
        
        int[] apu=e.getTagit();
        for (int i=0; i<apu.length;i++) {
            if (apu[i]==-1) continue;
            sb.append(tagit.getNimi(apu[i]));
        }
        
        return sb.toString();
    }
    
    /**
     * Tulostaa Esineen tiedot annettuun tietovirtaan
     * @param esine esine, jonka tiedot tulostetaan
     * @param os tietovirta, johon tulostetaan
     */
    public void tulostaTiedot(Esine esine, PrintStream os) {
        esine.tulosta(os);
        
        os.println("Tyyppi: "+tyypit.getNimi(esine.getTyyppi()));
        
        os.println("Tagit: ");
        int[] apu=esine.getTagit();
        for (int i=0; i<apu.length;i++) {
            if (apu[i]==-1) continue;
            os.println(tagit.getNimi(apu[i]));
        }
    }
    
    
    
    /**
     * Vaihtaa hakemistoa, josta ViihdePKirjan data luetaan ja minne se tallennetaan
     * @param hak hakemiston nimi
     */
    public void setHakemisto(String hak) {
        hakemisto=hak;
    }

    /**
     * Alustaa ohjelman lataamalla kaikki tiedot oletushakemistosta
     * @throws SailoException jos lukemisessa ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     * 
     * ViihdePKirja vpk=new ViihdePKirja();
     * 
     * Esine e1=new Esine(); e1.taytaAvengers(); e1.rekisteroi();
     * Esine e2=new Esine(); e2.taytaAvengers(); e2.rekisteroi();
     * vpk.setTyyppi(e1,"elokuva");
     * vpk.setTyyppi(e2,"video");
     * vpk.setTagit(e1, "aktion","romantiikka","","666","0/5");
     * vpk.setTagit(e2);
     * String test1=vpk.annaTiedot(e1);
     * String test2=vpk.annaTiedot(e2);
     * 
     * String testihakemisto="testidata";
     * vpk.setHakemisto(testihakemisto);
     * File dir = new File(testihakemisto);
     * File fes = new File(testihakemisto+"/esineet.dat");
     * File ftg = new File(testihakemisto+"/tagit.dat");
     * File ftp = new File(testihakemisto+"/tyypit.dat");
     * dir.mkdir();  
     * 
     * vpk.lisaa(e1);
     * vpk.lisaa(e2);
     * vpk.tallenna();
     * vpk=new ViihdePKirja();
     * vpk.getEsineita()===0;
     * vpk.getTageja()===0;
     * vpk.getTyyppeja()===0;
     * 
     * vpk.setHakemisto(testihakemisto);
     * vpk.lueTiedostosta();
     * vpk.getEsineita()===2;
     * vpk.getTageja()===4;
     * vpk.getTyyppeja()===2;
     * e1.toString().contentEquals(vpk.annaEsine(0).toString())===true;
     * e2.toString().contentEquals(vpk.annaEsine(1).toString())===true;
     * vpk.annaTiedot(vpk.annaEsine(0)).contentEquals(test1)===true;
     * vpk.annaTiedot(vpk.annaEsine(1)).contentEquals(test2)===true;
     * vpk.tallenna();
     * 
     * fes.delete() === true;
     * ftg.delete() === true;
     * ftp.delete() === true;
     * dir.delete()===true;
     * </pre>
     */
    public void lueTiedostosta() throws SailoException {
        try {
            esineet.lueTiedostosta(hakemisto);
        } catch (SailoException e) {
            throw new SailoException("Virhe ohjelman alustuksessa! " + e.getMessage());
        } catch (NullPointerException e) {
            throw new SailoException("VIRHE!");
        }
        
        try {
            tagit.lueTiedostosta(hakemisto);
        } catch (SailoException e) {
            throw new SailoException("Virhe ohjelman alustuksessa! " + e.getMessage());
            }
        
        try {
            tyypit.lueTiedostosta(hakemisto);
        } catch (SailoException e) {
            throw new SailoException("Virhe ohjelman alustuksessa! " + e.getMessage());
            }   
    }
    
    /**
     * Testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        ViihdePKirja vpk=new ViihdePKirja();
        
        Esine e1=new Esine(); e1.taytaAvengers(); e1.rekisteroi();
        
        Esine avengers1=new Esine(), avengers2=new Esine();
        avengers1.rekisteroi();
        avengers1.taytaAvengers();
        avengers2.rekisteroi();
        avengers2.taytaAvengers();
        
        vpk.lisaa(avengers1);
        vpk.lisaa(avengers2);
        vpk.lisaaTayttoEsine();
        
        avengers2.rekisteroi();
        
        System.out.println("============= VPK testi =================");

        for (int i = 0; i < vpk.getEsineita(); i++) {
            Esine esine = vpk.annaEsine(i);
            System.out.println("Esine paikassa: " + i);
            vpk.tulostaTiedot(esine, System.out);   
        }
        
        var tagiIDt = vpk.annaTagit("huh, hah, hei, iii");
        for (var tagi : tagiIDt) {
            System.out.print(tagi + ", ");
        }
        System.out.println("");
        
    }
    
    /**
     * Palauttaa taulukon tageja, jotka kuuluvat annetulle esineelle
     * @param esine Annettu esine
     * @return Tagit jotka kuuluvat annetulle esineelle
     */
    public String[] annaTagit(Esine esine) {
        return tagit.annaTagit(esine.getTagit());
    }

    /**
     * Palauttaa taulukon tagien ID:t, jotka annetusta merkkijonosta loytyy
     * Jos jokin taulukon tageista ei ole viela olemassa, se luodaan uudeksi tagina
     * @param s Merkkijono, jonka sisaltamien tagien ID:t yritetaan loytaa jo luoduista tageista
     * @return Taulukon joka sisaltaa loydettyjen tagien ID:t
     * @example
     */
    public int[] annaTagit(String s) {
        if (s == null) return null;
        String[] tagiLista = (s+",").split(",(\\s)*");
        for (var testi : tagiLista) {
            System.out.print(testi + ", ");
        }
        System.out.println(tagiLista.length); // Debuggaukseen
        if (tagiLista.length <= 0) return null;
        int[] tagiIDt = new int[tagiLista.length];
        for (int i = 0; i < tagiLista.length; ++i) {
            Tag seuraavaTagi = tagit.haeTagillaTaiLuoUusi(tagiLista[i].trim());
            tagiIDt[i] = seuraavaTagi == null ? -1 : seuraavaTagi.getId();
        }
        return tagiIDt;
    }

    /**
     * Siivoaa tagilistan kaikista tageista, joilla ei ole yhtaan omistajaa
     * @throws SailoException jos tulee jokin ongelma esim tagin poistamisessa
     */
    public void poistaOrvotTagit() throws SailoException {
        boolean orpo = true;
        for (int i = 0; i < tagit.getLkm(); ++i) {
            for (int j = 0; j < esineet.getLkm(); ++j) {
                for (int k = 0; k < esineet.anna(j).getTagit().length; ++k) {
                    if (tagit.haeIndeksilla(i).getId() == esineet.anna(j).getTagit()[k]) {
                        orpo = false;
                    }
                }
            if (!orpo)
                break;
            }
            if (orpo) {
                tagit.poista(i);
                tagit.paivitaSeuraavaID();
            }
            else orpo = true;
        }
    }
    
    /**
     * Siivoaa tagilistan kaikista tageista, joilla ei ole yhtaan omistajaa
     * Tama versio jattaa annetun tagin pois laskuista, kun orpoja etsitaan
     * @param poikkeus Poikkeus tagi, jota ei poisteta listalta
     * @throws SailoException jos tulee jokin ongelma esim tagin poistamisessa
     */
    public void poistaOrvotTagit(Tag poikkeus) throws SailoException {
        boolean orpo = true;
        for (int i = 0; i < tagit.getLkm(); ++i) {
            for (int j = 0; j < esineet.getLkm(); ++j) {
                for (int k = 0; k < esineet.anna(j).getTagit().length; ++k) {
                    if (tagit.haeIndeksilla(i).getId() == esineet.anna(j).getTagit()[k]) {
                        orpo = false;
                    }
                }
            if (!orpo)
                break;
            }
            if (orpo && tagit.haeIndeksilla(i).getId() != poikkeus.getId()) {
                tagit.poista(i);
                tagit.paivitaSeuraavaID();
            }
            else orpo = true;
        }
        
    }
    
    /**
     * Siivoaa tyyppilistan kaikista tyypeista, joilla ei ole yhtaan omistajaa
     * @throws SailoException jos tulee jokin ongelma esim tagin poistamisessa
     */
    public void poistaOrvotTyypit() throws SailoException {
        boolean orpo = true;
        for (int i = 0; i < tyypit.getLkm(); i++) {
            for (int j = 0; j < esineet.getLkm(); j++) {
                if (esineet.anna(j).getTyyppi()==tyypit.anna(i).getId()) {
                    orpo=false;
                    break;
                }
            }
            if (orpo)
                tyypit.poista(i);
            else
                orpo=true;
        }
    }
    

    
    /**
     * Kertoo onko merkkijono sopiva tyypin nimi
     * @param s tyypin nimi
     * @return virheilmoitus merkkijonona, jos tyypin nimestä tulee ongelmia, muuten null
     */
    public String kaykoTyyppi(String s) {
        if (s==null||s.trim().contentEquals("")) return "Virhe! Tyyppi ei saa olla tyhjä.";
        
        return null;
    }

    /**
     * Palauttaa tyypin id:tä vastaavan nimen, jos löytyy
     * @param id tyypin id
     * @return tyypin nimi merkkijonona, jos sellainen löytyy, muuten tyhjä merkkijono
     */
    public String getTyyppi(int id) {
        return tyypit.getNimi(id);
    }

    /**
     * Etsii esineiden joukosta hakutulokseen sopivat esineet
     * @param haku Annettu hakutulos (regex)
     * @return Listan hakutulokseen sopivista esineista
     */
    public ArrayList<Esine> etsiEsineet(String haku) {
        String[] hakuehdot = haku.split("<>");
        String tagiehdot = "";
        String lajitteluehto = "";
        ArrayList<Integer> tyyppiehdot = new ArrayList<Integer>();
        if (hakuehdot.length >= 1) tagiehdot = hakuehdot[0];
        if (hakuehdot.length >= 2) tyyppiehdot = tyypit.getId((hakuehdot[1]+",").split(","));
        if (hakuehdot.length >= 3) lajitteluehto = hakuehdot[2];
        var tagiehdotID = this.annaTagit(tagiehdot);
        if (tagiehdotID == null) tagiehdotID = new int[]{-1,-1,-1,-1};
        for (var tagiehto : tagiehdotID) System.out.println(tagiehto); // Debuggaukseen
        var tulos = esineet.etsi(tagiehdotID, tyyppiehdot);
        Collections.sort(tulos, new Esine.Vertailija(lajitteluehto));
        return tulos;
    }

    /**
     * Palauttaa ViihdePKirja-olioon tallennettujen tyyppien nimet String-taulukossa
     * @return Tyyppien nimet String-taulukossa
     */
    public String[] annaTyypit() {
        return tyypit.annaTyypit();
    }

}

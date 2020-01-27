/**
 * 
 */
package viihde;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;


/**
 * Toimii s�ili�n� Esine-luokan olioille. Pit�� siis esinerekisteri�
 * Osaa lis�t� ja poistaa esineit�
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 21 Mar 2019
 *
 */
public class Esineet {

    private Esine[] alkiot  = new Esine[5];
    private int lkm         = 0;
    private boolean muutettu = false;
    
    /**
     * Alustaa Esineet-olion kokoon 5
     */
    public Esineet() {}
    
    /**
     * Alustaa Esineet-olion m��ritt�en samalla esineiden maksimim��r�n 
     * @param koko Listalta toivottu koko
     */
    public Esineet(int koko) {
        alkiot = new Esine[koko];
    }
    
    /**
     * Lis�� alkioihin uuden Esine-tyyppisen olion
     * @param e Lis�tt�v� esine
     * <pre name="test">
     *      Esineet e = new Esineet(1);
     *      e.getLkm() === 0;
     *      Esine e1 = new Esine();
     *      Esine e2 = new Esine();
     *      e1.taytaAvengers();
     *      e2.taytaAvengers();
     *      e.lisaa(e1);
     *      e.getLkm() === 1;
     *      e.lisaa(e2);
     *      e.anna(0).getNimi() =R= "Avengers: Infinity War .*";
     *      e.anna(1).getNimi() =R= "Avengers: Infinity War .*";
     *      e.getLkm() === 2;
     * </pre>
     */
    public void lisaa(Esine e) {
        if (lkm >= alkiot.length) {
            // Jos tila loppuu, luodaan uusi kaksi kertaa pitempi lista, ja k�ytet��n t�stedes sit�
            Esine[] temp = new Esine[(alkiot.length+1)*2];
            for (int i = 0; i < alkiot.length; ++i) {
                temp[i] = alkiot[i];
            }
            alkiot = temp;
        }
        alkiot[lkm++] = e;
        muutettu = true;
    }
    
    /**
     * Palauttaa listasta halutun esineen
     * @param i Toivotun esineen indeksi
     * @return Esine, joka on indeksiss� i
     * @throws IndexOutOfBoundsException Jos yritet��n hakea yli listan rajojen
     */
    public Esine anna(int i) throws IndexOutOfBoundsException{
        if (i >= lkm || i < 0) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Palauttaa kuinka monta alkiota on t�h�n olioon talletettu
     * @return Tallennettujen alkioiden m��r�
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Lukee esineet tiedostosta
     * @param hakemisto Hakemisto josta etsit��n tiedosto
     * @throws SailoException jos tiedoston lukemisessa tulee jokin ongelma
     * <pre name="test">
     *  #THROWS SailoException
     *  #THROWS IndexOutOfBoundsException
     *  #import java.io.File;
     *  
     *  Esineet esineet = new Esineet();
     *  Esine avenger1 = new Esine(); Esine avenger2 = new Esine();
     *  avenger1.taytaAvengers(); avenger2.taytaAvengers();
     *  String hakemisto = "testidata";
     *  String tiedNimi = hakemisto+"/esineet";
     *  File file = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  file.delete();
     *  esineet.lueTiedostosta(hakemisto); #THROWS SailoException
     *  
     *  esineet.lisaa(avenger1); esineet.lisaa(avenger2);
     *  esineet.kirjoitaTiedostoon(hakemisto);
     *  esineet = new Esineet();
     *  esineet.lueTiedostosta(hakemisto);
     *  esineet.anna(0) === avenger1;
     *  esineet.anna(1) === avenger2;
     *  esineet.anna(2); #THROWS IndexOutOfBoundsException
     *  esineet.lisaa(avenger2);
     *  esineet.kirjoitaTiedostoon(hakemisto);
     *  file.delete() === true;
     *  dir.delete() === true;
     *  esineet.poista(avenger1);
     *  esineet.anna(0) === avenger2;
     *  esineet.anna(1) === avenger2;
     *  esineet.poista(avenger2);
     *  esineet.poista(avenger2);
     *  esineet.anna(0); #THROWS IndexOutOfBoundsException
     *  esineet.poista(avenger2); // Ei pitaisi olla ongelma, vaikkei mitaan loydy
     * </pre>
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        String tiedostonNimi = hakemisto + "/esineet.dat";
        try (var br = new BufferedReader(new FileReader(tiedostonNimi))) {
            String rivi = br.readLine();
            while (rivi != null) {
                if (Character.isDigit(rivi.charAt(0))) {
                    lisaa(luoEsine(rivi));
                }
                rivi = br.readLine();
            }
            
        } catch (FileNotFoundException e) {
            throw new SailoException("Esineet: Tiedostoa ei l�ytynyt!");
        } catch (IOException e) {
            throw new SailoException("IO Virhe!");
        }
    }
    
    /**
     * Kirjoittaa alkioiden tiedot tiedostoon
     * @param hakemisto Hakemisto josta etsit��n tiedosto johon kirjoittaa, tai johon luodaan uusi tiedosto
     * @throws SailoException jos tiedostoon kirjaamisessa menee jokin pieleen
     */
    public void kirjoitaTiedostoon(String hakemisto) throws SailoException {
        String tiedostonNimi = hakemisto + "/esineet.dat";
        String otsake = "eid\t|nimi\t\t\t|tyyppi\t|p�iv�m��r�\t|arvosana\t|kommentti\t|tg1\t|tg2\t|tg3\t|tg4\t|\n";
        StringBuilder data = new StringBuilder("");
        try (var fo = new PrintStream(new FileOutputStream(tiedostonNimi))) {
            data.append(otsake);
            for (int i = 0; i < lkm; ++i) {
                data.append(alkiot[i].annaTiedot());
            }
            System.out.println(data); // Debuggailua varten
            fo.printf(data.toString());
            } catch (FileNotFoundException e) {
            throw new SailoException("Esineet: Tiedostoa ei l�ytynyt!");
        }
    }
    
    /**
     * Luo esineen merkkijonon sis�lt�mill� tiedoilla
     * @param tiedot
     */
    private static Esine luoEsine(String tiedot) {
        Esine esine = new Esine();
        esine.parse(tiedot);
        return esine;
    }
    
    /**
     * Tulostaa alkioiden tiedot
     * @param out tietovirta johon tulostetaan
     */
    private void tulosta(PrintStream out) {
        for (int i = 0; i < lkm; ++i) {
            out.println(alkiot[i]);
        }
    }
    /**
     * Testaa Esineet-luokkaa
     * @param args Ei k�yt�ss�
     */
    public static void main(String[] args) {
        Esineet e = new Esineet(0); // Alustetaan koko 0:n
        try {
            e.lueTiedostosta(".");
            e.kirjoitaTiedostoon(".");
        } catch (SailoException e3) {
            System.out.println(e3.getMessage());
        }
        e.tulosta(System.out);
    }
    /**
     * Kertoo onko esineit� muokattu
     * @return True jos on muutettu, muulloin false
     */
    public boolean onkoMuutettu() {
        return muutettu;
    }
    /**
     * Korvaa esineen tietorakenteessa, jos samalla ID:ll� varustettu esine asustaa tietorakenteessa.
     * Muulloin lis�t��n uusi esine erillisen� alkiona.
     * @param e Esine, jolla korvataan tietorakenteessa mahd. sijaitseva esine
     */
    public void korvaaTaiLisaa(Esine e) {
        int id = e.getID();
        for (int i = 0; i < lkm; ++i) {
            if (alkiot[i].getID() == id) {
                alkiot[i] = e;
                muutettu = true;
                return;
            }
        }
        lisaa(e);
    }

    /**
     * Poistaa pyydetyn esineen, jos se loytyy sailosta
     * @param e Poistettava esine
     */
    public void poista(Esine e) {
        poista(e.getID());
    }

    /**
     * Poistaa pyydetyn esineen, jos se loytyy sailosta
     * @param id Poistettavan esineen id
     * 
     */
    public void poista(int id) {
        for (int i = 0; i < lkm; ++i) {
            if ( alkiot[i].getID() == id ) {
                for ( int j = i+1; j < lkm; ++j ) {
                    if ( j >= lkm || lkm <= 1 ) break; // Tarvitsee vain vapauttaa viimeisin alkioindeksi kayttoon. Tama tapahtuu silmukan jalkeen jokatapauksessa
                    alkiot[j-1] = alkiot[j]; // Siirretaan kaikki loydettya esinetta seuraavat alkiot yksi askel taaksepain
                }
                --lkm;
                return;
            }
        }
    }

    /**
     * Etsii esineiden joukosta hakuehtoihin sopivat esineet ja palauttaa listan niista
     * @param tagiehdot Tagiehdot, jotka taytyy toteutua
     * @param tyyppiehdot Tyyppiehdot, jotka taytyy toteutua
     * @return Lista joka sisaltaa hakutulokset
     */
    public ArrayList<Esine> etsi(int[] tagiehdot, ArrayList<Integer> tyyppiehdot) {
        ArrayList<Esine> tulos = new ArrayList<Esine>();

        // Testataan ensin tyyppiehdot
        tulos = etsiTyypeilla(tyyppiehdot);
        // Sitten erotetaan tuloksesta kaikki joihin tagiehdot eivat sovi
        tulos = erotaTageilla(tulos, tagiehdot);
            
        return tulos;
    }
    
    /**
     * Erottaa annetusta esineiden listasta vain ne esineet, joiden tageista loytyy joku parametrina annetuista tageista
     * @param lista Lista esineita
     * @param tagiehdot Tagit, joisa joku taytyy loytya esineesta, mikali sita ei haluta erottaa tuloksesta pois
     * @return Listan esineita, joilta loytyi joku vaadituista tageista
     */
    private ArrayList<Esine> erotaTageilla(ArrayList<Esine> lista, int[] tagiehdot) {
        var tulos = new ArrayList<Esine>();
        if (lista == null || lista.isEmpty()) return tulos;
        for (var esine : lista) {
            boolean alkioLisatty = false;
            boolean onkoTagiehtoja = false;
                
            var alkionTagit = esine.getTagit();
            for (var tagi : alkionTagit) {
                for (var tagiehto : tagiehdot) {
                    if (tagiehto == -1) continue;
                    onkoTagiehtoja = true;
                    if (!alkioLisatty && tagi == tagiehto) {
                        tulos.add(esine);
                        alkioLisatty = true;
                        break;
                    }
                }
                if (alkioLisatty) break;
            }
            if (!onkoTagiehtoja && !alkioLisatty) tulos.add(esine); 
            onkoTagiehtoja = false;
        }
        
        return tulos;
    }

    /**
     * Etsii taman olion listasta vain ne esineet, jotka vastaavat annettuja tyyppiehtoja
     * @param tyyppiehdot Annetut tyyppiehdot, joista esineen taytyy vahintaan yhta noudattaa, jos haluaa tulla valituksi tulokseen
     * @return Lista esineita jotka toteuttivat jonkin tyyppiehdoista
     */
    private ArrayList<Esine> etsiTyypeilla(ArrayList<Integer> tyyppiehdot) {
        var tulos = new ArrayList<Esine>();
        for (int i = 0; i < lkm; ++i) {
            Esine esine = alkiot[i];
            if (tyyppiehdot == null || tyyppiehdot.isEmpty()) {
                tulos.add(esine);
            } else {
                for (int tyyppi : tyyppiehdot) {
                    if (esine.getTyyppi() == tyyppi) {
                        tulos.add(esine);
                        break;
                    }
                }
            }
            
        }
        return tulos;
    }

}

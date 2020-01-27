package viihde;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;


/**
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 21.3.2019
 * Hallitsee tagejen luomisen, hakemisen
 */
public class Tagit {
    
    private LinkedList<Tag> tagit=new LinkedList<Tag>(); //LinkedList koska tageja manipuloidaan paljon
    
    /**
     * Alustaa Tagit-olion
     */
    public Tagit() {
        // ei tarvitse mitään
    }
     
    /**
     * @return palauttaa tagien lukumäärän
     */
    public int getLkm() {
        return tagit.size();
    }
    
    /**
     * Lisää tagin Huom! Pitää muistaa rekisteröidä!
     * @param tag tagin sisältö
     * @return Tag, joka lisättiin
     * @example
     * <pre name="test">
     * Tagit t=new Tagit();
     * int rek=t.lisaa("P").rekisteroi();
     * t.getLkm()===1;
     * t.lisaa("P").rekisteroi()===rek;
     * t.getLkm()===1;
     * t.lisaa("O").rekisteroi()===rek+1;
     * t.getLkm()===2;
     * t.lisaa("")===null;
     * t.getLkm()===2;
     * </pre>
     */
    public Tag lisaa(String tag) { 
        if (tag == null) return null;
        if (tag.length()<1) return null;
        String apu=tag.trim();
        
        if (haeTagilla(apu)!=null)
            return haeTagilla(apu);
        
        Tag tagi=new Tag(apu);
        tagit.add(tagi);
        return tagi;
    }
    
    /**
     * Lisää valmiin tagin tageihin, jos sitä ei vielä ole
     * @param t lisättävä tag
     */
    public void lisaa(Tag t) {
        String apu=t.getTag().trim();
        if (haeTagilla(apu)!=null)
            return;
        
        tagit.add(t);
    }
    
    /**
     * Lisää tagit, Huom! Ei rekisteröi niitä!
     * @param tag tagin sisältö
     * @return Tag[], jossa lisätyt tagit
     */
    public Tag[] lisaa(String[] tag) {
        Tag[] tagit1=new Tag[tag.length];
        for (int i=0; i< tag.length;i++) {
            tagit1[i]=lisaa(tag[i]);
        }
        return tagit1;
    }

    
    /**
     * Antaa tagit merkkijonojen taulukkona, paikalla 0 tag id:tä 0 vastaava tag jne
     * @param id tagien id:t
     * @return tagit merkkijonojen taulukkona
     */
    public String[] annaTagit(int[] id) {
        String[] palautettavat=new String[id.length];
        for (int i=0; i<id.length; i++) {
            if (id[i]<0||haeIDlla(id[i])==null) continue;
            palautettavat[i]=haeIDlla(id[i]).getTag();
        }
        return palautettavat;
    }
    
    /**
     * Palauttaa Tagin, jos id:tä vastaa Tag. Muuten palauttaa null
     * @param id haettavan tagin id
     * @return id:tä vastaava Tag
     */
    public Tag haeIDlla(int id) {
        for (Tag t:tagit) {
            if (t.getId()==id) return t;
        }
        return null;
    }
    
    /**
     * Palauttaa Tagin, jos jonoa vastaava tag on olemassa, muuten null
     * @param tag tagin sisältö merkkijonona
     * @return Tag
     */
    public Tag haeTagilla(String tag) {
        if (tagit.size()<1)return null;
        for (Tag t: tagit) {
            if (t.getTag().contentEquals(tag)) return t;
        }
        return null;
    }
    
    /**
     * Palauttaa Tagin, jos jonoa vastaava tag on olemassa, muuten luo uuden, rekisteroi sen ja palauttaa sen
     * @param tag tagin sisältö merkkijonona
     * @return Tag
     */
    public Tag haeTagillaTaiLuoUusi(String tag) {
        if (tagit.size()<1)return null;
        for (Tag t: tagit) {
            if (t.getTag().contentEquals(tag)) return t;
        }
        Tag uusiTagi = new Tag(tag);
        uusiTagi.rekisteroi();
        tagit.add(uusiTagi);
        return uusiTagi;
    }
    
    /**
     * Palauttaa id:tä vastaavan tagin, jos sellainen on, muuten "";
     * @param id id, jolla haetaan tagia
     * @return tagi, jos sellainen on
     */
    public String getNimi(int id) {
        for (Tag t: tagit) {
            if (t.getId()==id) return t.getTag();
        }
        return "";
    }
    
    /**
     * Lukee ja luo Tagit-luokan 
     * @param hakemisto hakemisto, josta luetaan "tagit.dat" nimisestä tiedostosta
     * @throws SailoException jos lukemisessa ongelmia
     * @example
     * <pre name="test">
     *  #THROWS SailoException
     *  #THROWS IndexOutOfBoundsException
     *  #import java.io.File;
     *  
     *  Tagit t = new Tagit();

     *  String hakemisto = "testidata";
     *  String tiedNimi = hakemisto+"/tagit";
     *  File file = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  file.delete();
     *  
     *  t.lueTiedostosta(hakemisto); #THROWS SailoException
     *  Tag t1=t.lisaa("auto");
     *  Tag t2=t.lisaa("mopo");
     *  t.kirjoitaTiedostoon(hakemisto);
     *  
     *  t = new Tagit();
     *  t.lueTiedostosta(hakemisto);
     *  t.haeTagilla(t1.getTag()).getTag() === t1.getTag();
     *  t.haeTagilla(t2.getTag()).getTag() === t2.getTag();
     *  t.getLkm()===2;
     *  t.kirjoitaTiedostoon(hakemisto);
     *  file.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        String tiedostonNimi = hakemisto + "/tagit.dat";
        try (var br = new BufferedReader(new FileReader(tiedostonNimi))) {
            String rivi = br.readLine();
            while (rivi != null) {
                if (Character.isDigit(rivi.charAt(0))) {
                    Tag t=parseTag(rivi);
                    if (t==null) throw new SailoException("tagit.dat viallinen!");
                    lisaa(t);
                }
                rivi = br.readLine();
            }
            
        } catch (FileNotFoundException e) {
            throw new SailoException("Tagit: Tiedostoa ei l�ytynyt!");
        } catch (IOException e) {
            throw new SailoException("IO Virhe!");
        }

    }

    /**
     * Parsii merkkijonosta tagin sisällön, ei aseta saatua tagia,
     * rekisteröinti hoidetaan varmuuden vuoksi Tagin luomisen jälkeen.
     * @param tiedot Tagin tiedot merkkijonona, "id|tag|"
     * @return Tag merkkijonon perusteella, null jos lukeminen ei onnistunut
     */
    public static Tag parseTag(String tiedot) {
        return Tag.parse(tiedot);
    }

    /**
     * Kirjoittaa tagien tiedot annettusta hakemistosta löytyvään "tagit.dat" tiedostoon.
     * @param hakemisto hakemisto, jossa kirjoitetaan "tagit.dat" tiedostoon
     * @throws SailoException jos tiedostoon kirjoittaminen ei onnistu
     */
    public void kirjoitaTiedostoon (String hakemisto) throws SailoException {
        String tiedostonNimi = hakemisto + "/tagit.dat";
        String otsake = "tgid|tagi";
        StringBuilder data=new StringBuilder(otsake+"\n");
        try (var fo = new PrintStream(new FileOutputStream(tiedostonNimi))) {
            for (Tag t: tagit) {
                data.append(t.annaTiedot());
                data.append("\n");
            }
            System.out.print(data);
            fo.printf(data.toString());
        } catch (FileNotFoundException e) {
            throw new SailoException("Tagit: Tiedostoa ei l�ytynyt!");
        }

    }
            
    /**
     * Apupääohjelma testaamiseen
     * @param args ei käytössä
     */
    public static void main(String[] args) {
          Tagit tt=new Tagit();
          
          tt.lisaa("vallan_mainio").rekisteroi();
          tt.lisaa("vallan_mainio ").rekisteroi();
          System.out.println(tt.getLkm()); //1
          tt.lisaa("EI!").rekisteroi();
          System.out.println(tt.getLkm()); //2
          
          
          try {
            tt.kirjoitaTiedostoon(".");
        } catch (SailoException e) {
            e.printStackTrace();
        }
    }

    /**
     * Palauttaa tagin joka sijaitsee annetussa indeksissa
     * @param i annettu indeksi
     * @return Tagi
     */
    public Tag haeIndeksilla(int i) {
        return tagit.get(i);
    }

    /**
     * Poistaa tagin joka sijaitsee annetussa indeksissa
     * @param i Annettu indeksi
     * @throws SailoException jos jokin menee pieleen
     */
    public void poista(int i) throws SailoException {
        if (i >= getLkm()) throw new SailoException("Virhe! Yritetty poistaa tagi kelvottomalla indeksilla! " + i);
        tagit.remove(i);
        
    }

    /**
     * Paivittaa tagien seuraavaID:n arvon
     * Tata on hyva kayttaa, jotta ID:t eivat kasva luvultaan hirveasti
     */
    public void paivitaSeuraavaID() {
        Tag.paivitaSeuraavaID(suurinID()+1);
        
    }
    private int suurinID() {
        int max = -1;
        for (int i = 0; i < tagit.size(); ++i) {
            if (max < tagit.get(i).getId()) max = tagit.get(i).getId();
        }
        return max;
    }

}

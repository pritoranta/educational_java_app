package viihde;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 20.3.2019
 * Tyyppi-olio, jolla on id ja nimi
 */
public class Tyyppi {
    
    private int id;
    private String nimi;
    
    private static int seuraavaID=1;

    /**
     * Luo Tyyppi-olion nimen pohjalta
     * @param nimi Tyypin nimi
     * @example
     * <pre name="test">
     * Tyyppi t=new Tyyppi("Elokuva");
     * t.getNimi()==="Elokuva";
     * t.getId()===0;
     * </pre>
     */
    public Tyyppi(String nimi) {
        this.nimi=nimi;
    }
    
    /**
     * Luo Tyypin nimen ja id:n avulla, mieluiten kuitenkin rekisteröidään id muuten
     * @param n nimi
     * @param i id
     */
    public Tyyppi(String n, int i) {
        nimi=n;
        id=i;
        if (seuraavaID<=i)
            seuraavaID=i+1;
    }
    
    /**
     * @return Tyyppi-olion nimi
     */
    public String getNimi() {
        return nimi;
    }
    
    /**
     * Asettaa uniikin ID:n ja palauttaa sen,
     * paitsi jos on jo rekisteröity palauttaa nykyisen id:n
     * @return ID, joka asetetaan
     * @example
     * <pre name="test">
     * Tyyppi t=new Tyyppi("Aapinen");
     * t.getId()===0;
     * t.rekisteroi();
     * t.getId()===1;
     * t.rekisteroi();
     * t.getId()===1;
     * 
     * Tyyppi s=new Tyyppi("");
     * s.getId()===0;
     * s.rekisteroi();
     * s.getId()===2;
     * </pre>
     */
    public int rekisteroi() {
        if (id>0) return id;
        id=seuraavaID;
        seuraavaID++;
        return id;
    }

    /**
     * Antaa Tyypin tiedot merkkijonona
     * @return tiedot merkkijonossa: "id\t|nimi"
     */
    public String annaTiedot() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%d\t|", id));
        sb.append(nimi);
        sb.append("|");
        return sb.toString();
    }
    
    /**
     * Luo ja palauttaa merkkijonon tietojen perusteella luodun Tyypin
     * @param tiedot tiedot, Tyyppi-luokan tuntemassa muodossa: "id|nimi|"
     * @return luotu Tyyppi
     */
    public static Tyyppi parse(String tiedot) {
        String[] palat=tiedot.split("[( )\t]*\\|");
        if (palat.length<2) return null;
        int id=Mjonot.erotaInt(palat[0], 0);
        String nimi1=palat[1];
        return new Tyyppi(nimi1,id);
    }
    
    /**
     * @return Tyyppi-olion id, jos ei ole rekisteröity palauttaa 0
     */
    public int getId() {
        return id;
    }
    
    /**
     * testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Tyyppi t=new Tyyppi("Elokuva");
        System.out.println(t.getNimi());                
        System.out.println(t.getId());                
    }
}

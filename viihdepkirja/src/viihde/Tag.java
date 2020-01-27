package viihde;

import java.util.Random;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 21.3.2019
 * Tag-olio, tietää oman id;nsä ja mitä itsessänsä lukee, max 20 merkkiä pitkä
 */
public class Tag {
    
    private int id;
    private String tag;
    private final int MAX_PITUUS=20;
    
    private static int seuraavaID=1;
    
    /**
     * Alustaa tagin satunnaisilla luvuilla annetulle id:lle
     */
    public Tag() {
        Random r=new Random();
        tag=Integer.toString(r.nextInt()%1000);
    }
    
    /**
     * Alustaa tagin annetuilla arvoilla, paitsi jos tag>MAX_PITUUS, pätkäisee sen sopivan mittaiseksi
     * @param tag merkkijonona sisältö
     */
    public Tag(String tag) {
        if(tag.length()>MAX_PITUUS) this.tag=tag.substring(0, MAX_PITUUS);
        else this.tag=tag;
    }
    
    /**
     * Muodostaa Tagin tagin ja id:n avulla, mieluummin rekisteröidään id
     * @param t tag
     * @param i id
     */
    public Tag(String t, int i) {
        tag=t;
        id=i;
        if (seuraavaID<=i) seuraavaID=i+1;
    }
    
    /**
     * Antaa tagille ID:n (uniikki)
     * @return tagille annettu id
     */
    public int rekisteroi() {
        if (id>0) return id;
        id=seuraavaID;
        seuraavaID++;
        return id;
    }
    
    /**
     * @return tagin id, 0 jos ei ole rekisteroity
     */
    public int getId() {
        return id;
    }
    
    /**
     * @return tagin sisältö
     */
    public String getTag() {
        return tag;
    }

    /**
     * Antaa tagin tiedot merkkijonossa
     * @return tiedot muodossa id|tag|
     */
    public String annaTiedot() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%d\t|", id));
        sb.append(tag+"|");
        return sb.toString();
    }

    /**
     * Parsii taginsa merkkijonosta
     * @param tiedot merkkijono, josta parsii tietonsa: "id|tag|"
     * @return Tag tietojen perusteella
     */
    public static Tag parse(String tiedot) {
        String[] palat=tiedot.split("[( )]*\\|");
        if (palat.length<2) return null;
        int id=Mjonot.erotaInt(palat[0],0);
        String tag=palat[1];
        return new Tag(tag,id);
    }


    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Tag t=new Tag();
        t.rekisteroi();
        Tag n=new Tag();
        Tag b=new Tag("123456789012345678901234567890");
        
        System.out.println(t.getTag());
        System.out.println(n.getTag());
        System.out.println(b.getTag());

    }

    /**
     * Paivittaa seuraavaID:n arvon annettuun arvoon
     * @param seuraava Annettu arvo
     */
    public static void paivitaSeuraavaID(int seuraava) {
        seuraavaID = seuraava;
        
    }

}

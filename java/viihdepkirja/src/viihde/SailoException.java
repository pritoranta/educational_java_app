/**
 * 
 */
package viihde;

/**
 * Poikkeusluokka viihdep�iv�kirjan s�ili�iden poikkeuksia varten
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 11 Mar 2019
 *
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    /**
     * Poikkeuksen muodostaja joka ottaa parametriksi virheilmoitusta varten toivotun viestin
     * @param msg Virhe viesti
     */
    public SailoException(String msg) {
        super(msg);
    }
}

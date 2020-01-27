/**
 * 
 */
package fxViihde;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import viihde.ViihdePKirja;

/**
 * Ohjain filtteridialogia varten
 * @author Iisakki S�kkinen, Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 23 Apr 2019
 *
 */
public class FiltteritController implements ModalControllerInterface<String>, Initializable {


    @FXML private ComboBoxChooser<String> choiceLajittelu;
    
    @FXML private ComboBoxChooser<String> choiceTyyppi;

//    @FXML private CheckBox checkboxPeli;
//
//    @FXML private CheckBox checkboxElokuva;
//
//    @FXML private CheckBox checkboxKirja;
//
//    @FXML private CheckBox checkboxSarja;
    
    @FXML private TextField editTagit;

    @FXML private Label labelVirhe;

    @FXML void handleDefaultCancel() {
        hakuehdot = null;
        ModalController.closeStage(labelVirhe);
    }

    @FXML void handleDefaultOK() {
        if (labelVirhe != null && !labelVirhe.getText().isEmpty()) return;
        //if (labelVirhe != null && labelVirhe.getText() != null && labelVirhe.getText() != null) return;
        tallenna();
        ModalController.closeStage(labelVirhe);
    }
    
    private void tallenna() {
        hakuehdot = (tagiehdot == null ? "" : tagiehdot) + "<>" +
                (tyyppiehdot == null ? "" : tyyppiehdot) + "<>" +
                (lajitteluehto == null ? "" : lajitteluehto); // Nama taytyy sitten erotella jossakin jos haluaa erikseen testata molempia
        
    }

    //==================================================
    
    private String hakuehdot;
    private String tagiehdot;
    private String tyyppiehdot;
    private String lajitteluehto;
    private static ViihdePKirja kohdeVpk;

    @Override public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    @Override public String getResult() {
        return hakuehdot;
    }

    @Override public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override public void setDefault(String oletus) {
        hakuehdot = oletus;
        
    }
    
    private void alusta() {
        editTagit.setOnKeyReleased(e -> kasitteleMuutosSuodattimeen(0, (TextField)e.getSource()));
        editTagit.setText("");
        naytaVirhe(null);
        String[] tyyp=kohdeVpk.annaTyypit();
        for (int i=0; i<tyyp.length; i++) {
            choiceTyyppi.add(tyyp[i]);
        }
        choiceTyyppi.addSelectionListener(e -> kasitteleMuutosTyyppiehtoihin(choiceTyyppi.getSelectedObject()));
        choiceLajittelu.add("Nimi laskeva");
        choiceLajittelu.add("Nimi nouseva");
        choiceLajittelu.addSelectionListener(e -> kasitteleMuutosLajitteluehtoihin(choiceLajittelu.getSelectedObject()));

//        checkboxElokuva.setOnAction(e -> kasitteleMuutosTyyppiehtoihin(0));
//        checkboxPeli.setOnAction(e -> kasitteleMuutosTyyppiehtoihin(1));
    }

    /**
     * Kasittelee muutokset lajitteluehtoihin
     * @param lajittelu Uudet lajitteluehdot
     */
    private void kasitteleMuutosLajitteluehtoihin(String lajittelu) {
        String virhe = null;
        virhe = muutaLajitteluehto(lajittelu);
        naytaVirhe(virhe);
    }

    /**
     * Muuttaa lajitteluehdot.
     * @param lajittelu
     * @return Virheilmoituksen jos jokin menee pieleen
     */
    private String muutaLajitteluehto(String lajittelu) {
        //if (lajittelu == null) return null; // Ei tarvitse olla ehtoja aina, tyhjakin kelpaa
        lajitteluehto = lajittelu;
        return null;
    }

    /**
     * Kasittelee muutokset tyyppiehtoihin
     * @param tyyppi Uudet tyyppiehdot
     */
    private void kasitteleMuutosTyyppiehtoihin(String tyyppi) {
        String virhe = null;
        
        virhe = muutaTyyppiehdot(tyyppi);

        naytaVirhe(virhe);
    }

    private void kasitteleMuutosSuodattimeen(int i, TextField edit) {
        //if (hakuehdot == null) return;
        String s = edit.getText().trim();
        String virhe = null;
        switch (i) {
        case 0:
            virhe = muutaTagiehdot(s);
            break;
        default:   
        }
        if (virhe == null ) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    /**
     * Muuttaa tyyppiehdot jos ei ole ongelmia, muuten palautetaan virheilmoitus merkkijonona
     * @param s Annetut uudet tyyppiehdot
     * @return Virheilmoituksen jos jokin menee pieleen
     */
    private String muutaTyyppiehdot(String s) {
        if (tyyppiehdot == null) tyyppiehdot = s;
        if (!tyyppiehdot.matches(s)) tyyppiehdot = tyyppiehdot + "," + s;
        return null;
    }

    /**
     * Tulostaa virheilmoituksen sille sovittuun labeliin
     * @param virhe Virheilmoitus joka halutaan nayttaa
     */
    void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
            }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    /**
     * Muuttaa tagiehdot, mikali ne eivat sisalla virheellisia merkkeja, ja muutenkin seuraavat oikeaa syntaksia
     * @param s Annetut tagiehdot
     * @return Virheilmoitus jos jotain menee pieleen, muulloin null
     */
    private String muutaTagiehdot(String s) {
        String virhe = kohdeVpk.kaykoTagit(s.split("(\\s)*,(\\s)*"));
        if (virhe != null && !virhe.isEmpty()) return virhe;
        if (!s.matches("(^[a-zA-Z]+((\\s)*,(\\s)*[a-zA-Z]*)*)*")) return "Virheelliset tagit!";
        String[] ehdot = s.split("(\\s)*,(\\s)*");
        StringBuilder uudetTagiehdot = new StringBuilder("");
        for (int i = 0; i < ehdot.length; ++i) {
            uudetTagiehdot.append(ehdot[i] + (i < ehdot.length - 1 ? "," : ""));
        }
        System.out.println(uudetTagiehdot); // Debuggailua varten vain
        tagiehdot = uudetTagiehdot.toString();
        return null;
    }

    /**
     * Luodaan suodatindialogi ja palautetaan muokatut hakuehdot
     * @param modalityStage Mille ollaan modaalisia, null = sovellukselle
     * @param oletus Oletus olio joka p
     * @param vpk ViihdePKirja, jota käytetään
     * @return null jos perutaan, muulloin muutetut hakuehdot
     * TODO: Tee suodattimille luokka mahdollisesti!
     */
    public static String kysySuodatin(Stage modalityStage, String oletus, ViihdePKirja vpk) {
        kohdeVpk=vpk;
        return ModalController.showModal(FiltteritController.class.getResource("FiltteritView.fxml"), "Suodattimet", modalityStage, oletus,null);
    }

}
 
package fxViihde;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import viihde.Esine;
import viihde.ViihdePKirja;

/**
 * @author Pietari Ritoranta ja Iisakki Säkkinen
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 19.2.2019
 * Muokkaus/lisäys -lehden controller
 */

public class MuokkausController implements ModalControllerInterface<Esine>, Initializable {

    @FXML private TextArea esineKommentti;
    @FXML private TextField esineNimi;
    @FXML private TextField esineArvosana;
    @FXML private TextField esineTagit; 
    @FXML private Label labelVirhe;
    @FXML private TextField esinePaiva;
    @FXML private TextField esineKk;
    @FXML private TextField esineVuosi;
    @FXML private TextField esineTyyppi;

    private TextField[] edits;

    
    @FXML void handlePeruuta() {
        esineKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    @FXML void handleTallenna() {
        // TODO: Lis�� virhe testej� mahdollisesti

        tallenna();
        ModalController.closeStage(labelVirhe);
    }
    
    @Override public Esine getResult() {
        return esineKohdalla;
    }

    @Override public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override public void setDefault(Esine oletus) {
        esineKohdalla = oletus;
        naytaEsine(esineKohdalla);
    }

    @Override public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }
    //======================================
    
    private Esine esineKohdalla; 
    private static ViihdePKirja kohdeVpk;    // Tarvitaan jotta tagien p�ivitt�minen onnistuu mutkitta  

    private static String[] nykyisetTagit; // T�ll� hetkell� muokattavissa olevan esineen tagit. Pit�� olla static koska
    private static char erotin=','; // merkki, jolla käyttäjän kuuluu erotella tagit
    private String nykyinenTyyppi;
    
    /**
     * Tulostaa virheilmoituksen sille sovittuun labeliin
     * @param virhe
     */
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
            }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    /**
     * Tallentaa tehdyt muutokset, mikali mikaan ei mene pieleen. 
     * Jos tulee ongelmia, metodi tulostaa virheen ja palauttaa void
     */
    private void tallenna() {
        if (esineKohdalla != null && esineKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhj�!");
            return;
        }
        if (labelVirhe != null && !labelVirhe.getText().isEmpty()) return;
        tallennaTagit();
        tallennaTyyppi();
    }
    
    /**
     * Tallentaa tagitiedot
     */
    private void tallennaTagit() {
        kohdeVpk.setTagit(esineKohdalla, nykyisetTagit);
    }
    
    /**
     * Tallentaa tyyppitiedot
     */
    private void tallennaTyyppi() {
        kohdeVpk.setTyyppi(esineKohdalla, nykyinenTyyppi);
    }
    
    /**
     * Tulostaa esineen tiedot sijoittaen jokaisen erillisen tiedon omaan paikkaansa
     * @param esine
     */
    private void naytaEsine(Esine esine) {
        if (esine == null) return;
        esineNimi.setText(esine.getNimi());
        esineArvosana.setText(String.format("%5.2f",esine.getArvosana()).trim());
        esineKommentti.setText(esine.getKommentti());
        if (kohdeVpk != null) nykyisetTagit = kohdeVpk.annaTagit(esine);
        esineTagit.setText(tulostaTagit());
        var pvm = esineKohdalla.getPvm().split("[/-]");
        if (pvm.length >= 2) {
        esinePaiva.setText(pvm[2]);
        esineKk.setText(pvm[1]);
        esineVuosi.setText(pvm[0]);
        }
        esineTyyppi.setText(kohdeVpk.getTyyppi(esine.getTyyppi()));
        nykyinenTyyppi = esineTyyppi.getText();
    }
    
    /**
     * Alustaa edit-kent�t antamalla niille metodin jota kutsua kun kentt�� muokataan
     */
    protected void alusta() {
        edits = new TextField[] {esineNimi, esineTagit, esineArvosana, esinePaiva, esineKk, esineVuosi, esineTyyppi};
        naytaVirhe(null);
        int i = 0;
        for (var edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased(e -> kasitteleMuutosEsineeseen(k, (TextField) e.getSource()));
        }
        final int k=++i;
        esineKommentti.setOnKeyReleased(e -> kasitteleMuutosEsineeseen(k, (TextArea) e.getSource()));
    }
    
    /**
     * Palauttaa muokattavan esineen tagit merkkijonona
     * @return Tagit merkkijonona
     */
    private String tulostaTagit() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < nykyisetTagit.length; ++i) {
            sb.append(nykyisetTagit[i] == null ? "" : (nykyisetTagit[i] + ( i < nykyisetTagit.length - 1 ? ", " : "")));
        }
        return sb.toString();
    }
    
    private String[] parseTagit(String s) {
        return s.split(""+erotin);
    }
    
    /**
     * Kasittelee muutokset esineeseen riippuen annetuista parametreista
     * @param k Annettu arvo, jonka perusteella valitaan, mita tiedon muutosta kasitellaan
     * @param edit TextField, josta voidaan hankkia tarvittava tieto esineen tiedon muuttamiseen
     */
    private void kasitteleMuutosEsineeseen(int k, TextField edit) {
        if (esineKohdalla == null) return;
        String s = edit.getText().trim();
        String virhe = null;
        switch(k) {
        case 1: virhe = esineKohdalla.setNimi(s);
                break;
        case 2: virhe = kohdeVpk.kaykoTagit(parseTagit(s));
                nykyisetTagit=parseTagit(s);
                break;
        case 3: virhe = esineKohdalla.setArvosana(s.replaceAll(",", "."));
                break;
        case 4: virhe = esineKohdalla.setPaiva(s);
                break;
        case 5: virhe = esineKohdalla.setKuukausi(s);
                break;
        case 6: virhe = esineKohdalla.setVuosi(s);
                break;
        case 7: virhe = kohdeVpk.kaykoTyyppi(s);
                nykyinenTyyppi=s;
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
     * Kasittelee muutokset esineeseen riippuen annetuista parametreista
     * @param k Annettu arvo, jonka perusteella valitaan, mita tiedon muutosta kasitellaan
     * @param edit TextArea, josta voidaan hankkia tarvittava tieto esineen tiedon muuttamiseen
     */
    private void kasitteleMuutosEsineeseen(int k, TextArea edit) {
        if (esineKohdalla==null) return;
        String s = edit.getText().trim();
        String virhe = null;
        switch(k) {
        case 8: virhe = esineKohdalla.setKommentti(s);
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
     * Luodaan esineen kysymysdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage Mille ollaan modaalisia, null = sovellukselle
     * @param oletus Mit� dataa n�ytet��n oletuksena
     * @param vpk ViihdePKirja, joka hoitaa tagien p�ivitt�misen (esim. tilanteessa, jossa k�ytt�j� keksii uuden tagin)
     * @return null jos perutaan, muuten t�ytetty tietue
     */
    public static Esine kysyEsine(Stage modalityStage, Esine oletus, ViihdePKirja vpk) {
        kohdeVpk = vpk;
        return ModalController.showModal(MuokkausController.class.getResource("MuokkausView.fxml"), "Merkinnat", modalityStage, oletus, null);
    }
    
    /**
     * Luodaan esineen kysymysdialogi ja palautetaan sama tietue muutettuna tai null
     * K�ytet��n mieluummin tuota versiota jossa annetaan viimeiseksi parametriksi ViihdePKirja!
     * @param modalityStage Mille ollaan modaalisia, null = sovellukselle
     * @param oletus Mit� dataa n�ytet��n oletuksena
     * @param tagit Tagit, jotka kuuluvat annetulle esineelle
     * @return null jos perutaan, muuten t�ytetty tietue
     */
    public static Esine kysyEsine(Stage modalityStage, Esine oletus, String[] tagit) {
        nykyisetTagit = tagit;
        return ModalController.showModal(MuokkausController.class.getResource("MuokkausView.fxml"), "Merkinnat", modalityStage, oletus, null);
    }

}

package fxViihde;

import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import viihde.Esine;
import viihde.SailoException;
import viihde.ViihdePKirja;

/**
 * @author Iisakki S�kkinen ja Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 21.3.2019
 * Apua seka Tietoja painikkeet eivat ole toiminnassa (katso handleApua() ja handleTietoja())
 */
public class ViihdeGUIController implements Initializable {

    @FXML private ListChooser<Esine> chooserEsine;
    @FXML private TextArea panelEsine;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }
        
    
    @FXML void handleLisaaUusi() {
        uusiEsine();
    }

    @FXML void handleMuokkaa() {
        muokkaa();
    }

    @FXML void handleSuodattimet() {
        suodata();
    }
    
    @FXML void handleApua() {
        Dialogs.showMessageDialog("Ei osata viel� auttaa!");
    }

    @FXML void handleAvaa() {
        alusta();
        avaa();
    }

    @FXML void handlePoista() {
        poista();
    }

    /**
     * Poistuu ohjelmasta kysyen ensin haluaako kayttaja tallentaa mahdolliset muutokset
     * Ohjelma tallentaa muutokset mikali kayttaja vastaa myonteisesti
     */
    @FXML void handlePoistu() {
        boolean tallennetaanko = Dialogs.showQuestionDialog("Tallennetaanko muutokset?", "Tallennetaanko tiedostoon tehdyt muutokset ennen poistumista?", "Tallenna", "Älä Tallenna");
        if (tallennetaanko) tallenna();
        
        Platform.exit();
    }
    
    @FXML void handleTietoja() {
        Dialogs.showMessageDialog("Ei osata viel� antaa tietoja!");
    }
    
    @FXML void handleTallenna() {
        tallenna();
    }
    
    //====================================================================
    
    private ViihdePKirja vpk = new ViihdePKirja();
    private Esine esineKohdalla;
    private TextArea areaEsine=new TextArea();
    private String hakuehdot = "";
    private String hakemisto ="data";
    
    /**
     * Alustaa ohjelman niin kuin tarvitsee
     */
    protected void alusta() {

       //panelEsine.setText(areaEsine.getText());
        vpk=new ViihdePKirja();
        areaEsine=panelEsine;
        panelEsine.setFont(new Font("Courier New", 12));

        
        chooserEsine.clear();
        chooserEsine.addSelectionListener(e -> naytaEsine());
    }
    
    /**
     * Tallentaa tiedot oletushakemistoon (HUOM! Ei ole viel� vaihtoehtoa muille hakemistoille)
     */
    protected void tallenna() {
        try {
            vpk.tallenna();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Virhe tallennuksessa!");
            System.err.println("Virhe tiedoston tallennuksessa: "+e.getMessage());
        }
    }
    
    /**
     * Avaa MuokkausControllerin, jossa voidaan sitten muokata valittua esinetta
     * Jos muutoksia ei tehda, tai muutokset perutaan, niin valittua esinettakaan ei korvata uudella
     */
    private void muokkaa() {
        if (esineKohdalla == null) return;
        Esine esine;
        try {
            esine = MuokkausController.kysyEsine(null, esineKohdalla.clone(), vpk);
            if (esine == null) return;
            vpk.korvaaTaiLisaa(esine);
            hae(esine.getID());
        } catch (CloneNotSupportedException e) {
            Dialogs.showMessageDialog("Virhe on tapahtunut.");
            System.err.println("Virhe muokkauksessa: "+e.getMessage());
        }
    }
    
    /**
     * Hankkii uudet suodatusehdot FiltteritControllerilta, ja paivittaa hakutulosten listan nailla uusilla ehdoilla
     */
    private void suodata() {
        if (hakuehdot == null) return;
        String haku = FiltteritController.kysySuodatin(null, hakuehdot, vpk);
        if (haku == null) return;
        hakuehdot = haku;
        hae(0,hakuehdot);
    }
    
    /**
     * Poistaa valitun esineen paivakirjasta. 
     * Etsii taman jalkeen sita aiemman esineen ja kohdistaa siihen valinnan
     */
    private void poista() {
        if (esineKohdalla == null) return;
        vpk.poista(esineKohdalla);
        if (vpk.getEsineita() <= 0) hae(0);
        else hae(vpk.annaEsine(vpk.getEsineita()-1).getID());
    }
    
    /**
     * Näyttää navigoinnista valitun Esineen tiedot
     */
    protected void naytaEsine() {
        esineKohdalla=chooserEsine.getSelectedObject();
        
        if(esineKohdalla==null)return;
        
        areaEsine.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(panelEsine)) {
            vpk.tulostaTiedot(esineKohdalla, os);
            //esineKohdalla.tulosta(os);
        }
    }
    
    /**
     * Hakee navigointipalkkiin Esineiden nimet (painettavissa)
     * @param eid Esineen id, joka valitaan näytettäväksi
     */
    protected void hae (int eid) {
        chooserEsine.clear();
        
        int index=0;
        for (int i=0; i<vpk.getEsineita(); i++) {
            Esine esine=vpk.annaEsine(i);
            if (esine.getID()==eid) index=i;
            chooserEsine.add(esine.getNimi(), esine);
        }
        chooserEsine.setSelectedIndex(index);
    }
    
    /**
     * Hakee navigointipalkkiin Esineiden nimet (painettavissa)
     * Hakee ainoastaan hakuehtoon sopivat kohteet
     * @param eid Esineen id, joka valitaan näytettäväksi
     * @param hakuehto Annettu hakuehto
     */
    protected void hae (int eid, String hakuehto) {
        String haku = "";
        if (hakuehto != null) haku = hakuehto;
        chooserEsine.clear();
        
        int index=0;
        ArrayList<Esine> esineet = vpk.etsiEsineet(haku);
        if (esineet == null) return;
        for (int i=0; i < esineet.size(); i++) {
            Esine esine = esineet.get(i);
            if (esine.getID()==eid) index=i;
            chooserEsine.add(esine.getNimi(), esine);
        }
        chooserEsine.setSelectedIndex(index);
    }
    
    /**
     * @param vpk ViihdePKirja jota käytetään tässä käyttöliittymässä
     */
    public void setVPK(ViihdePKirja vpk) {
        this.vpk = vpk;
        //this.vpk.lueTiedostosta();
        hae(0);
        naytaEsine();
    }

    
    /**
     * Alustaa vpk:n lukemalla sen valitun nimisestä tiedostosta
     * @param hak tiedosto josta kerhon tiedot luetaan
     */
    protected void lueTiedosto(String hak) {
            hakemisto = hak;
            vpk.setHakemisto(hak);
            try {
                vpk.lueTiedostosta();
            } catch (SailoException e) {
                Dialogs.showMessageDialog("Tiedostoa ei löytynyt tai se on viallinen.\nLuodaan uusi tiedosto.");
                System.err.println("Virhe tiedoston lukemisessa: "+e.getMessage());
            }
            hae(0);
     }
    
    /**
     * Alustaa vpk:n lukemalla sen oletushakemistosta
     */
    public void lueTiedosto() {
        try {
            vpk.lueTiedostosta();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Tiedostoa ei löytynyt tai se on viallinen.\nLuodaan uusi tiedosto.");
            System.err.println("Virhe tiedoston lukemisessa: "+e.getMessage());
        }
        hae(0);
    }
    
    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = VpkNimiController.kysyNimi(null, hakemisto);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }    
    /**
     * Lisää uuden esineen
     */
    protected void uusiEsine() {
        Esine uusi=new Esine();   //vpk.lisaaTayttoEsine();
        
        try {
            uusi = MuokkausController.kysyEsine(null, uusi.clone(), vpk);
            if (uusi == null) return;
            vpk.korvaaTaiLisaa(uusi);
            uusi.rekisteroi();
            hae(uusi.getID());
        } catch (CloneNotSupportedException e) {
            Dialogs.showMessageDialog("Virhe on tapahtunut.");
            System.err.println("Virhe Esineen lisäämisessä: "+e.getMessage());
        }
    }

    }


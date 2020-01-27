package fxViihde;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Pietari Ritoranta, Iisakki Säkkinen
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 25.4.2019
 * 
 * Kysytään vpk:n nimi dialogissa
 */
public class VpkNimiController implements ModalControllerInterface<String> {

    @FXML private TextField textNimi;
    private String nimi=null;

    @FXML private Button buttonOK;

    /**
     * Tallentaa haettavan tiedoston uuden nimen ja sulkee dialogin
     */
    @FXML private void handleOK() {
        nimi = textNimi.getText();
        ModalController.closeStage(textNimi);
    }

    @Override
    public String getResult() {
        return nimi;
    }

    @Override
    public void handleShown() {
        textNimi.requestFocus();
    }

    @Override
    public void setDefault(String oletus) {
        textNimi.setText(oletus);
    }
    
    /**
     * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä nimeä näytetään oletuksena
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                VpkNimiController.class.getResource("VpkNimiView.fxml"),
                "ViihdePKirja",
                modalityStage, oletus);
    }    
}

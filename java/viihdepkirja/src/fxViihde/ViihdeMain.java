package fxViihde;

import javafx.application.Application;
import javafx.stage.Stage;
import viihde.ViihdePKirja;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Iisakki S�kkinen ja Pietari Ritoranta
 * einoiisko@gmail.com, parerito@student.jyu.fi
 * @version 21.3.2019
 *
 */
public class ViihdeMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("ViihdeGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final ViihdeGUIController viihdeCtrl = (ViihdeGUIController) ldr.getController();
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("viihde.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Viihde");
            
            ViihdePKirja vpk=new ViihdePKirja();
            viihdeCtrl.setVPK(vpk);     
            viihdeCtrl.avaa();
            
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei k�yt�ss�
     */
    public static void main(String[] args) {
        launch(args);
    }
}
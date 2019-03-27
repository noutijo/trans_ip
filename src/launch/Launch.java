/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Noumodong
 */
public class Launch extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane pan = FXMLLoader.load(getClass().getResource("/fxml/accueil.fxml"));
        stage.getIcons().add(new Image(getClass().getResource("/icons/appicon.png").toString()));
        stage.setTitle("Transfert de fichier par IP en réseau local");
        stage.setMinWidth(650);
        stage.setMinHeight(480);
        Scene scene = new Scene(pan);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

}

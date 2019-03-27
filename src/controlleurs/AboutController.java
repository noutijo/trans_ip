/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleurs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Noumodong
 */
public class AboutController implements Initializable {

    @FXML
    void fermerCliquer(MouseEvent event) {
        if (AccueilController.controlMouvementFocus == 1) {
            BoxBlur ff = new BoxBlur(0, 0, 0);
            AccueilController.panFocuss.setEffect(ff);
            AccueilController.diaglogFocus.close();
            AccueilController.panControlMousee.setMouseTransparent(true);
            AccueilController.controlMouvementFocus=0;
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleurs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.*;
import managerSentAndReceive.Client;
import managerSentAndReceive.Serveur;

/**
 *
 * @author Noumodong
 */
public class AccueilController implements Initializable {

    @FXML
    private JFXTextField textFieldSelectionnerEnvoyer;

    @FXML
    private JFXTextField textFieldAddressIPEnvoyer;

    @FXML
    private JFXTextField portEnvoyer;

    @FXML
    private ProgressIndicator progressEnvoyer;
    public static ProgressIndicator progressEnvoyerr;

    @FXML
    private JFXTextField textFieldRecevoir;

    @FXML
    private HBox choisirRecevoir;

    @FXML
    private JFXTextField portRecevoir;

    @FXML
    private ProgressIndicator progressRecevoir;
    public static ProgressIndicator progressRecevoirr;
    @FXML
    private Text alertEnvoyer;
    public static Text alertEnvoyerr;
    File fileSelected = null;
    String cheminDesauvegarde = "C:\\oreolnoumodong\\";
    @FXML
    private AnchorPane panControlMouse;
    public static AnchorPane panControlMousee;
    private Thread mainThread;

    void annulerCliquerRecevoir(MouseEvent event) {

    }

    @FXML
    void annulerCliquerrEnvoyer(MouseEvent event) {
        mainThread.stop();
    }

    @FXML
    void envoyerCliquerrEnvoyer(MouseEvent event) {
        mainThread = new Client(textFieldAddressIPEnvoyer.getText(), Integer.parseInt(portEnvoyer.getText()), this.fileSelected);
        mainThread.start();
    }

    //permot to cotrol choosing folder that we want to send in local network
    @FXML
    void choisirFichierCliquerEnvoyer(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        this.fileSelected = fileChooser.showOpenDialog(null);
        if (this.fileSelected != null) {
            this.textFieldSelectionnerEnvoyer.setText(fileSelected.getAbsolutePath());

        } else if ((this.fileSelected == null && textFieldSelectionnerEnvoyer.getText().isEmpty())) {
            new Thread(() -> {
                try {
                    alertEnvoyer.setText("Veillez sélectionner un fichier si vous plait.");
                    Thread.sleep(5000);
                    alertEnvoyer.setText("");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
            //><
        }

    }

    @FXML
    void telechargerCliquerRecevoir(MouseEvent event) {
        new Serveur(cheminDesauvegarde, Integer.valueOf(portRecevoir.getText())).start();
    }

    @FXML
    void choisirCheminSauvegardeCliquerRecevoir(MouseEvent event) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Sélectionner un dossier");
        File pathsFolder = chooser.showDialog(null);

        if (pathsFolder != null) {
            this.cheminDesauvegarde = pathsFolder.getAbsolutePath() + "\\";
            textFieldRecevoir.setText(this.cheminDesauvegarde);

        } else {
            System.out.println("you don't select any folder please select folder");
        }
    }

    //about
    public static JFXDialog diaglogFocus;
    public static JFXDialog diaglogFocuss;
    @FXML
    private StackPane panStackFocus;

    @FXML
    private AnchorPane panFocus;
    public static AnchorPane panFocuss;
    public static int controlMouvementFocus = 0;

    @FXML
    void aboutCliquer(MouseEvent event) throws IOException {
        if (controlMouvementFocus == 0) {
            switchHeilpAbout("about");
        }
    }

    @FXML
    void helpCliquer(MouseEvent event) throws IOException {
        if (controlMouvementFocus == 0) {
            switchHeilpAbout("help");
        }
    }

    public void switchHeilpAbout(String chmn) throws IOException {
        panControlMouse.setMouseTransparent(false);
        BoxBlur ff = new BoxBlur(5.0, 5.0, 1);
        panFocus.setEffect(ff);
        Region root = FXMLLoader.load(getClass().getResource("/fxml/" + chmn + ".fxml"));
        diaglogFocus = new JFXDialog(panStackFocus, root, JFXDialog.DialogTransition.TOP);
        //dia.setBackground(Background.EMPTY);
        diaglogFocus.setOverlayClose(false);
        diaglogFocus.show();
        //
        controlMouvementFocus = 1;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AccueilController.panFocuss = panFocus;
        AccueilController.alertEnvoyerr = this.alertEnvoyer;
        AccueilController.progressEnvoyerr = this.progressEnvoyer;
        AccueilController.progressRecevoirr = this.progressRecevoir;
        AccueilController.diaglogFocuss = diaglogFocus;
        panControlMouse.setMouseTransparent(true);
        AccueilController.panControlMousee = panControlMouse;
        new File("C:\\oreolnoumodong").mkdir();

    }

}


package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.FindReq;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Direction;
import gestcourierclient.entities.Provenance;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class EditDirectionController implements Initializable {

    @FXML
    private TextField TXT_ABREV_DIR_EDT;
       
    @FXML
    private TextField TXT_CODE_DIR_EDT;

    @FXML
    private Button BTN_UPDT_DIR;

    @FXML
    private Button BTN_CLOS_DIR_EDT;

    @FXML
    private ComboBox<String> CMBX_PROV_DIR_EDT;

    @FXML
    private TextField TXT_DES_DIR_EDT;

    @FXML
    void editDirectionAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_DIR_EDT){
            new CloseWindow(event);
        }else if(event.getSource()==BTN_UPDT_DIR){
             //On verifie si les champs ne sont pas vide
            String codePro = CMBX_PROV_DIR_EDT.getValue();
            String codeDir = TXT_CODE_DIR_EDT.getText();
            String abrevDir = TXT_ABREV_DIR_EDT.getText();
            String desc = TXT_DES_DIR_EDT.getText();
            if("".equals(codeDir)||"".equals(codePro)||"".equals(desc)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type consernant et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui modifie le provenant
                
                */
                 Provenance prov= FindReq.findProvenant(codePro);
                 Direction direction= new Direction(codeDir,abrevDir,desc,prov);
                try {
                     Direction dir= updateDirection(direction);
                    if(dir!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "La direction a été modifié avec succée",null);
                        CMBX_PROV_DIR_EDT.setValue("");
                        TXT_CODE_DIR_EDT.setText("");
                         TXT_DES_DIR_EDT.setText("");
                    }
                   else if(dir==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur de modifier cette direction.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", e.getMessage(),null);
                }
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    void setTextFiled(String codePro,String codeDir,String abrevDir, String descr) {
       this.CMBX_PROV_DIR_EDT.setValue(codePro);
       this.TXT_CODE_DIR_EDT.setText(codeDir);
       this.TXT_ABREV_DIR_EDT.setText(abrevDir);
       this.TXT_DES_DIR_EDT.setText(descr);
    }
    private Direction updateDirection(Direction dir) {
         /* fonction qui se connecte avec l API REST du server et envois une requete de type
            put pour modifier une direction a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner la direction modifier.
        */
             String url ="http://localhost:8080/directions";
             Client client =ClientBuilder.newClient();
             Direction direction= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(dir),Direction.class);
             return direction;
    }
    
}


package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Provenance;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


public class NewProvenantController implements Initializable {

    @FXML
    private TextField TXT_ABRV_PROV;

    @FXML
    private Button BTN_ADD_PROV;

    @FXML
    private Button BTN_CLOS_PROV;

    @FXML
    private TextField TXT_LIBL_PROV;
    
    @FXML
    private TextField TXT_CODE_PROV;

    @FXML
    void newProvenantAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_PROV){
            CloseWindow closeWindow = new CloseWindow(event);
        }else if(event.getSource()==BTN_ADD_PROV){
            //On verifie si les champs ne sont pas vide
            String code= TXT_CODE_PROV.getText();
            String abrevProv= TXT_ABRV_PROV.getText();
            String libelProv= TXT_LIBL_PROV.getText();
            if("".equals(abrevProv)||"".equals(libelProv)||"".equals(code)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type Provenance et le donne les
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute la Provenance
                 */
                try {
                    Provenance pro=null;
                    pro= addProvenant(new Provenance(code,abrevProv,libelProv));
                   
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "l ajout a été effectué avec succée",null);
                        TXT_ABRV_PROV.setText("");
                        TXT_CODE_PROV.setText("");
                        TXT_LIBL_PROV.setText("");
                   
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", "Il ya eu une erreur de type:"+e.getMessage(),null);
                }
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

    private Provenance addProvenant(Provenance provenance) {
         /* fonction qui se connecte avec l API REST du server et envois une requete de type
            post pour ajouter un provenant a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner le provenant ajoute.
        */
             String url ="http://localhost:8080/provenances";
             Client client =ClientBuilder.newClient();
             Provenance prov= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(provenance),Provenance.class);
             System.out.println(prov);
             return prov;
    }
    
}

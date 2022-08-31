
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Consernant;
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

public class NewConsController implements Initializable {
    @FXML
    private TextField TXT_COD_CONS;

    @FXML
    private TextField TXT_NOM_CONS;

    @FXML
    private TextField TXT_PRENM_CONS;

    @FXML
    private Button BTN_ADD_CONS;

    @FXML
    private Button BTN_CLOS_CONS;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    @FXML
    public void newConsAction(ActionEvent event ){
        if(event.getSource()==BTN_CLOS_CONS){
            //Si c est le button fermer qui est cliquer on appelle la fonction qui ferme la fenetre
            CloseWindow closeWindow = new CloseWindow(event);
        }
        else if(event.getSource()==BTN_ADD_CONS){
            //On verifie si les champs ne sont pas vide
            String code_cons = TXT_COD_CONS.getText();
            String nom_cons = TXT_NOM_CONS.getText();
            String pren_cons = TXT_PRENM_CONS.getText();
            if("".equals(code_cons)||"".equals(nom_cons)||"".equals(pren_cons)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type consernant et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute le consernant
                
                */
                Consernant consernant =new Consernant(code_cons,nom_cons,pren_cons);
                try {
                    Consernant cons= addConsernant(consernant );
                    WindowDialog windowDialog = new WindowDialog("info", "Consernant ajoute avec succée",null);
                    TXT_COD_CONS.setText("");
                    TXT_NOM_CONS.setText("");
                    TXT_PRENM_CONS.setText("");
                      
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", "Une erreur est survenu veiller ressayer",null);
                }
            }
        }
    }
    public Consernant addConsernant(Consernant cons ){
        /* fonction qui se connecte avec l API REST du server et envois une requete de type
            post pour ajouter un consernaant a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner le consernant ajoute.
        */
             String url ="http://localhost:8080/consernants";
             Client client =ClientBuilder.newClient();
             Consernant consernant= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(cons),Consernant.class);
             return consernant;
    }
    
}

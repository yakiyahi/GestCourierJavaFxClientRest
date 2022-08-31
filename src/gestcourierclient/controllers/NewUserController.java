
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Utilisateur;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class NewUserController implements Initializable {

    @FXML
    private TextField TXT_NUM_USR;

    @FXML
    private TextField TXT_NOM_USR;

    @FXML
    private TextField TXT_PSEUDO_USR;

    @FXML
    private Button BTN_ADD_USR;

    @FXML
    private Button BTN_CLOS_USR;

    @FXML
    private PasswordField TXT_PASSWORD;

    @FXML
    private PasswordField TXT_CONFRM_PASS;
    @FXML
    private ComboBox<String> CMBX_ROLE_USR;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUserRule_cmbx();
    } 
    @FXML
    void newUsrAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_USR){
        new CloseWindow(event);
        }else if(event.getSource()==BTN_ADD_USR){
            //On verifie si les champs ne sont pas vide
            String user_num = TXT_NUM_USR.getText();
            String user_name= TXT_NOM_USR.getText();
            String pseudo = TXT_PSEUDO_USR.getText();
            String role = CMBX_ROLE_USR.getValue();
            String password = TXT_PASSWORD.getText();
            String confirmPass = TXT_CONFRM_PASS.getText();
            if("".equals(user_num)||"".equals(user_name)||"".equals(pseudo)||"".equals(role)||"".equals(password)||"".equals(confirmPass)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else if(!password.equals(confirmPass)){
                 new WindowDialog("warning", "Les mots de passe ne correspondent pas",null);
            }
            else{
                /*Si les champs ne sont ps vide,on creer un object de type utilisateur et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute le utiisateur
                
                */
                Utilisateur user =new Utilisateur(user_num, user_name, pseudo, password, role);
                try {
                    Utilisateur usr= addUtilisateur(user );
                    
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "Utilisteur ajoute avec succée",null);
                        TXT_NUM_USR.setText("");
                        TXT_NOM_USR.setText("");
                        TXT_PSEUDO_USR.setText("");
                        CMBX_ROLE_USR.setValue("");
                        TXT_PASSWORD.setText("");
                        TXT_CONFRM_PASS.setText("");
                   
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur d'insertion d'utilisateur", e.getMessage(),null);
                }
            }
        }
    }

    private Utilisateur addUtilisateur(Utilisateur user) {
         /* fonction qui se connecte avec l API REST du server et envois une requete de type
            post pour ajouter un utilisateur a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner l'utilisateur ajoute.
        */
             String url ="http://localhost:8080/utilisateurs";
             Client client =ClientBuilder.newClient();
             Utilisateur usr= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(user),Utilisateur.class);
             return usr;
    }

    private void setUserRule_cmbx() {
         ArrayList<String> role= new ArrayList<>();
        role.add("Simple");
        role.add("Admin");
        CMBX_ROLE_USR.getItems().clear();
        CMBX_ROLE_USR.setItems(FXCollections.observableArrayList(role));
    }
      
    
}

package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Consernant;
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
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


public class EditerUserController implements Initializable {
     @FXML
    private TextField EDT_TXT_NUM_USR;

    @FXML
    private TextField EDT_TXT_NOM_USR;

    @FXML
    private TextField EDT_TXT_PSEUDO_USR;

    @FXML
    private ComboBox<String> EDT_CMBX_ROLE_USR;

    @FXML
    private Button BTN_UPDT_USR;

    @FXML
    private Button EDT_BTN_CLOS_USR;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUserRule_cmbx();
    }
    @FXML
    void updateUsrAction(ActionEvent event) {
        if(event.getSource()==EDT_BTN_CLOS_USR){
            new CloseWindow(event);
        }else if(event.getSource()==BTN_UPDT_USR){
//On verifie si les champs ne sont pas vide
            String user_num = EDT_TXT_NUM_USR.getText();
            String user_name= EDT_TXT_NOM_USR.getText();
            String pseudo = EDT_TXT_PSEUDO_USR.getText();
            String role = EDT_CMBX_ROLE_USR.getValue();
            if("".equals(user_num)||"".equals(user_name)||"".equals(pseudo)||"".equals(role)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type utilisateur et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui modifie l'utilisateur
                
                */
                Utilisateur user =new Utilisateur(user_num, user_name, pseudo,"",role);
                try {
                    Utilisateur usr= updateUser(user);
                    if(usr!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "Utilisateur modifié avec succée",null);
                        EDT_TXT_NUM_USR.setText("");
                        EDT_TXT_NOM_USR.setText("");
                        EDT_TXT_PSEUDO_USR.setText("");
                        EDT_CMBX_ROLE_USR.setValue("");
                    }
                   else if(usr==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur de modifier cet utilisateiur.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", e.getMessage(),null);
                }
            }        }
    }    

    void setTextFiled(String userNum, String userName, String pseudo, String userRule) {
       this.EDT_TXT_NUM_USR.setText(userNum);
       this.EDT_TXT_NOM_USR.setText(userName);
       this.EDT_TXT_PSEUDO_USR.setText(pseudo);
       this.EDT_CMBX_ROLE_USR.setValue(userRule);
    }
     public Utilisateur updateUser(Utilisateur user ){
        /* fonction qui se connecte avec l API REST du server et envois une requete de type
            put pour modifier un utilisateur a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner l'utilisateur modifié.
        */
             String url ="http://localhost:8080/utilisateurs";
             Client client =ClientBuilder.newClient();
             Utilisateur usr= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(user),Utilisateur.class);
             return usr;
    }
      private void setUserRule_cmbx() {
        ArrayList<String> role= new ArrayList<>();
        role.add("Simple");
        role.add("Admin");
        EDT_CMBX_ROLE_USR.getItems().clear();
        EDT_CMBX_ROLE_USR.setItems(FXCollections.observableArrayList(role));
    }
}

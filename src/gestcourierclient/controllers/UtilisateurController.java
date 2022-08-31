
package gestcourierclient.controllers;

import gestcourierclient.ConfirmSuppression;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Utilisateur;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;


public class UtilisateurController implements Initializable {

      @FXML
    private Button BTN_NW_USR;

    @FXML
    private Button BTN_ACT_USR;

    @FXML
    private Button BTN_EDT_USR;

    @FXML
    private Button BTN_SUP_USR;

    @FXML
    private TextField TXT_RECH_USR;

    @FXML
    private Button BTN_RECH_USR;

    @FXML
    private TableView<Utilisateur> TBVW_USR;

    @FXML
    private TableColumn<Utilisateur,String> CLMN_NUM_USR;

    @FXML
    private TableColumn<Utilisateur,String> CLMN_NOM_USR;

    @FXML
    private TableColumn<Utilisateur,String> CLMN_PSEUDO_USR;

    @FXML
    private TableColumn<Utilisateur,String> CLMN_ROLE_USR;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         fetch_user_data();
         if(UtilisateurController.role.equals("Simple") ||this.role.equals("simple")){
            BTN_SUP_USR.setVisible(false);
            BTN_NW_USR.setVisible(false);
            BTN_EDT_USR.setVisible(false);
        }
    } 
    
    @FXML
    void utilisateurAction(ActionEvent event) throws IOException {
        if(event.getSource()==BTN_ACT_USR){
             fetch_user_data();
        }else if(event.getSource()==BTN_NW_USR){
            openNewUser(event);
        }else if(event.getSource()==BTN_EDT_USR){
            editUser(event);
        }else if(event.getSource()==BTN_SUP_USR){
            int index = TBVW_USR.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String num =TBVW_USR.getItems().get(index).getUserNum();
                deleteUser(num);
            }else{
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez selectionner la ligne a supprimé", null);
            }
        }else if(event.getSource()==BTN_RECH_USR){
            String motCle = TXT_RECH_USR.getText();
            if(motCle.equals("")){
                 fetch_user_data();
            }
            rech_user_data(motCle);
        }
    }   

    private void fetch_user_data() {
        try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/utilisateurs";
            Client client =ClientBuilder.newClient();
            List<Utilisateur> usrList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Utilisateur>>(){});
            
            ObservableList<Utilisateur> list=FXCollections.observableArrayList();
            list.addAll(usrList);

            CLMN_NUM_USR.setCellValueFactory(new PropertyValueFactory<>("userNum"));
            CLMN_NOM_USR.setCellValueFactory(new PropertyValueFactory<>("userName"));
            CLMN_PSEUDO_USR.setCellValueFactory(new PropertyValueFactory<>("userPseudo"));
            CLMN_ROLE_USR.setCellValueFactory(new PropertyValueFactory<>("userRule"));
            TBVW_USR.setItems(list);

        } catch (Exception e) {
            
            System.out.println("Connection du server echoue");
             WindowDialog windowDialog = new WindowDialog("erreur","Il ya eu une erreur au niveau du serveur",null);
        }
    }

    private void openNewUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/NewUserView.fxml"));
            //System.out.println(getClass());
            Stage stage = new Stage();
            stage.setTitle("Nouveau Utilisateur");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.show();
    }

    private void deleteUser(String num) {
        try {     
                    String confirm = ConfirmSuppression.suppConf();
                    if(confirm.equals("OK")){
                        //COmmunication et recuperation des donnes via l api rest du cote du server
                        String url="http://localhost:8080/utilisateurs/"+num;
                        Client client =ClientBuilder.newClient();

                        String delUsr= client
                                                .target(url)
                                                 .request(MediaType.TEXT_PLAIN)
                                                 .delete(String.class);
                        if(delUsr.equals("SUCCESS")){
                             WindowDialog windowDialog = new WindowDialog("info", "l'utilisateur a été supprime avec succee", null);
                        }else{
                             WindowDialog windowDialog = new WindowDialog("erreur", "Impossible de supprimer cet utilisateur veillez ressayer", null);
                        }
                        
                    }
                
                } catch (Exception e) {
                    //dialogError("Connection du server echoue","");
                    WindowDialog windowDialog = new WindowDialog("Une erreur est survenu au niveau du serveur",e.getMessage(), null);
                    }
    }

    private void editUser(ActionEvent event) {
       int index = TBVW_USR.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String userNum =TBVW_USR.getItems().get(index).getUserNum();
                String userName =TBVW_USR.getItems().get(index).getUserName();
                String pseudo =TBVW_USR.getItems().get(index).getUserPseudo();
                String userRule =TBVW_USR.getItems().get(index).getUserRule();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gestcourierclient/views/EditerUserView.fxml"));
                
                try {
                    loader.load();
                    } catch (IOException e) {
                    }
                
                //recuperation du controller du vue du lancer
                EditerUserController edtUser =loader.getController();
                edtUser.setTextFiled(userNum,userName,pseudo ,userRule);
                Parent parent =loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.initStyle(StageStyle.UTILITY);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
                stage.show();
            }else {
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez selectionner le champs a editer", null);
            }
    }
     private  static String role;
     public static void setRole(String role){
     UtilisateurController.role =role;
    }
    public String getRole(){
        return role;
    } 

    private void rech_user_data(String motCle) {
        try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/rechUtilisateurs/"+motCle;
            Client client =ClientBuilder.newClient();
            List<Utilisateur> usrList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Utilisateur>>(){});
            
            ObservableList<Utilisateur> list=FXCollections.observableArrayList();
            list.addAll(usrList);
            CLMN_NUM_USR.setCellValueFactory(new PropertyValueFactory<>("userNum"));
            CLMN_NOM_USR.setCellValueFactory(new PropertyValueFactory<>("userName"));
            CLMN_PSEUDO_USR.setCellValueFactory(new PropertyValueFactory<>("userPseudo"));
            CLMN_ROLE_USR.setCellValueFactory(new PropertyValueFactory<>("userRule"));
             TBVW_USR.getItems().clear();
            TBVW_USR.setItems(list);

        } catch (Exception e) {
            
           
        }
    }
}

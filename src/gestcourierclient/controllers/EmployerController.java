
package gestcourierclient.controllers;

import gestcourierclient.ConfirmSuppression;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Employer;
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


public class EmployerController implements Initializable {
    
     @FXML
    private Button BTN_NW_EMP;

    @FXML
    private Button BTN_ACT_EMP;

    @FXML
    private Button BTN_EDT_EMP;

    @FXML
    private Button BTN_SUP_EMP;
    
    @FXML
    private Button BTN_RECH_EMP;

    @FXML
    private TextField TXT_RECH_EMP;

    @FXML
    private TableView<Employer> TBVW_EMP;

    @FXML
    private TableColumn<Employer, String> CLMN_NUM_EMP;

    @FXML
    private TableColumn<Employer, String> CLMN_NOM_EMP;

    @FXML
    private TableColumn<Employer, String> CLMN_PRENOM_EMP;

    @FXML
    private TableColumn<Employer, String> CLMN_ADDR_EMP;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetch_empl_data();
         if(EmployerController.role.equals("Simple") ||EmployerController.role.equals("simple")){
            BTN_SUP_EMP.setVisible(false);
            BTN_NW_EMP.setVisible(false);
            BTN_EDT_EMP.setVisible(false);
        }
    }
    @FXML
    void emplAction(ActionEvent event) throws IOException {
        if(event.getSource()==BTN_NW_EMP){
            openNewEmployer(event);
        }else if(event.getSource()==BTN_ACT_EMP){
            fetch_empl_data();
        }else if(event.getSource()==BTN_EDT_EMP){
             editEmployer(event);
        }else if(event.getSource()==BTN_SUP_EMP){
            deleteEmpl();
        }else if(event.getSource()==BTN_RECH_EMP){
            String motCle = TXT_RECH_EMP.getText();
            if(motCle.equals("")){
                 fetch_empl_data();
            }
            rechEmployer(motCle);
        }
    }
    //LIstage des Consernants
     public void fetch_empl_data(){
         try {
             
            /* Connection a l API REST du server accesible dans cette lien
             la requeste envoye est de type get et la reponse envoye par le server
             est une liste des emplyer sous form de json.
             */
           
             String url="http://localhost:8080/employers";
            Client client =ClientBuilder.newClient();
		
            List<Employer> empList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Employer>>(){});
            
            /* 
                pour afficher la reponse recu venant du server dans le tableview
                on doit creer un object de type ObservableList
            */
            ObservableList<Employer> list=FXCollections.observableArrayList();
            list.addAll(empList);

            CLMN_NUM_EMP.setCellValueFactory(new PropertyValueFactory<>("numEmpl"));
            CLMN_NOM_EMP.setCellValueFactory(new PropertyValueFactory<>("nomEmpl"));
            CLMN_PRENOM_EMP.setCellValueFactory(new PropertyValueFactory<>("prenomEmpl"));
            CLMN_ADDR_EMP.setCellValueFactory(new PropertyValueFactory<>("adressEmpl"));
        
            TBVW_EMP.setItems(list);

        } catch (Exception e) {
            //dialogError("Connection du server echoue","");
            System.out.println("Connection du server echoue");
             WindowDialog windowDialog = new WindowDialog("erreur", "La connection au serveur a été interombu", null);
        }
    }
     //fonction qui ouvre la nouvelle fenetre d ajout d employer
     public void openNewEmployer(ActionEvent event) throws IOException{
          Parent root = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/NewEmployerView.fxml"));
            //System.out.println(getClass());
            Stage stage = new Stage();
            stage.setTitle("Nouveau employer");
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.show();
     }
     public void editEmployer(ActionEvent event){
       int index = TBVW_EMP.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String num =TBVW_EMP.getItems().get(index).getNumEmpl();
                String nom =TBVW_EMP.getItems().get(index).getNomEmpl();
                String prenom =TBVW_EMP.getItems().get(index).getPrenomEmpl();
                String addr=TBVW_EMP.getItems().get(index).getAdressEmpl();
                 
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gestcourierclient/views/EditerEmployerView.fxml"));
                
                try {
                    loader.load();
                    } catch (IOException e) {
                    }
                
                //recuperation du controller du vue du lancer
                EditerEmployerController edtEmpl=loader.getController();
                edtEmpl.setTextFiled(num, nom, prenom,addr);
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
     public void deleteEmpl(){
          int index = TBVW_EMP.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String num =TBVW_EMP.getItems().get(index).getNumEmpl();
                try {
             
                    String confirm = ConfirmSuppression.suppConf();
                    if(confirm.equals("OK")){
                        //COmmunication et recuperation des donnes via l api rest du cote du server
                        String url="http://localhost:8080/employers/"+num;
                        Client client =ClientBuilder.newClient();

                        String delResp= client
                                              .target(url)
                                              .request(MediaType.TEXT_PLAIN)
                                              .delete(String.class);
                        if(delResp.equals("SUCCESS")){
                             WindowDialog windowDialog = new WindowDialog("info", "l'employer a été supprime avec succee", null);
                        }else{
                             WindowDialog windowDialog = new WindowDialog("erreur", "Impossible de supprimer cet employer!veillez ressayer", null);
                        }
                        
                    }
                

                } catch (Exception e) {
                    //dialogError("Connection du server echoue","");
                    WindowDialog windowDialog = new WindowDialog("erreur","Une erreur est survenu au niveau du server"+e.getMessage(), null);
                    }
            }else{
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez selectionner la ligne a supprimé", null);
            }
         }
    private  static String role;
    public static void setRole(String role){
        EmployerController.role =role;
    }
    public String getRole(){
        return role;
    }

    private void rechEmployer(String motCle) {
       try {
             
            /* Connection a l API REST du server accesible dans cette lien
             la requeste envoye est de type get et la reponse envoye par le server
             est une liste des emplyer sous form de json.
             */
           
             String url="http://localhost:8080/employers";
            Client client =ClientBuilder.newClient();
		
            List<Employer> empList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Employer>>(){});
            
            /* 
                pour afficher la reponse recu venant du server dans le tableview
                on doit creer un object de type ObservableList
            */
            ObservableList<Employer> list=FXCollections.observableArrayList();
            list.addAll(empList);

            CLMN_NUM_EMP.setCellValueFactory(new PropertyValueFactory<>("numEmpl"));
            CLMN_NOM_EMP.setCellValueFactory(new PropertyValueFactory<>("nomEmpl"));
            CLMN_PRENOM_EMP.setCellValueFactory(new PropertyValueFactory<>("prenomEmpl"));
            CLMN_ADDR_EMP.setCellValueFactory(new PropertyValueFactory<>("adressEmpl"));
        
            TBVW_EMP.setItems(list);

        } catch (Exception e) {
            //dialogError("Connection du server echoue","");
            System.out.println("Connection du server echoue");
             WindowDialog windowDialog = new WindowDialog("erreur", "ereur au niveau du server:"+e.getMessage(), null);
        }
    }
}

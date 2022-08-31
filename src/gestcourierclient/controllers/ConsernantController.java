
package gestcourierclient.controllers;

import gestcourierclient.ConfirmSuppression;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Consernant;
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


public class ConsernantController implements Initializable {
    @FXML
    private Button BTN_NW_CONS;

    @FXML
    private Button BTN_ACT_CONS;

    @FXML
    private Button BTN_EDIT_CONS;

    @FXML
    private Button BTN_SUPP_CONS;
    @FXML
    private Button BTN_RECH_CONS;
    @FXML
    private TextField TXT_RECH_CONS;
    
    @FXML
    private TableView<Consernant> TBLVWW_CONS;
    @FXML
    private TableColumn<Consernant, String> CLMN_CODE_CONS;

    @FXML
    private TableColumn<Consernant, String> CLMN_NOM_CONS;

    @FXML
    private TableColumn<Consernant, String> CLMN_CODE_PRENOM;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetch_consern_data();
        if(ConsernantController.role.equals("Simple") ||ConsernantController.role.equals("simple")){
            BTN_SUPP_CONS.setVisible(false);
            BTN_NW_CONS.setVisible(false);
            BTN_EDIT_CONS.setVisible(false);
        }
    }    
    //LIstage des Consernants
     public void fetch_consern_data(){
         try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/consernants";
            Client client =ClientBuilder.newClient();
		
            List<Consernant> consList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Consernant>>(){});
            
            ObservableList<Consernant> list=FXCollections.observableArrayList();
            list.addAll(consList);

            CLMN_CODE_CONS.setCellValueFactory(new PropertyValueFactory<>("codeCons"));
            CLMN_NOM_CONS.setCellValueFactory(new PropertyValueFactory<>("nomCons"));
            CLMN_CODE_PRENOM.setCellValueFactory(new PropertyValueFactory<>("prenomCons"));
        
            TBLVWW_CONS.setItems(list);
            System.out.println("Listage des données");

        } catch (Exception e) {
            
            System.out.println("Connection du server echoue");
             WindowDialog windowDialog = new WindowDialog("erreur","Une erreur est suervenu au niveau du serveur",null);
        }
    }
     public void consAction(ActionEvent event) throws IOException, Exception{
         if(event.getSource()==BTN_ACT_CONS){
             
            fetch_consern_data();
            
         }else if(event.getSource()==BTN_NW_CONS){
             
            openNewConsernant(event);
            
         }else if(event.getSource()==BTN_RECH_CONS){
             String motCle = TXT_RECH_CONS.getText();
             if(motCle.equals("")){
                fetch_consern_data();
             }
             rech_consernant(motCle);
         }
         else if(event.getSource()==BTN_EDIT_CONS){
             
             editConsernant(event);
           
         }
         else if(event.getSource()==BTN_SUPP_CONS){
             int index = TBLVWW_CONS.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String code =TBLVWW_CONS.getItems().get(index).getCodeCons();
                try {
             
                    String confirm = ConfirmSuppression.suppConf();
                    if(confirm.equals("OK")){
                        //COmmunication et recuperation des donnes via l api rest du cote du server
                        String url="http://localhost:8080/consernants/"+code;
                        Client client =ClientBuilder.newClient();

                        String delResp= client
                                                .target(url)
                                                 .request(MediaType.TEXT_PLAIN)
                                                 .delete(String.class);
                        if(delResp.equals("SUCCESS")){
                             WindowDialog windowDialog = new WindowDialog("info", "le consrnant a été supprime avec succee", null);
                        }else{
                             WindowDialog windowDialog = new WindowDialog("erreur", "Impossible de supprimer ce consernant veillez ressayer", null);
                        }
                        
                    }
                

                } catch (Exception e) {
                    //dialogError("Connection du server echoue","");
                    WindowDialog windowDialog = new WindowDialog("erreur",e.getMessage(), null);
                    }
            }else{
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez selectionner la ligne a supprimé", null);
            }
         }
     }
     public void openNewConsernant(ActionEvent event) throws IOException{
          Parent root = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/NewConsView.fxml"));
            //System.out.println(getClass());
            Stage stage = new Stage();
            stage.setTitle("Nouveau consernant");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.show();
     }
     public void editConsernant(ActionEvent event){
       int index = TBLVWW_CONS.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String code =TBLVWW_CONS.getItems().get(index).getCodeCons();
                String nom =TBLVWW_CONS.getItems().get(index).getNomCons();
                String prenom =TBLVWW_CONS.getItems().get(index).getPrenomCons();
                 
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gestcourierclient/views/EditerConsernantView.fxml"));
                
                try {
                    loader.load();
                    } catch (IOException e) {
                    }
                
                //recuperation du controller du vue du lancer
                EditerConsernantController edtCons =loader.getController();
                edtCons.setTextFiled(code, nom, prenom);
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
       ConsernantController.role =role;
    }
    public String getRole(){
        return role;
    } 

    
    private void rech_consernant(String motCle)throws Exception {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/rechConsernants/"+motCle;
            Client client =ClientBuilder.newClient();
		
            List<Consernant> consList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Consernant>>(){});
            
            ObservableList<Consernant> list=FXCollections.observableArrayList();
            list.addAll(consList);
            CLMN_CODE_CONS.setCellValueFactory(new PropertyValueFactory<>("codeCons"));
            CLMN_NOM_CONS.setCellValueFactory(new PropertyValueFactory<>("nomCons"));
            CLMN_CODE_PRENOM.setCellValueFactory(new PropertyValueFactory<>("prenomCons"));
        
            TBLVWW_CONS.getItems().clear();
            TBLVWW_CONS.setItems(list);

    }
}

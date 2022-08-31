
package gestcourierclient.controllers;

import gestcourierclient.ConfirmSuppression;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Direction;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
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

public class DirectionController implements Initializable {

     @FXML
    private Button BTN_NW_DIR;

    @FXML
    private Button BTN_ACT_DIR;

    @FXML
    private Button BTN_EDT_DIR;

    @FXML
    private Button BTN_SUP_DIR;
    
    @FXML
    private Button BTN_RECH_DIR;

    @FXML
    private TextField TXT_RECH_DIR;

    @FXML
    private TableView<Direction> TBVW_DIR;

    @FXML
    private TableColumn<Direction, String> CLMN_CODE_DIR;
    

    @FXML
    private TableColumn<Direction, String> CLMN_DESCR_DIR;

    @FXML
    private TableColumn<Direction, String> CLMN_PROV_DIR;
    
     @FXML
    private TableColumn<Direction, String> CLMN_ABREV_DIR;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetch_direction_data();
         if(this.role.equals("Simple") ||this.role.equals("simple")){
            BTN_NW_DIR.setVisible(false);
            BTN_SUP_DIR.setVisible(false);
            BTN_EDT_DIR.setVisible(false);
        }
    }    
     @FXML
    void directServAction(ActionEvent event) throws IOException {
        if(event.getSource()==BTN_ACT_DIR){
            fetch_direction_data();
        }else if(event.getSource()==BTN_EDT_DIR){
             editDirection(event);
        }else if(event.getSource()==BTN_RECH_DIR){
            String motCle = TXT_RECH_DIR.getText();
            if(motCle.equals("")){
                 fetch_direction_data();
            }
            rechDirection(motCle);
        }
        else if(event.getSource()==BTN_NW_DIR){
            Parent root = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/NewDirectionView.fxml"));
             //System.out.println(getClass());
            Stage stage = new Stage();
            stage.setTitle("Nouveau direction");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.show();
         }else if(event.getSource()==BTN_SUP_DIR){
              int index = TBVW_DIR.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String codeDir =TBVW_DIR.getItems().get(index).getCodeDirect();
                try {
             
                    String confirm = ConfirmSuppression.suppConf();
                    if(confirm.equals("OK")){
                        //COmmunication et recuperation des donnes via l api rest du cote du server
                        String url="http://localhost:8080/directions/"+codeDir;
                        Client client =ClientBuilder.newClient();

                        String delResp= client
                                                .target(url)
                                                 .request(MediaType.TEXT_PLAIN)
                                                 .delete(String.class);
                        if(delResp.equals("SUCCESS")){
                             WindowDialog windowDialog = new WindowDialog("info", "la direction a été supprime avec succee", null);
                             fetch_direction_data();
                        }else{
                             WindowDialog windowDialog = new WindowDialog("erreur", "Impossible de supprimer cette direction veillez ressayer", null);
                        }
                        
                    }
                

                } catch (Exception e) {
                    //dialogError("Connection du server echoue","");
                    WindowDialog windowDialog = new WindowDialog("erreur","La connection au serveur a été interompu", null);
                    }
            }else{
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez selectionner la ligne a supprimé", null);
            }
         }
    }
    //Listage des directions ou service
     public void fetch_direction_data(){
         try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/directions";
            Client client =ClientBuilder.newClient();
		
            List<Direction> directList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Direction>>(){});
            
            ObservableList<Direction> list=FXCollections.observableArrayList();
            list.addAll(directList);
           
            CLMN_CODE_DIR.setCellValueFactory(new PropertyValueFactory<>("codeDirect"));
            CLMN_ABREV_DIR.setCellValueFactory(new PropertyValueFactory<>("abrevDirect"));
            CLMN_DESCR_DIR.setCellValueFactory(new PropertyValueFactory<>("descrptDirect"));
            CLMN_PROV_DIR.setCellValueFactory((CellDataFeatures<Direction, String> param) -> new SimpleStringProperty(param.getValue().getProvenance().getLibelProv()));
            TBVW_DIR.setItems(list);
        } catch (Exception e) {
             WindowDialog windowDialog = new WindowDialog("erreur","La connection au serveur a été interompu",null);
        }
    }

    private void editDirection(ActionEvent event) {
         int index = TBVW_DIR.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String codeDir =TBVW_DIR.getItems().get(index).getCodeDirect();
                String abrevDir =TBVW_DIR.getItems().get(index).getAbrevDirect();
                String codePro=TBVW_DIR.getItems().get(index).getProvenance().getCodeProv();
                String desc=TBVW_DIR.getItems().get(index).getDescrptDirect();
                 
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gestcourierclient/views/EditDirectionView.fxml"));
                
                try {
                    loader.load();
                    } catch (IOException e) {
                    }
                
                //recuperation du controller du vue du lancer
                EditDirectionController edtDir=loader.getController();
               edtDir.setTextFiled(codePro,codeDir,abrevDir,desc);
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
     DirectionController.role =role;
    }
    public String getRole(){
        return role;
    } 

    private void rechDirection(String motCle) {
       try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/rechDirections/"+motCle;
            Client client =ClientBuilder.newClient();
		
            List<Direction> directList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Direction>>(){});
            
            ObservableList<Direction> list=FXCollections.observableArrayList();
            list.addAll(directList);
           
            CLMN_CODE_DIR.setCellValueFactory(new PropertyValueFactory<>("codeDirect"));
            CLMN_DESCR_DIR.setCellValueFactory(new PropertyValueFactory<>("descrptDirect"));
            CLMN_PROV_DIR.setCellValueFactory((CellDataFeatures<Direction, String> param) -> new SimpleStringProperty(param.getValue().getProvenance().getLibelProv()));
            TBVW_DIR.getItems().clear();
            TBVW_DIR.setItems(list);
        } catch (Exception e) {
             WindowDialog windowDialog = new WindowDialog("erreur","Une erreur est suervenu:",null);
        }
    }
}

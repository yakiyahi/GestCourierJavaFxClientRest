package gestcourierclient.controllers;

import gestcourierclient.ConfirmSuppression;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Decision;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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

public class DecisionController implements Initializable {
     @FXML
    private Button BTN_NW_DECIS;

    @FXML
    private Button BTN_ACT_DECIS;

    @FXML
    private Button BTN_EDT_DECIS;

    @FXML
    private Button BTN_SUP_DECIS;
    
    @FXML
    private Button BTN_RECH_DECIS;

    @FXML
    private TextField TXT_RECH_DECIS;

    @FXML
    private TableView<Decision> TBVW_DECIS;

    @FXML
    private TableColumn<Decision, Long> CLMN_ID_DECIS;

    @FXML
    private TableColumn<Decision, String> CLMN_TYPE_DECIS;

    @FXML
    private TableColumn<Decision, Date> CLMN_DATE_DECIS;

    @FXML
    private TableColumn<Decision, String> CLMN_DESCR_DECIS;
    
    @FXML
    private TableColumn<Decision, String> CLMN_REF_COUR;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       fetch_decis_data();
        if(DecisionController.role.equals("Simple") ||DecisionController.role.equals("simple")){
            BTN_EDT_DECIS.setVisible(false);
            BTN_SUP_DECIS.setVisible(false);
            BTN_NW_DECIS.setVisible(false);
        }
    } 
    @FXML
    public void decisionAction(ActionEvent event ) throws IOException{
        if(event.getSource()==BTN_NW_DECIS){
            openNewConsernant(event);
        }else if(event.getSource()==BTN_ACT_DECIS){
            fetch_decis_data();
        }else if(event.getSource()==BTN_EDT_DECIS){
            editerDecision(event);
        }else if(event.getSource()==BTN_RECH_DECIS){
             String id = TXT_RECH_DECIS.getText();
            if(id.equals("")){
                 fetch_decis_data();
            }
            rechDecision(id);
        }
        else if(event.getSource()==BTN_SUP_DECIS){
             int index = TBVW_DECIS.getSelectionModel().getSelectedIndex();
            if(index>=0){
                Long id =TBVW_DECIS.getItems().get(index).getIdDecis();
                try {
             
                    String confirm = ConfirmSuppression.suppConf();
                    if(confirm.equals("OK")){
                        //COmmunication et recuperation des donnes via l api rest du cote du server
                        String url="http://localhost:8080/decisions/"+id;
                        Client client =ClientBuilder.newClient();

                        String delResp= client
                                                .target(url)
                                                 .request(MediaType.TEXT_PLAIN)
                                                 .delete(String.class);
                        if(delResp.equals("SUCCESS")){
                             WindowDialog windowDialog = new WindowDialog("info", "la decision a été supprimée avec succee", null);
                             fetch_decis_data();
                        }else{
                             WindowDialog windowDialog = new WindowDialog("erreur", "Impossible de supprimer cette decision.veillez ressayer", null);
                        }
                        
                    }
                

                } catch (Exception e) {
                    //dialogError("Connection du server echoue","");
                    WindowDialog windowDialog = new WindowDialog("erreur","Une erreur est survenu au niveau du serveur", null);
                    }
            }else{
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez selectionner la ligne a supprimé", null);
            }
        }
    }
    public void fetch_decis_data(){
         try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/decisions";
             Client client =ClientBuilder.newClient();
		
             List<Decision> decisList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Decision>>(){});
            
            ObservableList<Decision> list=FXCollections.observableArrayList();
            list.addAll(decisList);

            CLMN_ID_DECIS.setCellValueFactory(new PropertyValueFactory<>("idDecis"));
            CLMN_TYPE_DECIS.setCellValueFactory(new PropertyValueFactory<>("typeDecis"));
            CLMN_DESCR_DECIS.setCellValueFactory(new PropertyValueFactory<>("descrDcis"));
            CLMN_DATE_DECIS.setCellValueFactory(new PropertyValueFactory<>("dateDecis"));
            CLMN_REF_COUR.setCellValueFactory((CellDataFeatures<Decision, String> param) -> new SimpleStringProperty(param.getValue().getCourier().getRefCour()));
        
            TBVW_DECIS.setItems(list);

        } catch (Exception e) {
            
            System.out.println("Connection du server echoue");
             WindowDialog windowDialog = new WindowDialog("erreur","Une erreur est suervenu:"+e.getMessage(),null);
        }
    }

    private void openNewConsernant(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/NewDecisionView.fxml"));
            //System.out.println(getClass());
            Stage stage = new Stage();
            stage.setTitle("Nouveau Decision");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.show();
    }
    
    private void editerDecision(ActionEvent event) {
        
     int index = TBVW_DECIS.getSelectionModel().getSelectedIndex();
            if(index>=0){
                Long id =TBVW_DECIS.getItems().get(index).getIdDecis();
                String refCour =TBVW_DECIS.getItems().get(index).getCourier().getRefCour();
                String descr =TBVW_DECIS.getItems().get(index).getDescrDcis();
                Date date = TBVW_DECIS.getItems().get(index).getDateDecis();
                String type = TBVW_DECIS.getItems().get(index).getTypeDecis();
                 
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gestcourierclient/views/EditDecisionView.fxml"));
                
                try {
                    loader.load();
                    } catch (IOException e) {
                    }
                
                //recuperation du controller du vue du lancer
                EditDecisionController edtDecis =loader.getController();
                edtDecis.setTextFiled(id,refCour,descr,date,type);
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
     DecisionController.role =role;
    }
    public String getRole(){
        return role;
    } 

    private void rechDecision(String id) {
         try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/rechDecisions/"+Long.parseLong(id);
             Client client =ClientBuilder.newClient();
		
             List<Decision> decisList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Decision>>(){});
            
            ObservableList<Decision> list=FXCollections.observableArrayList();
            list.addAll(decisList);

            CLMN_ID_DECIS.setCellValueFactory(new PropertyValueFactory<>("idDecis"));
            CLMN_TYPE_DECIS.setCellValueFactory(new PropertyValueFactory<>("typeDecis"));
            CLMN_DESCR_DECIS.setCellValueFactory(new PropertyValueFactory<>("descrDcis"));
            CLMN_DATE_DECIS.setCellValueFactory(new PropertyValueFactory<>("dateDecis"));
            CLMN_REF_COUR.setCellValueFactory((CellDataFeatures<Decision, String> param) -> new SimpleStringProperty(param.getValue().getCourier().getRefCour()));
            TBVW_DECIS.getItems().clear();
            TBVW_DECIS.setItems(list);

        } catch (Exception e) {
             WindowDialog windowDialog = new WindowDialog("erreur","Une erreur est suervenu:",null);
        }
    }
}

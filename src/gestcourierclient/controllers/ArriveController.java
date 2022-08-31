
package gestcourierclient.controllers;

import gestcourierclient.ConfirmSuppression;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Arrive;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;


public class ArriveController implements Initializable {
    @FXML
    private Button BTN_NW_ARRVE;

    @FXML
    private Button BTN_ACT_ARRVE;

    @FXML
    private Button BTN_EDT_ARRVE;

    @FXML
    private Button BTN_SUP_ARRVE;
    
    @FXML
    private Button BTN_RECH_ARRVE;

    @FXML
    private TextField TXT_RECH_ARRVE;

    @FXML
    private TableView<Arrive> TBLVW_ARRV;

    @FXML
    private TableColumn<Arrive, Long> CLMN_ID_ARRV;

    @FXML
    private TableColumn<Arrive,Date> CLMN_DTE_ARRVE;

    @FXML
    private TableColumn<Arrive,Time> CLMN_HEUR_ARRVE;
    
    @FXML
    private Button BTN_RECH_BY_DATE_ARRVE;
    
    private  static String role;

    @FXML
    void arriveAction(ActionEvent event) throws IOException {
        if(event.getSource()==BTN_ACT_ARRVE){
            fetch_arrive_data();
        }else if(event.getSource()==BTN_NW_ARRVE){
            openNewArrive(event);
        }else if(event.getSource()==BTN_EDT_ARRVE){
             editArrivee(event);
        }else if(event.getSource()==BTN_RECH_ARRVE){
             String motCle = TXT_RECH_ARRVE.getText();
            if(motCle.equals("")){
                 fetch_arrive_data();
            }
            rechArriver(Long.parseLong(motCle));
        }else if(event.getSource()==BTN_RECH_BY_DATE_ARRVE){
             Long idArv =Long.valueOf(TXT_RECH_ARRVE.getText());
            if(idArv==null){
                 fetch_arrive_data();
            }
            rechArriver(idArv);
        }
        else if(event.getSource()==BTN_SUP_ARRVE){
             int index = TBLVW_ARRV.getSelectionModel().getSelectedIndex();
            if(index>=0){
                Long id =TBLVW_ARRV.getItems().get(index).getIdArive();
                try {
             
                    String confirm = ConfirmSuppression.suppConf();
                    if(confirm.equals("OK")){
                        //COmmunication et recuperation des donnes via l api rest du cote du server
                        String url="http://localhost:8080/arrivées/"+id;
                        Client client =ClientBuilder.newClient();

                        String delResp= client
                                                .target(url)
                                                 .request(MediaType.TEXT_PLAIN)
                                                 .delete(String.class);
                        if(delResp.equals("SUCCESS")){
                             WindowDialog windowDialog = new WindowDialog("info", "l'arrivée a été supprime avec succee", null);
                             fetch_arrive_data();
                        }else{
                             WindowDialog windowDialog = new WindowDialog("erreur", "Impossible de supprimer cet arrivée veillez ressayer", null);
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       fetch_arrive_data();
       if(ArriveController.role.equals("Simple") ||ArriveController.role.equals("simple")){
            BTN_SUP_ARRVE.setVisible(false);
            BTN_NW_ARRVE.setVisible(false);
            BTN_EDT_ARRVE.setVisible(false);
        }
    }    
    //LIstage des arrivées
     public void fetch_arrive_data(){
          
         try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/arrivées";
            Client client =ClientBuilder.newClient();
		
            List<Arrive> arriveList= client
				   .target(url)
				   .request(MediaType.APPLICATION_JSON)
                                    .get(new GenericType<List<Arrive>>(){});
            
            ObservableList<Arrive> list=FXCollections.observableArrayList();
            list.addAll(arriveList);

            CLMN_ID_ARRV.setCellValueFactory(new PropertyValueFactory<>("idArive"));
            CLMN_DTE_ARRVE.setCellValueFactory(new PropertyValueFactory<>("dateArive"));
            CLMN_HEUR_ARRVE.setCellValueFactory(new PropertyValueFactory<>("heurArive"));
            /*CLMN_HEUR_ARRVE.setCellValueFactory((TableColumn.CellDataFeatures<Arrive,Date> param) -> {
                DateFormat df = new SimpleDateFormat("hh:mm");
                LocalDate ldt= LocalDate.parse(param.getValue().getHeurArive().toString());
                return new SimpleObjectProperty<>(java.sql.Date.valueOf(ldt));
             }); */
                 
             
            TBLVW_ARRV.setItems(list);
            
        } catch (Exception e) {
             WindowDialog windowDialog = new WindowDialog("erreur","Une erreur est survenu", null);
            //e.printStackTrace();
        }
    }
     private void openNewArrive(ActionEvent event) throws IOException {
       Parent root = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/NewArriveView.fxml"));
            //System.out.println(getClass());
            Stage stage = new Stage();
            stage.setTitle("Nouveau Arrive");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.show();
    }

    private void editArrivee(ActionEvent event) {
       int index = TBLVW_ARRV.getSelectionModel().getSelectedIndex();
            if(index>=0){
                Long id =TBLVW_ARRV.getItems().get(index).getIdArive();
                Date date =TBLVW_ARRV.getItems().get(index).getDateArive();
                Time heur =TBLVW_ARRV.getItems().get(index).getHeurArive();
                 
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gestcourierclient/views/EditArriveView.fxml"));
                
                try {
                    loader.load();
                    } catch (IOException e) {
                    }
                
                //recuperation du controller du vue du lancer
                EditArriveController editArrv =loader.getController();
                editArrv.setTextFiled(id, date, heur);
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
   public static void setRole(String role){
        ArriveController.role =role;
    }
    public String getRole(){
        return role;
    }  

    private void rechArriver(Long id) {
          //COmmunication et recuperation des donnes via l api rest du cote du server
             String url="http://localhost:8080/rechArrivées/"+id;
            Client client =ClientBuilder.newClient();
		
            List<Arrive> arvList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Arrive>>(){});
            
            ObservableList<Arrive> list=FXCollections.observableArrayList();
            list.addAll(arvList);
            CLMN_ID_ARRV.setCellValueFactory(new PropertyValueFactory<>("idArive"));
            CLMN_DTE_ARRVE.setCellValueFactory(new PropertyValueFactory<>("dateArive"));
            CLMN_HEUR_ARRVE.setCellValueFactory(new PropertyValueFactory<>("heurArive"));
        
            TBLVW_ARRV.getItems().clear();
            TBLVW_ARRV.setItems(list);
    }

    private void rechArriverByDate(LocalDate date) {
       //COmmunication et recuperation des donnes via l api rest du cote du server
       Date dat = Date.valueOf(date);
             String url="http://localhost:8080/rechArrivéesDate/"+dat;
            Client client =ClientBuilder.newClient();
		
            List<Arrive> arvList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Arrive>>(){});
            
            ObservableList<Arrive> list=FXCollections.observableArrayList();
            list.addAll(arvList);
            CLMN_ID_ARRV.setCellValueFactory(new PropertyValueFactory<>("idArive"));
            CLMN_DTE_ARRVE.setCellValueFactory(new PropertyValueFactory<>("dateArive"));
            CLMN_HEUR_ARRVE.setCellValueFactory(new PropertyValueFactory<>("heurArive"));
        
            TBLVW_ARRV.getItems().clear();
            TBLVW_ARRV.setItems(list);
    }
}


package gestcourierclient.controllers;

import gestcourierclient.ConfirmSuppression;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Consernant;
import gestcourierclient.entities.Courier;
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


public class CourierController implements Initializable {
      @FXML
    private Button BTN_NW_COUR;

    @FXML
    private Button BTN_ACT_COUR;

    @FXML
    private Button BTN_EDT_COUR;

    @FXML
    private Button BTN_SUP_COUR;

    @FXML
    private TextField TXT_RECH_COUR;
    
    @FXML
    private Button BTN_RECH_COUR;

    @FXML
    private TableView<Courier> TBVW_COUR;

    @FXML
    private TableColumn<Courier, String> CLMN_REF_COUR;

    @FXML
    private TableColumn<Courier, String> CLMN_OBJECT_COUR;

    @FXML 
    private TableColumn<Courier, String> CLMN_TYPE_COUR;

    @FXML
    private TableColumn<Courier, String> CLMN_NOM_CONS_COUR;

    @FXML
    private TableColumn<Courier,String> CLMN_USR_COUR;

    @FXML
    private TableColumn<Courier, String> CLMN_DIR_MIN_COUR;
    
    @FXML
    private TableColumn<Courier, String> CLMN_HR_ARRV_COUR;
    
    @FXML
    private TableColumn<Courier,String> CLMN_DT_ARRV_COUR;

    @FXML
    void courierAction(ActionEvent event) throws IOException {
        if(event.getSource()==BTN_NW_COUR){
             Parent root = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/NewCourierView.fxml"));
             //System.out.println(getClass());
            Stage stage = new Stage();
            stage.setTitle("Nouveau courier");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.show();
        }else if(event.getSource()==BTN_ACT_COUR){
             fetch_courier_data();
        }else if(event.getSource()==BTN_RECH_COUR){
             String motCle = TXT_RECH_COUR.getText();
            if(motCle.equals("")){
                 fetch_courier_data();
            }
            rechCourrier(motCle);
        }
        else if(event.getSource()==BTN_SUP_COUR){
              int index = TBVW_COUR.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String code =TBVW_COUR.getItems().get(index).getRefCour();
                try {
             
                    String confirm = ConfirmSuppression.suppConf();
                    if(confirm.equals("OK")){
                        //COmmunication et recuperation des donnes via l api rest du cote du server
                        String url="http://localhost:8080/couriers/"+code;
                        Client client =ClientBuilder.newClient();

                        String delResp= client
                                                .target(url)
                                                 .request(MediaType.TEXT_PLAIN)
                                                 .delete(String.class);
                        
                        if(delResp.equals("SUCCESS")){
                             WindowDialog windowDialog = new WindowDialog("info", "le courier a été supprime avec succee", null);
                        }else{
                             WindowDialog windowDialog = new WindowDialog("erreur", "Impossible de supprimer ce courier veillez ressayer", null);
                        }
                        
                    }
                

                } catch (Exception e) {
                    //dialogError("Connection du server echoue","");
                    WindowDialog windowDialog = new WindowDialog("erreur",e.getMessage(), null);
                    }
            }else{
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez selectionner la ligne a supprimé", null);
            }
        }else if(event.getSource()==BTN_EDT_COUR){
                 editCourier(event);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetch_courier_data();
         if(this.role.equals("Simple") ||this.role.equals("simple")){
            BTN_SUP_COUR.setVisible(false);
            BTN_NW_COUR.setVisible(false);
            BTN_EDT_COUR.setVisible(false);
        }
    }    
    
     public void fetch_courier_data(){
         try {
             
            /* Connection a l API REST du server accesible dans cette lien
             la requeste envoye est de type get et la reponse envoye par le server
             est une liste des emplyer sous form de json.
             */
           
             String url="http://localhost:8080/couriers";
            Client client =ClientBuilder.newClient();
		
            List<Courier> courList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Courier>>(){});
            
            /* 
                pour afficher la reponse recu venant du server dans le tableview
                on doit creer un object de type ObservableList
            */

            ObservableList<Courier> list=FXCollections.observableArrayList();
            list.addAll(courList);

            CLMN_REF_COUR.setCellValueFactory(new PropertyValueFactory<>("refCour"));
            CLMN_OBJECT_COUR.setCellValueFactory(new PropertyValueFactory<>("descrCour"));
            CLMN_TYPE_COUR.setCellValueFactory(new PropertyValueFactory<>("typeCour"));
            CLMN_NOM_CONS_COUR.setCellValueFactory((CellDataFeatures<Courier, String> param) -> {
                if(param.getValue().getConsernant()!=null){
                    return new SimpleStringProperty(param.getValue().getConsernant().getNomCons());
                }
                return null;
             });
            CLMN_DIR_MIN_COUR.setCellValueFactory((CellDataFeatures<Courier, String> param) -> new SimpleStringProperty(param.getValue().getDirection().getDescrptDirect()));
            CLMN_DT_ARRV_COUR.setCellValueFactory((CellDataFeatures<Courier,String> param) -> new SimpleStringProperty(param.getValue().getArrive().getDateArive().toString()));
            CLMN_HR_ARRV_COUR.setCellValueFactory((CellDataFeatures<Courier,String> param) -> new SimpleStringProperty(param.getValue().getArrive().getHeurArive().toString()));
            CLMN_USR_COUR.setCellValueFactory((CellDataFeatures<Courier, String> param) -> new SimpleStringProperty(param.getValue().getUtilisateur().getUserName()));
            TBVW_COUR.setItems(list);

        } catch (Exception e) {
            //dialogError("Connection du server echoue","");
            System.out.println("Connection du server echoue");
             WindowDialog windowDialog = new WindowDialog("erreur", "ereur au niveau du server. Veillez ressayer !", null);
        }
    }

    private void editCourier(ActionEvent event) {
        int index = TBVW_COUR.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String refCour =TBVW_COUR.getItems().get(index).getRefCour();
                String type =TBVW_COUR.getItems().get(index).getTypeCour();
                String descr =TBVW_COUR.getItems().get(index).getDescrCour();
                String idArv =TBVW_COUR.getItems().get(index).getArrive().getIdArive().toString();
                String codeDir =TBVW_COUR.getItems().get(index).getDirection().getCodeDirect();
                String userNum =TBVW_COUR.getItems().get(index).getUtilisateur().getUserNum();
               try{
                    Consernant cons =TBVW_COUR.getItems().get(index).getConsernant();

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/gestcourierclient/views/EditerCourierView.fxml"));
                    loader.load();
                     //recuperation du controller du vue du lancer
                    EditerCourierController edtCour =loader.getController();
                    if(cons!=null){
                       edtCour.setTextFiled(refCour, type, descr,idArv,codeDir,userNum,cons.getCodeCons());
                    }else{
                        edtCour.setTextFiledNoCons(refCour, type, descr,idArv,codeDir,userNum);
                    }

                    Parent parent =loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.initStyle(StageStyle.UTILITY);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(
                        ((Node)event.getSource()).getScene().getWindow());
                    stage.show();
               }catch(Exception e){
                   new WindowDialog("warning", e.getMessage(), null);
               }
          
            }else {
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez selectionner le champs a editer", null);
            }
     }
     private  static String role;
     public static void setRole(String role){
     CourierController.role =role;
    }
    public String getRole(){
        return role;
    } 

    private void rechCourrier(String motCle) {
       String url="http://localhost:8080/rechCourier/"+motCle;
            Client client =ClientBuilder.newClient();
		
            List<Courier> courList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Courier>>(){});
            
            /* 
                pour afficher la reponse recu venant du server dans le tableview
                on doit creer un object de type ObservableList
            */

            ObservableList<Courier> list=FXCollections.observableArrayList();
            list.addAll(courList);

            CLMN_REF_COUR.setCellValueFactory(new PropertyValueFactory<>("refCour"));
            CLMN_OBJECT_COUR.setCellValueFactory(new PropertyValueFactory<>("descrCour"));
            CLMN_TYPE_COUR.setCellValueFactory(new PropertyValueFactory<>("typeCour"));
            CLMN_NOM_CONS_COUR.setCellValueFactory((CellDataFeatures<Courier, String> param) -> {
                if(param.getValue().getConsernant()!=null){
                    return new SimpleStringProperty(param.getValue().getConsernant().getNomCons());
                }
                return null;
             });
            CLMN_DIR_MIN_COUR.setCellValueFactory((CellDataFeatures<Courier, String> param) -> new SimpleStringProperty(param.getValue().getDirection().getDescrptDirect()));
            CLMN_DT_ARRV_COUR.setCellValueFactory((CellDataFeatures<Courier,String> param) -> new SimpleStringProperty(param.getValue().getArrive().getDateArive().toString()));
            CLMN_HR_ARRV_COUR.setCellValueFactory((CellDataFeatures<Courier,String> param) -> new SimpleStringProperty(param.getValue().getArrive().getHeurArive().toString()));
            CLMN_USR_COUR.setCellValueFactory((CellDataFeatures<Courier, String> param) -> new SimpleStringProperty(param.getValue().getUtilisateur().getUserName()));
            TBVW_COUR.getItems().clear();
            TBVW_COUR.setItems(list);
    }
}

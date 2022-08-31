
package gestcourierclient.controllers;
import gestcourierclient.ConfirmSuppression;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Provenance;
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



public class ProvenantController implements Initializable {
    @FXML
    private Button BTN_NW_PROV;

    @FXML
    private Button BTN_ACT_PROV;

    @FXML
    private Button BTN_EDT_PROV;

    @FXML
    private Button BTN_SUP_PROV;
    
    @FXML
    private Button BTN_RECH_PROV;
   
    @FXML
    private TextField TXT_RECH_PROV;

    @FXML
    private TableView<Provenance> TBVW_PROV;

    @FXML
    private TableColumn<Provenance, Long> CLMN_CODE_PROV;

    @FXML
    private TableColumn<Provenance, String> CLMN_ABRV_PROV;
    @FXML
    private TableColumn<Provenance, String> CLMN_LIBL_PROV;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       fetch_provenant_data();
       if(this.role.equals("Simple") ||this.role.equals("simple")){
            BTN_SUP_PROV.setVisible(false);
            BTN_NW_PROV.setVisible(false);
            BTN_EDT_PROV.setVisible(false);
        }
    }    
    @FXML
    void provenantAction(ActionEvent event) throws IOException {
        if(event.getSource()==BTN_ACT_PROV){
            fetch_provenant_data();
        }else if(event.getSource()==BTN_NW_PROV){
            openNewProvenant(event);
        }else if(event.getSource()==BTN_RECH_PROV){
            String motCle = TXT_RECH_PROV.getText();
            if(motCle.equals("")){
                 fetch_provenant_data();
            }
            rechProvenant(motCle);
        }
        else if(event.getSource()==BTN_EDT_PROV){
              editProvenant(event);
        }else if(event.getSource()==BTN_SUP_PROV){
                  int index = TBVW_PROV.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String code =TBVW_PROV.getItems().get(index).getCodeProv();
                try {
             
                    String confirm = ConfirmSuppression.suppConf();
                    if(confirm.equals("OK")){
                        //COmmunication et recuperation des donnes via l api rest du cote du server
                        String url="http://localhost:8080/provenances/"+code;
                        Client client =ClientBuilder.newClient();

                        String delResp= client
                                                .target(url)
                                                 .request(MediaType.TEXT_PLAIN)
                                                 .delete(String.class);
                        if(delResp.equals("SUCCESS")){
                             WindowDialog windowDialog = new WindowDialog("info", "la provenance a été supprime avec succee", null);
                             fetch_provenant_data();
                        }else{
                             WindowDialog windowDialog = new WindowDialog("erreur", "Impossible de supprimer cette provenance veillez ressayer", null);
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
    //Listage des ministeres
     public void fetch_provenant_data(){
         try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du serve
            
             String url="http://localhost:8080/provenances";
            Client client =ClientBuilder.newClient();
		
            List<Provenance> provList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Provenance>>(){});
            
             ObservableList<Provenance> list=FXCollections.observableArrayList();
            list.addAll(provList);
            CLMN_CODE_PROV.setCellValueFactory(new PropertyValueFactory<>("codeProv"));
            CLMN_ABRV_PROV.setCellValueFactory(new PropertyValueFactory<>("abrevProv"));
            CLMN_LIBL_PROV.setCellValueFactory(new PropertyValueFactory<>("libelProv"));
            TBVW_PROV.setItems(list);

        } catch (Exception e) {
             WindowDialog windowDialog = new WindowDialog("erreur","La connection au serveur a été interompu",null);
        }
    }

    private void openNewProvenant(ActionEvent event) throws IOException {
       Parent root = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/NewProvenantView.fxml"));
            //System.out.println(getClass());
            Stage stage = new Stage();
            stage.setTitle("Nouveau Provenant");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.show();
    }

    private void editProvenant(ActionEvent event){
         int index = TBVW_PROV.getSelectionModel().getSelectedIndex();
            if(index>=0){
                String code =TBVW_PROV.getItems().get(index).getCodeProv();
                String abrev=TBVW_PROV.getItems().get(index).getAbrevProv();
                String libel=TBVW_PROV.getItems().get(index).getLibelProv();
                 
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gestcourierclient/views/EditProvenantView.fxml"));
                
                try {
                    loader.load();
                    } catch (IOException e) {
                    }
                
                //recuperation du controller du vue du lancer
                EditProvenantController edtProv=loader.getController();
               edtProv.setTextFiled(code, abrev,libel);
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
    ProvenantController.role =role;
    }
    public String getRole(){
        return role;
    } 

    private void rechProvenant(String motCle) {
         try {
             
            
             //COmmunication et recuperation des donnes via l api rest du cote du serve
            
             String url="http://localhost:8080/rechProvenances/"+motCle;
            Client client =ClientBuilder.newClient();
		
            List<Provenance> provList= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Provenance>>(){});
            
             ObservableList<Provenance> list=FXCollections.observableArrayList();
            list.addAll(provList);
            CLMN_CODE_PROV.setCellValueFactory(new PropertyValueFactory<>("codeProv"));
            CLMN_ABRV_PROV.setCellValueFactory(new PropertyValueFactory<>("abrevProv"));
            CLMN_LIBL_PROV.setCellValueFactory(new PropertyValueFactory<>("libelProv"));
            TBVW_PROV.getItems().clear();
            TBVW_PROV.setItems(list);

        } catch (Exception e) {
             WindowDialog windowDialog = new WindowDialog("erreur","Une erreur est suervenu",null);
        }
    }
}

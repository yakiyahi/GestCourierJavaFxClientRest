
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.FindReq;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Arrive;
import gestcourierclient.entities.Consernant;
import gestcourierclient.entities.Courier;
import gestcourierclient.entities.Direction;
import gestcourierclient.entities.Utilisateur;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

public class NewCourierController implements Initializable {

      @FXML
    private Button BTN_ADD_COUR;

    @FXML
    private Button BTN_CLOS_COUR;

    @FXML
    private TextField TXT_RF_COUR;

    @FXML
    private ComboBox<String> CMBX_ID_ARV;

    @FXML
    private ComboBox<String> CMBX_CD_CONS;

    @FXML
    private ComboBox<String> CMBX_CD_DIR;

    @FXML
    private ComboBox<String> CMBX_ID_USR;

    @FXML
    private TextArea TXT_DSCR_COUR;

    @FXML
    private ComboBox<String> CMBX_TP_COUR;

   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCode_dir_on_comobox();
        setCode_cons_on_comobox();
        setId_usr_on_comobox();
        setId_arrv_on_comobox();
        setType_cmbx();
    }    
    @FXML
    void newCourierAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_COUR){
            new CloseWindow(event);
        }else if(event.getSource()==BTN_ADD_COUR){
                 //On verifie si les champs ne sont pas vide
            String codeDir = CMBX_CD_DIR.getValue();
            String codeCons = CMBX_CD_CONS.getValue();
            String idArrv = CMBX_ID_ARV.getValue();   
            String userNum =CMBX_ID_USR.getValue();
            String refCour = TXT_RF_COUR.getText();
            String type = CMBX_TP_COUR.getValue();
            String descr = TXT_DSCR_COUR.getText();

            if("".equals(codeDir)||"".equals(descr)||refCour.equals("")||"".equals(type)||"".equals(descr)||"".equals(userNum)||idArrv.equals("")){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type courier et le donne les
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute le direction
                
                 */
                 Direction dir = FindReq.findDirection(codeDir);
                 Utilisateur usr = FindReq.findUtilisateur(userNum);
                 Arrive arv = FindReq.findArrive(Long.parseLong(idArrv));
                
                    Courier cour =new Courier(refCour, type, descr, usr, dir, arv);
                   if(type.equals("PERSONEL")){
                      Consernant cons =FindReq.findConsernant(codeCons);
                      cour.setConsernant(cons);
                   } 
                 try{
                     Courier courier =addCourier(cour);
                      //Si la requete a été execute avec succees on affiche un message de succees
                      WindowDialog windowDialog = new WindowDialog("info", "le couriera été ajouté avec succée",null);
                      //CMBX_PROV_DIR.setValue("");
                      setCode_dir_on_comobox();
                      setType_cmbx() ;
                      setCode_cons_on_comobox();
                      setId_arrv_on_comobox();
                      setId_usr_on_comobox();
                      TXT_DSCR_COUR.setText("");
                      TXT_RF_COUR.setText("");
                   
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", "Il ya eu une erreur :"+e.getMessage(),null);
                }
            }
        }
        }

    private Courier addCourier(Courier cour) {
        String url ="http://localhost:8080/couriers";
             Client client =ClientBuilder.newClient();
             Courier courier= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(cour),Courier.class);
             return courier;
    
    }

    private void setCode_dir_on_comobox() {
         String url="http://localhost:8080/allNumsDirections";
            Client client =ClientBuilder.newClient();
		
            List<String> codeDir= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
      CMBX_CD_DIR.getItems().clear();
      CMBX_CD_DIR.setItems(FXCollections.observableArrayList(codeDir));
    }
     private void setCode_cons_on_comobox() {
         String url="http://localhost:8080/allNumsConsernants";
            Client client =ClientBuilder.newClient();
		
            List<String> codeCons= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
      CMBX_CD_CONS.getItems().clear();
      CMBX_CD_CONS.setItems(FXCollections.observableArrayList(codeCons));
    }
    private void setId_usr_on_comobox() {
         String url="http://localhost:8080/allIdUtilisateur";
            Client client =ClientBuilder.newClient();
		
            List<String> userNum= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
      CMBX_ID_USR.getItems().clear();
      CMBX_ID_USR.setItems(FXCollections.observableArrayList(userNum));
    }
    private void setId_arrv_on_comobox() {
         String url="http://localhost:8080/allIdArrivées";
            Client client =ClientBuilder.newClient();
		
            List<String> idArv= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
      CMBX_ID_ARV.getItems().clear();
      CMBX_ID_ARV.setItems(FXCollections.observableArrayList(idArv));
    }

    private void setType_cmbx() {
        ArrayList<String> typs= new ArrayList<>();
        typs.add("NON-PERSONEL");
        typs.add("PERSONEL");
        CMBX_TP_COUR.getItems().clear();
        CMBX_TP_COUR.setItems(FXCollections.observableArrayList(typs));
    }
}

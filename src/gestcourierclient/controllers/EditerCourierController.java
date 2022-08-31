
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


public class EditerCourierController implements Initializable {

    public static Courier cour;
     @FXML
    private Button BTN_UPDATE_COUR;

    @FXML
    private Button BTN_CLOS_COUR_EDT;

    @FXML
    private TextField TXT_RF_COUR_EDT;

    @FXML
    private ComboBox<String> CMBX_ID_ARV_EDT;

    @FXML
    private ComboBox<String> CMBX_CD_CONS_EDT;

    @FXML
    private ComboBox<String> CMBX_CD_DIR_EDT;

    @FXML
    private ComboBox<String> CMBX_ID_USR_EDT;

    @FXML
    private TextArea TXT_DSCR_COUR_EDT;

    @FXML
    private ComboBox<String> CMBX_TP_COUR_EDT;

   public void setCour(Courier c){
        EditerCourierController.cour =c;
    }
    public Courier getCour(){
        return EditerCourierController.cour;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*setCode_dir_on_comobox();
        setCode_cons_on_comobox();
        setId_usr_on_comobox();
        setId_arrv_on_comobox(); */
        setType_cmbx();
    }    
    @FXML
    void editerCourierAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_COUR_EDT){
            new CloseWindow(event);
        }else if(event.getSource()==BTN_UPDATE_COUR){
             //On verifie si les champs ne sont pas vide
            String codeCons =CMBX_CD_CONS_EDT.getValue();
            String codeDir = CMBX_CD_DIR_EDT.getValue();
            String refCour =TXT_RF_COUR_EDT.getText();
            Long idArv = Long.parseLong(CMBX_ID_ARV_EDT.getValue());
            String usrNum =CMBX_ID_USR_EDT.getValue();
            String descr = TXT_DSCR_COUR_EDT.getText();
            String type =CMBX_TP_COUR_EDT.getValue();
            
            if("".equals(codeDir)||"".equals(descr)||refCour.equals("")||"".equals(type)||"".equals(descr)||"".equals(usrNum)||idArv==null){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type courier et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui modifie le courier
                
                */
                 Direction dir = FindReq.findDirection(codeDir);
                 Utilisateur usr = FindReq.findUtilisateur(usrNum);
                 Arrive arv = FindReq.findArrive(idArv);
                
                    Courier cour =new Courier(refCour, type, descr, usr, dir, arv);
                   if(type.equals("PERSONEL")){
                      Consernant cons =FindReq.findConsernant(codeCons);
                      cour.setConsernant(cons);
                   }
                try {
                    Courier courier =updateCourier(cour);
                    
                    if(courier!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        new WindowDialog("info", "Consernant a été modifie avec succée",null);
                        setCode_dir_on_comobox();
                        setType_cmbx() ;
                        setCode_cons_on_comobox();
                        setId_arrv_on_comobox();
                        setId_usr_on_comobox();
                        TXT_DSCR_COUR_EDT.setText("");
                        TXT_RF_COUR_EDT.setText("");
                    }
                   else if(courier==null){
                       //Sinon on affiche un message d erreur
                         new WindowDialog("erreur", "Erreur de modifier le consernant.Veillez ressayer",null);
                         setCode_dir_on_comobox();
                         setType_cmbx() ;
                         setCode_cons_on_comobox();
                         setId_arrv_on_comobox();
                         setId_usr_on_comobox();
                         TXT_DSCR_COUR_EDT.setText("");
                         TXT_RF_COUR_EDT.setText("");
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", e.getMessage(),null);
                }
            }
        }
    }
    void setTextFiled(String refCour, String type, String descr, String idArv, String codeDir, String userNum, String codeCons) {
        
        CMBX_CD_CONS_EDT.setValue(codeCons);
        
        TXT_RF_COUR_EDT.setText(refCour);
        TXT_DSCR_COUR_EDT.setText(descr);
        CMBX_TP_COUR_EDT.setValue(type);
        CMBX_CD_DIR_EDT.setValue(codeDir);
        CMBX_ID_ARV_EDT.setValue(idArv);
        CMBX_ID_USR_EDT.setValue(userNum);
    }

    void setTextFiledNoCons(String refCour, String type, String descr, String idArv, String codeDir, String userNum) {
        TXT_RF_COUR_EDT.setText(refCour);
        TXT_DSCR_COUR_EDT.setText(descr);
        CMBX_TP_COUR_EDT.setValue(type);
        CMBX_CD_DIR_EDT.setValue(codeDir);
        CMBX_ID_ARV_EDT.setValue(idArv);
        CMBX_ID_USR_EDT.setValue(userNum);
    }
     private void setCode_dir_on_comobox() {
         String url="http://localhost:8080/allNumsDirections";
            Client client =ClientBuilder.newClient();
		
            List<String> codeDir= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
      CMBX_CD_DIR_EDT.getItems().clear();
      CMBX_CD_DIR_EDT.setItems(FXCollections.observableArrayList(codeDir));
    }
     private void setCode_cons_on_comobox() {
         String url="http://localhost:8080/allNumsConsernants";
            Client client =ClientBuilder.newClient();
		
            List<String> codeCons= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
      CMBX_CD_CONS_EDT.getItems().clear();
      CMBX_CD_CONS_EDT.setItems(FXCollections.observableArrayList(codeCons));
    }
    private void setId_usr_on_comobox() {
         String url="http://localhost:8080/allIdUtilisateur";
            Client client =ClientBuilder.newClient();
		
            List<String> userNum= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
      CMBX_ID_USR_EDT.getItems().clear();
      CMBX_ID_USR_EDT.setItems(FXCollections.observableArrayList(userNum));
    }
    private void setId_arrv_on_comobox() {
         String url="http://localhost:8080/allIdArrivées";
            Client client =ClientBuilder.newClient();
		
            List<String> idArv= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
      CMBX_ID_ARV_EDT.getItems().clear();
      CMBX_ID_ARV_EDT.setItems(FXCollections.observableArrayList(idArv));
    }

    private void setType_cmbx() {
        ArrayList<String> typs= new ArrayList<>();
        typs.add("NON-PERSONEL");
        typs.add("PERSONEL");
        CMBX_TP_COUR_EDT.getItems().clear();
        CMBX_TP_COUR_EDT.setItems(FXCollections.observableArrayList(typs));
    }

    private Courier updateCourier(Courier cour) {
        String url ="http://localhost:8080/couriers";
             Client client =ClientBuilder.newClient();
             Courier courier= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(cour),Courier.class);
             return courier;
    }
    
}

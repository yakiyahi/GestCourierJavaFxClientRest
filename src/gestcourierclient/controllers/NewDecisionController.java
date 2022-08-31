
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.FindReq;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Courier;
import gestcourierclient.entities.Decision;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;


public class NewDecisionController implements Initializable {

   @FXML
    private ComboBox<String> CMBX_TYPE_DECIS;

    @FXML
    private TextField TXT_DESCR_TYPE;

    @FXML
    private Button BTN_ADD_DECIS;

    @FXML
    private Button BTN_CLOS_DECIS;

    @FXML
    private DatePicker DT_DECIS;

    @FXML
    private ComboBox<String> CMBX_REF_COUR_DECIS;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCode_dir_on_comobox();
        setDateNow();
        setTypeDecis();
        
    }    
    @FXML
    void newDecisAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_DECIS){
            new CloseWindow(event);
        }else if(event.getSource()==BTN_ADD_DECIS){
            //On verifie si les champs ne sont pas vide
            String refCour = CMBX_REF_COUR_DECIS.getValue();
            String tyeCour = CMBX_TYPE_DECIS.getValue() ;
            String descr = TXT_DESCR_TYPE.getText();
            LocalDate date= DT_DECIS.getValue();
            java.sql.Date dateDecis=java.sql.Date.valueOf(date);
            if("".equals(refCour)||"".equals(tyeCour)||"".equals(descr)||dateDecis==null){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type consernant et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute le consernant
                */
                Courier courier = FindReq.findCourrier(refCour);
                Decision decision =new Decision(tyeCour, descr, dateDecis, courier);
                try {
                    Decision decis= addDecision(decision );
                    if(decis!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "Decision ajoute avec succée",null);
                        setCode_dir_on_comobox();
                        setTypeDecis();
                        TXT_DESCR_TYPE.setText("");
                        setDateNow();
                    }
                   else if(decis==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur d'ajout.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", e.getMessage(),null);
                }
            }
        }
    }
    private void setCode_dir_on_comobox() {
        String url="http://localhost:8080/allRefCours";
        Client client =ClientBuilder.newClient();
		
        List<String> refCour= client
                                    .target(url)
                                    .request(MediaType.APPLICATION_JSON)
                                    .get(new GenericType<List<String>>(){});
        
      CMBX_REF_COUR_DECIS.getItems().clear();
      CMBX_REF_COUR_DECIS.setItems(FXCollections.observableArrayList(refCour));
    }

    private Decision addDecision(Decision decision) {
         String url ="http://localhost:8080/decisions";
             Client client =ClientBuilder.newClient();
             Decision decis= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(decision),Decision.class);
             return decis;
    }
    public void setDateNow(){
         DT_DECIS.setValue(LocalDate.now());
        DateTimeFormatter dt=DateTimeFormatter.ofPattern("hh:mm:ss");
    }

    private void setTypeDecis() {
        ArrayList<String> typs= new ArrayList<>();
        typs.add("VISA");
        typs.add("REFU");
        CMBX_TYPE_DECIS.getItems().clear();
        CMBX_TYPE_DECIS.setItems(FXCollections.observableArrayList(typs));
    }
}

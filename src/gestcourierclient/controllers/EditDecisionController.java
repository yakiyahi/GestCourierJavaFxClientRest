
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.FindReq;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Courier;
import gestcourierclient.entities.Decision;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;

public class EditDecisionController implements Initializable {

     @FXML
    private TextField TXT_DESCR_EDT;

    @FXML
    private Button BTN_UPDT_DECIS;

    @FXML
    private Button BTN_CLOS_EDT_DECIS;

    @FXML
    private DatePicker DT_DECIS_EDT;

    @FXML
    private ComboBox<String> CMBX_REF_COUR_DECIS_EDT;

    @FXML
    private ComboBox<String> CMBX_TYPE_DECIS_EDT;

    @FXML
    private TextField TXT_ID_DECIS;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         setTypeDecis();
    }    
    @FXML
    void editDecisAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_EDT_DECIS){
            new CloseWindow(event);
        }else if(event.getSource()==BTN_UPDT_DECIS){
            Long id =Long.parseLong(TXT_ID_DECIS.getText());
            String refCour = CMBX_REF_COUR_DECIS_EDT.getValue();
            String tyeCour =CMBX_TYPE_DECIS_EDT.getValue() ;
            String descr = TXT_DESCR_EDT.getText();
            LocalDate date= DT_DECIS_EDT.getValue();
            java.sql.Date dateDecis=java.sql.Date.valueOf(date);
            if("".equals(refCour)||"".equals(tyeCour)||"".equals(descr)||dateDecis==null){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }
            else{
                /*Si les champs ne sont ps vide,on creer un object de type Decision et le donne les
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute Decision
                 */
                try {
                    Courier courier = FindReq.findCourrier(refCour);
                    Decision decision =new Decision(tyeCour, descr, dateDecis, courier);
                    decision.setIdDecis(id);
                    Decision decis= updateDecision(decision );

                    if(decis!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "la modification a été effectué avec succée:",null);
                        new CloseWindow(event);
                    }
                   else if(decis==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur de modifier la decision.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", "Il ya eu une erreur::"+e.getMessage(),null);
                }
            }
        }
    }

    private Decision updateDecision(Decision decision) {
        String url ="http://localhost:8080/decisions";
             Client client =ClientBuilder.newClient();
             Decision decis= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(decision),Decision.class);
             return decis;
    }
     private void setTypeDecis() {
        ArrayList<String> typs= new ArrayList<>();
        typs.add("VISA");
        typs.add("REFU");
        CMBX_TYPE_DECIS_EDT.getItems().clear();
        CMBX_TYPE_DECIS_EDT.setItems(FXCollections.observableArrayList(typs));
    }

    void setTextFiled(Long id, String refCour, String descr, Date date, String type) {
        TXT_ID_DECIS.setText(id.toString());
        CMBX_REF_COUR_DECIS_EDT.setValue(refCour);
        DT_DECIS_EDT.setValue(date.toLocalDate());
        TXT_DESCR_EDT.setText(descr);
        CMBX_TYPE_DECIS_EDT.setValue(type);
    }
}

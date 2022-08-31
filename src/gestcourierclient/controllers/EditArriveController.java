
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Arrive;
import java.net.URL;
import java.sql.Time;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


public class EditArriveController implements Initializable {

   @FXML
    private TextField TXT_EDT_HEUR_ARV;

    @FXML
    private Button BTN_UPDT_ARV;

    @FXML
    private Button BTN_EDT_CLOS_ARV;

    @FXML
    private DatePicker DT_EDT_ARV;

    @FXML
    private TextField TXT_EDT_ID_ARV;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    void setTextFiled(Long id, Date date, Time heur) {
        TXT_EDT_ID_ARV.setText(id.toString());
        TXT_EDT_HEUR_ARV.setText(heur.toString());
        DT_EDT_ARV.setValue(date.toLocalDate());
    }
    @FXML
    void editerArriveAction(ActionEvent event) {
        if(event.getSource()==BTN_EDT_CLOS_ARV){
           new CloseWindow(event);
        }else if(event.getSource()==BTN_UPDT_ARV){
              DateFormat dtf= new SimpleDateFormat("hh:mm");
           //On verifie si les champs ne sont pas vide
            LocalDate date= DT_EDT_ARV.getValue();
            //Conversion de Localdate a date
            //Date datArv = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateAr=Date.valueOf(date);
            String heur= TXT_EDT_HEUR_ARV.getText();
            Long id = Long.parseLong(TXT_EDT_ID_ARV.getText());
           // LocalTime lt = LocalTime.parse(heur);
            //Convertis LocalTime to Time
            //Time time =Time.valueOf(lt);
            //java.util.Date dathr =dtf.parse(heur);
            
            if(date==null||heur.equals("")|| id==null){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type Ministere et le donne les
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute arriver
                 */
                try {
                     Arrive arrive = new Arrive(dateAr,Time.valueOf(heur));
                     arrive.setIdArive(id);
                    Arrive arv= updateArrive(arrive);
                    if(arv!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "la modification a été effectué avec succée:",null);
                        new CloseWindow(event);
                    }
                   else if(arv==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur de modifier l'arrive.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", "Il ya eu une erreur::"+e.getMessage(),null);
                }
            }
        }
    }

    private Arrive updateArrive(Arrive arrive) {
         String url ="http://localhost:8080/arrivées";
             Client client =ClientBuilder.newClient();
             Arrive arrv= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(arrive),Arrive.class);
             return arrv;
    }
    
}

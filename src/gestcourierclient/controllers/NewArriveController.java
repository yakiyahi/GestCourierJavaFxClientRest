
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Arrive;
import java.net.URL;
import java.sql.Time;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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


public class NewArriveController implements Initializable {
    @FXML
    private DatePicker DT_ARV;

    @FXML
    private Button BTN_ADD_ARV;

    @FXML
    private Button BTN_CLOS_ARV;

    @FXML
    private TextField TXT_HEUR_ARV;

    @FXML
    void newArriveAction(ActionEvent event) throws ParseException {
        
        if(event.getSource()==BTN_CLOS_ARV){
            new  CloseWindow(event);
        }else{
            DateFormat dtf= new SimpleDateFormat("hh:mm");
           //On verifie si les champs ne sont pas vide
            LocalDate date= DT_ARV.getValue();
            //Conversion de Localdate a date
            //Date datArv = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateAr=Date.valueOf(date);
            String heur= TXT_HEUR_ARV.getText();
           // LocalTime lt = LocalTime.parse(heur);
            //Convertis LocalTime to Time
            //Time time =Time.valueOf(lt);
            //java.util.Date dathr =dtf.parse(heur);
            
            if(date==null||heur.equals("")){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type Ministere et le donne les
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute le minister
                 */
                try {
                     Arrive arrive = new Arrive(dateAr,Time.valueOf(heur));
                    Arrive arv= addArrive(arrive);
                    if(arv!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "l ajout a été effectué avec succée:",null);
                        TXT_HEUR_ARV.setText("");
                        DT_ARV.setValue(LocalDate.now());
                    }
                   else if(arv==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur d'ajout cette arrive.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", "Il ya eu une erreur de type:"+e.getMessage(),null);
                }
            }  
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DT_ARV.setValue(LocalDate.now());
        DateTimeFormatter dt=DateTimeFormatter.ofPattern("hh:mm:ss");
        String tm =LocalTime.now().format(dt);
        TXT_HEUR_ARV.setText(tm);

    }    

    private Arrive addArrive(Arrive arrive) {
         /* fonction qui se connecte avec l API REST du server et envois une requete de type
            post pour ajouter une arrive a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner l'arrive ajoute.
        */
             String url ="http://localhost:8080/arrivées";
             Client client =ClientBuilder.newClient();
             Arrive arrv= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(arrive),Arrive.class);
             return arrv;
    }
    
}

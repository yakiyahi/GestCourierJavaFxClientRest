
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Consernant;
import gestcourierclient.entities.Employer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class NewEmployerController implements Initializable {
    @FXML
    private TextField TXT_NOM_EMP;

    @FXML
    private TextField TXT_PRNM_EMP;

    @FXML
    private TextField TXT_ADDR_EMP;

    @FXML
    private Button BTN_ADD_EMP;

    @FXML
    private Button BTN_CLOS_EMPL;

    @FXML
    private TextField TXT_NUM_EMP;

    @FXML
    void newEmplAction(ActionEvent event) {
           if(event.getSource()==BTN_CLOS_EMPL){
            //Si c est le button fermer qui est cliquer on appelle la fonction qui ferme la fenetre
            CloseWindow closeWindow = new CloseWindow(event);
        }
        else if(event.getSource()==BTN_ADD_EMP){
            //On verifie si les champs ne sont pas vide
            String num_empl = TXT_NUM_EMP.getText();
            String nom_empl= TXT_NOM_EMP.getText();
            String prenom_empl= TXT_PRNM_EMP.getText();
            String addr_emp= TXT_ADDR_EMP.getText();
            if("".equals(num_empl)||"".equals(nom_empl)||"".equals(prenom_empl) ||"".equals(addr_emp)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type employer et le donne les
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute le employer
                 */
                 Employer employer= new Employer(num_empl,nom_empl,prenom_empl,addr_emp);
                try {
                    Employer empl= addEmployer(employer );
                    if(empl!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "l'employer a été ajouté avec succée",null);
                        TXT_NUM_EMP.setText("");
                        TXT_NOM_EMP.setText("");
                        TXT_PRNM_EMP.setText("");
                        TXT_ADDR_EMP.setText("");
                    }
                   else if(empl==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur d'ajout l'employer.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", "Il ya eu une erreur au niveau:"+e.getMessage(),null);
                }
            }
        }
    }

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    public Employer addEmployer(Employer employer){
         /* fonction qui se connecte avec l API REST du server et envois une requete de type
            post pour ajouter un employer a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner l'employer ajoute.
        */
             String url ="http://localhost:8080/employers";
             Client client =ClientBuilder.newClient();
             Employer empl= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(employer),Employer.class);
             return empl;
    }
    
}


package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
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


public class EditerEmployerController implements Initializable {
    
    public static Employer employer;
    
    @FXML
    private TextField TXT_NOM_EMP_EDT;

    @FXML
    private TextField TXT_PRNM_EMP_EDT;

    @FXML
    private TextField TXT_ADDR_EMP_EDT;

    @FXML
    private Button BTN_UPDT_EMP;

    @FXML
    private Button BTN_CLOS_EMPL_EDT;

    @FXML
    private TextField TXT_NUM_EMP_EDT;
    
    public void SetEmployer(Employer employer){
        EditerEmployerController.employer=employer;
    }
     public Employer getEmployer(){
        return EditerEmployerController.employer;
    }

    @FXML
    void edtEmplAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_EMPL_EDT){
            CloseWindow closeWindow = new CloseWindow(event);
        }else if(event.getSource()==BTN_UPDT_EMP){
            //On verifie si les champs ne sont pas vide
            String num = TXT_NUM_EMP_EDT.getText();
            String nom = TXT_NOM_EMP_EDT.getText();
            String prenom = TXT_PRNM_EMP_EDT.getText();
            String addr = TXT_ADDR_EMP_EDT.getText();
            if("".equals(num)||"".equals(nom)||"".equals(prenom) ||"".equals(addr)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type employer et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui modifie l'employer
                
                */
                Employer employer=new Employer(num,nom,prenom,addr);
                try {
                    //Appelle de la fonction qui modifie l'employer
                    
                    Employer empl= updateEmpl(employer);
                    
                    if(empl!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "l'enployer a été modifie avec succée",null);
                        TXT_NUM_EMP_EDT.setText("");
                        TXT_NOM_EMP_EDT.setText("");
                        TXT_PRNM_EMP_EDT.setText("");
                        TXT_ADDR_EMP_EDT.setText("");
                    }
                   else if(empl==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur de modifier cet employer.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur","Erreur au niveau du serveur est survenu"+ e.getMessage(),null);
                }
            }
        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setTextFiled(String num,String nom,String prenom,String addr){
        this.TXT_NUM_EMP_EDT.setText(num);
        this.TXT_NOM_EMP_EDT.setText(nom);
        this.TXT_PRNM_EMP_EDT.setText(prenom);
        this.TXT_ADDR_EMP_EDT.setText(addr);
    }
     public Employer updateEmpl(Employer empl ){
        /* fonction qui se connecte avec l API REST du server et envois une requete de type
            put pour modifier un employer a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner l'employer modifier.
        */
             String url ="http://localhost:8080/employers";
             Client client =ClientBuilder.newClient();
             Employer employer= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(empl),Employer.class);
             return employer;
    }
}


package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Consernant;
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


public class EditerConsernantController implements Initializable {
    
    private static Consernant cons;
   
    @FXML
    private TextField TXT_COD_CONS_EDT;

    @FXML
    private TextField TXT_NOM_CONS_EDT;

    @FXML
    private TextField TXT_PRENM_CONS_EDT;

    @FXML
    private Button BTN_UPDT_CONS;

    @FXML
    private Button BTN_CLOS_CONS_EDT;
    
    public void setCons(Consernant cons){
        EditerConsernantController.cons =cons;
    }
    public Consernant getCons(){
        return EditerConsernantController.cons;
    }
    @FXML
    public void updateConsAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_CONS_EDT){
            CloseWindow closeWindow = new CloseWindow(event);
        }else if(event.getSource()==BTN_UPDT_CONS){
            //On verifie si les champs ne sont pas vide
            String code_cons = TXT_COD_CONS_EDT.getText();
            String nom_cons = TXT_NOM_CONS_EDT.getText();
            String pren_cons = TXT_PRENM_CONS_EDT.getText();
            if("".equals(code_cons)||"".equals(nom_cons)||"".equals(pren_cons)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type consernant et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui modifie le consernant
                
                */
                Consernant consernant =new Consernant(code_cons,nom_cons,pren_cons);
                try {
                    Consernant consern= updateConsernant(consernant);
                    if(consern!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "Consernant a été modifie avec succée",null);
                        TXT_COD_CONS_EDT.setText("");
                        TXT_NOM_CONS_EDT.setText("");
                        TXT_PRENM_CONS_EDT.setText("");
                    }
                   else if(consern==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur de modifier le consernant.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", e.getMessage(),null);
                }
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public Consernant updateConsernant(Consernant cons ){
        /* fonction qui se connecte avec l API REST du server et envois une requete de type
            put pour modifier un consernaant a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner le consernant modifier.
        */
             String url ="http://localhost:8080/consernants";
             Client client =ClientBuilder.newClient();
             Consernant consernant= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(cons),Consernant.class);
             return consernant;
    }
  
     public void setTextFiled(String code,String nom,String prenom){
        this.TXT_COD_CONS_EDT.setText(code);
        this.TXT_NOM_CONS_EDT.setText(nom);
        this.TXT_PRENM_CONS_EDT.setText(prenom);
    }
}

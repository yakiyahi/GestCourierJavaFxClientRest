
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Provenance;
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

public class EditProvenantController implements Initializable {
      @FXML
    private TextField TXT_EDT_CODE_PROV;

    @FXML
    private Button BTN_UPDT_PROV;

    @FXML
    private Button BTN_CLOS_PROV_EDT;

    @FXML
    private TextField TXT_EDT_LIBL_PROV;

    @FXML
    private TextField TXT_EDT_ABRV_PROV;

    @FXML
    void editProvenantAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_PROV_EDT){
            CloseWindow closeWindow = new CloseWindow(event);
        }else if(event.getSource()==BTN_UPDT_PROV){
            //On verifie si les champs ne sont pas vide
            String code = TXT_EDT_CODE_PROV.getText();
            String abrev = TXT_EDT_ABRV_PROV.getText();
            String libel = TXT_EDT_LIBL_PROV.getText();
            if("".equals(code)||"".equals(abrev)||"".equals(libel)){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type consernant et le donne les 
                informations saisir par 'utilisateur
                on appelle la fonction qui modifie le provenant
                
                */
                Provenance provenance =new Provenance(code, abrev, libel);
                try {
                     Provenance prov= updateProvenant(provenance);
                    if(prov!=null){
                        //Si la requete a été execute avec succees on affiche un message de succees
                        WindowDialog windowDialog = new WindowDialog("info", "La provenance a été modifié avec succée",null);
                        TXT_EDT_CODE_PROV.setText("");
                        TXT_EDT_ABRV_PROV.setText("");
                        TXT_EDT_LIBL_PROV.setText("");
                    }
                   else if(prov==null){
                       //Sinon on affiche un message d erreur
                        WindowDialog windowDialog = new WindowDialog("erreur", "Erreur de modifier la provenance.Veillez ressayer",null);
                    }
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", e.getMessage(),null);
                }
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

    void setTextFiled(String code, String abrev, String libel) {
       this.TXT_EDT_CODE_PROV.setText(code);
       this.TXT_EDT_ABRV_PROV.setText(abrev);
        this.TXT_EDT_LIBL_PROV.setText(libel);
    }

    private Provenance updateProvenant(Provenance prov) {
         /* fonction qui se connecte avec l API REST du server et envois une requete de type
            put pour modifier une provenance a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner la provenance modifier.
        */
             String url ="http://localhost:8080/provenances";
             Client client =ClientBuilder.newClient();
             Provenance provenant= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(prov),Provenance.class);
             return provenant;
    }
    
}

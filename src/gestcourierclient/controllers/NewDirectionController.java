
package gestcourierclient.controllers;

import gestcourierclient.CloseWindow;
import gestcourierclient.FindReq;
import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Direction;
import gestcourierclient.entities.Provenance;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;


public class NewDirectionController implements Initializable {

    @FXML
    private TextField TXT_ABREV_DIR;
    
    @FXML
    private TextField TXT_CODE_DIR;

    @FXML
    private Button BTN_AD_DIR;

    @FXML
    private Button BTN_CLOS_DIR;

    @FXML
    private ComboBox<String> CMBX_PROV_DIR;

    @FXML
    private TextField TXT_DES_DIR;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCode_min_on_comobox();
    } 
    
    @FXML
    void newDirectionAction(ActionEvent event) {
        if(event.getSource()==BTN_CLOS_DIR){
            new CloseWindow(event);
        }else if(event.getSource()==BTN_AD_DIR){
              //On verifie si les champs ne sont pas vide
            String codeProv = CMBX_PROV_DIR.getValue();
            String codeDir = TXT_CODE_DIR.getText();
            String abrevDir = TXT_ABREV_DIR.getText();
            String descrDir= TXT_DES_DIR.getText();
            if("".equals(codeDir)||"".equals(descrDir)||codeProv.equals("")){
                //On affiche un message de warning si les champs sont vides
                WindowDialog windowDialog = new WindowDialog("warning", "Veillez saisir tous les champs",null);
            }else{
                /*Si les champs ne sont ps vide,on creer un object de type Direction et le donne les
                informations saisir par 'utilisateur
                on appelle la fonction qui ajoute le Direction
                
                 */
                 Provenance prov= FindReq.findProvenant(codeProv);
                 Direction direction= new Direction(codeDir,abrevDir,descrDir,prov);
                try {
                    Direction dir= addDirection(direction );
                    
                    //Si la requete a été execute avec succees on affiche un message de succees
                    WindowDialog windowDialog = new WindowDialog("info", "la direction a été ajouté avec succée",null);
                    //CMBX_PROV_DIR.setValue("");
                    setCode_min_on_comobox();
                    TXT_CODE_DIR.setText("");
                    TXT_ABREV_DIR.setText("");
                    TXT_DES_DIR.setText("");
                   
                } catch (Exception e) {
                    WindowDialog windowDialog = new WindowDialog("erreur", "Il ya eu une erreur au niveau:"+e.getMessage(),null);
                }
            }
        }
    }
      
    private Direction addDirection(Direction direction) {
        /* fonction qui se connecte avec l API REST du server et envois une requete de type
            post pour ajouter une direction a la base de donnee!
            si le requete fonctionne avec succee on va nous retourner la direction ajoute.
        */
             String url ="http://localhost:8080/directions";
             Client client =ClientBuilder.newClient();
             Direction dir= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(direction),Direction.class);
             return dir;
    }
    public void setCode_min_on_comobox(){
       String url="http://localhost:8080/allNumsPovenances";
            Client client =ClientBuilder.newClient();
		
            List<String> codesProv= client
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
        
        CMBX_PROV_DIR.getItems().clear();
        CMBX_PROV_DIR.setItems(FXCollections.observableArrayList(codesProv));
        
        
    }
    
}

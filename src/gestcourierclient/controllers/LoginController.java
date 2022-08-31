
package gestcourierclient.controllers;

import gestcourierclient.WindowDialog;
import gestcourierclient.entities.Login;
import gestcourierclient.entities.Utilisateur;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author yaki
 */
public class LoginController implements Initializable {
    
   @FXML
    private Label label;

    @FXML
    private TextField TXT_USR_NAME;

    @FXML
    private PasswordField PASSWR_FLD;

    @FXML
    private Button BTN_LOGIN;
    
    @FXML
    private VBox VB_CENTER;
    
    @FXML
    private Label LBL_MSG;
    
    @FXML
    private void loginAction(ActionEvent event) throws IOException {
        try {
             String pseudo = TXT_USR_NAME.getText();
        String password = PASSWR_FLD.getText();
        if("".equals(pseudo)){
            LBL_MSG.setText("Veillez saisir le pseudo");
        }else if("".equals(password)){
            LBL_MSG.setText("Veillez saisir le mot de passe");
        }else{
             String response= login(pseudo, password);
             if(response.equals("SUCCESS")){
                 Node node =(Node) event.getSource();
                Stage stage = (Stage)node.getScene().getWindow();
                stage.setMaximized(true);
                stage.close();
                stage.hide();
                //Ouverture de la page d'accueil
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/gestcourierclient/views/HomeView.fxml")));
                stage.setTitle("Gestion des couriers");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                 LBL_MSG.setText("");
                 TXT_USR_NAME.setText("");
                 PASSWR_FLD.setText("");
             }else{
                  LBL_MSG.setText("Nom d 'utilisateur ou mot de passe incorect");
                  TXT_USR_NAME.setText("");
                  PASSWR_FLD.setText("");
             }
             
        }
        } catch (Exception e) {
            new WindowDialog("erreur", "Un ereur est survenu au niveau du server.Veriler resayer a plutard!", null);
        }
      
    }
    public String login(String pseudo,String password){
         Login login = new Login(pseudo,password);
             String url ="http://localhost:8080/login";
             Client client =ClientBuilder.newClient();
             Utilisateur user= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				//.get(new GenericType<Utilisateur>(){});
                                .post(Entity.json(login),Utilisateur.class);
            if(user == null){
                return "ERREUR";
            }else{
                HomeController.setRole(user.getUserRule());
                 return "SUCCESS"; 
            }  
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}

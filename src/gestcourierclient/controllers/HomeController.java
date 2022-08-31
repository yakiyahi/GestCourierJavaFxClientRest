
package gestcourierclient.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomeController implements Initializable {
    @FXML
    private BorderPane BORDR_PAN;
    @FXML
    private Button BTN_ACC;

    @FXML
    private Button BTN_CONSRN;

    @FXML
    private Button BTN_ARV;

    @FXML
    private Button BTN_PROV;

    @FXML
    private Button BTN_EMP;

    @FXML
    private Button BTN_COUR;

    @FXML
    private Button BTN_DECIS;

    @FXML
    private Button BTN_USR;

    @FXML
    private Button BTN_DIR;

    @FXML
    private Button BTN_PARAM;

    @FXML
    private Button BTN_LOGOUT;
   
    private  static String role;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(this.getRole());
            Parent accueil;
        try {
            accueil = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/AccueilView.fxml"));
            BORDR_PAN.setCenter(accueil);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }    
    @FXML
    private void handleButton(ActionEvent event) throws IOException {
        if(event.getSource()==BTN_ACC){
            Parent accueil = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/AccueilView.fxml"));
            BORDR_PAN.setCenter(accueil);
        }else if(event.getSource()==BTN_CONSRN){
            ConsernantController.setRole(this.getRole());
            Parent consernant = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/ConsernantView.fxml"));
            BORDR_PAN.setCenter(consernant);
        }else if(event.getSource()==BTN_EMP){
            EmployerController.setRole(this.getRole());
            Parent emp = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/EmployerView.fxml"));
            BORDR_PAN.setCenter(emp);
        }else if(event.getSource()==BTN_ARV){
            ArriveController.setRole(this.getRole());
            Parent arrv = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/ArriveView.fxml"));
            BORDR_PAN.setCenter(arrv);
        }else if(event.getSource()==BTN_PROV){
            ProvenantController.setRole(HomeController.role);
            Parent prov = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/ProvenantView.fxml"));
            BORDR_PAN.setCenter(prov);
        }else if(event.getSource()==BTN_DIR){
            DirectionController.setRole(HomeController.role);
            Parent dir = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/DirectionView.fxml"));
            BORDR_PAN.setCenter(dir);
        }else if(event.getSource()==BTN_COUR){
            CourierController.setRole(HomeController.role);
            Parent cour = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/CourierView.fxml"));
            BORDR_PAN.setCenter(cour);
        }else if(event.getSource()==BTN_DECIS){
            DecisionController.setRole(HomeController.role);
            Parent decis = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/DecisionView.fxml"));
            BORDR_PAN.setCenter(decis);
        }else if(event.getSource()==BTN_USR){
            UtilisateurController.setRole(HomeController.role);
            Parent decis = FXMLLoader.load(getClass().getResource("/gestcourierclient/views/UtilisateurView.fxml"));
            BORDR_PAN.setCenter(decis);
        }else if(event.getSource()==BTN_LOGOUT){
             Node node =(Node) event.getSource();
                Stage stage = (Stage)node.getScene().getWindow();
                stage.setMaximized(false);
                stage.close();
                //Ouverture de la page d'accueil
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/gestcourierclient/views/Login.fxml")));
                stage.setTitle("Gestion des couriers");
                stage.setScene(scene);
                stage.show();
        }
        
    }
    public static void setRole(String role){
        HomeController.role =role;
    }
    public String getRole(){
        return role;
    }
}


package gestcourierclient;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ConfirmSuppression {
    public static String suppConf(){
        String response=null;
         Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("Confirmation");
         alert.setContentText("Voullez vous vraiment supprime ?");
         Optional<ButtonType> option  = alert.showAndWait();
         if(option.get()==ButtonType.OK){
             response="OK";
         }else if(option.get()==ButtonType.CANCEL){
             response="CANCEL";
         }
        return response;
    }
    
}

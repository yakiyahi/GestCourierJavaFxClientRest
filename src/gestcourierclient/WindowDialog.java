
package gestcourierclient;

import javafx.scene.control.Alert;


public class WindowDialog {
    public WindowDialog(String type,String info,String header){
        switch (type) {
            case "confirm":
                {
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText(info);
                    alert.setHeaderText(header);
                    alert.showAndWait();
                    break;
                }
            case "warning":
                {
                    Alert alert=new Alert(Alert.AlertType.WARNING);
                    alert.setContentText(info);
                    alert.setHeaderText(header);
                    alert.showAndWait();
                    break;
                }
            case "erreur":
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(info);
                    alert.setHeaderText(header);
                    alert.showAndWait();
                    break;
                }
            case "info":
                {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(info);
                    alert.setHeaderText(header);
                    alert.showAndWait();
                    break;
                }
            default:
                break;
        }
         
    }
}

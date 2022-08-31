
package gestcourierclient;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class CloseWindow {
    public CloseWindow(ActionEvent event){
        //Fermeture de la fenetre
        Node node =(Node) event.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        stage.close();
    }
}

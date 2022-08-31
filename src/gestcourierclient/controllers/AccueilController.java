
package gestcourierclient.controllers;

import gestcourierclient.WindowDialog;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

public class AccueilController implements Initializable {
    @FXML
    private BarChart<String, Integer> MY_CHART;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
             int i;
        XYChart.Series<String,Integer> series = new XYChart.Series<>();
        
        String lien="http://localhost:8080/graph";
            Client client =ClientBuilder.newClient();
		
            List<String> graphLis= client
					.target(lien)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<String>>(){});
       
            graphLis.forEach((g)->{
                String[] object= g.split(",");
		System.out.println(object[0]+" "+object[1]);
                series.getData().add(new XYChart.Data<>(object[0],Integer.parseInt(object[1])));
		});
            
          MY_CHART.getData().add(series);
        } catch (Exception e) {
            new WindowDialog("erreur", "ereur au niveau du server.Veriler resayer a plutard!", null);
        }
       
    }    
    
}

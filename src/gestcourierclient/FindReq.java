
package gestcourierclient;

import gestcourierclient.entities.Arrive;
import gestcourierclient.entities.Consernant;
import gestcourierclient.entities.Courier;
import gestcourierclient.entities.Direction;
import gestcourierclient.entities.Provenance;
import gestcourierclient.entities.Utilisateur;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;


public class FindReq {
    private static Provenance provenant;
    public static Provenance findProvenant(String codePro){
         String url="http://localhost:8080/provenances/"+codePro;
            Client client =ClientBuilder.newClient();
		
            Provenance prov= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<Provenance>(){});
            return prov;
    }
    public static Consernant findConsernant(String codeCons){
         String url="http://localhost:8080/consernants/"+codeCons;
            Client client =ClientBuilder.newClient();
		
            Consernant cons= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<Consernant>(){});
            return cons;
    }

    public static Direction findDirection(String codeDir) {
        String url="http://localhost:8080/directions/"+codeDir;
            Client client =ClientBuilder.newClient();
		
            Direction dir= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<Direction>(){});
            return dir;
    }

    public static Arrive findArrive(Long idArr) {
        String url="http://localhost:8080/arrivées/"+idArr;
            Client client =ClientBuilder.newClient();
		
            Arrive arrv= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<Arrive>(){});
            return arrv;
    }

    public static Utilisateur findUtilisateur(String userNum) {
         String url="http://localhost:8080/utilisateurs/"+userNum;
            Client client =ClientBuilder.newClient();
		
            Utilisateur user= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<Utilisateur>(){});
            return user;
    }
 public static Courier findCourrier(String refCour) {
         String url="http://localhost:8080/couriers/"+refCour;
            Client client =ClientBuilder.newClient();
		
            Courier cour= client
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<Courier>(){});
            return cour;
    }
  
    
}

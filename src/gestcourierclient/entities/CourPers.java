
package gestcourierclient.entities;


public class CourPers extends Courier{
    private Consernant consernant;

    public CourPers() {
        super();
    }

   public CourPers(String refCour, String descrCour,String typeCour,Utilisateur utilisateur, Direction direction, Arrive arrive, Consernant consernant1) {
        super(refCour, descrCour,typeCour,utilisateur, direction, arrive);
        this.consernant = consernant1;
    }

    public Consernant getConsernant() {
        return consernant;
    }

    public void setConsernant(Consernant consernant) {
        this.consernant = consernant;
    }
    
}

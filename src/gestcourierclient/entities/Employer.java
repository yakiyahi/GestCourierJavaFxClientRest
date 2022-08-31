
package gestcourierclient.entities;

import java.io.Serializable;
import java.util.Collection;


public class Employer implements Serializable{
    private String numEmpl;
    private String nomEmpl;
    private String prenomEmpl;
    private String adressEmpl;
    public Collection<Courier> courier;
    
    public Employer() {
        super();
    }

    public Employer(String numEmpl, String nomEmpl, String prenomEmpl, String adressEmpl) {
        this.numEmpl = numEmpl;
        this.nomEmpl = nomEmpl;
        this.prenomEmpl = prenomEmpl;
        this.adressEmpl = adressEmpl;
    }

    public String getNumEmpl() {
        return numEmpl;
    }

    public void setNumEmpl(String numEmpl) {
        this.numEmpl = numEmpl;
    }

    public String getNomEmpl() {
        return nomEmpl;
    }

    public void setNomEmpl(String nomEmpl) {
        this.nomEmpl = nomEmpl;
    }

    public String getPrenomEmpl() {
        return prenomEmpl;
    }

    public void setPrenomEmpl(String prenomEmpl) {
        this.prenomEmpl = prenomEmpl;
    }

    public String getAdressEmpl() {
        return adressEmpl;
    }

    public void setAdressEmpl(String adressEmpl) {
        this.adressEmpl = adressEmpl;
    }

    public Collection<Courier> getCourier() {
        return courier;
    }

    public void setCourier(Collection<Courier> courier) {
        this.courier = courier;
    }
}

package gestcourierclient.entities;

import java.io.Serializable;
import java.util.Collection;

public class Consernant implements Serializable{
    private String codeCons;
    private String nomCons;
    private String prenomCons;
    private Collection<Courier> couriers;

    public Consernant() {
        super();
    }

    public Consernant(String codeCons, String nomCons, String prenomCons) {
        this.codeCons = codeCons;
        this.nomCons = nomCons;
        this.prenomCons = prenomCons;
    }

    public String getCodeCons() {
        return codeCons;
    }

    public void setCodeCons(String codeCons) {
        this.codeCons = codeCons;
    }

    public String getNomCons() {
        return nomCons;
    }

    public void setNomCons(String nomCons) {
        this.nomCons = nomCons;
    }

    public String getPrenomCons() {
        return prenomCons;
    }

    public void setPrenomCons(String prenomCons) {
        this.prenomCons = prenomCons;
    }

    public Collection<Courier> getCouriers() {
        return couriers;
    }

    public void setCouriers(Collection<Courier> couriers) {
        this.couriers = couriers;
    }
}

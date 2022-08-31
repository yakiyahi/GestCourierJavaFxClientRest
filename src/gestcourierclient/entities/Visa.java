
package gestcourierclient.entities;

import java.util.Date;


public class Visa {
    private int idVisa;
    private Date dateDecisVisa;
    private String obserVisa;
    public Courier courier;

    public Visa() {
        super();
    }

    public Visa(Date dateDecisVisa, String obserVisa, Courier courier) {
        this.dateDecisVisa = dateDecisVisa;
        this.obserVisa = obserVisa;
        this.courier = courier;
    }

    public int getIdVisa() {
        return idVisa;
    }

    public void setIdVisa(int idVisa) {
        this.idVisa = idVisa;
    }

    public Date getDateDecisVisa() {
        return dateDecisVisa;
    }

    public void setDateDecisVisa(Date dateDecisVisa) {
        this.dateDecisVisa = dateDecisVisa;
    }

    public String getObserVisa() {
        return obserVisa;
    }

    public void setObserVisa(String obserVisa) {
        this.obserVisa = obserVisa;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}

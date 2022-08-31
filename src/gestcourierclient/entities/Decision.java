
package gestcourierclient.entities;

import java.io.Serializable;
import java.sql.Date;

public class Decision implements Serializable{
    private Long idDecis;
    private String typeDecis;
    private String descrDcis;
    private Date dateDecis;
    private Courier courier;

    public Decision() {
        super();
    }

    public Decision(String typeDecis, String descrDcis, Date dateDecis, Courier courier) {
        this.typeDecis = typeDecis;
        this.descrDcis = descrDcis;
        this.dateDecis = dateDecis;
        this.courier = courier;
    }

    public Long getIdDecis() {
        return idDecis;
    }

    public void setIdDecis(Long idDecis) {
        this.idDecis = idDecis;
    }

    public Date getDateDecis() {
        return dateDecis;
    }

    public void setDateDecis(Date dateDecis) {
        this.dateDecis = dateDecis;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public String getTypeDecis() {
        return typeDecis;
    }

    public void setTypeDecis(String typeDecis) {
        this.typeDecis = typeDecis;
    }

    public String getDescrDcis() {
        return descrDcis;
    }

    public void setDescrDcis(String descrDcis) {
        this.descrDcis = descrDcis;
    }

    @Override
    public String toString() {
        return "Decision{" +
                "idDecis=" + idDecis +
                ", typeDecis='" + typeDecis + '\'' +
                ", descrDcis='" + descrDcis + '\'' +
                ", dateDecis=" + dateDecis +
                ", courier=" + courier +
                '}';
    }
}

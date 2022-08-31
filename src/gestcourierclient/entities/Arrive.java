package gestcourierclient.entities;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

public class Arrive implements Serializable{
    private Long idArive;
    private Date dateArive;
    private Time heurArive;
    public Arrive() {
        super();
    }

    public Arrive( Date dateArive, Time heurArive) {
        this.dateArive = dateArive;
        this.heurArive = heurArive;
    }

    public Long getIdArive() {
        return idArive;
    }

    public void setIdArive(Long idArive) {
        this.idArive = idArive;
    }

    public Date getDateArive() {
        return dateArive;
    }

    public void setDateArive(Date dateArive) {
        this.dateArive = dateArive;
    }

    public Time getHeurArive() {
        return heurArive;
    }

    public void setHeurArive(Time heurArive) {
        this.heurArive = heurArive;
    }
}

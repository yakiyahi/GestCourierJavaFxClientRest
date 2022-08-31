
package gestcourierclient.entities;

import java.util.Collection;

public class Provenance {
    private String codeProv;
    private String abrevProv;
    private String libelProv;
    public Collection<Direction> direction;
    public Provenance() {
        super();
    }

    public Provenance(String codeProv,String abrevProv, String libel) {
        this.abrevProv = abrevProv;
        this.libelProv = libel;
        this.codeProv =codeProv;
    }

    public String getCodeProv() {
        return codeProv;
    }

    public void setCodeProv(String codeProv) {
        this.codeProv= codeProv;
    }

    public String getAbrevProv() {
        return abrevProv;
    }

    public void setAbrevProv(String abrevProv) {
        this.abrevProv =abrevProv ;
    }
     public String getLibelProv() {
        return libelProv;
    }

    public void setLibelProv(String libelProv) {
        this.libelProv =libelProv ;
    }
    public Collection<Direction> getDirection() {
        return direction;
    }

    public void setDirection(Collection<Direction> direction) {
        this.direction = direction;
    }
    
}

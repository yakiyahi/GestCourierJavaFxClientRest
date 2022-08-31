
package gestcourierclient.entities;

public class Direction {
    private String codeDirect;
    private String abrevDirect;
    private String descrptDirect;
    public Provenance  provenance;
    
    public Direction() {
        super();
    }

    public Direction(String codeDirect, String abrevDirect, String descrptDirect) {
        this.codeDirect = codeDirect;
        this.abrevDirect = abrevDirect;
        this.descrptDirect = descrptDirect;
    }

    public Direction(String codeDirect, String abrevDirect, String descrptDirect, Provenance provenance) {
        this.codeDirect = codeDirect;
        this.abrevDirect = abrevDirect;
        this.descrptDirect = descrptDirect;
        this.provenance = provenance;
    }

    public String getCodeDirect() {
        return codeDirect;
    }

    public void setCodeDirect(String codeDirect) {
        this.codeDirect = codeDirect;
    }
    public String getAbrevDirect() {
        return abrevDirect;
    }

    public void setAbrevDirect(String abrevDirect) {
        this.abrevDirect = abrevDirect;
    }
    public String getDescrptDirect() {
        return descrptDirect;
    }

    public void setDescrptDirect(String descrptDirect) {
        this.descrptDirect = descrptDirect;
    }

    public Provenance getProvenance() {
        return provenance;
    }

    public void setProvenance(Provenance provenance) {
        this.provenance = provenance;
    }
}

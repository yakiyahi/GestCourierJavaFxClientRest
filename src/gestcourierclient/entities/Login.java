
package gestcourierclient.entities;

import java.io.Serializable;

public class Login implements Serializable{
    private String pseudo;
    private String password;
    
    public Login(){
        super();
    }
    public Login(String pseudo,String password){
        this.pseudo = pseudo;
        this.password = password;
    }
    public String getPseudo(){
        return this.pseudo;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPseudo(String pseudo){
       this.pseudo = pseudo;
    }
    public void setPassword(String password){
        this.password = password;
    }
}

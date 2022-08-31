
package gestcourierclient.entities;

public class Utilisateur {
    private String userNum;
    private String userName;
    private String userPseudo;
    private String userPassword;
    private String userRule;

    public Utilisateur() {
        super();
    }

    public Utilisateur(String userNum,String userName, String userPseudo, String userPassword, String userRule) {
        this.userNum=userNum;
        this.userName = userName;
        this.userPseudo = userPseudo;
        this.userPassword = userPassword;
        this.userRule = userRule;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRule() {
        return userRule;
    }

    public void setUserRule(String userRule) {
        this.userRule = userRule;
    }

    public String getUserPseudo() {
        return userPseudo;
    }

    public void setUserPseudo(String userPseudo) {
        this.userPseudo = userPseudo;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}

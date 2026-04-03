package it.mapsgroup.gzoom.mybatis.dto;

public class ContactMechEx extends ContactMech {
    private UserLogin userLogin;

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }
}

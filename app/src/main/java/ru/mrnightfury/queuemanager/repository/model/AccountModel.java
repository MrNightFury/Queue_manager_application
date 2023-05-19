package ru.mrnightfury.queuemanager.repository.model;

public class AccountModel {
    private static AccountModel instance;
    public static AccountModel getInstance() {
        if (instance == null) {
            instance = new AccountModel();
        }
        return instance;
    }

    private String login = null;
    private String password = null;
    private String token = null;

    public AccountModel() {

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean hasAccount() {
        return login != null;
    }
}

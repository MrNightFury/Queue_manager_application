package ru.mrnightfury.queuemanager.repository.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserResponse;

public class AccountModel {
    private static MutableLiveData<AccountModel> instance;
    public static LiveData<AccountModel> getInstance() {
        if (instance == null) {
            instance = new MutableLiveData<>(new AccountModel()) ;
        }
        return instance;
    }

    private String login = null;
    private String password = null;
    private String username = null;
    @Deprecated
    private String token = null;

    private boolean loaded = false;

    public AccountModel() {

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Deprecated
    public String getToken() {
        return token;
    }

    public void setAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Deprecated
    public void setToken(String token) {
        this.token = token;
    }

    public boolean hasAccount() {
        return login != null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserResponse(UserResponse user) {
        this.username = user.getUsername();
        instance.setValue(instance.getValue());
        loaded = true;
        Log.i("AM", "Username set");
    }

    public boolean isLoaded() {
        return loaded;
    }
}

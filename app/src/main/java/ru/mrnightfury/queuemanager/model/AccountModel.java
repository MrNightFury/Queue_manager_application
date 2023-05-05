package ru.mrnightfury.queuemanager.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import kotlin.jvm.internal.Lambda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.model.networkAPI.LoginRequest;
import ru.mrnightfury.queuemanager.model.networkAPI.NetworkService;
import ru.mrnightfury.queuemanager.model.networkAPI.QueueManagerAPI;
import ru.mrnightfury.queuemanager.model.networkAPI.Result;

public class AccountModel {
    private SharedPreferences sharedPrefs;
    private String login;
    private String password;
    private String token;
    private QueueManagerAPI API;

    private static AccountModel instance = null;
    private static String loginKey = "login_key";
    private static String passwordKey = "password_key";
    private static String tokenKey = "token_key";

    public static AccountModel getInstance() {
        if (instance == null) {
            instance = new AccountModel();
        }
        return instance;
    }

    public AccountModel() {
        API = NetworkService.getInstance()
                .getJSONApi();
    }

    public void load (Context context){
        sharedPrefs = context.getApplicationContext()
                .getSharedPreferences("queue_manager.account", Context.MODE_PRIVATE);
        login = sharedPrefs.getString(loginKey, null);
        password = sharedPrefs.getString(passwordKey, null);
    }

    public boolean hasAccount(){
        return login != null && password != null;
    }

    public void setAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void saveAccount() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(loginKey, login);
        editor.putString(passwordKey, password);
        editor.apply();
    }

    public void checkConnection(Runnable onSuccess, Runnable onFailure) {
        API.checkConnection()
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        onSuccess.run();
                    }
                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        onFailure.run();
                    }
                });
    };

    public interface onResultGetCallback<E> {
        void onResult (E result);
    }
    public interface onFailureCallback<E> {
        void onFailure(Call<E> call, Throwable t);
    }

    public void login(onResultGetCallback<Result> onResultGetCallback, onFailureCallback<Result> onFailureCallback) {
        API.getJWT(new LoginRequest(login, password)).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().isSuccess()) {
                    token = response.body().getMessage();
                }
                onResultGetCallback.onResult(response.body());
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                onFailureCallback.onFailure(call, t);
            }
        });
    }

    public interface OnCheckCallback {
        void onSuccess();
        void onError();
    }
}

package ru.mrnightfury.queuemanager.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.LoginRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkService;
import ru.mrnightfury.queuemanager.repository.networkAPI.QueueManagerAPI;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.Result;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserResponse;

@Deprecated
public class AccountModel {
//    private static final String TAG = "AM";
//    private SharedPreferences sharedPrefs;
//    private String login;
//    private String password;
//    private String token;
//
//    private User user;
//    private final QueueManagerAPI API;

//    private static AccountModel instance = null;
//    private static final String loginKey = "login_key";
//    private static final String passwordKey = "password_key";
//    private static String tokenKey = "token_key";

//    public static AccountModel getInstance() {
//        if (instance == null) {
//            instance = new AccountModel();
//        }
//        return instance;
//    }

//    public AccountModel() {
//        API = NetworkService.getInstance()
//                .getJSONApi();
//    }

    public void load (Context context) {
//        sharedPrefs = context.getApplicationContext()
//                .getSharedPreferences("queue_manager.account", Context.MODE_PRIVATE);
//        login = sharedPrefs.getString(loginKey, null);
//        password = sharedPrefs.getString(passwordKey, null);
    }

    public boolean hasAccount() {
//        Log.i("AM", (Boolean.valueOf(login != null && password != null)).toString() + login + password);
////        return login != null && password != null;
        return false;
    }

    public void saveAccount() {
//        SharedPreferences.Editor editor = sharedPrefs.edit();
//        editor.putString(loginKey, login);
//        editor.putString(passwordKey, password);
//        editor.apply();
//        Log.i(TAG, "Account saved");
//        Log.i(TAG, sharedPrefs.getString(loginKey, "failed"));
    }

    public void checkConnection(Runnable onSuccess, Runnable onFailure) {
//        API.checkConnection()
//                .enqueue(new Callback<Object>() {
//                    @Override
//                    public void onResponse(Call<Object> call, Response<Object> response) {
//                        onSuccess.run();
//                    }
//                    @Override
//                    public void onFailure(Call<Object> call, Throwable t) {
//                        onFailure.run();
//                    }
//                });
    };

    public interface onResultGetCallback<E> {
        void onResult (E result);
    }
    public interface onFailureCallback<E> {
        void onFailure(Call<E> call, Throwable t);
    }

    public void login(onResultGetCallback<Result> onResultGetCallback, onFailureCallback<Result> onFailureCallback) {
//        API.getJWT(new LoginRequest(login, password)).enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//                if (response.body().isSuccess()) {
//                    token = response.body().getMessage();
//                    saveAccount();
//                }
//                onResultGetCallback.onResult(response.body());
//            }
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                onFailureCallback.onFailure(call, t);
//            }
//        });
    }

    public void getAccount(onResultGetCallback<UserResponse> onResultGetCallback, onFailureCallback<UserResponse> onFailureCallback) {
//        if (user == null) {
//            API.getUser(login).enqueue(new Callback<>() {
//                @Override
//                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                    onResultGetCallback.onResult(response.body());
//                    user = response.body();
//                }
//                @Override
//                public void onFailure(Call<UserResponse> call, Throwable t) {
//                    onFailureCallback.onFailure(call, t);
//                }
//            });
//        } else {
//            onResultGetCallback.onResult(user);
//        }
    }

    public interface OnCheckCallback {
//        void onSuccess();
//        void onError();
    }
}

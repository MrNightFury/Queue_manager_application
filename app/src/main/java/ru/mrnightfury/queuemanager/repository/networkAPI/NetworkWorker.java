package ru.mrnightfury.queuemanager.repository.networkAPI;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mrnightfury.queuemanager.model.User;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.LoginRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.Result;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserCreateRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserResponse;

public class NetworkWorker {
    private static String TAG = "NW";
    private static NetworkWorker instance;
    private static String[] URLs = {
            "http://10.0.2.2:8000/", // Для эмулятора
            "http://192.168.1.128:8000/"
    };

    public static NetworkWorker getInstance() {
        if (instance == null) {
            instance = new NetworkWorker();
        }
        return instance;
    }

    private QueueManagerAPI API;
    private NetworkWorker() {

    }

    public void checkConnection(Runnable onSuccess, Runnable onFailure) {
        NetworkService.getInstance().connect(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                API = NetworkService.getInstance().getJSONApi();
                onSuccess.run();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                onFailure.run();
            }
        }, URLs);
    }

    public void logIn(AccountModel account, OnSuccess<Result> onSuccess, OnFailure<Result> onFailure) {
        API.getJWT(new LoginRequest(account.getLogin(), account.getPassword()))
                .enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                onFailure.onFailure(call, t);
            }
        });
    }

    public void createAccount(UserCreateRequest request, OnSuccess<Result> onSuccess, OnFailure<Result> onFailure) {
        Log.i(TAG, "Create Account request");
        API.createUser(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                onFailure.onFailure(call, t);
            }
        });
    }

    public void getUser(String login, OnSuccess<UserResponse> onSuccess, OnFailure<UserResponse> onFailure) {
        Log.i(TAG, "User get request");
        API.getUser(login).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.i(TAG, "Login get response");
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                onFailure.onFailure(call, t);
            }
        });
    }

    public interface OnSuccess<E> {
        void onResult (E result);
    }
    public interface OnFailure<E> {
        void onFailure(Call<E> call, Throwable t);
    }
}

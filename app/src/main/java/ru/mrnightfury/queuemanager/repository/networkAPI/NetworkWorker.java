package ru.mrnightfury.queuemanager.repository.networkAPI;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.LoginRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.Result;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserCreateRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserResponse;

public class NetworkWorker {
    private static String TAG = "NW";
    private static NetworkWorker instance;
    private static String[] URLs = {
            "http://10.0.2.2:8000/", // Для эмулятора
            "http://192.168.1.128:8000/",
            "http://192.168.137.216:8000"
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
        NetworkService.getInstance().connect(new Callback<>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
//                Log.i("dasdSA", "UYGGHYUYGYU");
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
                Log.i(TAG, "JWT get response");
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.i(TAG, "Failed to get JWT");
                onFailure.onFailure(call, t);
            }
        });
    }

    public void createAccount(UserCreateRequest request, OnSuccess<Result> onSuccess, OnFailure<Result> onFailure) {
        Log.i(TAG, "Create Account request");
        API.createUser(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.i(TAG, "Create Account response");
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.i(TAG, "Failed to Post Create Account");
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
                Log.i(TAG, "Failed to get User");
                onFailure.onFailure(call, t);
            }
        });
    }

    public void loadQueues(OnSuccess<QueueResponse[]> onSuccess, OnFailure<QueueResponse[]> onFailure) {
        Log.i(TAG, "Queues get request");
        API.getQueues().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<QueueResponse[]> call, Response<QueueResponse[]> response) {
                Log.i(TAG, "Queue get response");
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<QueueResponse[]> call, Throwable t) {
                Log.i(TAG, "Failed to get Queues");
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

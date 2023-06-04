package ru.mrnightfury.queuemanager.repository.networkAPI;


import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;
import ru.mrnightfury.queuemanager.repository.AccountRepository;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.LoginRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueCreateRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueDeleteRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueuePutBody;
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
            "http://192.168.137.216:8000/",
            "http://192.168.149.245:8000"
    };


    public static NetworkWorker getInstance() {
        if (instance == null) {
            instance = new NetworkWorker();
        }
        return instance;
    }

    private String connectedURL;
    private QueueManagerAPI API;
    private OkSse oksse;
    private String token;
    private LiveData<AccountModel> account = AccountModel.getInstance();
    private NetworkWorker() {
        oksse = new OkSse();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void checkConnection(Runnable onSuccess, Runnable onFailure) {
        NetworkService.getInstance().connect(new Callback<>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
//                Log.i("dasdSA", "UYGGHYUYGYU");
                API = NetworkService.getInstance().getJSONApi();
                connectedURL = call.request().url().toString();
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
                if (response.body().isSuccess()) {
                    setToken(response.body().getMessage());
                }
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.i(TAG, "Failed to get JWT");
                AccountRepository.getInstance().incorrectAccount();
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
        Log.i(TAG, "User " + login + " get request");
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
                Log.i(TAG, "Queues get response");
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<QueueResponse[]> call, Throwable t) {
                Log.i(TAG, "Failed to get Queues: " + t.getMessage());
                onFailure.onFailure(call, t);
            }
        });
    }

    public void loadQueue(String queueId, OnSuccess<QueueResponse> onSuccess, OnFailure<QueueResponse> onFailure) {
        Log.i(TAG, "Queue ret request");
        API.getQueue(queueId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {
                Log.i(TAG, "Queue get response");
                onSuccess.onResult(response.body());
            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {
                Log.i(TAG, "Failed to get Queue");
                onFailure.onFailure(call, t);
            }
        });
    }

    public void putQueue(String queueId, String command,
                         OnSuccess<Result> onSuccess, OnFailure<Result> onFailure) {
        Log.i(TAG, "Queue put: " + command);
        API.putQueue(queueId, this.token, new QueuePutBody(command)).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() != 401) {
                    onSuccess.onResult(response.body());
                } else {
                    logIn(account.getValue(),
                            result -> {
                                if (result.isSuccess()) {
                                    putQueue(queueId, command, onSuccess, onFailure);
                                }
                            },
                            (c, t) -> {
                                onFailure.onFailure(call, t);
                            }
                    );
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                onFailure.onFailure(call, t);
            }
        });
    }


    public void createQueue (QueueCreateRequest queue, OnSuccess<Result> onSuccess, OnFailure<Result> onFailure) {
        Log.i(TAG, "Queue create: " + queue.toString());
        API.createQueue(this.token, queue).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() != 401) {
                    onSuccess.onResult(response.body());
                } else {
                    Log.i(TAG, "Deprecated JWT...");
                    logIn(account.getValue(),
                            result -> {
                                if (result.isSuccess()) {
                                    createQueue(queue, onSuccess, onFailure);
                                }
                            },
                            (c, t) -> {
                                onFailure.onFailure(call, t);
                            }
                    );
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                onFailure.onFailure(call, t);
            }
        });
    }

    public void deleteQueue (String id, OnSuccess<Result> onSuccess, OnFailure<Result> onFailure) {
        Log.i(TAG, "Queue create: ");
        API.deleteQueue(this.token, new QueueDeleteRequest(id)).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() != 401) {
                    onSuccess.onResult(response.body());
                } else {
                    logIn(account.getValue(),
                            result -> {
                                if (result.isSuccess()) {
                                    deleteQueue(id, onSuccess, onFailure);
                                }
                            },
                            (c, t) -> {
                                onFailure.onFailure(call, t);
                            }
                    );
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                onFailure.onFailure(call, t);
            }
        });
    }

    public ServerSentEvent watchQueue(String queueId, OnMessage listener){
        Request request = new Request.Builder()
                .url(connectedURL + "queue/" + queueId + "/subscribe")
                .build();
        return oksse.newServerSentEvent(request, new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, okhttp3.Response response) {
                Log.i("SSE", "SSE channel opened");
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                Log.i("SSE", message);
                listener.run(sse, id, event, message);
            }

            @WorkerThread
            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                // When a comment is received
            }

            @WorkerThread
            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                return true; // True to use the new retry time received by SSE
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, okhttp3.Response response) {
                return false;
            }

            @WorkerThread
            @Override
            public void onClosed(ServerSentEvent sse) {
                Log.i("SSE", "SSE channel closed");
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                return null;
            }
        });
//        return sse;
    }

    public interface OnSuccess<E> {
        void onResult (E result);
    }
    public interface OnFailure<E> {
        void onFailure(Call<E> call, Throwable t);
    }
    public interface OnMessage {
        public void run(ServerSentEvent sse, String id, String event, String message);
    }
}

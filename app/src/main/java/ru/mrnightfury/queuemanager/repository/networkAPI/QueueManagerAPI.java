package ru.mrnightfury.queuemanager.repository.networkAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.mrnightfury.queuemanager.model.Queue;
import ru.mrnightfury.queuemanager.model.User;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.LoginRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.Result;

public interface QueueManagerAPI {
    @GET("/user/{login}")
    public Call<User> getUser(@Path("login") String login);

    @GET("/queues")
    public Call<Queue[]> getQueues();

    @GET("/")
    public Call<Object> checkConnection();

    @POST("/login")
    public Call<Result> getJWT(@Body LoginRequest request);
}

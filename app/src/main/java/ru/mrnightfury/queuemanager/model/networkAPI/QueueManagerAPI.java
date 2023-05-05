package ru.mrnightfury.queuemanager.model.networkAPI;

import java.lang.reflect.Array;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.mrnightfury.queuemanager.model.Queue;
import ru.mrnightfury.queuemanager.model.User;

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

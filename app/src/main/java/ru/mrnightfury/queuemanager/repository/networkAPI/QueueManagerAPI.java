package ru.mrnightfury.queuemanager.repository.networkAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.mrnightfury.queuemanager.model.Queue;
import ru.mrnightfury.queuemanager.model.User;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.LoginRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.Result;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserCreateRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserResponse;

public interface QueueManagerAPI {
    @GET("/user/{login}")
    public Call<UserResponse> getUser(@Path("login") String login);

    @GET("/queues")
    public Call<Queue[]> getQueues();

    @GET("/")
    public Call<Object> checkConnection();

    @POST("/login")
    public Call<Result> getJWT(@Body LoginRequest request);

    @POST("/users")
    public Call<Result> createUser(@Body UserCreateRequest request);
}

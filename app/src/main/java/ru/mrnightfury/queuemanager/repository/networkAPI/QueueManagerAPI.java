package ru.mrnightfury.queuemanager.repository.networkAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.LoginRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueCreateRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueDeleteRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueuePutBody;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.Result;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserCreateRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserResponse;

public interface QueueManagerAPI {
    @GET("/user/{login}")
    public Call<UserResponse> getUser(@Path("login") String login);

    @GET("/")
    public Call<Object> checkConnection();

    @POST("/login")
    public Call<Result> getJWT(@Body LoginRequest request);

    @POST("/users")
    public Call<Result> createUser(@Body UserCreateRequest request);

    @GET("/queues")
    public Call<QueueResponse[]> getQueues();

    @GET("/queue/{id}")
    public Call<QueueResponse> getQueue(@Path("id") String queueId);

    @GET("/queue/{id}/check")
    public Call<Result> checkQueue(@Path("id") String queueId);

    @PUT("/queue/{id}")
    public Call<Result> putQueue(@Path("id") String queueId, @Header("authorization") String token,
                                 @Body QueuePutBody body);

    @POST("/queues")
    public Call<Result> createQueue(@Header("authorization") String token, @Body QueueCreateRequest body);

    @HTTP(method="DELETE", path="/queues", hasBody=true)
    public Call<Result> deleteQueue(@Header("authorization") String token, @Body QueueDeleteRequest request);
}

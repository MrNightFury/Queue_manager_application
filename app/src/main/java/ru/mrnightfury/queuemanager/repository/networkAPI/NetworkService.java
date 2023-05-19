package ru.mrnightfury.queuemanager.repository.networkAPI;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static String TAG = "NS";
    private Retrofit mRetrofit = null;
    private static final String BASE_URL = "http://ipservera:port/";

    private NetworkService() {

    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public QueueManagerAPI getJSONApi() {
        return mRetrofit.create(QueueManagerAPI.class);
    }

    public void connect(Callback<Object> callback, String[] URLs) {
        int[] counter = {0};
        for (String URL : URLs) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofit.create(QueueManagerAPI.class)
                    .checkConnection()
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Log.i(TAG, "Successfully connected to " + URL);
                            if (mRetrofit == null) {
                                mRetrofit = retrofit;
                                callback.onResponse(call, response);
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.i(TAG, "Failed to connected to " + URL);
                            counter[0]++;
                            if (counter[0] == URLs.length) {
                                callback.onFailure(call, t);
                            }
                        }
                    });
        }
    }
}

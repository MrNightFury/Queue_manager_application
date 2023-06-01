package ru.mrnightfury.queuemanager.repository.networkAPI.body;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Queue {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    @Nullable
    private String description;

    @SerializedName("config")
    @Expose
    private Config config;

    @SerializedName("queuedPeople")
    @Expose
    private UserState[] queuedPeople;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public Config getConfig() {
        return config;
    }

    public UserState[] getQueuedPeople() {
        return queuedPeople;
    }

    public static class Config {
        @SerializedName("owner")
        @Expose
        private String owner;
    }

    public static class UserState {
        @SerializedName("login")
        @Expose
        private String login;

        @SerializedName("frozen")
        @Expose
        @Nullable
        private Boolean frozen;

        public String getLogin() {
            return login;
        }

        @Nullable
        public Boolean isFrozen() {
            return frozen;
        }
    }
}

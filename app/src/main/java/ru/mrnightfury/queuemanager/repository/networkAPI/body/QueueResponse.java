package ru.mrnightfury.queuemanager.repository.networkAPI.body;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueueResponse {
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
        public Owner owner;

        public static class Owner {
            @SerializedName("login")
            @Expose
            public String login;

            @SerializedName("type")
            @Expose
            public String type;
        }
    }

    public static class UserState {
        @SerializedName("login")
        @Expose
        private String login;

        @SerializedName("frozen")
        @Expose
        @Nullable
        private Boolean frozen;

        @SerializedName("type")
        @Expose
        private String type;

        public String getType() {
            return type;
        }

        public String getLogin() {
            return login;
        }

        @Nullable
        public Boolean isFrozen() {
            return frozen;
        }
    }
}

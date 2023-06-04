package ru.mrnightfury.queuemanager.repository.networkAPI.body;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueueCreateRequest {
    @SerializedName("command")
    @Expose
    private String command = "create";

    @SerializedName("arguments")
    @Expose
    private Arguments arguments;

    public QueueCreateRequest(String name, String description, QueueCreateRequest.Arguments.Config config) {
        this.arguments = new Arguments(name, description, config);
    }

    public static class Arguments {
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("config")
        @Expose
        private QueueCreateRequest.Arguments.Config config;

        public static class Config {
            @SerializedName("accessType")
            @Expose
            @Nullable
            private String accessType;

            @SerializedName("length")
            @Expose
            @Nullable
            private Integer length;

            public Config(@Nullable String accessType, @Nullable Integer length) {
                this.accessType = accessType;
                this.length = length;
            }
        }

        public Arguments(String name, String description, QueueCreateRequest.Arguments.Config config) {
            this.name = name;
            this.description = description;
            this.config = config;
        }
    }

}

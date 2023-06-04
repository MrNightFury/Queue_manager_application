package ru.mrnightfury.queuemanager.repository.networkAPI.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueueDeleteRequest {
    @SerializedName("command")
    @Expose
    private String command;

    @SerializedName("arguments")
    @Expose
    private Arguments arguments;

    public QueueDeleteRequest(String id) {
        this.command = "delete";
        this.arguments = new Arguments(id);
    }

    public static class Arguments {
        @SerializedName("id")
        @Expose
        private String id;

        public Arguments(String id) {
            this.id = id;
        }
    }
}

package ru.mrnightfury.queuemanager.repository.networkAPI.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueuePutBody {
    @SerializedName("command")
    @Expose
    private String command;

    public QueuePutBody(String command) {
        this.command = command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static final String JOIN = "join";
    public static final String LEAVE = "leave";
    public static final String FREEZE = "freeze";
    public static final String POP = "pop";
}

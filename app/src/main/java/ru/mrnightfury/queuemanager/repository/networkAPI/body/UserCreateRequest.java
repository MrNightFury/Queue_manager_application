package ru.mrnightfury.queuemanager.repository.networkAPI.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCreateRequest {
    private class Arguments {
        @SerializedName("login")
        @Expose
        private String login;

        @SerializedName("password")
        @Expose
        private String password;

        @SerializedName("username")
        @Expose
        private String username;

        public Arguments(String login, String password, String username) {
            this.login = login;
            this.password = password;
            this.username = username;
        }
    }
    @SerializedName("command")
    @Expose
    private final String command = "create";

    @SerializedName("arguments")
    @Expose
    private final Arguments arguments;

    public UserCreateRequest(String login, String password, String username) {
        this.arguments = new Arguments(login, password, username);
    }

    public String getLogin() {
        return arguments.login;
    }

    public String getPassword() {
        return arguments.password;
    }

    public String getUsername() {
        return arguments.username;
    }

    public String getCommand() {
        return command;
    }

    public void setLogin(String login) {
        this.arguments.login = login;
    }

    public void setPassword(String password) {
        this.arguments.password = password;
    }

    public void setUsername(String username) {
        this.arguments.username = username;
    }
}

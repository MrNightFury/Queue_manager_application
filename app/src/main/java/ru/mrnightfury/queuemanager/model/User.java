package ru.mrnightfury.queuemanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class User {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("vk")
    @Expose
    private String vk;

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }
}

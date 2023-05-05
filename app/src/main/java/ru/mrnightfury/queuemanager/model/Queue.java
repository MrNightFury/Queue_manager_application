package ru.mrnightfury.queuemanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Queue {
    private static class Config {
        @SerializedName("owner")
        @Expose
        private String owner;
        @SerializedName("accessType")
        @Expose
        private AccessType accessType;
        @SerializedName("length")
        @Expose
        private int length;

        public Config(String owner, AccessType accessType, int length) {
            this.owner = owner;
            this.accessType = accessType;
            this.length = length;
        }

        public String getOwner() {
            return owner;
        }

        public AccessType getAccessType() {
            return accessType;
        }

        public int getLength() {
            return length;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public void setAccessType(AccessType accessType) {
            this.accessType = accessType;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("config")
    @Expose
    private Config config;
    @SerializedName("users")
    @Expose
    private User[] users;

    public Queue(String name, String description, Config config, User[] users) {
        this.name = name;
        this.description = description;
        this.config = config;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Config getConfig() {
        return config;
    }

    public User[] getUsers() {
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}

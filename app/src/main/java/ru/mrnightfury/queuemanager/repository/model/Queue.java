package ru.mrnightfury.queuemanager.repository.model;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

public class Queue {
    private String id;
    private String name;
    private String description;
    private QueueResponse.Config config;
    private ArrayList<Queue.User> queuedPeople = new ArrayList<>();

    public static class User {
        private String login;
        private Boolean frozen;
        private String type;
        private String username;

        public User(QueueResponse.UserState u) {
            this.username = u.getUsername();
            this.login = u.getLogin();
            this.frozen = u.isFrozen();
            this.type = u.getType();
        }

        public String getLogin() {
            return login;
        }

        public Boolean getFrozen() {
            return frozen;
        }

        public String getType() {
            return type;
        }

        public String getUsername() {
            return username;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public void setFrozen(Boolean frozen) {
            this.frozen = frozen;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public Queue(QueueResponse q) {
        this.id = q.getId();
        this.config = q.getConfig();
        this.name = q.getName();
        this.description = q.getDescription();
        for (QueueResponse.UserState u : q.getQueuedPeople()) {
            this.queuedPeople.add(new User(u));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public QueueResponse.Config getConfig() {
        return config;
    }

    public ArrayList<User> getQueuedPeople() {
        return queuedPeople;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConfig(QueueResponse.Config config) {
        this.config = config;
    }

    public void setQueuedPeople(ArrayList<User> queuedPeople) {
        this.queuedPeople = queuedPeople;
    }
}

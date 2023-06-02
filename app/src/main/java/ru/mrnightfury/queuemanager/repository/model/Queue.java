package ru.mrnightfury.queuemanager.repository.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

public class Queue {
    private String id;
    private String name;
    private String description;
    private QueueResponse.Config config;
    private MutableLiveData<ArrayList<Queue.User>> queuedPeople = new MutableLiveData<>();

    public Queue(QueueResponse q) {
        this.id = q.getId();
        this.config = q.getConfig();
        this.name = q.getName();
        this.description = q.getDescription();
        this.queuedPeople.setValue(new ArrayList<>());
        for (QueueResponse.UserState u : q.getQueuedPeople()) {
            this.queuedPeople.getValue().add(new User(u));
        }
    }

    public static class User {
        private String login;
        private Boolean frozen;
        private String type;
        private String username;

        public User(QueueResponse.UserState u) {
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

    public LiveData<ArrayList<User>> getQueuedPeople() {
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

    public void notifyListUpdate() {
        this.queuedPeople.setValue(this.queuedPeople.getValue());
    }

//    public void setQueuedPeople(ArrayList<User> queuedPeople) {
//        this.queuedPeople = queuedPeople;
//    }
}

package ru.mrnightfury.queuemanager.repository.model;

import java.util.HashMap;

public class UsernameCache {
    HashMap<String, String> map = new HashMap<>();

    public void setUsername(String login, String username) {
        map.put(login, username);
    }

    public String getUsername(String login) {
        return map.get(login);
    }
}

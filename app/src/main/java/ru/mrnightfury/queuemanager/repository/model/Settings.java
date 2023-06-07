package ru.mrnightfury.queuemanager.repository.model;

import androidx.annotation.NonNull;

public class Settings {
    private Boolean isServiceEnabled;

    public Settings setServiceEnabled(Boolean serviceEnabled) {
        isServiceEnabled = serviceEnabled;
        return this;
    }

    public Boolean isServiceEnabled() {
        return isServiceEnabled;
    }

    @NonNull
    @Override
    public String toString() {
        return "Settings{" +
                "isServiceEnabled=" + isServiceEnabled +
                '}';
    }
}
package ru.mrnightfury.queuemanager.repository.model;

public enum LoginStates {
    NONE,
    CONNECTION_FAILED,
    CONNECTED,

    LOGGED,
    INCORRECT_LOGIN_OR_PASSWORD,
    NOT_FOUND,
    LOGGING
}

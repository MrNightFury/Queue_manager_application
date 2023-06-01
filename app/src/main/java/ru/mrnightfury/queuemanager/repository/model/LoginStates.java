package ru.mrnightfury.queuemanager.repository.model;

public enum LoginStates {
    NONE,
    CONNECTION_CHECKING,
    CONNECTION_FAILED,
    CONNECTED,
    WAITING_FOR_USER,
    EXIT,

    LOGGED,
    INCORRECT_LOGIN_OR_PASSWORD,
    NOT_FOUND,
    LOGGING
}

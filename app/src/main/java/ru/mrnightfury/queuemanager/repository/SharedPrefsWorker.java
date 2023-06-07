package ru.mrnightfury.queuemanager.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mrnightfury.queuemanager.repository.model.Settings;

public class SharedPrefsWorker {
    private static SharedPrefsWorker instance;
    public static SharedPrefsWorker getInstance() {
        if (instance == null) {
            throw new RuntimeException("Getting SharedPrefsWorker without initialization");
        }
        return instance;
    }
    public static void init(Context context) {
        instance = new SharedPrefsWorker(context);
    }

    private static final String login_key = "login_key";
    private static final String password_key = "password_key";
    private static final String token_key = "token_key";

    private final SharedPreferences sharedPrefs;
    private SharedPrefsWorker(Context context) {
        sharedPrefs = context.getSharedPreferences(
                "queue_manager.app", Context.MODE_PRIVATE);
    }

    public String getLogin(){
        return sharedPrefs.getString(login_key, null);
    }

    public String getPassword(){
        return sharedPrefs.getString(password_key, null);
    }

    public String getToken(){
        return sharedPrefs.getString(token_key, null);
    }

    public void saveAccount(String login, String password) {
        sharedPrefs.edit()
                .putString(login_key, login)
                .putString(password_key, password)
                .apply();
    }

    public void saveToken(String token) {
        sharedPrefs.edit()
                .putString(token_key, token)
                .apply();
    }

    @Deprecated
    public SharedPreferences getSharedPrefs() {
        return sharedPrefs;
    }

    private static final String settingsKeyPrefix = "settings";
    public Settings getSettings() {
        return new Settings()
                .setServiceEnabled(sharedPrefs.getBoolean(settingsKeyPrefix + "_serviceEnabled", false));
    }

    public void saveSettings(Settings settings) {
        sharedPrefs.edit()
                .putBoolean(settingsKeyPrefix + "_serviceEnabled", settings.isServiceEnabled())
                .apply();
    }

    public void deleteAccount() {
        sharedPrefs.edit()
                .remove(login_key)
                .remove(password_key)
                .apply();
    }
}

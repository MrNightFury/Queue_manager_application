package ru.mrnightfury.queuemanager.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.mrnightfury.queuemanager.repository.model.Settings;

public class SettingsRepository {
    private static SettingsRepository instance;
    private static final String TAG = "SettingsRepo";

    public static SettingsRepository getInstance() {
        if (instance == null) {
            instance = new SettingsRepository();

        }
        return instance;
    }

    private SharedPrefsWorker sharedPrefsWorker;
    private MutableLiveData<Settings> settings;

    public SettingsRepository() {
        sharedPrefsWorker = SharedPrefsWorker.getInstance();
        settings = new MutableLiveData<>(sharedPrefsWorker.getSettings());
        Log.i(TAG, settings.getValue().toString());
    }

    public LiveData<Settings> getSettings() {
        return settings;
    }

    public void notifySettingsChanged() {
        settings.setValue(settings.getValue());
        sharedPrefsWorker.saveSettings(settings.getValue());
    }
}

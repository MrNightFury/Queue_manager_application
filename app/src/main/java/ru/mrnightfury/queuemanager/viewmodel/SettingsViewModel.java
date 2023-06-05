package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mrnightfury.queuemanager.repository.SettingsRepository;
import ru.mrnightfury.queuemanager.repository.model.Settings;

public class SettingsViewModel extends ViewModel {
    private SettingsRepository repository;
    private LiveData<Settings> settings;

    public SettingsViewModel() {
        repository = SettingsRepository.getInstance();
        settings = repository.getSettings();
    }

    public LiveData<Settings> getSettings() {
        return settings;
    }

    public void notifySettingsChanged() {
        repository.notifySettingsChanged();
    }
}

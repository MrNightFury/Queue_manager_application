package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.mrnightfury.queuemanager.repository.SettingsRepository;
import ru.mrnightfury.queuemanager.repository.model.Settings;

public class SettingsViewModel extends ViewModel {
    private final SettingsRepository repository;
    private final LiveData<Settings> settings;

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

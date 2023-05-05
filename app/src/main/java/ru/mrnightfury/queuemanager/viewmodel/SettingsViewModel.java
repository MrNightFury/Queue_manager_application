package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    private MutableLiveData<Boolean> data = new MutableLiveData<>();

    public LiveData<Boolean> getData() {
        return data;
    }

    public void check(Boolean check) {
        this.data.setValue(check);
    }
}

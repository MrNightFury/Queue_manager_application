package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.mrnightfury.queuemanager.repository.AccountRepository;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;

public class AccountViewModel extends ViewModel {
    AccountRepository repository = AccountRepository.getInstance();

    LiveData<AccountModel> model = repository.getAccount();

    public LiveData<AccountModel> getAccount() {
        return model;
    }

    public void loadUser() {
        if (!model.getValue().isLoaded()) {
            repository.loadData();
        }
    }

    public void exit() {
        repository.exit();
    }

    public void logOut() {
        repository.logOut();
    }
}

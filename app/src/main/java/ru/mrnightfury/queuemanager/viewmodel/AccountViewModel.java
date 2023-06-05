package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.mrnightfury.queuemanager.repository.AccountRepository;
import ru.mrnightfury.queuemanager.repository.QueuesRepository;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;

public class AccountViewModel extends ViewModel {
    AccountRepository repository = AccountRepository.getInstance();
//    QueuesRepository rep =

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

//    public void watchUser() {
//        repository.watchUser();
//    }
//
//    public void close() {
//        repository.close();
//    }
}

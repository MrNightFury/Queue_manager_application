package ru.mrnightfury.queuemanager.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

import ru.mrnightfury.queuemanager.repository.QueuesRepository;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.model.Queue;

public class QueueViewModel extends ViewModel {
    private LiveData<AccountModel> account = AccountModel.getInstance();
    private QueuesRepository repository;
    private LiveData<Queue> chosenQueue;
    private MutableLiveData<ArrayList<Queue.User>> users = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Boolean> isUserInQueue = new MutableLiveData<>();

    public QueueViewModel() {
        this.repository = QueuesRepository.getInstance();
        this.chosenQueue = repository.getQueue();
//        this.users.setValue();
    }

    public LiveData<Queue> getChosenQueue() {
        return chosenQueue;
    }

    public LiveData<Boolean> getPeopleChangedTrigger() {
        return repository.getPeopleChangedTrigger();
    }

//    public void chooseQueue(String id) {
//        repository.chooseQueue(id);
//    }

    public void updateQueue() {
        repository.updateChosenQueue();
    }

    public void updatePeopleList() {
        users.getValue().clear();
        users.getValue().addAll(chosenQueue.getValue().getQueuedPeople());
        users.setValue(users.getValue());
        checkUserInQueue();
    }

    public LiveData<ArrayList<Queue.User>> getUsers() {
        return this.users;
    }

    public void subscribe() {
        repository.subscribe();
    }

    public void cancelSubscribe() {
        repository.cancelSubscribe();
    }

    public MutableLiveData<Boolean> getIsUserInQueue() {
        return isUserInQueue;
    }

    public void checkUserInQueue() {
        for (Queue.User u : users.getValue()) {
            if (Objects.equals(u.getLogin(), account.getValue().getLogin())) {
                isUserInQueue.setValue(true);
                return;
            }
        }
        isUserInQueue.setValue(false);
    }
}

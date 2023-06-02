package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.repository.QueuesRepository;
import ru.mrnightfury.queuemanager.repository.model.Queue;

public class QueueViewModel extends ViewModel {
    QueuesRepository repository;
    private LiveData<Queue> chosenQueue;
    private MutableLiveData<ArrayList<Queue.User>> users = new MutableLiveData<>(new ArrayList<>());

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
}

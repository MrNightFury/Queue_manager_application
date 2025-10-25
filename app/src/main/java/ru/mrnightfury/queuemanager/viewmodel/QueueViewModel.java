package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

import ru.mrnightfury.queuemanager.repository.QueuesRepository;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.model.Queue;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueuePutBody;

public class QueueViewModel extends ViewModel {
    private final LiveData<AccountModel> account = AccountModel.getInstance();
    private final QueuesRepository repository;
    private final LiveData<Queue> chosenQueue;
    private final MutableLiveData<ArrayList<Queue.User>> users = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Boolean> isUserInQueue = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isUserFrozen = new MutableLiveData<>(false);

    public QueueViewModel() {
        this.repository = QueuesRepository.getInstance();
        this.chosenQueue = repository.getQueue();
    }

    public LiveData<Queue> getChosenQueue() {
        return chosenQueue;
    }

    public LiveData<Boolean> getPeopleChangedTrigger() {
        return repository.getPeopleChangedTrigger();
    }

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

//    public void subscribe() {
//        repository.subscribe();
//    }

    public void cancelSubscribe() {
        repository.cancelSubscribe();
    }

    public LiveData<Boolean> getIsUserInQueue() {
        return isUserInQueue;
    }
    public LiveData<Boolean> getIsUserFrozen() {
        return isUserFrozen;
    }

    public void checkUserInQueue() {
        for (Queue.User u : users.getValue()) {
            if (Objects.equals(u.getLogin(), account.getValue().getLogin())) {
                isUserInQueue.setValue(true);
                isUserFrozen.setValue(u.getFrozen());
                return;
            }
        }
        isUserInQueue.setValue(false);
    }

    public void joinOrLeave() {
        repository.queuePut(isUserInQueue.getValue() ? QueuePutBody.LEAVE : QueuePutBody.JOIN);
    }

    public void freeze() {
        repository.queuePut(QueuePutBody.FREEZE);
    }

    public void pop() {
        repository.queuePut(QueuePutBody.POP);
    }

    public void deleteQueue() {
        repository.deleteQueue(chosenQueue.getValue().getId());
    }

    public LiveData<String> getQueueEditionState() {
        return repository.getQueueEditionState();
    }

    public LiveData<Boolean> getQueueLoadState() {
        return repository.getQueueLoadState();
    }

    public Boolean isFavourite() {
        return repository.isFavourite(chosenQueue.getValue().getId());
    }
}

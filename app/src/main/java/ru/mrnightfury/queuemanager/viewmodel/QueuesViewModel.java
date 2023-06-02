package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.repository.QueuesRepository;
import ru.mrnightfury.queuemanager.repository.model.Queue;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

public class QueuesViewModel extends ViewModel {
    public QueuesRepository repository;
    public LiveData<ArrayList<QueueResponse>> queues;
    public LiveData<Queue> chosenQueue;

    public QueuesViewModel() {
        repository = QueuesRepository.getInstance();
        queues = repository.getAvailableQueues();
        chosenQueue = repository.getQueue();
    }

    public LiveData<ArrayList<QueueResponse>> getQueues() {
        return queues;
    }

    public void chooseQueue(String id) {
        repository.chooseQueue(id);
    }

    public LiveData<Queue> getChosenQueue() {
        return chosenQueue;
    }

    public void update() {
        repository.loadQueues();
    }

    public void updateQueue() {
        repository.updateChosenQueue();
    }
}

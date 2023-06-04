package ru.mrnightfury.queuemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mrnightfury.queuemanager.repository.QueuesRepository;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueCreateRequest;

public class QueueCreationViewModel extends ViewModel {
    private QueuesRepository repository = QueuesRepository.getInstance();
//    public MutableLiveData<String> name = new MutableLiveData<>();
//    public MutableLiveData<String> description = new MutableLiveData<>();

    public void createQueue(String name, String description) {
        repository.createQueue(new QueueCreateRequest(name, description,
                new QueueCreateRequest.Arguments.Config(null, null)));
    }

    public LiveData<String> getQueueCreationState() {
        return repository.getQueueEditionState();
    }
}

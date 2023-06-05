package ru.mrnightfury.queuemanager.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.repository.QueuesRepository;
import ru.mrnightfury.queuemanager.repository.database.FavouriteEntity;
import ru.mrnightfury.queuemanager.repository.model.Queue;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

public class FavouriteViewModel extends ViewModel {
    private QueuesRepository repository;

    public FavouriteViewModel() {
        repository = QueuesRepository.getInstance();
        repository.loadFavourites();
    }

    public LiveData<ArrayList<QueueResponse>> getFavouriteQueues() {
        return repository.getFavouriteQueues();
    }

    public ArrayList<FavouriteEntity> getFavouriteEntities() {
        return repository.getFavouriteQueuesIds();
    }

    public void update(){
        repository.loadFavourites();
    }
}

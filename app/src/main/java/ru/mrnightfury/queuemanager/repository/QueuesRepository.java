package ru.mrnightfury.queuemanager.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkWorker;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.Queue;

public class QueuesRepository {
    private static QueuesRepository instance;
    private static String TAG = "QR";

    public static QueuesRepository getInstance() {
        if (instance == null) {
            instance = new QueuesRepository();
        }
        return instance;
    }

    private NetworkWorker worker;
    private MutableLiveData<ArrayList<Queue>> availableQueues = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Queue> chosenQueue = new MutableLiveData<>();

    private QueuesRepository() {
        worker = NetworkWorker.getInstance();
    }

    public void loadQueues() {
        loadQueues(() -> {});
    }
    public void loadQueues(Runnable run) {
        worker.loadQueues(
                queues -> {
                    availableQueues.setValue(new ArrayList<>(Arrays.asList(queues)));
                    run.run();
                },
                (call, t) -> {}
        );
    }

    public LiveData<ArrayList<Queue>> getAvailableQueues() {
        return availableQueues;
    }

    public void chooseQueue(String id) {
        chooseQueue(id, true);
    }
    public void chooseQueue(String id, boolean first) {
        for (Queue q : availableQueues.getValue()) {
            if (Objects.equals(q.getId(), id)) {
                chosenQueue.setValue(q);
                return;
            }
        }
        if (first) {
            loadQueues(() -> chooseQueue(id, false));
        } else {
            Log.i(TAG, "Queue not found");
        }
    }

//    @Nullable
//    private Queue getQueue(String id) {
//        for (Queue q : availableQueues.getValue()) {
//            if (Objects.equals(q.getId(), id)) {
//                return q;
//            }
//        }
//        return null;
//    }
}

package ru.mrnightfury.queuemanager.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ru.mrnightfury.queuemanager.repository.model.Queue;
import ru.mrnightfury.queuemanager.repository.model.UsernameCache;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkWorker;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

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
    private MutableLiveData<ArrayList<QueueResponse>> availableQueues = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Queue> chosenQueue = new MutableLiveData<>();
    UsernameCache cache = new UsernameCache();

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

    public LiveData<ArrayList<QueueResponse>> getAvailableQueues() {
        return availableQueues;
    }

    public LiveData<Queue> getQueue() {
        return this.chosenQueue;
    }

    public void chooseQueue(String id) {
        chooseQueue(id, true);
    }
    public void chooseQueue(String id, boolean first) {
        for (QueueResponse q : availableQueues.getValue()) {
            if (Objects.equals(q.getId(), id)) {
                chosenQueue.setValue(new Queue(q));
                loadUsernames();
                updateChosenQueue();
                return;
            }
        }
        if (first) {
            loadQueues(() -> chooseQueue(id, false));
        } else {
            Log.i(TAG, "Queue not found");
        }
    }

    public void updateChosenQueue() {
        worker.loadQueue(chosenQueue.getValue().getId(),
                q -> {
                    Log.i(TAG, "Chosen queue updated");
                    chosenQueue.setValue(new Queue(q));
                },
                (call, t) -> {}
        );
    }

    public void updateChosenQueuePeopleList() {
        worker.loadQueue(chosenQueue.getValue().getId(),
                q -> {
                    Queue newQ = new Queue(q);
                    chosenQueue.getValue().getQueuedPeople().setValue(newQ.getQueuedPeople().getValue());
                },
                (call, t) -> {});
    }

    public void loadUsernames() {
        for (Queue.User u : chosenQueue.getValue().getQueuedPeople().getValue()) {
            String username = cache.getUsername(u.getLogin());
            Log.i(TAG, u.getLogin() + "-" + username);
            if (username != null) {
                u.setUsername(username);
            } else {
                worker.getUser(u.getLogin(),
                        result -> {
                            cache.setUsername(u.getLogin(), result.getUsername());
                            u.setUsername(result.getUsername());
                            chosenQueue.getValue().notifyListUpdate();
                        },
                        (call, t) -> {});
            }
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

package ru.mrnightfury.queuemanager.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.here.oksse.ServerSentEvent;

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
    private MutableLiveData<Boolean> peopleChangedTrigger = new MutableLiveData<>();
    private UsernameCache cache = new UsernameCache();
    ServerSentEvent queueSSE;

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

    public Queue c() {
        return chosenQueue.getValue();
    }

    public void chooseQueue(String id) {
        chooseQueue(id, true);
    }
    public void chooseQueue(String id, boolean first) {
        for (QueueResponse q : availableQueues.getValue()) {
            if (Objects.equals(q.getId(), id)) {
                chosenQueue.setValue(new Queue(q));
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
                    loadUsernames();
                },
                (call, t) -> {}
        );
    }

    public LiveData<Boolean> getPeopleChangedTrigger() {
        return peopleChangedTrigger;
    }

    public void updateChosenQueuePeopleList() {
        worker.loadQueue(chosenQueue.getValue().getId(),
                q -> {
                    Queue newQ = new Queue(q);
                    chosenQueue.getValue().setQueuedPeople(newQ.getQueuedPeople());
                    loadUsernames();
                    peopleChangedTrigger.setValue(true);
                },
                (call, t) -> {});
    }

    public void loadUsernames() {
        Log.i(TAG, "Loading usernames");
        for (
//                int i = 0; i < chosenQueue.getValue().getQueuedPeople().size(); i++) {
                Queue.User u : chosenQueue.getValue().getQueuedPeople()) {
//            final int index = i;
//            Queue.User u = chosenQueue.getValue().getQueuedPeople().get(i);
            String username = cache.getUsername(u.getLogin());
//            Log.i(TAG, u.getLogin() + "-" + username);
//            chosenQueue.getValue().getQueuedPeople().get(i).setUsername("ASD");
            if (username != null) {
                u.setUsername(username);
            } else if (!Objects.equals(u.getType(), "NOT_LOGGED")) {
                worker.getUser(u.getLogin(),
                        result -> {
                            cache.setUsername(u.getLogin(), result.getUsername());
                            u.setUsername(result.getUsername());
//                            Log.i(TAG, result.getUsername() + "-" + u.getUsername() + "-" + cache.getUsername(u.getLogin()) + "-" + chosenQueue.getValue().getQueuedPeople().get(index).getUsername());
                            peopleChangedTrigger.setValue(true);
//                            chosenQueue.getValue().notifyListUpdate();
//                            for (Queue.User uu : chosenQueue.getValue().getQueuedPeople()) {
//                                Log.i("ASD", uu.getUsername());
//                            }
                        },
                        (call, t) -> {});
            }
        }

    }

    public void subscribe() {
        queueSSE = worker.watchQueue(chosenQueue.getValue().getId(), (sse, id, event, message) -> {
            updateChosenQueuePeopleList();
//            Log.i(TAG, "Queue updated");
        });
    }

    public void cancelSubscribe() {
        queueSSE.close();
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

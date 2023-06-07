package ru.mrnightfury.queuemanager.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.here.oksse.ServerSentEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ru.mrnightfury.queuemanager.repository.database.FavouriteDatabase;
import ru.mrnightfury.queuemanager.repository.database.FavouriteEntity;
import ru.mrnightfury.queuemanager.repository.model.Queue;
import ru.mrnightfury.queuemanager.repository.model.UsernameCache;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkWorker;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueCreateRequest;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

public class QueuesRepository {
    private static QueuesRepository instance;
    private static final String TAG = "QR";

    public static QueuesRepository getInstance() {
        if (instance == null) {
            instance = new QueuesRepository();
        }
        return instance;
    }

    private final NetworkWorker worker;
    private FavouriteDatabase db;
    private final MutableLiveData<ArrayList<QueueResponse>> availableQueues = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Queue> chosenQueue = new MutableLiveData<>();
    private final MutableLiveData<Boolean> peopleChangedTrigger = new MutableLiveData<>();
    private final MutableLiveData<String> queueEditionState = new MutableLiveData<>("None");
    private final MutableLiveData<Boolean> chosenQueueIsLoaded = new MutableLiveData<>();

    private ArrayList<FavouriteEntity> favouriteQueuesIds = new ArrayList<>();
    private final MutableLiveData<ArrayList<QueueResponse>> favouriteQueues = new MutableLiveData<>(new ArrayList<>());

    private final UsernameCache cache = new UsernameCache();
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
    public LiveData<String> getQueueEditionState() {
        return queueEditionState;
    }

    @Deprecated
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
                subscribe();
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
        for (Queue.User u : chosenQueue.getValue().getQueuedPeople()) {
            String username = cache.getUsername(u.getLogin());
            if (username != null) {
                u.setUsername(username);
            } else if (!Objects.equals(u.getType(), "NOT_LOGGED") && !Objects.equals(u.getType(), "VK")) {
                worker.getUser(u.getLogin(),
                        result -> {
                            cache.setUsername(u.getLogin(), result.getUsername());
                            u.setUsername(result.getUsername());
                            peopleChangedTrigger.setValue(true);
                        },
                        (call, t) -> {});
            }
        }
    }

    public LiveData<Boolean> getQueueLoadState() {
        return chosenQueueIsLoaded;
    }

    public void subscribe() {
        queueSSE = worker.watchQueue(chosenQueue.getValue().getId(), (sse, id, event, message) -> {
            if (!Objects.equals(message, "{\"op\":\"delete\"}")) {
                updateChosenQueuePeopleList();
            } else {
                chosenQueue.postValue(null);
            }
        });
    }

    public void cancelSubscribe() {
        queueSSE.close();
    }

    public void queuePut(String command) {
        worker.putQueue(chosenQueue.getValue().getId(), command,
                result -> {},
                (call, t) -> {}
        );
    }

    public void createQueue(QueueCreateRequest queue) {
        worker.createQueue(queue,
                result -> {
                    if (result.isSuccess()) {
                        Log.i(TAG, "Successful created queue");
                        loadQueues(() -> {
                            chooseQueue(result.getMessage());
                            queueEditionState.setValue("Success");
                        });
                    } else {
                        Log.i(TAG, result.getMessage());
                        queueEditionState.setValue(result.getMessage());
                    }
                },
                (call, t) -> {
                    queueEditionState.setValue("Request error");
                });
    }

    public void deleteQueue(String id) {
        worker.deleteQueue(id,
                result -> {
                    if (result.isSuccess()) {
                        chosenQueue.setValue(null);
                        loadQueues();
                        queueEditionState.setValue("Deleted");
                    } else {
                        Log.i(TAG, result.getMessage() == null ? "null": result.getMessage());
                        queueEditionState.setValue("Success");
                    }
                },
                (call, t) -> {
                    queueEditionState.setValue("Request error");
                });
    }

    public void init(Context context) {
        db = FavouriteDatabase.getDatabase(context);
        new Thread(() -> {
            favouriteQueuesIds = new ArrayList<>(Arrays.asList(db.favouriteDao().getFavourite()));
            loadFavourites();
        }).start();
    }


    private int counter;
    private int count;
    public void loadFavourites() {
        favouriteQueues.getValue().clear();
        count = favouriteQueuesIds.size();
        counter = 0;
        for (FavouriteEntity e : favouriteQueuesIds) {
            worker.checkQueue(e.getQueueId(),
                    result -> {
                        if (result.isSuccess()) {
                            loadFavourite(e.getQueueId());
                        } else if (Objects.equals(result.getMessage(), "Queue does not exist")) {
                            new Thread(() -> {
                                db.favouriteDao().removeFromFavourite(e);
                                counter++;
                            }).start();
                        }
                    },
                    (call, t) -> {}
            );
        }
    }

    public void loadFavourite(String id) {
        worker.loadQueue(id,
                result -> {
                    favouriteQueues.getValue().add(result);
                    Log.i("NOW", String.valueOf(favouriteQueues.getValue().size()));
                    counter++;
                    if (counter >= count) {
                        notifyFavouriteQueuesChanged();
                    }
                },
                (call, t) -> {});
    }

    public void notifyFavouriteQueuesChanged() {
        Log.i("NOTIFY", String.valueOf(favouriteQueues.getValue().size()));
        favouriteQueues.postValue(favouriteQueues.getValue());
        Log.i("NOTIFY", String.valueOf(favouriteQueues.getValue().size()));
    }

    public LiveData<ArrayList<QueueResponse>> getFavouriteQueues() {
        return favouriteQueues;
    }

    public void addToFavourite(String id) {
        favouriteQueuesIds.add(new FavouriteEntity(id));
        new Thread(() -> {
            db.favouriteDao().addToFavourite(new FavouriteEntity(id));
            loadFavourites();
        }).start();
    }

    public ArrayList<FavouriteEntity> getFavouriteQueuesIds() {
        return favouriteQueuesIds;
    }

    public Boolean isFavourite(String id) {
        for (FavouriteEntity e : favouriteQueuesIds) {
            if (Objects.equals(e.getQueueId(), id)) {
                return true;
            }
        }
        return false;
    }

    public void deleteFromFavourites(String id) {
        FavouriteEntity forDelete = null;
        for (FavouriteEntity e : favouriteQueuesIds) {
            if (Objects.equals(e.getQueueId(), id)) {
                forDelete = e;
                break;
            }
        }
        if (forDelete != null) {
            FavouriteEntity e = forDelete;
            favouriteQueuesIds.remove(e);
            new Thread(() -> {
                db.favouriteDao().removeFromFavourite(e);
                loadFavourites();
            }).start();
        }
    }
}

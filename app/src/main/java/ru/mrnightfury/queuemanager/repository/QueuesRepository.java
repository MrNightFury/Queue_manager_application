package ru.mrnightfury.queuemanager.repository;

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
    private Queue[] availableQueues;

    private QueuesRepository() {
        worker = NetworkWorker.getInstance();
    }

    private void loadQueues() {
        worker.loadQueues(
                queues -> {
                    availableQueues = queues;
                },
                (call, t) -> {}
        );
    }
}

package ru.mrnightfury.queuemanager.repository.model;

import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

@Deprecated
public class QueueManager {
    private static QueueManager manager;
    public static QueueManager getInstance() {
        if (manager == null) {
            manager = new QueueManager();
        }
        return manager;
    }

    private QueueResponse[] queues;
    public void setQueues(QueueResponse[] queues) {
        this.queues = queues;
    }
}

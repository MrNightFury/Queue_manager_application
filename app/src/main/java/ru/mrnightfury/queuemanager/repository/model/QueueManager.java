package ru.mrnightfury.queuemanager.repository.model;

import ru.mrnightfury.queuemanager.repository.networkAPI.body.Queue;

public class QueueManager {
    private static QueueManager manager;
    public static QueueManager getInstance() {
        if (manager == null) {
            manager = new QueueManager();
        }
        return manager;
    }

    private Queue[] queues;
    public void setQueues(Queue[] queues) {
        this.queues = queues;
    }
}

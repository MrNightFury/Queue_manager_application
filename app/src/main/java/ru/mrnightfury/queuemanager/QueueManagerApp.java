package ru.mrnightfury.queuemanager;

import android.util.Log;

import ru.mrnightfury.queuemanager.repository.SharedPrefsWorker;

public class QueueManagerApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("APP", "App created");
        SharedPrefsWorker.init(getApplicationContext());
    }
}
package ru.mrnightfury.queuemanager.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "ASDASDASD");
        if (NotificationsService.isRunning()) {
            context.stopService(new Intent(context, NotificationsService.class));
        }
    }
}
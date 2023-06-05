package ru.mrnightfury.queuemanager.background;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.here.oksse.ServerSentEvent;

import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;
import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.Util;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkWorker;
import ru.mrnightfury.queuemanager.view.mainActivity.MainActivity;

public class NotificationsService extends Service {
    private static final String TAG = "NotificationService";
    private static Boolean isServiceRunning = false;
    private String login;
    private ServerSentEvent sse;
    private PendingIntent stopServiceIntent;

    public static Boolean isRunning() {
        return isServiceRunning;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "CREATED");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopServiceIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
                new Intent(this, Receiver.class), PendingIntent.FLAG_IMMUTABLE);

        isServiceRunning = true;
        if (intent == null) {
            stopSelf();
        }
        this.login = intent.getStringExtra("login");
        if (login == null) {
            Log.e(TAG, "No login provided");
            stopSelf();
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        Notification notification =
                new Notification.Builder(this, "1")
                        .setContentTitle("Service is started")
//                        .setContentText("TEXT")
                        .setSmallIcon(R.drawable.people_type_site_icon)
                        .setContentIntent(pendingIntent)
//                        .setTicker("TICKER")
                        .setColor(getColor(R.color.white))
                        .build();

        Log.i(TAG, "STARTED");
        startForeground(1, notification);
        watchUser();
        return super.onStartCommand(intent, flags, startId);
    }

    public void watchUser() {
        if (sse != null) {
            sse.close();
        }
        sse = NetworkWorker.getInstance().watchUser(login, new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, Response response) {

            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                String text = Util.parseUpdateString(message, login);
                if (Objects.equals(text, "WTF")) {
                    Log.wtf(TAG, "Incorrect SSE");
                    Log.i(TAG, message);
                    return;
                }
                Log.i(TAG, text);

                text = getApplicationContext().getString(R.string.you_are_next_nitification) + " \"" + text + "\"";

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                managerCompat.createNotificationChannel(new NotificationChannel("1",
                        getApplicationContext().getString(R.string.notificationChannel_name),
                        NotificationManager.IMPORTANCE_HIGH));


                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), String.valueOf(1))
                        .setSmallIcon(R.drawable.home_icon)
                        .setContentTitle(text)
                        .setColor(getColor(R.color.white))
                        .addAction(R.drawable.people_type_site_icon, "Stop Service", stopServiceIntent)
//                        .setContentText("text")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                managerCompat.notify(1, builder.build());
            }

            @Override
            public void onComment(ServerSentEvent sse, String comment) {

            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                return false;
            }

            @Override
            public void onClosed(ServerSentEvent sse) {

            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                return null;
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.i("SERVICE", "DESTROYED");
        isServiceRunning = false;
        if (sse != null) {
            sse.close();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package ru.mrnightfury.queuemanager.background;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import ru.mrnightfury.queuemanager.repository.AccountRepository;
import ru.mrnightfury.queuemanager.repository.SettingsRepository;
import ru.mrnightfury.queuemanager.repository.model.Settings;

public class ServiceLauncher {
    private SettingsRepository sRepository;
    private LiveData<Settings> settings;
    private AccountRepository aRepository;
    private AppCompatActivity activity;
    private String login;

    public ServiceLauncher(AppCompatActivity activity) {
        this.activity = activity;
        sRepository = SettingsRepository.getInstance();
        aRepository = AccountRepository.getInstance();
        login = aRepository.getAccount().getValue().getLogin();
        settings = sRepository.getSettings();

        NotificationChannel chan = new NotificationChannel(
                activity.getApplicationContext().getPackageName(),
                "Service",
                NotificationManager.IMPORTANCE_LOW);
        NotificationManager manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        settings.observe(activity, s -> {
            if (s.isServiceEnabled() && !NotificationsService.isRunning() && aRepository.isLogged()) {
                launchService();
            } else if (!s.isServiceEnabled() && NotificationsService.isRunning()) {
                stopService();
            }
        });

        aRepository.getAccount().observe(activity, a -> {
            if (settings.getValue().isServiceEnabled()
                    && !NotificationsService.isRunning()
                    && aRepository.isLogged()) {
                launchService();
            } else if (!aRepository.isLogged()) {
                stopService();
            }
        });
    }

    public void launchService() {
        activity.startService(
                new Intent(activity.getApplicationContext(), NotificationsService.class)
                        .putExtra("login",
                                aRepository.getAccount().getValue().getLogin()
                        ));
    }

    public void stopService() {
        activity.stopService(new Intent(activity.getApplicationContext(), NotificationsService.class));
    }

    public String getNewLogin() {
        return aRepository.getAccount().getValue().getLogin();
    }
}

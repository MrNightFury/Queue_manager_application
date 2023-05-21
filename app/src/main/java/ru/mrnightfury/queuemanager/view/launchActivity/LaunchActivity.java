package ru.mrnightfury.queuemanager.view.launchActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Bundle;

import ru.mrnightfury.queuemanager.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
//        new ViewModelProvider(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
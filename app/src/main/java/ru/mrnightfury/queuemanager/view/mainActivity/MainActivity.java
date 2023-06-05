package ru.mrnightfury.queuemanager.view.mainActivity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.background.ServiceLauncher;
import ru.mrnightfury.queuemanager.databinding.ActivityMainBinding;
import ru.mrnightfury.queuemanager.repository.AccountRepository;
import ru.mrnightfury.queuemanager.repository.QueuesRepository;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    LoginViewModel loginVM;
    ActivityMainBinding binding;
    ServiceLauncher launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        QueuesRepository.getInstance().init(getApplicationContext());

//        DynamicColors.applyToActivityIfAvailable(this);

        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences("queue.main", Context.MODE_PRIVATE);
        View navHost = findViewById(R.id.fragmentContainerView);
        NavController navController = Navigation.findNavController(navHost);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.startFragment, R.id.queueListFragment, R.id.menuFragment)
                .build();

        NavigationUI.setupWithNavController(binding.menu, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        launcher = new ServiceLauncher(this);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = super.onCreateView(parent, name, context, attrs);
        loginVM = new ViewModelProvider(this).get(LoginViewModel.class);
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            View navHost = findViewById(R.id.fragmentContainerView);
            navController = Navigation.findNavController(navHost);
            navController.navigateUp();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
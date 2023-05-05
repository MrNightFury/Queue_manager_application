package ru.mrnightfury.queuemanager.view.mainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.mrnightfury.queuemanager.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DynamicColors.applyToActivityIfAvailable(this);

//        TextView nameView = findViewById(R.id.nameField);
//        TextView idView = findViewById(R.id.idField);
//        findViewById(R.id.button).setOnClickListener(view1 -> {
//            NetworkService.getInstance()
//                    .getJSONApi()
//                    .getUser("admin")
//                    .enqueue(new Callback<User>() {
//                        @Override
//                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
//                            User user = response.body();
//
//                            nameView.setText(user.getName());
//                            idView.setText(user.getId());
//                        }
//
//                        @Override
//                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
//                            nameView.setText("Something went wrong");
//                            t.printStackTrace();
//                        }
//                    });
//        });
        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences("queue.main", Context.MODE_PRIVATE);
        View navHost = findViewById(R.id.fragmentContainerView);
        NavController navController = Navigation.findNavController(navHost);
//        NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.menu), navController);


        BottomNavigationView menu = findViewById(R.id.menu);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.startFragment, R.id.queueListFragment, R.id.menuFragment)
                .build();

        NavigationUI.setupWithNavController(menu, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.cardBackground)));
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
}
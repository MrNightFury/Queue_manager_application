package ru.mrnightfury.queuemanager.view.launchActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.viewmodel.LoginStates;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;

public class ConnectionCheckFragment extends Fragment {
    LiveData<LoginStates> state;
    LoginViewModel loginVM;
    TextView textView;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connection_check, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginVM = new ViewModelProvider(this).get(LoginViewModel.class);
        state = loginVM.getState();
        textView = view.findViewById(R.id.connection_info_text);
        navController = Navigation.findNavController(view);

        state.observe(getViewLifecycleOwner(), newState -> {
            Log.i("LoginState", newState.name());
            switch (newState) {
                case CONNECTED:
                    textView.setText(getResources().getString(R.string.login_wait_text));
                    break;
                case CONNECTION_FAILED:
                    Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case LOGGED:
                    navController.navigate(R.id.action_connectionSucceeded);
                    break;
                case NOT_FOUND:
                    navController.navigate(R.id.action_loginNeeded);
                    break;
                case LOGGING:
                    textView.setText("");
                    break;
            }
        });
        loginVM.initialize(getContext().getApplicationContext());
//        Navigation.findNavController(getView()).navigate(R.id.action_connectionCheckSucceeded);
    }
}
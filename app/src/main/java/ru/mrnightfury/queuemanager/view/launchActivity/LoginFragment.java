package ru.mrnightfury.queuemanager.view.launchActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.viewmodel.LoginStates;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {
    LiveData<LoginStates> state;
    LoginViewModel loginVM;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginVM = new ViewModelProvider(this).get(LoginViewModel.class);
        state = loginVM.getState();
        navController = Navigation.findNavController(view);

        EditText loginED = view.findViewById(R.id.login_input_field);
        EditText passwordED = view.findViewById(R.id.password_input_field);

        state.observe(getViewLifecycleOwner(), newState -> {
            switch (newState) {
                case LOGGED:
                    navController.navigate(R.id.action_loginSucceeded);
                    break;
                case INCORRECT_LOGIN_OR_PASSWORD:
                    Toast.makeText(getContext(), loginVM.getStatus(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        view.findViewById(R.id.signIn_button).setOnClickListener(view1 -> {
            navController.navigate(R.id.action_signInRequired);
        });
        view.findViewById(R.id.login_button).setOnClickListener(view1 -> {
            String login = loginED.getText().toString();
            String password = passwordED.getText().toString();

            loginVM.login(login, password);
        });
    }
}
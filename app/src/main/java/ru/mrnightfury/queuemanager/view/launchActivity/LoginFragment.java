package ru.mrnightfury.queuemanager.view.launchActivity;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentLoginBinding;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;
import ru.mrnightfury.queuemanager.viewmodel.AccountViewModel;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModelOld;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    LiveData<LoginStates> state;
//    LoginViewModelOld loginVM;
    FragmentLoginBinding binding;
    NavController navController;
    AccountViewModel accountVM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater);

//                DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
//        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        accountVM = new ViewModelProvider(this).get(AccountViewModel.class);
        state = accountVM.getLoginState();

        binding.signInButton.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_signInRequired);
        });

        binding.loginButton.setOnClickListener(view1 -> {

        });

//        binding.set

//        loginVM = new ViewModelProvider(this).get(LoginViewModelOld.class);
//        state = loginVM.getState();


//        EditText loginED = view.findViewById(R.id.login_input_field);
//        EditText passwordED = view.findViewById(R.id.password_input_field);

//        state.observe(getViewLifecycleOwner(), newState -> {
//            switch (newState) {
//                case LOGGED:
//                    navController.navigate(R.id.action_loginSucceeded);
//                    break;
//                case INCORRECT_LOGIN_OR_PASSWORD:
////                    Toast.makeText(getContext(), loginVM.getStatus(), Toast.LENGTH_SHORT).show();
//                    Log.w(TAG, "Incorrect login or password");
//                    break;
//            }
//        });
//        view.findViewById(R.id.signIn_button).setOnClickListener(view1 -> {
//            navController.navigate(R.id.action_signInRequired);
//        });
//        view.findViewById(R.id.login_button).setOnClickListener(view1 -> {
//            String login = loginED.getText().toString();
//            String password = passwordED.getText().toString();
//
//            loginVM.login(login, password);
//        });
    }
}
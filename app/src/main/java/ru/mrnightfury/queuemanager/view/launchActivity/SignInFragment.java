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
import android.widget.Toast;

import ru.mrnightfury.queuemanager.databinding.FragmentSignInBinding;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;

public class SignInFragment extends Fragment {
    private static final String TAG = "SignInFragment";
    private FragmentSignInBinding binding;
    NavController navController;
    LoginViewModel accountVM;
    LiveData<LoginStates> state;
    LiveData<Integer> error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        accountVM = new ViewModelProvider(this).get(LoginViewModel.class);
        state = accountVM.getLoginState();
        error = accountVM.getError();

        state.observe(getViewLifecycleOwner(), newState -> {

            switch (newState) {
                case LOGGING:
                    navController.navigateUp();
                    navController.navigateUp();
                    break;
                case INCORRECT_LOGIN_OR_PASSWORD:
                    if (accountVM.getStatus() != null) {
                        Toast.makeText(getContext(), accountVM.getStatus(),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });

        binding.signInButton.setOnClickListener(view1 -> {
            accountVM.createAccount(
                    binding.loginInputField.getText().toString(),
                    binding.loginInputField2.getText().toString(),
                    binding.passwordInputField.getText().toString()
            );
        });

        error.observe(getViewLifecycleOwner(), newError -> {
            if (newError != 0) {
                Toast.makeText(getContext(), newError, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
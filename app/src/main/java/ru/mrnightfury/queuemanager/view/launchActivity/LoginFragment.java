package ru.mrnightfury.queuemanager.view.launchActivity;

import androidx.activity.OnBackPressedCallback;
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

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentLoginBinding;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    LiveData<LoginStates> state;
//    LoginViewModelOld loginVM;
    FragmentLoginBinding binding;
    NavController navController;
    LoginViewModel accountVM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        accountVM = new ViewModelProvider(this).get(LoginViewModel.class);
        state = accountVM.getLoginState();

        binding.signInButton.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_signInRequired);
        });

        binding.loginButton.setOnClickListener(view1 -> {
            Log.i("TEST", "1");
            accountVM.login(
                    binding.loginInputField.getText().toString(),
                    binding.passwordInputField.getText().toString()
            );
        });

        state.observe(getViewLifecycleOwner(), state -> {
            Log.i("LSObserver2", state.name());
            switch (state) {
                case LOGGING:
//                    Log.i("ASDASD", "asda");
                    navController.navigateUp();
                    break;
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
                System.exit(0);
            }
        });
    }
}
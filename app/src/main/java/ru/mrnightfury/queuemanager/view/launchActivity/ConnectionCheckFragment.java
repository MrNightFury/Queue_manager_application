package ru.mrnightfury.queuemanager.view.launchActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentConnectionCheckBinding;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;

public class ConnectionCheckFragment extends Fragment {
    private LiveData<LoginStates> state;
    private LoginViewModel accountVM;
    private NavController navController;
    FragmentConnectionCheckBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConnectionCheckBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        accountVM = new ViewModelProvider(this).get(LoginViewModel.class);
        state = accountVM.getLoginState();
        binding.setLoginState(state);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.retryConnectionButton.setOnClickListener(view1 -> {
            accountVM.connect();
        });

        state.observe(getViewLifecycleOwner(), newState -> {
            final String TAG = "LSObserver";
            Log.i(TAG, newState.name());
            switch (newState) {
                case NONE:
                    accountVM.connect();
                    break;
                case CONNECTION_CHECKING:
                    binding.connectionInfoText.setText(R.string.connection_wait_text);
                case CONNECTED:
                    binding.connectionInfoText.setText(R.string.login_wait_text);
                    break;
                case CONNECTION_FAILED:
                    binding.connectionInfoText.setText(R.string.connection_failed_text);
                    Toast.makeText(getContext(), R.string.connection_failed_text, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case LOGGED:
                    navController.navigate(R.id.action_connectionSucceeded);
                    break;
                case NOT_FOUND:
                case INCORRECT_LOGIN_OR_PASSWORD:
                    navController.navigate(R.id.action_loginNeeded);
                    break;
                case LOGGING:
                    binding.connectionInfoText.setText(R.string.loggingIn_wait_text);
                    break;
//                case WAITING_FOR_USER:
//                    navController.popBackStack();
//                    break;
            }
        });
    }
}
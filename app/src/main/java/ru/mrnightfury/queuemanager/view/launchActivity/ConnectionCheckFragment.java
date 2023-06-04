package ru.mrnightfury.queuemanager.view.launchActivity;

import android.content.Intent;
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
import ru.mrnightfury.queuemanager.view.mainActivity.MainActivity;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;

public class ConnectionCheckFragment extends Fragment {
    private static final String TAG = "CChF";
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

//        binding.checkLoginStateButton.setOnClickListener(view1 -> {
//            Log.i(TAG, state.getValue().name());
//        });

        state.observe(getViewLifecycleOwner(), newState -> {
            final String TAG = "LSObserver";
            if (navController.getCurrentDestination().getId() != R.id.connectionCheckFragment) {
                Log.wtf(TAG, "Какого хера");
                return;
            }
            Log.i(TAG, newState.name());
            switch (newState) {
                case NONE:
                    accountVM.connect();
                    break;
                case CONNECTION_CHECKING:
                    binding.connectionInfoText.setText(R.string.connection_wait_text);
                    break;
                case CONNECTED:
                    binding.connectionInfoText.setText(R.string.login_wait_text);
                    break;
                case CONNECTION_FAILED:
                    binding.connectionInfoText.setText(R.string.connection_failed_text);
                    Toast.makeText(getContext(), R.string.connection_failed_text, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case LOGGED:
//                    navController.navigate(R.id.action_connectionSucceeded);
                    startActivity(new Intent(getContext(), MainActivity.class));
                    break;
                case NOT_FOUND:
                case INCORRECT_LOGIN_OR_PASSWORD:
                    navController.navigate(R.id.action_loginNeeded);
                    break;
                case LOGGING:
                    binding.connectionInfoText.setText(R.string.loggingIn_wait_text);
                    break;
                case EXIT:
                    accountVM.connect();
//                    navController.navigateUp();
//                    getActivity().finish();
//                    getActivity().finishAffinity();
                    break;

//                case WAITING_FOR_USER:
//                    navController.popBackStack();
//                    break;
            }
        });
    }
}
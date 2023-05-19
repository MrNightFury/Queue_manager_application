package ru.mrnightfury.queuemanager.view.launchActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import ru.mrnightfury.queuemanager.viewmodel.AccountViewModel;

public class ConnectionCheckFragment extends Fragment {
    private LiveData<LoginStates> state;
//    LoginViewModelOld loginVM;
//    TextView textView;
    private AccountViewModel accountVM;
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
//        return inflater.inflate(R.layout.fragment_connection_check, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        accountVM = new ViewModelProvider(this).get(AccountViewModel.class);
        state = accountVM.getLoginState();
        binding.setLoginState(state);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.retryConnectionButton.setOnClickListener(view1 -> {
            accountVM.connect();
        });

        state.observe(getViewLifecycleOwner(), newState -> {
            final String TAG = "LSObserver";
            if (!isVisible() || !isAdded() || isDetached()) {
                Log.i(TAG, "Observe prevented: " + isVisible() + isAdded() + isDetached());
                return;
            }
            Log.i(TAG, newState.name());
            switch (newState) {
                case NONE:
                    binding.connectionInfoText.setText(R.string.connection_wait_text);
                    break;
                case CONNECTED:
                    binding.connectionInfoText.setText(R.string.login_wait_text);
                    break;
                case CONNECTION_FAILED:
                    binding.connectionInfoText.setText(R.string.connection_failed_text);
                    Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT)
                            .show();
//                    binding.setLoginState(state);
                    break;
                case LOGGED:
                    navController.navigate(R.id.action_connectionSucceeded);
                    break;
                case NOT_FOUND:
                    navController.navigate(R.id.action_loginNeeded);
                    break;
                case LOGGING:
                    binding.connectionInfoText.setText(R.string.loggingIn_wait_text);
                    break;
            }
        });
        accountVM.connect();
//        loginVM = new ViewModelProvider(this).get(LoginViewModelOld.class);
//        state = loginVM.getState();
//        textView = view.findViewById(R.id.connection_info_text);


//        state.observe(getViewLifecycleOwner(), newState -> {
//            final String TAG = "LSObserver";
//            if (!isVisible() || !isAdded() || isDetached()) {
//                Log.i(TAG, "Observe prevented: " + isVisible() + isAdded() + isDetached());
//                return;
//            }
//            Log.i(TAG, newState.name());
//            switch (newState) {
//                case CONNECTED:
////                    binding.connectionInfoText.setText(R.string.login_wait_text);
//                    break;
//                case CONNECTION_FAILED:
////                    binding.connectionInfoText.setText(R.string.connection_failed_text);
//                    Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT)
//                            .show();
//                    break;
//                case LOGGED:
//                    navController.navigate(R.id.action_connectionSucceeded);
//                    break;
//                case NOT_FOUND:
//                    navController.navigate(R.id.action_loginNeeded);
//                    break;
//                case LOGGING:
////                    binding.connectionInfoText.setText("");
//                    break;
//            }
//        });
//        loginVM.initialize(getContext().getApplicationContext());
//        Navigation.findNavController(getView()).navigate(R.id.action_connectionCheckSucceeded);
    }
}
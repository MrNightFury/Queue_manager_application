package ru.mrnightfury.queuemanager.view.mainActivity;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentConnectionCheckBinding;
import ru.mrnightfury.queuemanager.databinding.FragmentStartBinding;
import ru.mrnightfury.queuemanager.viewmodel.AccountViewModel;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;

public class StartFragment extends Fragment {
    FragmentStartBinding binding;
//    LoginViewModel loginVM;
    AccountViewModel accountVM;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountVM = new ViewModelProvider(this).get(AccountViewModel.class);
        navController = Navigation.findNavController(view);

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavDestination dest = navController.getCurrentDestination();
                if (dest != null && dest.getId() == R.id.startFragment) {
                    accountVM.exit();
                    getActivity().finishAffinity();
                } else {
                    navController.navigateUp();
                }
            }
        });
    }
}
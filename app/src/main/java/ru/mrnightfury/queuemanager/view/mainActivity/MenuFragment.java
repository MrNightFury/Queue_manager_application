package ru.mrnightfury.queuemanager.view.mainActivity;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentMenuBinding;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.viewmodel.AccountViewModel;
import ru.mrnightfury.queuemanager.viewmodel.SettingsViewModel;

public class MenuFragment extends Fragment {
    SettingsViewModel settingsVM;
    AccountViewModel accountVM;
    FragmentMenuBinding binding;
    LiveData<AccountModel> account;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountVM = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
        account = accountVM.getAccount();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        settingsVM = new ViewModelProvider(this).get(SettingsViewModel.class);
        accountVM.loadUser();

        settingsVM.getSettings().observe(getViewLifecycleOwner(), s -> {
            binding.serviceSwitch.setChecked(s.isServiceEnabled());
        });

        account.observe(getViewLifecycleOwner(), model -> {
            binding.accountLoginText.setText(model.getLogin());
            binding.accountUsernameText.setText(model.getUsername());
        });
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

        binding.logOutButton.setOnClickListener(v -> {
            accountVM.logOut();
            getActivity().finish();
        });

        binding.enterDevMenuButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_toDevMenu);
        });

        binding.serviceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsVM.getSettings().getValue().setServiceEnabled(isChecked);
            settingsVM.notifySettingsChanged();
        });
    }
}
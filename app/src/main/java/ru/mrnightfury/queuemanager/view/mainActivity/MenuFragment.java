package ru.mrnightfury.queuemanager.view.mainActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentMenuBinding;
import ru.mrnightfury.queuemanager.repository.AccountRepository;
import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;
import ru.mrnightfury.queuemanager.viewmodel.AccountViewModel;
import ru.mrnightfury.queuemanager.viewmodel.LoginViewModel;
import ru.mrnightfury.queuemanager.viewmodel.SettingsViewModel;

public class MenuFragment extends Fragment {
    SettingsViewModel settingsVM;
    AccountViewModel accountVM;
    LoginViewModel loginVM;
    FragmentMenuBinding binding;
    LiveData<AccountModel> account;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountVM = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
//        loginVM = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        account = accountVM.getAccount();
//        loginVM = new ViewModelProvider(getActivity()).get(LoginViewModelOld.class);

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
        account.observe(getViewLifecycleOwner(), model -> {
            binding.accountLoginText.setText(model.getLogin());
            binding.accountUsernameText.setText(model.getUsername());
        });
        accountVM.loadUser();
//        TextView loginView = view.findViewById();
    }
}
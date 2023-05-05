package ru.mrnightfury.queuemanager.view.mainActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.viewmodel.SettingsViewModel;

public class MenuFragment extends Fragment {
    SettingsViewModel settingsVM;
    SwitchMaterial switch1;
    SwitchMaterial switch2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        switch1 = view.findViewById(R.id.switch1);
        switch2 = view.findViewById(R.id.switch2);
        settingsVM = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsVM.getData().observe(getViewLifecycleOwner(), state -> {
            switch1.setChecked(state);
            switch2.setChecked(state);
        });
        switch1.setOnCheckedChangeListener((checker, isChecked) -> {
            settingsVM.check(isChecked);
        });
        switch2.setOnCheckedChangeListener((checker, isChecked) -> {
            settingsVM.check(isChecked);
        });
        return view;
    }
}
package ru.mrnightfury.queuemanager.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.background.NotificationsService;
import ru.mrnightfury.queuemanager.databinding.FragmentDevMenuBinding;

public class DevMenuFragment extends Fragment {
    FragmentDevMenuBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDevMenuBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.checkServiceButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), Boolean.toString(NotificationsService.isRunning()), Toast.LENGTH_SHORT).show();
        });
    }
}
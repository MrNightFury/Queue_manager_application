package ru.mrnightfury.queuemanager.view.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentQueueBinding;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.Queue;
import ru.mrnightfury.queuemanager.viewmodel.QueuesViewModel;

public class QueueFragment extends Fragment {
    FragmentQueueBinding binding;
    QueuesViewModel queuesVM;
    LiveData<Queue> queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQueueBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queuesVM = new ViewModelProvider(this).get(QueuesViewModel.class);
        queue = queuesVM.getChosenQueue();
    }
}
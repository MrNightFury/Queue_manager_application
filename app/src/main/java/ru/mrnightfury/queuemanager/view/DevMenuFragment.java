package ru.mrnightfury.queuemanager.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.background.NotificationsService;
import ru.mrnightfury.queuemanager.databinding.FragmentDevMenuBinding;
import ru.mrnightfury.queuemanager.repository.database.FavouriteEntity;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;
import ru.mrnightfury.queuemanager.viewmodel.FavouriteViewModel;
import ru.mrnightfury.queuemanager.viewmodel.QueuesViewModel;

public class DevMenuFragment extends Fragment {
    FragmentDevMenuBinding binding;
    QueuesViewModel queuesVM;
    FavouriteViewModel favouriteVM;

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
        queuesVM = new ViewModelProvider(this).get(QueuesViewModel.class);
        favouriteVM = new ViewModelProvider(this).get(FavouriteViewModel.class);

        binding.checkServiceButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), Boolean.toString(NotificationsService.isRunning()), Toast.LENGTH_SHORT).show();
        });
        binding.checkFavouriteButton.setOnClickListener(v -> {
            for (FavouriteEntity e : favouriteVM.getFavouriteEntities()) {
                Log.i("TAG", e.getQueueId());
            }
            for (QueueResponse q : favouriteVM.getFavouriteQueues().getValue()) {
                Log.i("TAG", q.getName());
            }
        });
    }
}
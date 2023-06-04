package ru.mrnightfury.queuemanager.view.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentCreateQueueBinding;
import ru.mrnightfury.queuemanager.viewmodel.QueueCreationViewModel;

public class CreateQueueFragment extends Fragment {
    FragmentCreateQueueBinding binding;
    QueueCreationViewModel creationVM;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateQueueBinding.inflate(inflater);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_create_queue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        creationVM = new ViewModelProvider(this).get(QueueCreationViewModel.class);
        navController = Navigation.findNavController(view);

//        activity.getMenuInflater().inflate(R.menu.queue_menu, activity.getSupportActionBar().getCustomView());

        binding.confirmCreateQueue.setOnClickListener(v -> {
            creationVM.getQueueCreationState().observe(getViewLifecycleOwner(), newState -> {
                if (navController.getCurrentDestination().getId() != R.id.createQueueFragment) {
                    return;
                }
                if (Objects.equals(newState, "Success")) {
                    navController.navigate(R.id.action_queueCreated);
                }
            });

            creationVM.createQueue(
                    binding.queueNameInput.getText().toString(),
                    binding.queueDescriptionInput.getText().toString()
            );
        });
//        binding.confirmCreateQueue.setOnClickListener(v -> {
//            creationVM.check();
//        });
    }


}
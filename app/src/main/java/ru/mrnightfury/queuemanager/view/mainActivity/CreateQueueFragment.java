package ru.mrnightfury.queuemanager.view.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateQueueBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        creationVM = new ViewModelProvider(this).get(QueueCreationViewModel.class);
        navController = Navigation.findNavController(view);

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
    }


}
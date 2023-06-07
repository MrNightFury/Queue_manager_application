package ru.mrnightfury.queuemanager.view.mainActivity;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentStartBinding;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;
import ru.mrnightfury.queuemanager.viewmodel.AccountViewModel;
import ru.mrnightfury.queuemanager.viewmodel.FavouriteViewModel;
import ru.mrnightfury.queuemanager.viewmodel.QueuesViewModel;

public class StartFragment extends Fragment {
    private static final String TAG = "SF";
    FragmentStartBinding binding;
    QueuesViewModel queuesVM;
    AccountViewModel accountVM;
    FavouriteViewModel favouriteVM;
    NavController navController;

    LiveData<ArrayList<QueueResponse>> queues;
    ArrayList<QueueResponse> queuesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountVM = new ViewModelProvider(this).get(AccountViewModel.class);
        queuesVM = new ViewModelProvider(this).get(QueuesViewModel.class);
        favouriteVM = new ViewModelProvider(this).get(FavouriteViewModel.class);
        navController = Navigation.findNavController(view);
        queues = favouriteVM.getFavouriteQueues();
        queuesList = new ArrayList<>(queues.getValue());

        RecyclerView recyclerView = binding.favouriteList;
        QueueListAdapter adapter = new QueueListAdapter(queuesList, queuesVM);

        adapter.setOnItemClickListener((queue, position) -> {
            QueueResponse chosenQueue = queuesList.get(position);
            Log.i(TAG, "Navigating to queue " + chosenQueue.getId());
            queuesVM.chooseQueue(chosenQueue.getId());
            navController.navigate(R.id.action_toFavouriteQueue);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        binding.swipeFavourite.setOnRefreshListener(() -> {
            favouriteVM.update();
        });

        queues.observe(getViewLifecycleOwner(), (newQueues -> {
            Log.i(TAG, "Refreshing list...");
            queuesList.clear();
            queuesList.addAll(newQueues);
            adapter.notifyDataSetChanged();
            if (binding.swipeFavourite.isRefreshing()) {
                binding.swipeFavourite.setRefreshing(false);
            }
        }));

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

        favouriteVM.update();
    }
}
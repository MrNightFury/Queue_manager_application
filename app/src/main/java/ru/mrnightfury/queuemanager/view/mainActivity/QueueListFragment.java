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
//import ru.mrnightfury.queuemanager.databinding.QueueListItemLayoutBinding;
import ru.mrnightfury.queuemanager.databinding.FragmentQueueListBinding;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;
import ru.mrnightfury.queuemanager.viewmodel.AccountViewModel;
import ru.mrnightfury.queuemanager.viewmodel.QueuesViewModel;

public class QueueListFragment extends Fragment {
    private static final String TAG = "QLF";
    NavController navController;
//    LoginViewModel loginVM;
    AccountViewModel accountVM;
    QueuesViewModel queuesVM;
    FragmentQueueListBinding binding;
    LiveData<ArrayList<QueueResponse>> queues;
    ArrayList<QueueResponse> queuesList;
//    SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQueueListBinding.inflate(inflater);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_queue_list, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        accountVM = new ViewModelProvider(this).get(AccountViewModel.class);
        queuesVM = new ViewModelProvider(this).get(QueuesViewModel.class);
        navController = Navigation.findNavController(view);
        queues = queuesVM.getQueues();
        queuesList = queues.getValue();

        RecyclerView recyclerView = binding.queueList;
//        RecyclerView recyclerView = view.findViewById(R.id.queue_list);
        QueueListAdapter adapter = new QueueListAdapter(queuesList, queuesVM);

        adapter.setOnItemClickListener((queue, position) -> {
            QueueResponse chosenQueue = queuesList.get(position);
            Log.i(TAG, "Navigating to queue " + chosenQueue.getId());
            queuesVM.chooseQueue(chosenQueue.getId());
            navController.navigate(R.id.action_openQueueFromList);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        binding.queuesSwipeLayout.setOnRefreshListener(() -> {
            queuesVM.update();
        });

        binding.addQueueButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_toCreateQueueFragment);
        });


        queues.observe(getViewLifecycleOwner(), (newQueues -> {
            Log.i(TAG, "Refreshing list...");
            queuesList.clear();
            queuesList.addAll(newQueues);
            adapter.notifyDataSetChanged();
            if (binding.queuesSwipeLayout.isRefreshing()) {
                binding.queuesSwipeLayout.setRefreshing(false);
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

        queuesVM.update();
    }
}
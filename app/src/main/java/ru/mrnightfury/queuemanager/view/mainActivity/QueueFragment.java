package ru.mrnightfury.queuemanager.view.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentQueueBinding;
import ru.mrnightfury.queuemanager.repository.model.Queue;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;
import ru.mrnightfury.queuemanager.Util;
import ru.mrnightfury.queuemanager.viewmodel.QueuesViewModel;

public class QueueFragment extends Fragment {
    FragmentQueueBinding binding;
    QueuesViewModel queuesVM;
    LiveData<Queue> queue;
    QueuePeopleListAdapter adapter;

    ArrayList<Queue.User> users;

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
        users = new ArrayList<>();
        adapter = new QueuePeopleListAdapter(getContext(), R.layout.people_in_queue_item_layout, users);
        binding.queuedPeopleList.setAdapter(adapter);

//        getActivity().getActionBar().setTitle(queue.getValue().getName());
//        binding.setQueue(queue);
//        getActivity().getActionBar().getCustomView().findViewById()

        queue.observe(getViewLifecycleOwner(), q -> {
//            getActivity().getActionBar().setTitle(queue.getValue().getName());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(q.getName());
//            ((AppCompatActivity) getActivity()).getSupportActionBar().
            binding.queueDescription.setText(q.getDescription());
            if (binding.queueSwipeLayout.isRefreshing()) {
                binding.queueSwipeLayout.setRefreshing(false);
            }
        });

        queue.getValue().getQueuedPeople().observe(getViewLifecycleOwner(), list -> {
            binding.queuePeopleCount.setText(Util.formatCount(list.size()));
            users.clear();
            users.addAll(list);
            Log.i("TAG", users.toString());
            adapter.notifyDataSetChanged();
        });

        binding.queueSwipeLayout.setOnRefreshListener(() -> {

        });
//        queue.getValue().getQueuedPeople()
    }


}
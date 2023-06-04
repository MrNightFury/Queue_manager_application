package ru.mrnightfury.queuemanager.view.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.Objects;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentQueueBinding;
import ru.mrnightfury.queuemanager.repository.model.Queue;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;
import ru.mrnightfury.queuemanager.Util;
import ru.mrnightfury.queuemanager.viewmodel.QueueViewModel;
import ru.mrnightfury.queuemanager.viewmodel.QueuesViewModel;

public class QueueFragment extends Fragment {
    private static final String TAG = "QF";
    private FragmentQueueBinding binding;
    private NavController navController;
//    @Deprecated
//    private QueuesViewModel queuesVM;
    private QueueViewModel queueVM;
//    private LiveData<Queue> queue;
    private QueuePeopleListAdapter adapter;

//    @Deprecated
//    private ArrayList<Queue.User> users;

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
        queueVM = new ViewModelProvider(this).get(QueueViewModel.class);
        navController = Navigation.findNavController(view);

        getActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.queue_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.deleteQueue_menuItem) {
                    queueVM.deleteQueue();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        queueVM.getQueueEditionState().observe(getViewLifecycleOwner(), newState -> {
            if (Objects.equals(newState, "deleted")) {
//                navController.navigateUp();
            }
        });
//        queue = queueVM.getChosenQueue();
//        queuesVM = new ViewModelProvider(this).get(QueuesViewModel.class);
//        queue = queuesVM.getChosenQueue();

//        users = new ArrayList<>();
        adapter = new QueuePeopleListAdapter(getContext(), R.layout.people_in_queue_item_layout,
                queueVM.getUsers().getValue());
        binding.queuedPeopleList.setAdapter(adapter);

        queueVM.getChosenQueue().observe(getViewLifecycleOwner(), q -> {
            if (q == null) {
                navController.navigateUp();
                return;
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(q.getName());
            binding.queueDescription.setText(q.getDescription());
            queueVM.updatePeopleList();
            if (binding.queueSwipeLayout.isRefreshing()) {
                binding.queueSwipeLayout.setRefreshing(false);
            }
        });

        queueVM.getUsers().observe(getViewLifecycleOwner(), newList -> {
            binding.queuePeopleCount.setText(Util.formatCount(newList.size()));
            adapter.notifyDataSetChanged();
        });

//        binding.queueStar.setOnClickListener(v -> {
//            for (Queue.User u : queueVM.c().getQueuedPeople()) {
//                Log.i("ASD", u.getUsername() == null ? "null" : u.getUsername());
//            }
//        });

//        queue.getValue().getQueuedPeople().observe(getViewLifecycleOwner(), list -> {
//            binding.queuePeopleCount.setText(Util.formatCount(list.size()));
//
//            users.clear();
//            users.addAll(list);
//            Log.i("TAG", users.toString());
//            adapter.notifyDataSetChanged();
//        });

        binding.setIsUserInQueue(queueVM.getIsUserInQueue());
        binding.setIsUserFrozen(queueVM.getIsUserFrozen());

        binding.setLifecycleOwner(getViewLifecycleOwner());

//        queueVM.getIsUserInQueue().observe(getViewLifecycleOwner(), b -> {
//            Log.i("ASDAS", Boolean.toString(b));
//        });

        queueVM.subscribe();

        binding.joinOrLeaveButton.setOnClickListener(v -> queueVM.joinOrLeave());
        binding.freezeButton.setOnClickListener(v -> queueVM.freeze());
        binding.popQueueButton.setOnClickListener(v -> queueVM.pop());
        binding.queueSwipeLayout.setOnRefreshListener(() -> queueVM.updateQueue());

        queueVM.getPeopleChangedTrigger().observe(getViewLifecycleOwner(), v -> {
            queueVM.updatePeopleList();
        });

        binding.queuedPeopleList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (binding.queuedPeopleList.getChildAt(0) != null) {
                    binding.queueSwipeLayout.setEnabled(
                            binding.queuedPeopleList.getFirstVisiblePosition() == 0
                                    && binding.queuedPeopleList.getChildAt(0).getTop() == 0);
                }
            }
        });
//        queue.getValue().getQueuedPeople()
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        queueVM.cancelSubscribe();
    }
}
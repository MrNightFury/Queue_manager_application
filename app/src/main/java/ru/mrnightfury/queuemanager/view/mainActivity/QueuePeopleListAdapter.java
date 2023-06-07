package ru.mrnightfury.queuemanager.view.mainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.function.Function;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.PeopleInQueueItemLayoutBinding;
import ru.mrnightfury.queuemanager.repository.model.Queue;

public class QueuePeopleListAdapter extends ArrayAdapter<Queue.User> {
    private final LayoutInflater inflater;
    private final ArrayList<Queue.User> list;

    public QueuePeopleListAdapter(Context context, int resource, ArrayList<Queue.User> list) {
        super(context, resource, list);
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PeopleInQueueItemLayoutBinding binding = PeopleInQueueItemLayoutBinding.inflate(inflater);
        View view = binding.getRoot();

        Queue.User user = list.get(position);
        binding.peopleUsername.setText(user.getUsername() == null ? user.getLogin() : user.getUsername());
        binding.peopleIcon.setImageResource(
                ((Function<String, Integer>) (type) -> {
                    switch (type) {
                        case "VK":
                            return R.drawable.people_type_vk_icon;
                        case "SITE":
                            return R.drawable.people_type_site_icon;
                        default:
                            return R.drawable.people_type_not_registered_icon;
                    }
                }).apply(user.getType())
        );
        binding.frozenIcon.setVisibility(user.getFrozen() ? View.VISIBLE : View.GONE);
        return view;
    }
}

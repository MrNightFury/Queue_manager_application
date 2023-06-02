package ru.mrnightfury.queuemanager.view.mainActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.PeopleInQueueItemLayoutBinding;
import ru.mrnightfury.queuemanager.repository.model.Queue;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

public class QueuePeopleListAdapter extends ArrayAdapter<Queue.User> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Queue.User> list;

    public QueuePeopleListAdapter(Context context, int resource, ArrayList<Queue.User> list) {
        super(context, resource, list);
        this.list = list;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PeopleInQueueItemLayoutBinding binding = PeopleInQueueItemLayoutBinding.inflate(inflater);
//        View view = inflater.inflate(this.layout, parent, false);
        View view = binding.getRoot();

        Queue.User user = list.get(position);
//        Log.i("QPLA", user.getUsername() == null ? "null" : user.getUsername());
        binding.peopleUsername.setText(user.getUsername() == null ? user.getLogin() : user.getUsername());
        binding.peopleIcon.setImageResource(R.drawable.people_type_vk_icon);
//        TextView title = view.findViewById(R.id.title);
//        TextView description = view.findViewById(R.id.description);
//        ImageView image = view.findViewById(R.id.imageView);

//        Item item = items.get(position);

//        title.setText(item.getTitle());
//        description.setText(item.getDescription());
//        image.setImageResource(R.drawable.broccoli);

        return view;
    }
}

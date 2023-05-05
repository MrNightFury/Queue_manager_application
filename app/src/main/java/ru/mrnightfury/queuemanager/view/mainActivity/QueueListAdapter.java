package ru.mrnightfury.queuemanager.view.mainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.model.Queue;

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Queue> queues;
    OnClickListener listener = null;

    interface OnClickListener {
        void onClick (Queue item, int position);
    }

    QueueListAdapter(Context context, int resource, List<Queue> queues) {
        this.queues = queues;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener (OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public QueueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.queue_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueListAdapter.ViewHolder holder, int position) {
        Queue queue = queues.get(position);
        holder.name.setText(queue.getName());
        holder.peopleCount.setText(queue.getDescription());
//        holder.image.setImageResource(R.drawable.broccoli);

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(queue, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return queues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, peopleCount;
        final ImageView image;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.queue_name);
            peopleCount = view.findViewById(R.id.queue_people_count);
            image = view.findViewById(R.id.queue_star);
        }
    }
}

package ru.mrnightfury.queuemanager.view.mainActivity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//import ru.mrnightfury.queuemanager.databinding.QueueListItemLayoutBinding;
import ru.mrnightfury.queuemanager.databinding.QueueListItemLayoutBinding;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.ViewHolder> {
//    QueueListItemLayoutBinding binding;
//    private final LayoutInflater inflater;
    private final ArrayList<QueueResponse> queues;
    OnClickListener listener = null;

    interface OnClickListener {
        void onClick (QueueResponse item, int position);
    }

    public QueueListAdapter(ArrayList<QueueResponse> queues) {
        this.queues = queues;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.queue_list_item_layout, parent, false);
//        view.setOnClickListener(v -> Log.i("DSA", "HIBBHIBHI"));
//        Log.i("ASSD", "ADSSA....");
        return new ViewHolder(
                QueueListItemLayoutBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QueueListAdapter.ViewHolder holder, int position) {
        QueueResponse queue = queues.get(position);
        QueueListItemLayoutBinding binding = holder.getBinding();
        binding.queueName.setText(queue.getName());
        binding.queueDescription.setText(queue.getDescription());
        binding.queuePeopleCount.setText(Integer.toString(queue.getQueuedPeople().length));

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

    public void setOnItemClickListener (OnClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        QueueListItemLayoutBinding binding;
        ViewHolder(QueueListItemLayoutBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        QueueListItemLayoutBinding getBinding() {
            return binding;
        }
    }
}

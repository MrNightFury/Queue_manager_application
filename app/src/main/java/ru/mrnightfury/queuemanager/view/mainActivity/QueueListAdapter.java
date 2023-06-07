package ru.mrnightfury.queuemanager.view.mainActivity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.QueueListItemLayoutBinding;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.QueueResponse;
import ru.mrnightfury.queuemanager.viewmodel.QueuesViewModel;

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.ViewHolder> {
    private final ArrayList<QueueResponse> queues;
    private final QueuesViewModel queuesVM;
    OnClickListener listener = null;

    interface OnClickListener {
        void onClick (QueueResponse item, int position);
    }

    public QueueListAdapter(ArrayList<QueueResponse> queues, QueuesViewModel vm) {
        this.queues = queues;
        this.queuesVM = vm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        holder.isFavourite = queuesVM.isFavourite(queue.getId());
        holder.updateStar();

        binding.queueStar.setOnClickListener(v -> {
            if (!holder.isFavourite) {
                holder.isFavourite = true;
                queuesVM.addToFavourite(queue.getId());
            } else {
                holder.isFavourite = false;
                queuesVM.deleteFromFavourite(queue.getId());
            }
            holder.updateStar();
        });

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
        Boolean isFavourite;
        ViewHolder(QueueListItemLayoutBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        void updateStar() {
            binding.queueStar.setImageResource(isFavourite ? R.drawable.star_icon_filled : R.drawable.star_icon);
        }

        QueueListItemLayoutBinding getBinding() {
            return binding;
        }
    }
}

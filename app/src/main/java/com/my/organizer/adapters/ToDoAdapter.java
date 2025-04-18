package com.my.organizer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.my.organizer.R;
import com.my.organizer.models.ToDo;

public class ToDoAdapter extends ListAdapter<ToDo, ToDoAdapter.ToDoViewHolder> {
    private OnItemClickListener listener;

    public ToDoAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ToDo> DIFF_CALLBACK = new DiffUtil.ItemCallback<ToDo>() {
        @Override
        public boolean areItemsTheSame(@NonNull ToDo oldItem, @NonNull ToDo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDo oldItem, @NonNull ToDo newItem) {
            return oldItem.getTask().equals(newItem.getTask()) &&
                    oldItem.isCompleted() == newItem.isCompleted();
        }
    };

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo toDo = getItem(position);
        holder.textViewTask.setText(toDo.getTask()); // ✅ Fixed: Used getTask()

        // Click Listener for item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(toDo);
            }
        });

        // Click Listener for delete button
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(toDo);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(ToDo toDo);
        void onDeleteClick(ToDo toDo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTask;
        ImageButton buttonDelete;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.txtToDoTitle); // ✅ Updated ID
            buttonDelete = itemView.findViewById(R.id.buttonDeleteToDo);
        }
    }
}

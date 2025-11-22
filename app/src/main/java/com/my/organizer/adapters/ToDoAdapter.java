package com.my.organizer.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.my.organizer.R;
import com.my.organizer.models.ToDo;
import java.util.Date;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDo> toDoList;
    private final Context context;
    private OnToDoClickListener listener;

    public interface OnToDoClickListener {
        void onToDoClick(ToDo toDo);
    }

    public void setOnToDoClickListener(OnToDoClickListener l) { this.listener = l; }

    public ToDoAdapter(Context context) {
        this.context = context;
    }

    public void setToDoList(List<ToDo> toDoList) {
        this.toDoList = toDoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ToDoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo t = toDoList.get(position);

        holder.title.setText(t.getTitle() == null ? "" : t.getTitle());
        holder.description.setText(t.getDescription() == null ? "" : t.getDescription());

        Date d = t.getDueDate();
        String formatted = d == null ? "" : DateFormat.format("dd MMM yyyy", d).toString();
        holder.dueDate.setText(formatted);

        holder.expiryTime.setText(t.getExpiryTime() == null ? "" : t.getExpiryTime());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onToDoClick(t);
        });
    }

    @Override
    public int getItemCount() {
        return toDoList == null ? 0 : toDoList.size();
    }

    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, dueDate, expiryTime;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_todo_title);
            description = itemView.findViewById(R.id.tv_todo_description);
            dueDate = itemView.findViewById(R.id.tv_todo_due_date);
            expiryTime = itemView.findViewById(R.id.tv_todo_expiry_time);
        }
    }
}

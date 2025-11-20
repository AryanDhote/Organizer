package com.my.organizer.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.my.organizer.R;
import com.my.organizer.models.ToDo;

import java.util.Date;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDo> toDoList;
    private final Context context;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        // defensive checks
        if (toDoList == null || position < 0 || position >= toDoList.size()) return;

        ToDo toDo = toDoList.get(position);

        holder.title.setText(toDo.getTitle() != null ? toDo.getTitle() : "");
        holder.description.setText(toDo.getDescription() != null ? toDo.getDescription() : "");

        // map to your layout id tv_todo_due_date
        Date dueDate = toDo.getDueDate();
        if (dueDate != null) {
            CharSequence formattedDate = DateFormat.format("dd MMM yyyy", dueDate);
            holder.date.setText(formattedDate);
        } else {
            holder.date.setText(""); // or "No due date"
        }

        // Determine error container color resource if defined, otherwise fallback
        int errorContainerColorResId = context.getResources()
                .getIdentifier("md_theme_errorContainer", "color", context.getPackageName());
        int errorContainerColor = (errorContainerColorResId != 0)
                ? ContextCompat.getColor(context, errorContainerColorResId)
                : ContextCompat.getColor(context, android.R.color.holo_red_light);

        // highlight expired tasks (only if dueDate exists)
        Date now = new Date();
        if (dueDate != null && dueDate.before(now)) {
            holder.itemView.setBackgroundColor(errorContainerColor);
            holder.title.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
            holder.title.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return toDoList == null ? 0 : toDoList.size();
    }

    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date, expiryTime;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            // IDs now match your item_todo.xml
            title = itemView.findViewById(R.id.tv_todo_title);
            description = itemView.findViewById(R.id.tv_todo_description);
            date = itemView.findViewById(R.id.tv_todo_due_date);
            expiryTime = itemView.findViewById(R.id.tv_todo_expiry_time); // present in layout
        }
    }
}

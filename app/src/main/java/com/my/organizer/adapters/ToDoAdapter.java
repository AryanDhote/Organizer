package com.my.organizer.adapters;

import android.content.Context;
import android.graphics.Color;
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
        ToDo toDo = toDoList.get(position);

        holder.title.setText(toDo.getTitle());
        holder.description.setText(toDo.getDescription());

        Date dueDate = toDo.getDueDate();
        String formattedDate = DateFormat.format("dd MMM yyyy", dueDate).toString();
        holder.date.setText(formattedDate);

        // Highlight expired tasks
        Date now = new Date();
        if (dueDate != null && dueDate.before(now)) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_errorContainer));
            holder.title.setTextColor(Color.RED);
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return toDoList == null ? 0 : toDoList.size();
    }

    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            description = itemView.findViewById(R.id.text_description);
            date = itemView.findViewById(R.id.text_due_date);
        }
    }
}

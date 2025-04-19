package com.my.organizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.organizer.R;
import com.my.organizer.models.ToDo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDo> toDoList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    private final LayoutInflater inflater;

    public ToDoAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void submitList(List<ToDo> list) {
        this.toDoList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_todo, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo toDo = toDoList.get(position);
        holder.tvTitle.setText(toDo.getTitle());
        holder.tvDate.setText(dateFormat.format(toDo.getDate()));
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvToDoTitle);
            tvDate = itemView.findViewById(R.id.tvToDoDate);
        }
    }
}

package com.my.organizer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.my.organizer.R;
import com.my.organizer.models.ToDo;

import java.util.*;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.VH> {

    public void setSelectionEnabled(boolean b) {
    }

    public void setShowOverflow(boolean b) {
    }

    public interface OnToDoClickListener { void onClick(ToDo t); }
    public interface OnToDoLongClickListener { void onLongClick(ToDo t); }

    private final List<ToDo> list = new ArrayList<>();
    private final Set<Integer> selectedIds = new HashSet<>();

    private boolean selectionMode = false;

    private OnToDoClickListener clickListener;
    private OnToDoLongClickListener longClickListener;

    public void setToDoList(List<ToDo> data) {
        list.clear();
        if (data != null) list.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnToDoClickListener(OnToDoClickListener l) {
        clickListener = l;
    }

    public void setOnToDoLongClickListener(OnToDoLongClickListener l) {
        longClickListener = l;
    }

    public void startSelection(ToDo t) {
        selectionMode = true;
        toggleSelection(t);
    }

    public void toggleSelection(ToDo t) {
        int id = t.getId();
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);

        if (selectedIds.isEmpty()) selectionMode = false;

        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedIds.clear();
        selectionMode = false;
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedIds.size();
    }

    public List<ToDo> getSelectedItems() {
        List<ToDo> selected = new ArrayList<>();
        for (ToDo t : list) {
            if (selectedIds.contains(t.getId())) {
                selected.add(t);
            }
        }
        return selected;
    }

    public ToDo getItemAt(int pos) {
        return list.get(pos);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {

        ToDo t = list.get(pos);
        boolean selected = selectedIds.contains(t.getId());

        h.title.setText(t.getTitle());

        // ✅ FIX: show actual description (no hardcoded text)
        h.description.setText(t.getDescription() == null ? "" : t.getDescription());

        h.checkbox.setVisibility(selectionMode ? View.VISIBLE : View.GONE);
        h.checkbox.setChecked(selected);

        h.itemView.setOnClickListener(v -> {
            if (selectionMode) {
                toggleSelection(t);
                if (clickListener != null) clickListener.onClick(t);
            } else {
                if (clickListener != null) clickListener.onClick(t);
            }
        });

        h.itemView.setOnLongClickListener(v -> {
            startSelection(t);
            if (longClickListener != null) longClickListener.onLongClick(t);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView title, description;
        MaterialCheckBox checkbox;

        VH(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.tv_todo_title);
            description = v.findViewById(R.id.tv_todo_description);
            checkbox = v.findViewById(R.id.selection_checkbox);
        }
    }
}
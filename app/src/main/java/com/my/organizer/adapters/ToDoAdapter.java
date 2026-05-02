package com.my.organizer.adapters;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.my.organizer.R;
import com.my.organizer.models.ToDo;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FINAL FIXED ToDoAdapter
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.VH> {

    // ✅ INTERFACES (MISSING BEFORE)
    public interface ToDoClickListener {
        void onToDoClick(ToDo toDo);
    }

    public interface ToDoLongClickListener {
        void onToDoLongClick(ToDo toDo);
    }

    public interface ToDoActionListener {
        void onEditToDo(ToDo toDo);
        void onDeleteToDo(ToDo toDo);
    }

    private final List<ToDo> list = new ArrayList<>();
    private final Set<Integer> selectedIds = new HashSet<>();

    private ToDoClickListener clickListener;
    private ToDoLongClickListener longClickListener;
    private ToDoActionListener actionListener;

    private boolean selectionEnabled = true;
    private boolean showOverflow = true;

    private final SimpleDateFormat format =
            new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    // ✅ FIX: ADD CONSTRUCTOR WITH CONTEXT
    public ToDoAdapter() {}

    public ToDoAdapter(android.content.Context context) {}

    // ========================
    // SETTERS (MISSING BEFORE)
    // ========================
    public void setOnToDoClickListener(ToDoClickListener l) {
        this.clickListener = l;
    }

    public void setOnToDoLongClickListener(ToDoLongClickListener l) {
        this.longClickListener = l;
    }

    public void setOnToDoActionListener(ToDoActionListener l) {
        this.actionListener = l;
    }

    public void setSelectionEnabled(boolean enabled) {
        this.selectionEnabled = enabled;
        if (!enabled) clearSelection();
        notifyDataSetChanged();
    }

    public void setShowOverflow(boolean show) {
        this.showOverflow = show;
        notifyDataSetChanged();
    }

    public void setToDoList(List<ToDo> data) {
        list.clear();
        if (data != null) list.addAll(data);
        notifyDataSetChanged();
    }

    // ========================
    // ADAPTER METHODS
    // ========================
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {

        ToDo t = list.get(pos);

        h.title.setText(t.getTitle());
        h.desc.setText(t.getDescription());
        h.date.setText(t.getDueDate() != null ? format.format(t.getDueDate()) : "");
        h.time.setText(t.getExpiryTime());

        boolean selected = selectedIds.contains(t.getId());

        if (selectionEnabled) {
            h.overlay.setVisibility(selected ? View.VISIBLE : View.GONE);
            h.checkbox.setChecked(selected);
        } else {
            h.overlay.setVisibility(View.GONE);
            h.checkbox.setChecked(false);
        }

        // CLICK
        h.itemView.setOnClickListener(v -> {
            if (selectionEnabled && !selectedIds.isEmpty()) {
                toggleSelection(t);
            } else if (clickListener != null) {
                clickListener.onToDoClick(t);
            }
        });

        // LONG CLICK
        h.itemView.setOnLongClickListener(v -> {
            if (!selectionEnabled) return false;
            toggleSelection(t);
            if (longClickListener != null) {
                longClickListener.onToDoLongClick(t);
            }
            return true;
        });

        // MENU
        if (h.menu != null) {
            h.menu.setVisibility(showOverflow ? View.VISIBLE : View.GONE);

            h.menu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenu().add("Edit");
                popup.getMenu().add("Delete");

                popup.setOnMenuItemClickListener(item -> {
                    if (actionListener == null) return false;

                    if (item.getTitle().equals("Edit")) {
                        actionListener.onEditToDo(t);
                    } else {
                        actionListener.onDeleteToDo(t);
                    }
                    return true;
                });

                popup.show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ========================
    // SELECTION METHODS (MISSING BEFORE)
    // ========================
    public void toggleSelection(ToDo t) {
        int id = t.getId();
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);
        notifyDataSetChanged();
    }

    public List<ToDo> getSelectedItems() {
        List<ToDo> result = new ArrayList<>();
        for (ToDo t : list) {
            if (selectedIds.contains(t.getId())) result.add(t);
        }
        return result;
    }

    public int getSelectedCount() {
        return selectedIds.size();
    }

    public void clearSelection() {
        selectedIds.clear();
        notifyDataSetChanged();
    }

    public ToDo getItemAt(int pos) {
        return list.get(pos);
    }

    // ========================
    // VIEW HOLDER
    // ========================
    static class VH extends RecyclerView.ViewHolder {

        TextView title, desc, date, time;
        View overlay;
        MaterialCheckBox checkbox;
        ImageButton menu;
        CardView root;

        VH(@NonNull View v) {
            super(v);

            root = v.findViewById(R.id.card_root);
            title = v.findViewById(R.id.tv_todo_title);
            desc = v.findViewById(R.id.tv_todo_description);
            date = v.findViewById(R.id.tv_todo_due_date);
            time = v.findViewById(R.id.tv_todo_expiry_time);

            overlay = v.findViewById(R.id.selection_overlay);
            checkbox = v.findViewById(R.id.selection_checkbox_overlay);

            // optional menu
            View m = v.findViewById(R.id.btn_show_todos);
            menu = (m instanceof ImageButton) ? (ImageButton) m : null;
        }
    }
}
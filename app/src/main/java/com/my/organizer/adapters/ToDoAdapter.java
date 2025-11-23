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

import com.google.android.material.chip.Chip;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.my.organizer.R;
import com.my.organizer.models.ToDo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Robust ToDoAdapter: tolerant to missing optional views in item_todo.xml.
 * Works when overflow button is present or absent.
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.VH> {

    public interface ToDoClickListener { void onToDoClick(ToDo toDo); }
    public interface ToDoLongClickListener { void onToDoLongClick(ToDo toDo); }
    public interface ToDoActionListener { void onEditToDo(ToDo toDo); void onDeleteToDo(ToDo toDo); }

    private final List<ToDo> items = new ArrayList<>();
    private final Set<Integer> selectedIds = new HashSet<>();

    private ToDoClickListener clickListener;
    private ToDoLongClickListener longClickListener;
    private ToDoActionListener actionListener;

    private boolean selectionEnabled = true;
    private boolean showOverflow = true;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public ToDoAdapter() {}

    public ToDoAdapter(android.content.Context ctx) { this(); }

    // runtime controls
    public void setSelectionEnabled(boolean enabled) {
        this.selectionEnabled = enabled;
        if (!enabled) clearSelection();
        notifyDataSetChanged();
    }

    public void setShowOverflow(boolean show) {
        this.showOverflow = show;
        notifyDataSetChanged();
    }

    public void setToDoList(List<ToDo> list) {
        items.clear();
        if (list != null) items.addAll(list);
        selectedIds.retainAll(getAllIds());
        notifyDataSetChanged();
    }

    public void setOnToDoClickListener(ToDoClickListener l) { this.clickListener = l; }
    public void setOnToDoLongClickListener(ToDoLongClickListener l) { this.longClickListener = l; }
    public void setOnToDoActionListener(ToDoActionListener l) { this.actionListener = l; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ToDo t = items.get(position);

        // Title / description / date / time
        holder.title.setText(t.getTitle() == null ? "" : t.getTitle());
        holder.desc.setText(t.getDescription() == null ? "" : t.getDescription());
        holder.desc.setMaxLines(3);

        Date due = t.getDueDate();
        holder.dueDate.setText(due == null ? "" : dateFormat.format(due));
        holder.expiryTime.setText(t.getExpiryTime() == null ? "" : t.getExpiryTime());

        // priority chip if present
        if (holder.priorityChip != null) {
            holder.priorityChip.setVisibility(View.GONE); // default hidden; your logic can set visible
        }

        // overflow button might be absent in layout; guard against null
        if (holder.btnMore != null) {
            holder.btnMore.setVisibility(showOverflow ? View.VISIBLE : View.GONE);
        }

        // selection visuals
        boolean selected = selectedIds.contains(getId(t));
        if (selectionEnabled) {
            if (holder.selectionOverlay != null) holder.selectionOverlay.setVisibility(selected ? View.VISIBLE : View.GONE);
            if (holder.selectionCheckbox != null) holder.selectionCheckbox.setChecked(selected);
        } else {
            if (holder.selectionOverlay != null) holder.selectionOverlay.setVisibility(View.GONE);
            if (holder.selectionCheckbox != null) holder.selectionCheckbox.setChecked(false);
        }

        // Click / long click
        holder.itemView.setOnClickListener(v -> {
            if (selectionEnabled && !selectedIds.isEmpty()) {
                toggleSelection(t);
                if (longClickListener != null) longClickListener.onToDoLongClick(t);
            } else {
                if (clickListener != null) clickListener.onToDoClick(t);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (!selectionEnabled) return false;
            if (longClickListener != null) {
                longClickListener.onToDoLongClick(t);
                toggleSelection(t);
                return true;
            } else {
                toggleSelection(t);
                return true;
            }
        });

        // Overflow popup (if present)
        if (holder.btnMore != null) {
            holder.btnMore.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(holder.btnMore.getContext(), holder.btnMore);
                popup.getMenu().add(0, 0, 0, "Edit");
                popup.getMenu().add(0, 1, 1, "Delete");
                popup.setOnMenuItemClickListener((MenuItem item) -> {
                    if (actionListener == null) return false;
                    if (item.getItemId() == 0) {
                        actionListener.onEditToDo(t);
                        return true;
                    } else if (item.getItemId() == 1) {
                        actionListener.onDeleteToDo(t);
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    // Selection helpers
    public void toggleSelection(ToDo t) {
        int id = getId(t);
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);
        int pos = indexOfId(id);
        if (pos != -1) notifyItemChanged(pos);
    }

    public void toggleSelectionById(int id) {
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);
        int pos = indexOfId(id);
        if (pos != -1) notifyItemChanged(pos);
    }

    public List<ToDo> getSelectedItems() {
        List<ToDo> sel = new ArrayList<>();
        for (ToDo t : items) if (selectedIds.contains(getId(t))) sel.add(t);
        return sel;
    }

    public int getSelectedCount() { return selectedIds.size(); }

    public void clearSelection() {
        if (selectedIds.isEmpty()) return;
        List<Integer> copy = new ArrayList<>(selectedIds);
        selectedIds.clear();
        for (Integer id : copy) {
            int idx = indexOfId(id);
            if (idx != -1) notifyItemChanged(idx);
        }
    }

    public ToDo getItemAt(int pos) {
        return (pos >= 0 && pos < items.size()) ? items.get(pos) : null;
    }

    private int getId(ToDo t) {
        return t == null ? -1 : t.getId();
    }

    private int indexOfId(int id) {
        for (int i = 0; i < items.size(); i++) if (getId(items.get(i)) == id) return i;
        return -1;
    }

    private Set<Integer> getAllIds() {
        Set<Integer> s = new HashSet<>();
        for (ToDo t : items) s.add(getId(t));
        return s;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, desc, dueDate, expiryTime;
        View selectionOverlay;
        MaterialCheckBox selectionCheckbox; // optional
        ImageButton btnMore; // optional overflow
        Chip priorityChip; // optional
        CardView root;

        VH(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.card_root);

            // required views (must exist in item_todo.xml)
            title = safeFindText(itemView, R.id.tv_todo_title);
            desc = safeFindText(itemView, R.id.tv_todo_description);
            dueDate = safeFindText(itemView, R.id.tv_todo_due_date);
            expiryTime = safeFindText(itemView, R.id.tv_todo_expiry_time);

            // optional views (may be absent depending on layout)
            selectionOverlay = itemView.findViewById(R.id.selection_overlay);
            selectionCheckbox = itemView.findViewById(R.id.selection_checkbox_overlay) instanceof MaterialCheckBox
                    ? (MaterialCheckBox) itemView.findViewById(R.id.selection_checkbox_overlay)
                    : (itemView.findViewById(R.id.selection_checkbox) instanceof MaterialCheckBox
                    ? (MaterialCheckBox) itemView.findViewById(R.id.selection_checkbox)
                    : null);

            View maybeBtn = itemView.findViewById(R.id.btn_show_todos);
            btnMore = (maybeBtn instanceof ImageButton) ? (ImageButton) maybeBtn : null;

            View maybeChip = itemView.findViewById(R.id.chip_priority);
            priorityChip = (maybeChip instanceof Chip) ? (Chip) maybeChip : null;
        }

        // helper to avoid NullPointerException when findViewById returns null
        private TextView safeFindText(View root, int id) {
            View v = root.findViewById(id);
            return (v instanceof TextView) ? (TextView) v : new TextView(root.getContext());
        }
    }
}

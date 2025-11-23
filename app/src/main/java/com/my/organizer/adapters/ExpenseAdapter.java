package com.my.organizer.adapters;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.my.organizer.R;
import com.my.organizer.models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Defensive ExpenseAdapter:
 * - Guards optional views (btn_more, chips, overlay checkbox) before using them.
 * - Supports multi-select via selectedIds set.
 * - Keeps same external API: setExpenseList(), setOnExpenseClickListener(), setOnExpenseLongClickListener().
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.VH> {

    public interface OnExpenseClickListener { void onExpenseClick(Expense e); }
    public interface OnExpenseLongClickListener { void onExpenseLongClick(Expense e); }
    public interface OnExpenseActionListener { void onEditExpense(Expense e); void onDeleteExpense(Expense e); }

    private final List<Expense> items = new ArrayList<>();
    private final Set<Integer> selectedIds = new HashSet<>();

    private OnExpenseClickListener clickListener;
    private OnExpenseLongClickListener longClickListener;
    private OnExpenseActionListener actionListener;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private boolean selectionEnabled = true;
    private boolean showOverflow = true;

    public ExpenseAdapter() {}

    public void setExpenseList(List<Expense> list) {
        items.clear();
        if (list != null) items.addAll(list);
        selectedIds.retainAll(getAllIds());
        notifyDataSetChanged();
    }

    public void setOnExpenseClickListener(OnExpenseClickListener l) { this.clickListener = l; }
    public void setOnExpenseLongClickListener(OnExpenseLongClickListener l) { this.longClickListener = l; }
    public void setOnExpenseActionListener(OnExpenseActionListener l) { this.actionListener = l; }

    public void setSelectionEnabled(boolean enabled) {
        this.selectionEnabled = enabled;
        if (!enabled) clearSelection();
        notifyDataSetChanged();
    }

    public void setShowOverflow(boolean show) {
        this.showOverflow = show;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Expense e = items.get(position);

        // Title (note) / amount / date / category
        holder.title.setText(e.getNote() == null || e.getNote().isEmpty() ? "Expense" : e.getNote());
        holder.amount.setText(String.format(Locale.getDefault(), "₹%.2f", e.getAmount()));

        Date d = e.getDate();
        holder.date.setText(d == null ? "" : dateFormat.format(d));

        // Category chip (if present)
        if (holder.categoryChip != null) {
            String cat = e.getType() == null ? "Other" : e.getType().name();
            holder.categoryChip.setText(cat);
            holder.categoryChip.setVisibility(View.VISIBLE);
        }

        // Description text (if present)
        if (holder.description != null) {
            holder.description.setText(e.getNote() == null ? "" : e.getNote());
            holder.description.setMaxLines(3);
        }

        // Overflow button may be absent in layout — guard it
        if (holder.btnMore != null) {
            holder.btnMore.setVisibility(showOverflow ? View.VISIBLE : View.GONE);
        }

        // Selection visuals
        boolean selected = selectedIds.contains(getId(e));
        if (selectionEnabled) {
            if (holder.selectionOverlay != null) holder.selectionOverlay.setVisibility(selected ? View.VISIBLE : View.GONE);
            if (holder.selectionCheckbox != null) holder.selectionCheckbox.setChecked(selected);
        } else {
            if (holder.selectionOverlay != null) holder.selectionOverlay.setVisibility(View.GONE);
            if (holder.selectionCheckbox != null) holder.selectionCheckbox.setChecked(false);
        }

        // Click handling
        holder.itemView.setOnClickListener(v -> {
            if (selectionEnabled && !selectedIds.isEmpty()) {
                toggleSelection(e);
                if (longClickListener != null) longClickListener.onExpenseLongClick(e);
            } else {
                if (clickListener != null) clickListener.onExpenseClick(e);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (!selectionEnabled) return false;
            if (longClickListener != null) {
                longClickListener.onExpenseLongClick(e);
                toggleSelection(e);
                return true;
            } else {
                toggleSelection(e);
                return true;
            }
        });

        // Overflow popup (if present)
        if (holder.btnMore != null) {
            holder.btnMore.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(holder.btnMore.getContext(), holder.btnMore);
                popup.getMenu().add(0, 0, 0, "Edit");
                popup.getMenu().add(0, 1, 1, "Delete");
                popup.setOnMenuItemClickListener((MenuItem item1) -> {
                    if (actionListener == null) return false;
                    if (item1.getItemId() == 0) {
                        actionListener.onEditExpense(e);
                        return true;
                    } else if (item1.getItemId() == 1) {
                        actionListener.onDeleteExpense(e);
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

    // selection helpers
    public void toggleSelection(Expense e) {
        int id = getId(e);
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);
        int idx = indexOfId(id);
        if (idx != -1) notifyItemChanged(idx);
    }

    public List<Expense> getSelectedItems() {
        List<Expense> sel = new ArrayList<>();
        for (Expense e : items) {
            if (selectedIds.contains(getId(e))) sel.add(e);
        }
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

    public Expense getItemAt(int pos) {
        return (pos >= 0 && pos < items.size()) ? items.get(pos) : null;
    }

    private int getId(Expense e) {
        return e == null ? -1 : e.getId();
    }

    private int indexOfId(int id) {
        for (int i = 0; i < items.size(); i++) if (getId(items.get(i)) == id) return i;
        return -1;
    }

    private Set<Integer> getAllIds() {
        Set<Integer> s = new HashSet<>();
        for (Expense e : items) s.add(getId(e));
        return s;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, amount, date, description;
        View selectionOverlay;
        MaterialCheckBox selectionCheckbox; // optional
        ImageButton btnMore; // optional
        Chip categoryChip; // optional

        VH(@NonNull View itemView) {
            super(itemView);
            // required ids expected in your item_expense.xml
            title = safeFindText(itemView, R.id.tv_expense_title);
            amount = safeFindText(itemView, R.id.tv_expense_amount);
            date = safeFindText(itemView, R.id.tv_expense_date);
            description = safeFindText(itemView, R.id.tv_expense_description);

            // optional: selection overlay / checkbox (may not exist in older layout)
            View overlay = itemView.findViewById(R.id.selection_overlay);
            selectionOverlay = overlay;

            View maybeCheck = itemView.findViewById(R.id.selection_checkbox_overlay);
            if (maybeCheck instanceof MaterialCheckBox) selectionCheckbox = (MaterialCheckBox) maybeCheck;
            else {
                View maybeCheck2 = itemView.findViewById(R.id.selection_checkbox);
                if (maybeCheck2 instanceof MaterialCheckBox) selectionCheckbox = (MaterialCheckBox) maybeCheck2;
                else selectionCheckbox = null;
            }

            // optional overflow button
            View maybeBtn = itemView.findViewById(R.id.btn_show_expenses);
            btnMore = (maybeBtn instanceof ImageButton) ? (ImageButton) maybeBtn : null;

            // optional category chip
            View maybeChip = itemView.findViewById(R.id.tv_expense_category);
            categoryChip = (maybeChip instanceof Chip) ? (Chip) maybeChip : null;
        }

        // safe text lookup (returns empty TextView if missing to avoid NPEs)
        private TextView safeFindText(View root, int id) {
            View v = root.findViewById(id);
            return (v instanceof TextView) ? (TextView) v : new TextView(root.getContext());
        }
    }
}

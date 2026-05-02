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

import com.google.android.material.checkbox.MaterialCheckBox;
import com.my.organizer.R;
import com.my.organizer.models.Expense;

import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.VH> {

    // ---------------- INTERFACES ----------------
    public interface OnExpenseClickListener { void onExpenseClick(Expense e); }
    public interface OnExpenseLongClickListener { void onExpenseLongClick(Expense e); }
    public interface OnExpenseActionListener {
        void onEditExpense(Expense e);
        void onDeleteExpense(Expense e);
    }

    private final List<Expense> items = new ArrayList<>();
    private final Set<Integer> selectedIds = new HashSet<>();

    private OnExpenseClickListener clickListener;
    private OnExpenseLongClickListener longClickListener;
    private OnExpenseActionListener actionListener;

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    private boolean selectionMode = false;
    private boolean showOverflow = true;

    // ---------------- REQUIRED METHODS (FIX FOR YOUR ERRORS) ----------------

    public void setSelectionEnabled(boolean enabled) {
        this.selectionMode = false;
        if (!enabled) {
            selectedIds.clear();
        }
        notifyDataSetChanged();
    }

    public void setShowOverflow(boolean show) {
        this.showOverflow = show;
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

    public Expense getItemAt(int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    public List<Expense> getSelectedItems() {
        List<Expense> list = new ArrayList<>();
        for (Expense e : items) {
            if (selectedIds.contains(e.getId())) {
                list.add(e);
            }
        }
        return list;
    }

    // IMPORTANT: must be PUBLIC (your error)
    public void toggleSelection(Expense e) {
        int id = e.getId();
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);
    }

    // ---------------- DATA ----------------

    public void setExpenseList(List<Expense> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnExpenseClickListener(OnExpenseClickListener l) { clickListener = l; }
    public void setOnExpenseLongClickListener(OnExpenseLongClickListener l) { longClickListener = l; }
    public void setOnExpenseActionListener(OnExpenseActionListener l) { actionListener = l; }

    // ---------------- VIEW HOLDER ----------------

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        Expense e = items.get(position);
        boolean selected = selectedIds.contains(e.getId());

        holder.title.setText(e.getNote() == null ? "Expense" : e.getNote());
        holder.amount.setText(String.format(Locale.getDefault(), "₹%.2f", e.getAmount()));
        holder.date.setText(e.getDate() == null ? "" : dateFormat.format(e.getDate()));
        holder.description.setText(e.getNote() == null ? "" : e.getNote());

        // ---------------- CHECKBOX LOGIC ----------------
        if (selectionMode) {
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(selected);
        } else {
            holder.checkbox.setVisibility(View.GONE);
        }

        holder.overlay.setVisibility(selected ? View.VISIBLE : View.GONE);

        // ---------------- CLICK ----------------
        holder.itemView.setOnClickListener(v -> {

            if (selectionMode) {
                toggleSelection(e);
                notifyDataSetChanged();

                if (selectedIds.isEmpty()) {
                    selectionMode = false;
                }

                notifyDataSetChanged();

            } else {
                if (clickListener != null)
                    clickListener.onExpenseClick(e);
            }
        });

        // ---------------- LONG PRESS ----------------
        holder.itemView.setOnLongClickListener(v -> {
            selectionMode = true;
            toggleSelection(e);
            notifyDataSetChanged();

            if (longClickListener != null)
                longClickListener.onExpenseLongClick(e);

            return true;
        });

        // ---------------- MENU ----------------
        if (holder.more != null) {
            holder.more.setVisibility(showOverflow ? View.VISIBLE : View.GONE);

            holder.more.setOnClickListener(v -> {
                PopupMenu menu = new PopupMenu(holder.more.getContext(), holder.more);
                menu.getMenu().add("Edit");
                menu.getMenu().add("Delete");

                menu.setOnMenuItemClickListener(item -> {
                    if (actionListener == null) return false;

                    if (item.getTitle().equals("Edit")) {
                        actionListener.onEditExpense(e);
                        return true;
                    } else {
                        actionListener.onDeleteExpense(e);
                        return true;
                    }
                });

                menu.show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ---------------- VIEW HOLDER ----------------

    static class VH extends RecyclerView.ViewHolder {

        TextView title, amount, date, description;
        MaterialCheckBox checkbox;
        View overlay;
        ImageButton more;

        VH(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_expense_title);
            amount = itemView.findViewById(R.id.tv_expense_amount);
            date = itemView.findViewById(R.id.tv_expense_date);
            description = itemView.findViewById(R.id.tv_expense_description);

            checkbox = itemView.findViewById(R.id.selection_checkbox);
            overlay = itemView.findViewById(R.id.selection_overlay);
            more = itemView.findViewById(R.id.btn_show_expenses);
        }
    }
}
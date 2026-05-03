package com.my.organizer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.my.organizer.R;
import com.my.organizer.models.Expense;

import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.VH> {

    public void setSelectionEnabled(boolean b) {
    }

    public void setShowOverflow(boolean b) {

    }

    public interface OnExpenseClickListener { void onClick(Expense e); }
    public interface OnExpenseLongClickListener { void onLongClick(Expense e); }

    private final List<Expense> list = new ArrayList<>();
    private final Set<Integer> selectedIds = new HashSet<>();

    private OnExpenseClickListener clickListener;
    private OnExpenseLongClickListener longClickListener;

    private boolean selectionMode = false;

    private final SimpleDateFormat df =
            new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    // ---------------- PUBLIC METHODS ----------------

    public void setExpenseList(List<Expense> data) {
        list.clear();
        if (data != null) list.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnExpenseClickListener(OnExpenseClickListener l) {
        clickListener = l;
    }

    public void setOnExpenseLongClickListener(OnExpenseLongClickListener l) {
        longClickListener = l;
    }

    public void toggleSelection(Expense e) {
        int id = e.getId();
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);

        if (selectedIds.isEmpty()) selectionMode = false;

        notifyDataSetChanged();
    }

    public void startSelection(Expense e) {
        selectionMode = true;
        toggleSelection(e);
    }

    public void clearSelection() {
        selectedIds.clear();
        selectionMode = false;
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedIds.size();
    }

    public List<Expense> getSelectedItems() {
        List<Expense> selected = new ArrayList<>();
        for (Expense e : list) {
            if (selectedIds.contains(e.getId())) {
                selected.add(e);
            }
        }
        return selected;
    }

    public Expense getItemAt(int pos) {
        return list.get(pos);
    }

    // ---------------- ADAPTER ----------------

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {

        Expense e = list.get(pos);
        boolean selected = selectedIds.contains(e.getId());

        h.title.setText(e.getNote() == null ? "Expense" : e.getNote());
        h.amount.setText("₹" + e.getAmount());
        h.date.setText(e.getDate() == null ? "" : df.format(e.getDate()));

        // ✅ Checkbox only in selection mode
        h.checkbox.setVisibility(selectionMode ? View.VISIBLE : View.GONE);
        h.checkbox.setChecked(selected);

        // CLICK
        h.itemView.setOnClickListener(v -> {
            if (selectionMode) {
                toggleSelection(e);
                if (clickListener != null) clickListener.onClick(e);
            } else {
                if (clickListener != null) clickListener.onClick(e);
            }
        });

        // LONG CLICK
        h.itemView.setOnLongClickListener(v -> {
            startSelection(e);
            if (longClickListener != null) longClickListener.onLongClick(e);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView title, amount, date;
        MaterialCheckBox checkbox;

        VH(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.tv_expense_title);
            amount = v.findViewById(R.id.tv_expense_amount);
            date = v.findViewById(R.id.tv_expense_date);
            checkbox = v.findViewById(R.id.selection_checkbox);
        }
    }
}
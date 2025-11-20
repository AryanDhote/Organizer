package com.my.organizer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.my.organizer.R;
import com.my.organizer.models.Expense;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private OnExpenseClickListener onExpenseClickListener;

    // Interface for handling clicks
    public interface OnExpenseClickListener {
        void onExpenseClick(Expense expense);
    }

    // Method to set the listener
    public void setOnExpenseClickListener(OnExpenseClickListener listener) {
        this.onExpenseClickListener = listener;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int pos) {
        Expense e = expenseList.get(pos);
        holder.amount.setText(String.format("₹%.2f", e.getAmount()));
        holder.category.setText(e.getType().name());  // enum → String
        holder.note.setText(e.getNote());
        holder.date.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(e.getDate()));

        // Set click listener on item view
        holder.itemView.setOnClickListener(v -> {
            if (onExpenseClickListener != null) {
                onExpenseClickListener.onExpenseClick(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (expenseList == null) ? 0 : expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView amount, date, category, note;
        ExpenseViewHolder(@NonNull View v) {
            super(v);
            amount   = v.findViewById(R.id.input_amount);
            date     = v.findViewById(R.id.text_date);
            category = v.findViewById(R.id.spinner_category);
            note     = v.findViewById(R.id.input_note);
        }
    }
}

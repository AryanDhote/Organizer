package com.my.organizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.organizer.R;
import com.my.organizer.models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    private final LayoutInflater inflater;

    public ExpenseAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void submitList(List<Expense> list) {
        this.expenseList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvAmount.setText("₹" + expense.getAmount());
        holder.tvDescription.setText(expense.getDescription());
        holder.tvDate.setText(dateFormat.format(expense.getDate()));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvDescription, tvDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvExpanseAmount);
            tvDescription = itemView.findViewById(R.id.tvExpanseDescription);
            tvDate = itemView.findViewById(R.id.tvExpanseDate);
        }
    }
}

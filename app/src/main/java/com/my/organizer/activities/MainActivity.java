package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.my.organizer.R;
import com.my.organizer.fragments.ExpenseListFragment;
import com.my.organizer.fragments.FragmentHome;
import com.my.organizer.fragments.ToDoListFragment;
import com.my.organizer.models.Expense;
import com.my.organizer.models.ToDo;

public class MainActivity extends AppCompatActivity
        implements FragmentHome.OnHomeActionListener,
        ToDoListFragment.OnToDoListActionListener,
        ExpenseListFragment.OnExpenseListActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add home fragment only once
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FragmentHome())
                    .commit();
        }
    }

    // Utility to show a fragment and add to back stack
    private void showFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    // ----------------------------
    // FragmentHome.OnHomeActionListener
    // ----------------------------
    @Override
    public void onAddToDo() {
        startActivity(new Intent(this, AddEditToDoActivity.class));
    }

    @Override
    public void onAddExpense() {
        startActivity(new Intent(this, AddEditExpenseActivity.class));
    }

    @Override
    public void onShowAllToDos() {
        showFragment(new ToDoListFragment(), "ToDoListFragment");
    }

    @Override
    public void onShowAllExpenses() {
        showFragment(new ExpenseListFragment(), "ExpenseListFragment");
    }

    @Override
    public void onEditToDo(ToDo toDo) {
        if (toDo == null) return;
        Intent i = new Intent(this, AddEditToDoActivity.class);
        i.putExtra(AddEditToDoActivity.EXTRA_TODO, toDo);
        startActivity(i);
    }

    @Override
    public void onEditExpense(Expense expense) {
        if (expense == null) return;
        Intent i = new Intent(this, AddEditExpenseActivity.class);
        i.putExtra(AddEditExpenseActivity.EXTRA_EXPENSE, expense);
        startActivity(i);
    }

    // ----------------------------
    // ToDoListFragment.OnToDoListActionListener
    // ----------------------------
    // These are the same actions as above; delegate to the same methods
    @Override
    public void onAddToDoFromList() {
        onAddToDo();
    }

    @Override
    public void onEditToDoFromList(ToDo toDo) {
        onEditToDo(toDo);
    }

    // ----------------------------
    // ExpenseListFragment.OnExpenseListActionListener
    // ----------------------------
    @Override
    public void onAddExpenseFromList() {
        onAddExpense();
    }

    @Override
    public void onEditExpenseFromList(Expense expense) {
        onEditExpense(expense);
    }

    // Back press: pop fragments first
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

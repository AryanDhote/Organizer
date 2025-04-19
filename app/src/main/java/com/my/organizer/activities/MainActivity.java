package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.my.organizer.R;
import com.my.organizer.fragments.FragmentHome;
import com.my.organizer.fragments.ToDoListFragment;
import com.my.organizer.fragments.ExpenseListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadFragment(new FragmentHome());
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void navigateToToDoList() {
        loadFragment(new ToDoListFragment());
    }

    public void navigateToExpenseList() {
        loadFragment(new ExpenseListFragment());
    }

    public void navigateToAddExpense() {
        Intent intent = new Intent(this, AddEditExpenseActivity.class);
        startActivity(intent);
    }

    public void navigateToAddToDo() {
        Intent intent = new Intent(this, AddEditToDoActivity.class);
        startActivity(intent);
    }
}

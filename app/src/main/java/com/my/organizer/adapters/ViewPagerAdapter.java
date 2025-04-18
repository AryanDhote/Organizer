package com.my.organizer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.my.organizer.fragments.ToDoListFragment;
import com.my.organizer.fragments.ExpenseListFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity activity) { // Accept FragmentActivity
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (position == 0) ? new ToDoListFragment() : new ExpenseListFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // Two tabs: To-Do and Expense
    }
}

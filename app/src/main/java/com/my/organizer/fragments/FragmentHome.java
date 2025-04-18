package com.my.organizer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.my.organizer.R;
import com.my.organizer.activities.AddEditExpenseActivity;
import com.my.organizer.activities.AddEditToDoActivity;
import com.my.organizer.adapters.ViewPagerAdapter;

public class FragmentHome extends Fragment {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabAddToDo, fabAddExpense;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        fabAddToDo = view.findViewById(R.id.fabAddToDo);
        fabAddExpense = view.findViewById(R.id.fabAddExpense);

        // Set up ViewPager2 with Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        // Add Tabs
        tabLayout.addTab(tabLayout.newTab().setText("To-Do").setContentDescription("To-Do Tab"));
        tabLayout.addTab(tabLayout.newTab().setText("Expenses").setContentDescription("Expenses Tab"));

        // Sync ViewPager2 with TabLayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                updateFABVisibility(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
                updateFABVisibility(position);
            }
        });

        // Handle Floating Action Button Clicks
        fabAddToDo.setOnClickListener(v -> {
            Log.d("FragmentHome", "Add To-Do clicked");
            Toast.makeText(getContext(), "Adding a new To-Do", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), AddEditToDoActivity.class));
        });

        fabAddExpense.setOnClickListener(v -> {
            Log.d("FragmentHome", "Add Expense clicked");
            Toast.makeText(getContext(), "Adding a new Expense", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), AddEditExpenseActivity.class));
        });

        return view;
    }

    private void updateFABVisibility(int position) {
        if (position == 0) {
            fabAddToDo.setVisibility(View.VISIBLE);
            fabAddExpense.setVisibility(View.GONE);
        } else {
            fabAddToDo.setVisibility(View.GONE);
            fabAddExpense.setVisibility(View.VISIBLE);
        }
    }
}

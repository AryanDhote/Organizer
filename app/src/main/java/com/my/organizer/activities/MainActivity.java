package com.my.organizer.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.my.organizer.R;
import com.my.organizer.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Set up ViewPager with Adapter
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set text for the tab
            if (position == 0) {
                tab.setText("To-Do List");  // Text for the To-Do List Tab
                tab.setContentDescription("To-Do List Tab");  // Content description for accessibility
            } else {
                tab.setText("Expenses");  // Text for the Expenses Tab
                tab.setContentDescription("Expenses Tab");  // Content description for accessibility
            }

            // Optional: Add icons if needed
            // You can uncomment and add icons for each tab if you want to further enhance the experience.
            // if (position == 0) {
            //     tab.setIcon(R.drawable.ic_todolist);  // Icon for To-Do List Tab
            // } else {
            //     tab.setIcon(R.drawable.ic_expense);  // Icon for Expenses Tab
            // }
        }).attach();
    }
}

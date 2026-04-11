package com.surajpetwal.tasktracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.surajpetwal.tasktracker.ui.MainPagerAdapter
import com.surajpetwal.tasktracker.utils.DatabaseTest

class MainActivity : AppCompatActivity() {
    
    private lateinit var viewPager: androidx.viewpager2.widget.ViewPager2
    private lateinit var tabLayout: com.google.android.material.tabs.TabLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupViewPager()
        
        // Run comprehensive database tests
        val databaseTest = DatabaseTest(this)
        databaseTest.runAllTests()
    }
    
    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
    }
    
    private fun setupViewPager() {
        val adapter = MainPagerAdapter(this)
        viewPager.adapter = adapter
        
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Daily"
                1 -> "Calendar"
                2 -> "Stats"
                else -> "Daily"
            }
        }.attach()
    }
}

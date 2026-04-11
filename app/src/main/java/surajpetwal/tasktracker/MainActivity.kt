package com.surajpetwal.tasktracker

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.surajpetwal.tasktracker.dialog.NotificationSettingsDialog
import com.surajpetwal.tasktracker.ui.MainPagerAdapter
import com.surajpetwal.tasktracker.utils.DatabaseTest
import com.surajpetwal.tasktracker.utils.NotificationScheduler

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
        
        // Initialize notifications
        initializeNotifications()
    }
    
    private fun initializeNotifications() {
        val prefs = getSharedPreferences("notification_prefs", 0)
        val scheduler = NotificationScheduler(this)
        
        if (prefs.getBoolean("daily_summary_enabled", true)) {
            val hour = prefs.getInt("daily_summary_hour", 9)
            val minute = prefs.getInt("daily_summary_minute", 0)
            scheduler.scheduleDailySummary(hour, minute)
        }
        
        if (prefs.getBoolean("task_reminders_enabled", true)) {
            scheduler.scheduleTaskReminders()
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notification_settings -> {
                NotificationSettingsDialog.newInstance().show(supportFragmentManager, "notification_settings")
                true
            }
            else -> super.onOptionsItemSelected(item)
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

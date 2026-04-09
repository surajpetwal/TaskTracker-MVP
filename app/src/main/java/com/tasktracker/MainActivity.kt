package com.tasktracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tasktracker.utils.DatabaseTest

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Run comprehensive database tests
        val databaseTest = DatabaseTest(this)
        databaseTest.runAllTests()
    }
}

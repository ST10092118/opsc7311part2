package com.opsc7311.opsc7311poepart2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.opsc7311.opsc7311poepart2.fragments.EntriesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        val EntriesFragment = EntriesFragment()

        makeCurrentFragment(EntriesFragment)

        bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.entries_mi -> makeCurrentFragment(EntriesFragment)
            }
            true

        }

    }
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }



    }


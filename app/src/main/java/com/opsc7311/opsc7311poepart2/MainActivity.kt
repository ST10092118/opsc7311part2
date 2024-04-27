package com.opsc7311.opsc7311poepart2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.opsc7311.opsc7311poepart2.fragments.EntriesFragment
import com.opsc7311.opsc7311poepart2.databinding.ActivityMainBinding
import com.opsc7311.opsc7311poepart2.fragments.CalendarFragment
import com.opsc7311.opsc7311poepart2.fragments.CategoriesFragment
import com.opsc7311.opsc7311poepart2.fragments.ReportsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Default homepage/fragment when app launches
        makeCurrentFragment(EntriesFragment())

        //Code for when a different button is pressed on the navigation menu
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.entries_mi -> makeCurrentFragment(EntriesFragment())
                R.id.calender -> makeCurrentFragment(CalendarFragment())
                R.id.reports -> makeCurrentFragment(ReportsFragment())
                R.id.categories -> makeCurrentFragment(CategoriesFragment())

                else ->{

                }
            }
            true
        }
    }
    //Code for the change to another page/fragment to take place.
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }


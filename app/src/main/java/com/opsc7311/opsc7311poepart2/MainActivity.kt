package com.opsc7311.opsc7311poepart2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.opsc7311.opsc7311poepart2.activities.RegisterDemoActivity
import com.opsc7311.opsc7311poepart2.activities.RegisterLoginActivity
import com.opsc7311.opsc7311poepart2.databinding.ActivityMainBinding
import com.opsc7311.opsc7311poepart2.fragments.CalendarFragment
import com.opsc7311.opsc7311poepart2.fragments.CategoriesFragment
import com.opsc7311.opsc7311poepart2.fragments.EntriesFragment
import com.opsc7311.opsc7311poepart2.fragments.GoalFragment
import com.opsc7311.opsc7311poepart2.fragments.ReportsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //mAuth.signOut()
        //Default homepage/fragment when app launches
        val isLoggedIn = checkLoginStatus()

        if (isLoggedIn) {
            setupBottomNavigation()
        } else {
            redirectToRegisterActivity()
        }
    }

    private fun checkLoginStatus(): Boolean {

        return if(mAuth.currentUser != null) true
        else false
    }

    private fun setupBottomNavigation() {
        // This function was adapted from Youtube
        // https://www.youtube.com/watch?v=Chso6xrJ6aU
        // Stevdza-San
        // https://www.youtube.com/@StevdzaSan

        // Default homepage/fragment when app launches
        makeCurrentFragment(EntriesFragment())

        // Code for when a different button is pressed on the navigation menu
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.entries_mi -> makeCurrentFragment(EntriesFragment())
                //R.id.calender -> makeCurrentFragment(CalendarFragment())
                R.id.reports -> makeCurrentFragment(GoalFragment())
                R.id.categories -> makeCurrentFragment(CategoriesFragment())
                R.id.logout -> {
                    mAuth.signOut()
                    redirectToMain()
                }
                else -> {
                }
            }
            true
        }
    }

    private fun redirectToRegisterActivity() {
        startActivity(Intent(this, RegisterLoginActivity::class.java))
        finish() // Finish MainActivity so the user can't go back without logging in
    }

    private fun redirectToMain(){
        startActivity(Intent(this, this::class.java))
        finish()
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }
}

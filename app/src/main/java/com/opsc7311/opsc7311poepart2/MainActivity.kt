package com.opsc7311.opsc7311poepart2

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.opsc7311.opsc7311poepart2.fragments.PomodoroFragment
import com.opsc7311.opsc7311poepart2.fragments.PomodoroTimesheetFragment
import com.opsc7311.opsc7311poepart2.activities.RegisterLoginActivity
import com.opsc7311.opsc7311poepart2.databinding.ActivityMainBinding
import com.opsc7311.opsc7311poepart2.fragments.CalendarFragment
import com.opsc7311.opsc7311poepart2.fragments.CategoriesFragment
import com.opsc7311.opsc7311poepart2.fragments.CreateTaskFragment
import com.opsc7311.opsc7311poepart2.fragments.EntriesFragment
import com.opsc7311.opsc7311poepart2.fragments.GoalFragment
import com.opsc7311.opsc7311poepart2.fragments.LeaderboardFragment
import com.opsc7311.opsc7311poepart2.fragments.ReportsFragment
import com.opsc7311.opsc7311poepart2.fragments.TimesheetEntryFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var  navigationView: NavigationView

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById<DrawerLayout>(R.id.main)

        toolbar = findViewById<Toolbar>(R.id.nav_toolbar)
        setSupportActionBar(toolbar)

        navigationView = findViewById<NavigationView>(R.id.nav_view)




        // Replace the fragment with the default fragment on app launch
        if (savedInstanceState == null){
            makeCurrentFragment(ReportsFragment())
        }
        //mAuth.signOut()
        //Default homepage/fragment when app launches
        val isLoggedIn = checkLoginStatus()

        if (isLoggedIn) {
            setupBottomNavigation()
            setupMainMenu()
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
        makeCurrentFragment(ReportsFragment())

        // Code for when a different button is pressed on the navigation menu
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.reports -> makeCurrentFragment(ReportsFragment())
                R.id.calender -> makeCurrentFragment(CalendarFragment())
                //R.id.reports -> makeCurrentFragment(GoalFragment())
                R.id.categories -> makeCurrentFragment(CategoriesFragment())
                R.id.create -> makeCurrentFragment(TimesheetEntryFragment())
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

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.navigation_reports -> makeCurrentFragment(ReportsFragment())
            R.id.navigation_tasks ->  makeCurrentFragment(EntriesFragment())
            R.id.navigation_calendar -> makeCurrentFragment(CalendarFragment())
            R.id.navigation_categories -> makeCurrentFragment(CategoriesFragment())
            R.id.navigation_pomodoro -> makeCurrentFragment(PomodoroTimesheetFragment())
            R.id.navigation_goal -> makeCurrentFragment(GoalFragment())

            R.id.navigation_leaderboard -> makeCurrentFragment(LeaderboardFragment())
            R.id.navigation_settings -> makeCurrentFragment(SettingsFragment())

            R.id.navigation_logout -> {
                mAuth.signOut()
                redirectToMain()
            }
            else -> {
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true    }

    private fun setupMainMenu() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    // Handle back button press to close the navigation drawer
    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            //onBackPressedDispatcher.onBackPressed()
            super.onBackPressed()
        }
    }
}

package com.opsc7311.opsc7311poepart2.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.ViewPagerAdapter
import com.opsc7311.opsc7311poepart2.fragments.LoginFragment
import com.opsc7311.opsc7311poepart2.fragments.RegisterFragment

class RegisterLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_login)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        // Create an adapter that returns a fragment for each tab
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(LoginFragment(), "Login")
        adapter.addFragment(RegisterFragment(), "Register")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}
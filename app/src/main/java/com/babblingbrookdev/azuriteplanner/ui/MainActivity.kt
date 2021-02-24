package com.babblingbrookdev.azuriteplanner.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.babblingbrookdev.azuriteplanner.R
import com.babblingbrookdev.azuriteplanner.databinding.ActivityMainBinding
import com.babblingbrookdev.azuriteplanner.ui.chart.ChartFragment
import com.babblingbrookdev.azuriteplanner.ui.list.ListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        replaceFragment(ChartFragment())

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_chart -> replaceFragment(ChartFragment())
                R.id.menu_list -> replaceFragment(ListFragment())
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // R.id.menu_about -> replaceFragment(AboutFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

    override fun getActivityFab(): FloatingActionButton {
        return binding.mainFab
    }
}
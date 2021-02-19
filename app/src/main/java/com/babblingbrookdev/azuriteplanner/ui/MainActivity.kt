package com.babblingbrookdev.azuriteplanner.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.babblingbrookdev.azuriteplanner.R
import com.babblingbrookdev.azuriteplanner.databinding.ActivityMainBinding
import com.babblingbrookdev.azuriteplanner.ui.chart.ChartFragment
import com.babblingbrookdev.azuriteplanner.ui.list.ListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chartFragment = ChartFragment()
        val listFragment = ListFragment()

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_chart -> replaceFragment(chartFragment)
                R.id.menu_list -> replaceFragment(listFragment)
                else -> replaceFragment(chartFragment)
            }
            true
        }

        replaceFragment(chartFragment)
    }

    private fun replaceFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
}
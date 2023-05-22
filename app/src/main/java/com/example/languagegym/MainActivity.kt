package com.example.languagegym

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.languagegym.databinding.ActivityMainBinding
import com.example.languagegym.ui.list.DetailsFragment
import com.example.languagegym.ui.list.ListFragment

class MainActivity : AppCompatActivity() {

    private val language: String = "EN"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null
    private lateinit var filterSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportFragmentManager.addOnBackStackChangedListener {
            currentFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            configureContextButton()
        }


        filterSpinner = Spinner(this)
        setupFilterSpinner()

        binding.appBarMain.contextToolbarButton.setImageResource(R.drawable.ic_filter)
        binding.appBarMain.contextToolbarButton.setOnClickListener {
            filterSpinner.performClick()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun configureContextButton() {
        when (currentFragment) {
            is ListFragment -> {
                binding.appBarMain.contextToolbarButton.visibility = View.VISIBLE
                binding.appBarMain.contextToolbarButton.setImageResource(R.drawable.ic_filter)
            }
            is DetailsFragment -> {
                binding.appBarMain.contextToolbarButton.setImageResource(R.drawable.ic_edit)
            }
            else -> {
                binding.appBarMain.contextToolbarButton.visibility = View.GONE
            }
        }
    }

    private fun setupFilterSpinner() {
        val filterOptions = when (language) {
            "EN" -> arrayOf("Noun", "Verb", "Adjective", "Adverb")
            "RU" -> arrayOf("Существительное", "Глагол", "Прилагательное", "Наречие")
            // ...
            else -> arrayOf()
        }

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterOptions)

        filterSpinner.adapter = adapter
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val partOfSpeech = filterOptions[position]

                val listFragment =
                    supportFragmentManager.findFragmentByTag("HomeFragment") as? ListFragment
                //TODO change this realization
               // listFragment?.loadWordsByFilter(partOfSpeech)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}
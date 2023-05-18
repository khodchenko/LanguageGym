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
import com.example.languagegym.helpers.DictionaryDatabaseHelper
import com.example.languagegym.databinding.ActivityMainBinding
import com.example.languagegym.ui.home.DetailsFragment
import com.example.languagegym.ui.home.ListFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val language: String = "EN"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DictionaryDatabaseHelper
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
        //words data base
        databaseHelper = DictionaryDatabaseHelper(this)
        GlobalScope.launch {
            databaseHelper.writableDatabase
        }
        // Добавляем слушатель фрагментов
        supportFragmentManager.addOnBackStackChangedListener {
            currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            // Вызываем метод, который будет настраивать кнопку в зависимости от текущего фрагмента
            configureContextButton()
        }

        // Настраиваем контекстную кнопку
        filterSpinner = Spinner(this)
        setupFilterSpinner()

        binding.appBarMain.contextToolbarButton.setImageResource(R.drawable.ic_filter)
        binding.appBarMain.contextToolbarButton.setOnClickListener {
            // Отображаем выпадающий список фильтра
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

    // Метод для настройки кнопки в зависимости от текущего фрагмента
    private fun configureContextButton() {
        when (currentFragment) {
            is ListFragment -> {
                // Настраиваем кнопку для первого фрагмента
                binding.appBarMain.contextToolbarButton.visibility = View.VISIBLE
                binding.appBarMain.contextToolbarButton.setImageResource(R.drawable.ic_filter)
            }
            is DetailsFragment -> {
                // Настраиваем кнопку для второго фрагмента
                binding.appBarMain.contextToolbarButton.setImageResource(R.drawable.ic_edit)
            }
            // Добавляем обработку для других фрагментов
            // ...
            else -> {
                // Нет текущего фрагмента
                binding.appBarMain.contextToolbarButton.visibility = View.GONE
            }
        }
    }

    private fun setupFilterSpinner() {
        // Заполняем список элементами в зависимости от текущего языка
        val filterOptions = when (language) {
            "EN" -> arrayOf("Noun", "Verb", "Adjective", "Adverb")
            "RU" -> arrayOf("Существительное", "Глагол", "Прилагательное", "Наречие")
            // ...
            else -> arrayOf()
        }

        // Создаем адаптер для списка элементов
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterOptions)

        // Привязываем адаптер к списку и устанавливаем обработчик события выбора элемента
        filterSpinner.adapter = adapter
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val partOfSpeech = filterOptions[position]
                // Вызываем метод в HomeFragment для фильтрации списка слов
                val listFragment =
                    supportFragmentManager.findFragmentByTag("HomeFragment") as? ListFragment
                listFragment?.filterByPartOfSpeech(partOfSpeech)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Ничего не делаем
            }
        }
    }
}
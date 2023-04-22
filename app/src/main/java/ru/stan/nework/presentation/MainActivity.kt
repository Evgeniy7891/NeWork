package ru.stan.nework.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.R
import ru.stan.nework.databinding.ActivityMainBinding
import ru.stan.nework.utils.BOTTONMENU
import ru.stan.nework.utils.MAIN

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MAIN = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        BOTTONMENU = binding.bottomMenu
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomMenu.setupWithNavController(navController)
        binding.bottomMenu.selectedItemId
        binding.bottomMenu.isVisible = false
        setContentView(binding.root)
    }
}
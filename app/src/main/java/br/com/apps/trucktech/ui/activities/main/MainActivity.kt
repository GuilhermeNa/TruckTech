package br.com.apps.trucktech.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import br.com.apps.trucktech.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment_container_main) }

    private val componentsViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponentsManager()
        initNavigationView()
    }

    private fun initComponentsManager() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation)
        componentsViewModel.components.observe(this) { components ->
            if(components.hasBottomNavigation) {
                bottomNavigation.visibility = View.VISIBLE
            } else {
                bottomNavigation.visibility = View.GONE
            }
        }
    }

    private fun initNavigationView() {
        val navigationView: BottomNavigationView =
            findViewById(R.id.activity_main_bottom_navigation)
        navigationView.setupWithNavController(navController)
    }


}
package com.example.pulsepay12

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.pulsepay12.adapter.MovementsAdapter
import com.example.pulsepay12.databinding.ActivityMainBinding
import com.example.pulsepay12.model.Transaction
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity(), MovementsAdapter.OnTransactionListener{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
       when (item.itemId) {
            R.id.MenuInicio-> {navController.navigate(R.id.action_global_dashboardFragment)}
            R.id.MenuRecargar-> {navController.navigate(R.id.action_global_rechargeFragment)}
           R.id.MenuPagar-> {navController.navigate(R.id.action_global_PaymentFragment)}
            R.id.MenuMovimientos-> {navController.navigate(R.id.action_global_movementFragment)}
            R.id.MenuAjustes-> {navController.navigate(R.id.action_global_settingsFragment)}
            R.id.MenuCerrarSesion-> {finish()}
        }
        return super.onOptionsItemSelected(item)
    }

    fun showFab(show: Boolean) {
        binding.fab.visibility = if (show) View.VISIBLE else View.GONE
    }
    fun setFabClickListener(listener: View.OnClickListener?) {
        binding.fab.setOnClickListener(listener)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}
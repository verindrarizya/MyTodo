package com.verindrzya.mytodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.verindrzya.mytodo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        val KEY_CLICKED_WIDGET_TODO_ID = "KEY_CLICKED_WIDGET_TODO_ID"
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Action Bar set with nav support
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.listFragment, R.id.settingsActivity_dest),
            binding.root
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // To prevent the navigation navigate to detail when user is on list after pressing back
        // from detail in case configuration change happened
        if (savedInstanceState == null) {
            val idFromClickedWidget = intent.getIntExtra(KEY_CLICKED_WIDGET_TODO_ID, 0)
            if (idFromClickedWidget != 0) {
                val bundle = Bundle().apply {
                    putInt("id", idFromClickedWidget)
                }
                navController.navigate(R.id.detailFragment, bundle)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
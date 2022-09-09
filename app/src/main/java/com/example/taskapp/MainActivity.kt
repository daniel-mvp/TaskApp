package com.example.taskapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))

        binding.toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                binding.ivToolbarIcon.setOnClickListener {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                }
            } else if (destination.id == R.id.profileFragment) {
                binding.ivToolbarIcon.setOnClickListener {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.onBoardingFragment || destination.id == R.id.authFragment ||
                destination.id == R.id.registrationFragment || destination.id == R.id.welcomeFragment ||
                destination.id == R.id.createProfileFragment ||
                destination.id == R.id.confirmPasswordFragment
            ) {
                binding.toolbar.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
            }
        }
    }

}
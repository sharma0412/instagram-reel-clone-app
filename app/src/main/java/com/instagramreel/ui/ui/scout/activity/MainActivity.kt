package com.instagramreel.ui.ui.scout.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.instagramreel.R

import com.instagramreel.databinding.ActivityMainBinding
import com.instagramreel.ui.ui.athlete.activity.AthleteMainActivity
import com.instagramreel.ui.ui.scout.fragment.WelcomeScreenFragment
import com.instagramreel.ui.sharedPref.AppPrefs


class MainActivity : AppCompatActivity(), WelcomeScreenFragment.WelcomeScreenCallback {

    lateinit var binding: ActivityMainBinding
    var position = 0
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(this@MainActivity)
    }
    var role: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        role = appPrefs.getInt("role")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController

        if (!appPrefs.getStringKey("token").isNullOrEmpty()) {
            if (appPrefs.getInt("isVerify") == 0) {
                navController.navigate(R.id.numberFragment)
            }
            else if (appPrefs.getInt("isVerify") == 1){
                if (role == 1) {
                    navController.navigate(R.id.myDetailFragment)
                }
                else if (role == 2) {
                    navController.navigate(R.id.athleteUpdateDetailsFragment)
                }
            }
            else if (appPrefs.getInt("isVerify") == 2){
                if (role == 1) {
                    startActivity(Intent(this@MainActivity, ScoutHomeActivity::class.java))
                }
                else if (role == 2) {
                    startActivity(Intent(this@MainActivity, AthleteMainActivity::class.java))
                }
            }
        }
    }

    override fun onClick(position: Int) {
        this.position = position
        when (position) {
            0 ->
                findNavController(R.id.fragmentContainerView).navigate(R.id.signinFragment)
            1 ->
                findNavController(R.id.fragmentContainerView).navigate(R.id.signupFragment)
        }
    }
}
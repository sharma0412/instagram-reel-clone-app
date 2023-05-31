package com.instagramreel.ui.ui.scout.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.instagramreel.R
import com.instagramreel.databinding.ActivityScoutHomeBinding
import com.instagramreel.ui.callbacks.onBottomNavigationScout


class ScoutHomeActivity : AppCompatActivity(), onBottomNavigationScout {

    lateinit var binding: ActivityScoutHomeBinding
    private lateinit var navController: NavController
    private var mBackPressed: Long = 0
    private val timeInterval = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scout_home)
        initUi()
        onDestinationChanged()
    }

    private fun onDestinationChanged() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                when (destination.id) {
                    R.id.editProfileFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.discoverReelsFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.notificationsFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.tellFriendFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.securityFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.changePasswordFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.personProfileFragment2 -> {
                        binding.bnv.visibility = View.GONE
                    }
                    else -> {
                        binding.bnv.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initUi() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnv.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label == "ReelsFragment") {
                binding.bnv.setBackgroundColor(
                    ContextCompat.getColor(
                        this@ScoutHomeActivity,
                        R.color.black
                    )
                )
            }
        }
        bottomNavigationBar()
    }

    private fun bottomNavigationBar() {

        binding.bnv.itemIconTintList = null

        binding.bnv.selectedItemId = R.id.reelsFragment


        binding.bnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileFragment -> {
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.profileFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.discoverFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.inboxFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.settingFragment,true)
                    findNavController(R.id.fragmentContainerView2).navigate(R.id.profileFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ScoutHomeActivity,
                            R.color.white
                        )
                    )
                }
                R.id.discoverFragment -> {
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.profileFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.discoverFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.inboxFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.settingFragment,true)
                    findNavController(R.id.fragmentContainerView2).navigate(R.id.discoverFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ScoutHomeActivity,
                            R.color.white
                        )
                    )
                }
                R.id.reelsFragment -> {
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.profileFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.discoverFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.reelsFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.inboxFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.settingFragment,true)
                    findNavController(R.id.fragmentContainerView2).navigate(R.id.reelsFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ScoutHomeActivity,
                            R.color.black
                        )
                    )
                }
                R.id.inboxFragment -> {
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.profileFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.discoverFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.inboxFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.settingFragment,true)
                    findNavController(R.id.fragmentContainerView2).navigate(R.id.inboxFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ScoutHomeActivity,
                            R.color.white
                        )
                    )
                }
                R.id.settingFragment -> {
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.profileFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.discoverFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.inboxFragment,true)
                    findNavController(R.id.fragmentContainerView2).popBackStack(R.id.settingFragment,true)
                    findNavController(R.id.fragmentContainerView2).navigate(R.id.settingFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ScoutHomeActivity,
                            R.color.white
                        )
                    )
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        if(navController.popBackStack().not()) {
            //Last fragment: Do your operation here
            val selectedItemId: Int = binding.bnv.selectedItemId
            if (R.id.reelsFragment != selectedItemId) {
                binding.bnv.selectedItemId = R.id.reelsFragment
            } else {
                if (mBackPressed + timeInterval > System.currentTimeMillis()) {
                    finishAffinity()
                    super.onBackPressed()
                    return
                } else {
                    Toast.makeText(baseContext, "Tap back button in order to exit", Toast.LENGTH_SHORT)
                        .show() }

                mBackPressed = System.currentTimeMillis()
            }
        }
    }

    override fun getEditProfile() {
        findNavController(R.id.fragmentContainerView2).navigate(R.id.action_profileFragment_to_editProfileFragment)
    }

    override fun getDiscoverReels() {
        findNavController(R.id.fragmentContainerView2).navigate(R.id.action_discoverFragment_to_discoverReelsFragment)
    }
}
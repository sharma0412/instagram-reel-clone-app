package com.instagramreel.ui.ui.athlete.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.instagramreel.R
import com.instagramreel.databinding.ActivityAthleteMainBinding
import com.instagramreel.ui.callbacks.onBottomNavigationClickAthlete
import com.instagramreel.ui.model.athlete.kickoff.KickOffResponse
import com.instagramreel.ui.sharedPref.AppPrefs
import java.util.*


class AthleteMainActivity : AppCompatActivity(), onBottomNavigationClickAthlete {

    lateinit var binding: ActivityAthleteMainBinding
    private lateinit var navController: NavController
    private var mBackPressed: Long = 0
    private val timeInterval = 2000
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(this@AthleteMainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_athlete_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@AthleteMainActivity)
        initUi()
    }

    private fun initUi() {
//        getCurrentLocation()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.athleteContainer) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnv.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label == "AthleteReelsFragment") {
                binding.bnv.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AthleteMainActivity,
                        R.color.black
                    )
                )
            }
        }
        bottomNavigationBar()
        onClick()
    }

    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task->
                    val location: Location?= task.result
                    if (location!=null) {
                        Log.d("MyLocation", "getCurrentLocation: ${location.latitude} , ${location.longitude}")
                        try {

                            val geo = Geocoder(this@AthleteMainActivity, Locale.getDefault())
                            val addresses:List<Address> = geo.getFromLocation(location.latitude, location.longitude, 1)

                            appPrefs.setString("locationName",addresses[0].locality+", "+addresses[0].countryName)
                            /*if (addresses.isEmpty()) {
                                yourtextboxname.setText("Waiting for Location");
                            }
                            else {
                                yourtextboxname.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                            }*/
                        }
                        catch (e:Exception) {

                        }
                    }
                }
            }
            else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun checkPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
    }

    private fun onClick() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                when (destination.id) {
                    R.id.gameDetailsFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.profileReelsFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.athleteChatFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.athleteSettingFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.notificationsFragment2 -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.athleteSecurityFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.athleteChangePasswordFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.athleteTellFriendFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.placeSelectionFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    R.id.athleteEditProfileFragment -> {
                        binding.bnv.visibility = View.GONE
                    }
                    else -> {
                        binding.bnv.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun bottomNavigationBar() {
        binding.bnv.itemIconTintList = null

        binding.bnv.selectedItemId = R.id.reels

        binding.bnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.kickoffFragment -> {
                    findNavController(R.id.athleteContainer).popBackStack(R.id.insightFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteInboxFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteProfileFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.kickoffFragment,true)
                    findNavController(R.id.athleteContainer).navigate(R.id.kickoffFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@AthleteMainActivity,
                            R.color.white
                        )
                    )
                }
                R.id.insightFragment -> {
                    findNavController(R.id.athleteContainer).popBackStack(R.id.insightFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteInboxFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteProfileFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.kickoffFragment,true)
                    findNavController(R.id.athleteContainer).navigate(R.id.insightFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@AthleteMainActivity,
                            R.color.white
                        )
                    )
                }
                R.id.athleteReelsFragment -> {
                    findNavController(R.id.athleteContainer).popBackStack(R.id.insightFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteReelsFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteInboxFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteProfileFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.kickoffFragment,true)
                    findNavController(R.id.athleteContainer).navigate(R.id.athleteReelsFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@AthleteMainActivity,
                            R.color.black
                        )
                    )
                }
                R.id.athleteInboxFragment -> {
                    findNavController(R.id.athleteContainer).popBackStack(R.id.insightFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteInboxFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteProfileFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.kickoffFragment,true)
                    findNavController(R.id.athleteContainer).navigate(R.id.athleteInboxFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@AthleteMainActivity,
                            R.color.white
                        )
                    )
                }
                R.id.athleteProfileFragment -> {
                    findNavController(R.id.athleteContainer).popBackStack(R.id.insightFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteInboxFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.athleteProfileFragment,true)
                    findNavController(R.id.athleteContainer).popBackStack(R.id.kickoffFragment,true)
                    findNavController(R.id.athleteContainer).navigate(R.id.athleteProfileFragment)
                    binding.bnv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@AthleteMainActivity,
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
            if (R.id.athleteReelsFragment != selectedItemId) {
                binding.bnv.selectedItemId = R.id.athleteReelsFragment
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

    override fun getCurrentFragment(model: KickOffResponse.Body) {
        val bundle = Bundle()
        bundle.putInt("kickOffId",model.id)
        findNavController(R.id.athleteContainer).navigate(R.id.action_kickoffFragment_to_gameDetailsFragment, bundle)
    }

    override fun getChatFragment() {
        findNavController(R.id.athleteContainer).navigate(R.id.action_athleteInboxFragment_to_athleteChatFragment)
    }

    override fun getAtheleteSettingsFragment() {
        findNavController(R.id.athleteContainer).navigate(R.id.action_athleteProfileFragment_to_athleteSettingFragment)
    }
}
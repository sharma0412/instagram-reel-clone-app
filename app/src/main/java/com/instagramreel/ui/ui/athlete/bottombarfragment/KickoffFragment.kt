package com.instagramreel.ui.ui.athlete.bottombarfragment

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.instagramreel.databinding.FragmentKickoffBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.callbacks.onBottomNavigationClickAthlete
import com.instagramreel.ui.dialog.LocationPickerDialog
import com.instagramreel.ui.model.athlete.kickoff.KickOffResponse
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.ui.athlete.adapter.KickoffGameAdapter
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.athlete.kickoff.KickoffViewModel
import com.instagramreel.ui.viewmodel.scout.GetSupportsDataViewModel
import java.util.*

class KickoffFragment : Fragment(), View.OnClickListener {

    private var locationPickerDialog: LocationPickerDialog? = null
    private var sportsList: ArrayList<GetSportsResponse.Body>? = null
    lateinit var binding: FragmentKickoffBinding
    var callback: onBottomNavigationClickAthlete? = null
    private var localityName = ""
    private var countyName = ""
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(requireContext())
    }
    var kickOffList = ArrayList<KickOffResponse.Body>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var viewModelFactory: ViewModelFactory
    private var kickoffViewModel: KickoffViewModel? = null

    private val getSupportsDataViewModel by viewModels<GetSupportsDataViewModel> {
        ViewModelFactory(
            Application(),
            Repository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKickoffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory(Application(), Repository())
        kickoffViewModel = ViewModelProvider(this, viewModelFactory).get(
            KickoffViewModel::class.java
        )

        getSupportsDataViewModel.getSportsList()
        kickoffViewModel!!.getKickoffList()
        setObserver()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        localityName = appPrefs.getStringKey("locality").toString()
        countyName = appPrefs.getStringKey("country").toString()
        countyName = appPrefs.getStringKey("locationName").toString()

        binding.tvLocationName.text = appPrefs.getStringKey("locationName").toString()
        initUi()

    }

    private fun setObserver() {
        kickoffViewModel?.kickOff?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        kickOffList = it?.data?.body as ArrayList<KickOffResponse.Body>
                        gameRV()
                    }
                    Status.LOADING -> {
                        BaseActivity.showLoader(requireActivity())
                    }
                    Status.ERROR -> {
                        BaseActivity.hideLoader()
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        getSupportsDataViewModel.getData.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        sportsList = it?.data?.body as ArrayList<GetSportsResponse.Body>

                        val model = GetSportsResponse.Body(
                            "2022-06-16T05:22:57.000Z",
                            0,
                            "All sports",
                            1,
                            "2022-06-16T05:22:57.000Z"
                        )
                        sportsList!!.add(0, model)

                        val adapter: ArrayAdapter<String> = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            sportsList!!.map { d -> d.name }
                        )

                        binding.tvSpinner.adapter = adapter
                    }
                    Status.LOADING -> {
                        BaseActivity.showLoader(requireContext())
                    }
                    Status.ERROR -> {
                        BaseActivity.hideLoader()
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as onBottomNavigationClickAthlete
    }

    private fun initUi() {
        binding.ivLocation.setOnClickListener(this)
        binding.tvLocationName.setOnClickListener(this)
    }

    private fun gameRV() {
        binding.rvGames.adapter =
            KickoffGameAdapter(
                this@KickoffFragment,
                kickOffList,
                object : KickoffGameAdapter.KickoffCallback {
                    override fun onClick(
                        kickOffList: ArrayList<KickOffResponse.Body>,
                        position: Int
                    ) {
                        val model: KickOffResponse.Body = kickOffList[position]
                        callback?.getCurrentFragment(model)
                    }
                })
    }

    override fun onClick(p0: View?) {
        binding.apply {
            when (p0) {
                ivLocation -> {
                    getLocation()
                    binding.tvLocationName.text = appPrefs.getStringKey("locationName").toString()
                }
                tvLocationName -> {
                    locationPickerDialog = LocationPickerDialog(
                        requireContext(),
                        object : LocationPickerDialog.Listener {
                            override fun onSelectLocation(location: String) {
                                binding.tvLocationName.text = location
                                appPrefs.setString("locationName", location)
                            }

                        },
                        requireView()
                    )
                    locationPickerDialog?.show()
                }
            }
        }
    }

    private fun getLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
            val location: Location = task.result
            Log.d("MyLocation", "getCurrentLocation: ${location.latitude} , ${location.longitude}")
            try {

                val geo = Geocoder(requireContext(), Locale.getDefault())
                val addresses: List<Address> =
                    geo.getFromLocation(location.latitude, location.longitude, 1)

                appPrefs.setString(
                    "locationName",
                    addresses[0].locality + ", " + addresses[0].countryName
                )
            } catch (e: Exception) {

            }
        }
    }
}
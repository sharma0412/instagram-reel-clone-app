package com.instagramreel.ui.ui.athlete.bottombarfragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.instagramreel.R
import com.instagramreel.databinding.FragmentAtheleteProfileBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.callbacks.onBottomNavigationClickAthlete
import com.instagramreel.ui.model.athlete.getmydetails.Body
import com.instagramreel.ui.model.athlete.getmydetails.GetDetailsRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.ui.athlete.adapter.ProfileReelsAdapter
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.profile.ProfileDetailsViewModel

class AthleteProfileFragment : Fragment() {

    private var userData: Body? = null
    lateinit var binding: FragmentAtheleteProfileBinding
    var callback: onBottomNavigationClickAthlete? = null
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(requireContext())
    }

    private lateinit var viewModelFactory: ViewModelFactory
    private var profileDetailsViewModel: ProfileDetailsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAtheleteProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory(Application(), Repository())
        profileDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(
            ProfileDetailsViewModel::class.java
        )

        profileDetailsViewModel!!.getAthleteProfileData(GetDetailsRequest(appPrefs.getInt("userID")))

        initUI()
        setObserver()
    }

    private fun setObserver() {
        profileDetailsViewModel?.profile?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()

                        userData = resource.data?.body

                        if (resource.data?.body?.profilePic.isNullOrEmpty()) {
                            Glide.with(requireContext()).load(R.drawable.profile_placeholder).into(binding.circleProfileIV)
                        }
                        else {
                            Glide.with(requireContext()).load(resource.data?.body?.profilePic).placeholder(
                                R.drawable.profile_placeholder).into(binding.circleProfileIV)
                        }

                        binding.apply {
                            tvName.text = resource.data?.body?.fullname
                            if (resource.data?.body?.bio.isNullOrEmpty()) {
                                tvDescription.text = ""
                            }
                            else {
                                tvDescription.text = resource.data?.body?.bio
                            }
                        }
                        experienceRV()
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

    private fun initUI() {
        onClick()
    }

    private fun experienceRV() {
        binding.rvReels.adapter = ProfileReelsAdapter(this,userData)
    }

    private fun onClick() {
        binding.apply {
            ivSettings.setOnClickListener {
                callback?.getAtheleteSettingsFragment()
            }
            btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_athleteProfileFragment_to_athleteEditProfileFragment)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as onBottomNavigationClickAthlete
    }
}
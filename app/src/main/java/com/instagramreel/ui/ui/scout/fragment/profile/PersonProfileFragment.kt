package com.instagramreel.ui.ui.scout.fragment.profile

import android.app.Application
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
import com.instagramreel.databinding.FragmentPersonProfileBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.athlete.getmydetails.GetDetailsRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.ui.athlete.adapter.ReelsProfileAdapter
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.profile.ProfileDetailsViewModel

class PersonProfileFragment : Fragment(),View.OnClickListener {

    private var click: String? = null
    private var userId: Int? = null
    lateinit var binding: FragmentPersonProfileBinding

    private lateinit var viewModelFactory: ViewModelFactory
    private var profileDetailsViewModel: ProfileDetailsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("USERID")
        click = arguments?.getString("Click")

        viewModelFactory = ViewModelFactory(Application(), Repository())
        profileDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(
            ProfileDetailsViewModel::class.java
        )

        profileDetailsViewModel!!.getAthleteProfileData(GetDetailsRequest(userId!!))
        initUi()
        setObserver()
    }

    private fun initUi() {
        binding.apply {
            btnBack.setOnClickListener(this@PersonProfileFragment)
        }
    }

    private fun setObserver() {
        profileDetailsViewModel?.profile?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()

                        binding.rvReels.adapter = ReelsProfileAdapter(this@PersonProfileFragment,it.data?.body,click)
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

    override fun onClick(p0: View?) {
        binding.apply {
            when(p0) {
                btnBack -> {
                    findNavController().popBackStack()
                }
            }
        }
    }
}
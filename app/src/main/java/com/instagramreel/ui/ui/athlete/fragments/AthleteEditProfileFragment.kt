package com.instagramreel.ui.ui.athlete.fragments

import android.app.Activity
import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.instagramreel.R
import com.instagramreel.databinding.FragmentAthleteEditProfileBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.athlete.updatedetails.AthletUpdateRequest
import com.instagramreel.ui.model.athlete.updatedetails.SportId
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.utils.Constants
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.scout.GetMyDetailsViewModel
import com.instagramreel.ui.viewmodel.scout.GetSupportsDataViewModel
import com.instagramreel.ui.viewmodel.scout.UpdateDetailsViewModel
import java.io.File

class AthleteEditProfileFragment : Fragment() {

    private lateinit var fileUri: Uri
    private var profileFile: File? = null
    private var sportsList: ArrayList<GetSportsResponse.Body>? = null
    lateinit var binding: FragmentAthleteEditProfileBinding
    lateinit var viewModelFactory: ViewModelFactory
    private var viewModel: UpdateDetailsViewModel? = null
    private val sportId = ArrayList<SportId>()
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(requireContext())
    }
    private val getSupportsDataViewModel by viewModels<GetSupportsDataViewModel> {
        ViewModelFactory(
            Application(),
            Repository()
        )
    }
    private val getMyDetailsViewModel by viewModels<GetMyDetailsViewModel> {
        ViewModelFactory(
            Application(),
            Repository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAthleteEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelFactory = ViewModelFactory(Application(), Repository())
        getSupportsDataViewModel.getSportsList()

        viewModel = ViewModelProvider(this, viewModelFactory).get(
            UpdateDetailsViewModel::class.java
        )
        getMyDetailsViewModel.getMyDetails()

        setObserver()
        initUi()

    }

    private fun initUi() {
        binding.imageView.setOnClickListener {
            setProfilePicker()
        }

        binding.btSave.setOnClickListener {
            viewModel?.onAtheUpdateDetails(
                AthletUpdateRequest(
                    binding.etName.text.toString(),
                    Constants.DOB,
                    binding.etHeight.text.toString(),
                    Constants.POSITION,
                    sportId,
                    binding.etWeight.text.toString(),
                    binding.etBio.text.toString(),
                    "s3.url"
                )
            )
        }
    }

    private fun setObserver() {
        getSupportsDataViewModel.getData.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        sportsList = it?.data?.body as ArrayList<GetSportsResponse.Body>
                        /*binding.rvSportList.adapter = SportsListAdapter(
                            this@AthleteEditProfileFragment,
                            sportsList,
                            object : SportsListAdapter.SportsCallback {
                                override fun onClick(id: List<Int>, name: List<String>) {
                                    list.clear()
                                    sportsIdList.clear()
                                    list.addAll(name)
                                    for (i in id.indices) {
                                        sportsIdList.add(SportId(id[i].toString()))
                                    }
                                }
                            })*/
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

        getMyDetailsViewModel.getDetails.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        appPrefs.getStringKey("token")
                        binding.etName.setText(it.data?.body?.fullname)
                        if (it.data?.body?.bio.isNullOrEmpty()) {
                            binding.etBio.setText("")
                        } else {
                            binding.etBio.setText(it.data?.body?.bio)
                        }
                        if (it.data?.body?.profilePic != null) {
                            Glide.with(requireContext()).load(it.data.body.profilePic)
                                .placeholder(R.drawable.profile_placeholder).into(binding.imageView)
                        } else {
                            Glide.with(requireContext()).load(R.drawable.profile_placeholder)
                                .into(binding.imageView)
                        }

                        binding.tvEmail.text = it.data?.body?.email

                        binding.etHeight.setText(it.data?.body?.height.toString())
                        binding.etWeight.setText(it.data?.body?.weight.toString())
                        binding.etNumber.text = it.data?.body?.phoneNo.toString()

                        Constants.POSITION = it.data?.body?.position.toString()
                        Constants.DOB = it.data?.body?.dob.toString()


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

        viewModel?.atheletedetails?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        appPrefs.getStringKey("token")
                        Toast.makeText(
                            requireContext(),
                            "Profile update successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
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
    }

    private fun setProfilePicker() {
        ImagePicker.with(this)
            .compress(1024)
            .crop(1f, 1f)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    fileUri = data?.data!!
                    profileFile = fileUri.path?.let { File(it) }

                    Glide.with(requireActivity()).load(fileUri)
                        .placeholder(R.drawable.profile_placeholder)
                        .into(binding.imageView)
                }
                ImagePicker.RESULT_ERROR -> {
                    //showToast("" + ImagePicker.getError(data))
                }
                else -> {
                    //showToast("Task Cancelled")
                }
            }
        }

}
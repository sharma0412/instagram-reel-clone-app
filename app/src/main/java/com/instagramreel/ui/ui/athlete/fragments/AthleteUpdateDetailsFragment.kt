package com.instagramreel.ui.ui.athlete.fragments

import android.app.Application
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.instagramreel.databinding.FragmentAthleteUpdateDetailsBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.athlete.updatedetails.AthletUpdateRequest
import com.instagramreel.ui.model.athlete.updatedetails.SportId
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.ui.athlete.activity.AthleteMainActivity
import com.instagramreel.ui.ui.athlete.adapter.SportsListAdapter
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.utils.getDD
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.scout.GetMyDetailsViewModel
import com.instagramreel.ui.viewmodel.scout.GetSupportsDataViewModel
import com.instagramreel.ui.viewmodel.scout.UpdateDetailsViewModel
import java.util.*


class AthleteUpdateDetailsFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentAthleteUpdateDetailsBinding
    private var sportsList: ArrayList<GetSportsResponse.Body> = ArrayList()
    lateinit var viewModelFactory: ViewModelFactory
    private var viewModel: UpdateDetailsViewModel? = null
    val list: ArrayList<String> = ArrayList()
    val sportsIdList: ArrayList<SportId> = ArrayList()

    //private var getMyDetailsViewModel: GetMyDetailsViewModel? = null
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
        binding = FragmentAthleteUpdateDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        initUi()
        viewModelFactory = ViewModelFactory(Application(), Repository())
        getSupportsDataViewModel.getSportsList()
        /* getMyDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(
             GetMyDetailsViewModel::class.java
         )*/


        viewModel = ViewModelProvider(this, viewModelFactory).get(
            UpdateDetailsViewModel::class.java
        )

        getMyDetailsViewModel.getMyDetails()

    }

    private fun initUi() {
        binding.apply {
            tvSports.setOnClickListener(this@AthleteUpdateDetailsFragment)
            btDone.setOnClickListener(this@AthleteUpdateDetailsFragment)
            appCompatImageView3.setOnClickListener(this@AthleteUpdateDetailsFragment)
            btnContinue.setOnClickListener(this@AthleteUpdateDetailsFragment)
        }
    }

    private fun setObserver() {
        getSupportsDataViewModel.getData.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        sportsList = it?.data?.body as ArrayList<GetSportsResponse.Body>
                        binding.recyclerView2.adapter = SportsListAdapter(
                            this@AthleteUpdateDetailsFragment,
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
                            })
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
                        appPrefs.setInt("isVerify", 2)
                        binding.etName.text = it?.data?.body?.fullname.toString()
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
                        appPrefs.setInt("isVerify", 2)
                        startActivity(Intent(requireContext(), AthleteMainActivity::class.java))
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

    override fun onClick(v: View?) {
        when (v) {
            binding.tvSports -> {
                binding.recyclerView2.visibility = View.VISIBLE
                binding.btDone.visibility = View.VISIBLE
            }
            binding.btDone -> {
                binding.recyclerView2.visibility = View.GONE
                binding.btDone.visibility = View.GONE
                val arr = list.toArray()
                val str = TextUtils.join(",", arr)
                binding.tvSports.text = str
            }
            binding.appCompatImageView3 -> {
                datePickerDialogue()
            }
            binding.btnContinue -> {
                val dateOfBirth =
                    binding.tvDate.text.toString() + "/" + binding.tvMonth.text.toString() + "/" + binding.tvYear.text.toString()
                viewModel?.onAtheUpdateDetails(
                    AthletUpdateRequest(
                        binding.etName.text.toString(),
                        dateOfBirth,
                        binding.etHeight.text.toString(),
                        binding.etPosition.text.toString(),
                        sportsIdList,
                        binding.etWeight.text.toString(),
                        "",
                        "null"
                    )
                )
            }
        }
    }

    private fun datePickerDialogue() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, yr, monthOfYear, dayOfMonth ->
            val mm = monthOfYear + 1
            binding.tvDate.text = getDD(dayOfMonth).toString()
            binding.tvMonth.text = getDD(mm).toString()
            binding.tvYear.text = yr.toString()
        }, year, month, day)
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }
}
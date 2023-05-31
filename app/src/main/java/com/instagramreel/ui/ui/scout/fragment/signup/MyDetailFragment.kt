package com.instagramreel.ui.ui.scout.fragment.signup

import android.app.Application
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.instagramreel.databinding.FragmentMyDetailBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.ExperienceModel
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse
import com.instagramreel.ui.model.scout.updatedetails.UpdateDetailsRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.ui.scout.activity.ScoutHomeActivity
import com.instagramreel.ui.ui.scout.adapter.ExperienceAdapter
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.utils.getDD
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.scout.GetMyDetailsViewModel
import com.instagramreel.ui.viewmodel.scout.GetSupportsDataViewModel
import com.instagramreel.ui.viewmodel.scout.UpdateDetailsViewModel
import java.util.*


class MyDetailFragment : Fragment() {

    private lateinit var getToDate: String
    private lateinit var getFromDate: String
    lateinit var binding: FragmentMyDetailBinding
    var adapter: ExperienceAdapter? = null
    private lateinit var viewModelFactory: ViewModelFactory
    private var getSupportsDataViewModel: GetSupportsDataViewModel? = null
    private var getMyDetailsViewModel: GetMyDetailsViewModel? = null
    private var sportsList: ArrayList<GetSportsResponse.Body> = ArrayList()
    private var viewModel: UpdateDetailsViewModel? = null
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(requireContext())
    }
    private val experienceList = ArrayList<ExperienceModel>()
    private var getExperienceTitle: String? = null
    var idList: List<Int> = ArrayList()
    var itemId = ""
    var itemName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory(Application(), Repository())
        getSupportsDataViewModel = ViewModelProvider(this, viewModelFactory).get(
            GetSupportsDataViewModel::class.java
        )
        getSupportsDataViewModel!!.getSportsList()

        getMyDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(
            GetMyDetailsViewModel::class.java
        )
        getMyDetailsViewModel!!.getMyDetails()

        viewModel = ViewModelProvider(this, viewModelFactory).get(
            UpdateDetailsViewModel::class.java
        )
        setObserver()
        initUI()
    }

    private fun initUI() {
        onClick()
    }

    private fun setObserver() {
        getSupportsDataViewModel?.getData?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        sportsList = it?.data?.body as ArrayList<GetSportsResponse.Body>
                        val arrayAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            sportsList.map { d -> d.name }
                        )
                        idList = sportsList.map { d -> d.id }
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spnSpecialized.adapter = arrayAdapter
                        rvExperience()
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

        getMyDetailsViewModel?.getDetails?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        appPrefs.getStringKey("token")
                        binding.nameET.text = it?.data?.body?.fullname.toString()
                        binding.emailET.text = it?.data?.body?.email.toString()
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

        viewModel?.details?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        appPrefs.getStringKey("token")
                        appPrefs.setInt("isVerify", 2)
                        startActivity(Intent(requireContext(), ScoutHomeActivity::class.java))
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

    private fun rvExperience() {
        adapter = ExperienceAdapter(this, experienceList)
        binding.rvExperience.adapter = adapter
    }

    private fun onClick() {
        binding.apply {
            btnContinue.setOnClickListener {
                val dateOfBirth =
                    binding.tvDate.text.toString() + "/" + binding.tvMonth.text.toString() + "/" + binding.tvYear.text.toString()
                val country = countryET.textView_selectedCountry.text.toString()

                if (experienceList.isNullOrEmpty()) {

                }
                viewModel?.onUpdateDetails(
                    UpdateDetailsRequest(
                        country,
                        dateOfBirth,
                        experienceList
                    )
                )
            }

            spnSpecialized.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long
                    ) {
                        itemName = sportsList.map { d -> d.name }[position]
                        itemId = idList[position].toString()

                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                        // your code here
                    }
                }

            btnAdd.setOnClickListener {

                binding.rvExperience.visibility = View.VISIBLE
                getExperienceTitle = binding.experienceET.text.toString()
                getFromDate = binding.tvFrom.text.toString()
                getToDate = binding.tvTo.text.toString()
                //getSpecialized = binding.spnSpecialized.selectedItem.toString()

                experienceList.add(
                    ExperienceModel(
                        getExperienceTitle!!,
                        getFromDate,
                        getToDate,
                        itemId,
                        itemName
                    )
                )

                binding.experienceET.setText("")
                binding.tvFrom.text = "Choose a date"
                binding.tvTo.text = "Choose a date"

                rvExperience()
            }

            tvDate.setOnClickListener {
                datePickerDialogue()
            }

            tvMonth.setOnClickListener {
                datePickerDialogue()
            }

            tvYear.setOnClickListener {
                datePickerDialogue()
            }

            tvFrom.setOnClickListener {
                fromDatePicker(tvFrom)
            }
            tvTo.setOnClickListener {
                fromDatePicker(tvTo)
            }
        }
    }

    private fun fromDatePicker(textView: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, yr, monthOfYear, dayOfMonth ->
            val mm = monthOfYear + 1
            val dateFrom =
                getDD(dayOfMonth).toString() + "/" + getDD(mm).toString() + "/" + yr.toString()
            textView.text = dateFrom
        }, year, month, day)
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
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
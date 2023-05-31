package com.instagramreel.ui.ui.scout.fragment.signup

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.instagramreel.R
import com.instagramreel.databinding.FragmentNumberBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.auth.otpverification.NumberRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.AuthenticationViewModelFactory
import com.instagramreel.ui.viewmodel.auth.AuthenticationViewModel

class NumberFragment : Fragment() {

    lateinit var binding: FragmentNumberBinding
    private lateinit var authenticationViewModelFactory: AuthenticationViewModelFactory
    private var authenticationViewModel: AuthenticationViewModel? = null
    private var countryCode = "+91"
    private var countryName = "IN"
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setObserver()
    }

    private fun initUI() {
        authenticationViewModelFactory = AuthenticationViewModelFactory(Application(), Repository())
        authenticationViewModel = ViewModelProvider(
            this,
            authenticationViewModelFactory
        ).get(AuthenticationViewModel::class.java)
        onClick()
    }

    private fun setObserver() {
        authenticationViewModel?.number?.observe(requireActivity()) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        appPrefs.getStringKey("token")
                        val bundle = Bundle()
                        bundle.putString("number",binding.etPhone.text.toString())
                        findNavController().navigate(R.id.action_numberFragment_to_numberVerificationFragment)
                    }
                    Status.LOADING -> {
                        BaseActivity.showLoader(requireActivity())
                        //Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
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

    private fun onClick() {
        binding.apply {
            btnContinue.setOnClickListener {
                BaseActivity.showLoader(requireActivity())
                authenticationViewModel?.onClickNumberVerification(
                    NumberRequest(countryCode, countryName, etPhone.text.toString())
                )
            }
            btnBack.setOnClickListener {

            }

            spinner.setOnCountryChangeListener {
                countryCode = spinner.selectedCountryCode.toString()
                countryName = spinner.selectedCountryName.toString()
            }
        }
    }
}
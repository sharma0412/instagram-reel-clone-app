package com.instagramreel.ui.ui.athlete.settings

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.instagramreel.databinding.FragmentChangePasswordBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.changepassword.ChangePasswordRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.AuthenticationViewModelFactory
import com.instagramreel.ui.viewmodel.changepassword.ChangePasswordViewModel

class AthleteChangePasswordFragment : Fragment() {

    lateinit var binding: FragmentChangePasswordBinding
    private lateinit var authenticationViewModelFactory: AuthenticationViewModelFactory
    private var changePasswordViewModel: ChangePasswordViewModel? = null
    private val appPrefs : AppPrefs by lazy {
        AppPrefs(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        authenticationViewModelFactory = AuthenticationViewModelFactory(Application(), Repository())
        changePasswordViewModel = ViewModelProvider(this, authenticationViewModelFactory).get(
            ChangePasswordViewModel::class.java
        )
        onClick()
    }

    private fun onClick() {
        binding.apply {
            btnConfirm.setOnClickListener {
                val currentPassword = etCurrentPassword.text.toString()
                val newPassword = etNewPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                if (newPassword != confirmPassword) {
                    Toast.makeText(requireContext(),"New password doesn't match",Toast.LENGTH_SHORT).show()
                } else {
                    changePasswordViewModel?.onChangePassword(ChangePasswordRequest(newPassword,currentPassword))
                    setObserver()
                }
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setObserver() {
        changePasswordViewModel?.changePassword?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        Toast.makeText(requireContext(),it.data?.message,Toast.LENGTH_SHORT).show()
                        appPrefs.getStringKey("token")
                        findNavController().popBackStack()
                    }
                    Status.LOADING -> {
                        BaseActivity.showLoader(requireActivity())
                    }
                    Status.ERROR -> {
                        BaseActivity.hideLoader()
                        Toast.makeText(requireContext(),resource.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
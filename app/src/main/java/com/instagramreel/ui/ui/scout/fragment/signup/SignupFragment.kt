package com.instagramreel.ui.ui.scout.fragment.signup

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.instagramreel.R
import com.instagramreel.databinding.FragmentSignupBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.auth.signupmodel.SignUpRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.utils.showHidePass
import com.instagramreel.ui.viewModelFactory.AuthenticationViewModelFactory
import com.instagramreel.ui.viewmodel.auth.AuthenticationViewModel


class SignupFragment : Fragment() {

    lateinit var binding: FragmentSignupBinding
    private lateinit var authenticationViewModelFactory: AuthenticationViewModelFactory
    private var authenticationViewModel: AuthenticationViewModel? = null
    private var role = 1
    private val appPrefs : AppPrefs by lazy {
        AppPrefs(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        authenticationViewModelFactory = AuthenticationViewModelFactory(Application(), Repository())
        authenticationViewModel = ViewModelProvider(this, authenticationViewModelFactory).get(
            AuthenticationViewModel::class.java
        )
        onClick()
        setObserver()
    }

    private fun setObserver() {
        authenticationViewModel?.signUp?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        appPrefs.setToken("token", it.data?.body?.token.toString())
                        appPrefs.setInt("role", role)
                        appPrefs.setInt("isVerify",0)
                        findNavController().navigate(R.id.action_signupFragment_to_numberFragment)
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

    private fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
        }
    }

    private fun onSNACK(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue)).show()
    }

    private fun isValidation(): Boolean {
        binding.apply {
            when {
                etFullname.text.toString().trim().isEmpty() -> onSNACK(
                    etFullname,
                    "Please enter your name..."
                )
                etEmail.text.toString().trim().isEmpty() -> onSNACK(
                    etEmail,
                    "Please enter your email address..."
                )
                (!isValidEmail(etEmail.text.toString())) -> onSNACK(
                    etEmail,
                    "Please enter valid email address..."
                )
                etPassword.text.toString().trim().isEmpty() -> onSNACK(
                    etPassword,
                    "Please enter your password"
                )
                etPassword.text.toString().trim().length < 6 -> onSNACK(
                    etPassword, "Password must contain at least 6 characters"
                )
                /*confirmPasswordET.text.toString().trim().isEmpty() -> onSNACK(
                    confirmPasswordET,
                    "Please enter your confirm password"
                )
                (passwordET.text.toString() != confirmPasswordET.text.toString()) -> onSNACK(
                    confirmPasswordET,
                    "Password do not match "
                )
                (!termsUseCB.isChecked) -> onSNACK(
                    termsUseCB,
                    "Please agree terms to use. "
                )*/
                else -> return true

            }
        }
        return false
    }

    private fun onClick() {
        binding.apply {
            athleteBTN.setOnClickListener {
                role = 2
                athleteBTN.setTextColor(ContextCompat.getColor(requireContext(),R.color.blue))
                athleteBTN.setBackgroundResource(R.drawable.white_button)
                scoutBTN.setTextColor(ContextCompat.getColor(requireContext(),R.color.unselected_text))
                scoutBTN.setBackgroundColor(ContextCompat.getColor(requireContext(),android.R.color.transparent))
            }
            scoutBTN.setOnClickListener {
                role = 1
                scoutBTN.setTextColor(ContextCompat.getColor(requireContext(),R.color.blue))
                scoutBTN.setBackgroundResource(R.drawable.white_button)
                athleteBTN.setTextColor(ContextCompat.getColor(requireContext(),R.color.unselected_text))
                athleteBTN.setBackgroundColor(ContextCompat.getColor(requireContext(),android.R.color.transparent))
            }
            tvLogin.setOnClickListener {
                findNavController().navigate(R.id.signinFragment)
            }

            eyeIV.setOnClickListener {
                showHidePass(etPassword,eyeIV)
            }

            btnSignup.setOnClickListener {
                if (isValidation()) {
                    BaseActivity.showLoader(requireActivity())
                    authenticationViewModel?.onClickRegisterUser(
                        SignUpRequest(
                            binding.etEmail.text.toString(),
                            binding.etFullname.text.toString(),
                            binding.etPassword.text.toString(),
                            role.toString()
                        )
                    )
                }
            }
        }
    }
}
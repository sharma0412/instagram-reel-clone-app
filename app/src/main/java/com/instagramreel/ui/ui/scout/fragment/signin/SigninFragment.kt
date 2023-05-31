package com.instagramreel.ui.ui.scout.fragment.signin

import android.app.Application
import android.content.Intent
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
import com.instagramreel.databinding.FragmentSigninBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.auth.signinmodel.SignInRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.ui.athlete.activity.AthleteMainActivity
import com.instagramreel.ui.ui.scout.activity.ScoutHomeActivity
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.utils.showHidePass
import com.instagramreel.ui.viewModelFactory.AuthenticationViewModelFactory
import com.instagramreel.ui.viewmodel.auth.AuthenticationViewModel


class SigninFragment : Fragment() {

    lateinit var binding: FragmentSigninBinding
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(requireContext())
    }

    private lateinit var authenticationViewModelFactory: AuthenticationViewModelFactory
    private var authenticationViewModel: AuthenticationViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setObserver()

    }

    private fun setObserver() {
        authenticationViewModel!!.login.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                        appPrefs.setString("token", it.data?.body?.token.toString())
                        appPrefs.setInt("isVerify", it.data?.body?.userData?.isVerified!!.toInt())
                        appPrefs.setInt("role", it.data.body.userData.role)
                        appPrefs.setInt("userID",it.data.body.userData.id)
                        if (it.data.body.userData.isVerified == 0) {
                            findNavController().navigate(R.id.action_signinFragment_to_numberFragment)
                        } else if (it.data.body.userData.isVerified == 1) {
                            if (it.data.body.userData.role == 1) {
                                findNavController().navigate(R.id.action_signinFragment_to_myDetailFragment2)
                            } else if (it.data.body.userData.role == 2) {
                                findNavController().navigate(R.id.action_signinFragment_to_athleteUpdateDetailsFragment2)
                            }
                        } else if (it.data.body.userData.isVerified == 2) {
                            if (it.data.body.userData.role == 1) {
                                startActivity(
                                    Intent(
                                        requireContext(),
                                        ScoutHomeActivity::class.java
                                    )
                                )
                            } else if (it.data.body.userData.role == 2) {
                                startActivity(
                                    Intent(
                                        requireContext(),
                                        AthleteMainActivity::class.java
                                    )
                                )
                            }
                        }
                    }
                    Status.LOADING -> {
                        BaseActivity.showLoader(requireActivity())
                        //Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        BaseActivity.hideLoader()
                        Toast.makeText(
                            requireContext(),
                            resource.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun initUI() {
        authenticationViewModelFactory = AuthenticationViewModelFactory(Application(), Repository())
        authenticationViewModel = ViewModelProvider(this, authenticationViewModelFactory).get(
            AuthenticationViewModel::class.java
        )
        onClick()
    }

    private fun onSNACK(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue)).show()
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidation(): Boolean {
        binding.apply {
            when {
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
                else -> return true
            }
        }
        return false
    }

    private fun onClick() {
        binding.apply {
            tvForgot.setOnClickListener {
                findNavController().navigate(R.id.action_signinFragment_to_forgotPassFragment)
            }

            tvSignup.setOnClickListener {
                findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
            }
            eyeIV.setOnClickListener {
                showHidePass(etPassword, eyeIV)
            }

            btnSignin.setOnClickListener {
                if (isValidation()) {
                    BaseActivity.showLoader(requireActivity())
                    authenticationViewModel?.onClickLoginUser(
                        SignInRequest(
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString()
                        )
                    )
                }
                //startActivity(Intent(requireContext(),AthleteMainActivity::class.java))
            }
        }
    }
}

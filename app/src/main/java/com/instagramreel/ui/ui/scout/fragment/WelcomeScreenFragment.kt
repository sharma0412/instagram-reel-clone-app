package com.instagramreel.ui.ui.scout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.instagramreel.R
import com.instagramreel.databinding.FragmentWelcomeScreenBinding

class WelcomeScreenFragment : Fragment() {

    val callback: WelcomeScreenCallback? = null
    private var position = 0
    lateinit var binding: FragmentWelcomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        onClick()
    }

    private fun onClick() {
        binding.apply {
            btnSignin.setOnClickListener {
                position = 1
                findNavController().navigate(R.id.action_welcomeScreenFragment_to_signinFragment)
            }
            btnSignup.setOnClickListener {
                position = 2
                findNavController().navigate(R.id.action_welcomeScreenFragment_to_signupFragment)
            }
        }
    }

    interface WelcomeScreenCallback {
        fun onClick(position: Int)
    }
}
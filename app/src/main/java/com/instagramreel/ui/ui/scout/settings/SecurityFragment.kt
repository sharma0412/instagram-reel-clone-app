package com.instagramreel.ui.ui.scout.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.instagramreel.R
import com.instagramreel.databinding.FragmentSecurityBinding

class SecurityFragment : Fragment() {

    lateinit var binding: FragmentSecurityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecurityBinding.inflate(inflater, container, false)
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
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            tvChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_securityFragment_to_changePasswordFragment)
            }
        }
    }
}
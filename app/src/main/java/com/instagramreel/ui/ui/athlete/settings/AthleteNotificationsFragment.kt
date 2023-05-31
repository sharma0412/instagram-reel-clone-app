package com.instagramreel.ui.ui.athlete.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.instagramreel.databinding.FragmentNotificationsBinding

class AthleteNotificationsFragment : Fragment() {

    lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
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
                //replaceFragment(AthleteSettingFragment(), isBackStack = true, showAnimation = true)
                findNavController().popBackStack()
            }
        }
    }
}
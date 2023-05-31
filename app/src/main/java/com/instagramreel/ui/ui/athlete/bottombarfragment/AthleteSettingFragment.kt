package com.instagramreel.ui.ui.athlete.bottombarfragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.instagramreel.R
import com.instagramreel.databinding.FragmentAtheleteSettingsBinding
import com.instagramreel.ui.ui.scout.activity.MainActivity
import com.instagramreel.ui.sharedPref.AppPrefs

class AthleteSettingFragment : Fragment() {

    lateinit var binding: FragmentAtheleteSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAtheleteSettingsBinding.inflate(inflater,container,false)
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
            clNotification.setOnClickListener {
                findNavController().navigate(R.id.action_athleteSettingFragment_to_notificationsFragment2)
            }
            clTell.setOnClickListener {
                findNavController().navigate(R.id.action_athleteSettingFragment_to_athleteTellFriendFragment)
            }
            clSecurity.setOnClickListener {
                findNavController().navigate(R.id.action_athleteSettingFragment_to_athleteSecurityFragment)
            }
            clLogout.setOnClickListener {
                promptLogoutConfirmation()
            }
        }
    }

    private fun promptLogoutConfirmation() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Are you sure you want to logout ?")
        builder.setTitle("Logout")
        builder.setCancelable(true)
        builder.setPositiveButton(
            "Yes"
        ) { _, _ ->
            AppPrefs(requireContext()).setToken("token", "")
            startActivity(Intent(requireActivity(),MainActivity::class.java))
        }
        builder.setNegativeButton(
            "No"
        ) { dialogInterface, _ -> dialogInterface.dismiss() }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}
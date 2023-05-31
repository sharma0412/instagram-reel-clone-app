package com.instagramreel.ui.ui.scout.bottombarfragment
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.instagramreel.R
import com.instagramreel.databinding.FragmentSettingBinding
import com.instagramreel.ui.ui.scout.activity.MainActivity
import com.instagramreel.ui.sharedPref.AppPrefs

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
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
                findNavController().navigate(R.id.action_settingFragment_to_notificationsFragment)
            }
            clTell.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_tellFriendFragment)
            }
            clSecurity.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_securityFragment)
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
            //replaceFragment(WelcomeScreenFragment(), isBackStack = false, showAnimation = true)
            startActivity(Intent(requireContext(),MainActivity::class.java))
        }
        builder.setNegativeButton(
            "No"
        ) { dialogInterface, _ -> dialogInterface.dismiss() }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}
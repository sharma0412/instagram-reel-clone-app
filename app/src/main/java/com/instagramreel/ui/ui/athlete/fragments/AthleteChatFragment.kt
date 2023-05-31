package com.instagramreel.ui.ui.athlete.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.instagramreel.databinding.FragmentAthleteChatBinding

class AthleteChatFragment : Fragment(),View.OnClickListener {

    lateinit var binding: FragmentAthleteChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAthleteChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

    }

    private fun initUI() {
        onClickListener()
    }

    private fun onClickListener() {
        binding.apply {
            btnAttachment.setOnClickListener {
                //showPopupWindow(btnAttachment,maincl)
            }
            btnBack.setOnClickListener(this@AthleteChatFragment)
            btnCamera.setOnClickListener {
                cameraPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1)
            }
        }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnBack -> {
                Navigation.findNavController(requireView()).popBackStack()
            }
        }
    }
}
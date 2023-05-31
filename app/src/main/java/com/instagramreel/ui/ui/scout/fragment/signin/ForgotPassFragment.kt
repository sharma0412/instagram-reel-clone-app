package com.instagramreel.ui.ui.scout.fragment.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.instagramreel.databinding.FragmentForgotPassBinding


class ForgotPassFragment : Fragment() {

    lateinit var binding: FragmentForgotPassBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPassBinding.inflate(inflater,container,false)
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
        }
    }
}

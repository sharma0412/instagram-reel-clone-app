package com.instagramreel.ui.ui.athlete.bottombarfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.instagramreel.databinding.FragmentAthleteInboxBinding
import com.instagramreel.ui.callbacks.onBottomNavigationClickAthlete
import com.instagramreel.ui.ui.athlete.adapter.AthleteInboxAdapter


class AthleteInboxFragment : Fragment() {

    lateinit var binding: FragmentAthleteInboxBinding
    var callback: onBottomNavigationClickAthlete? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAthleteInboxBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

    }

    private fun initUI() {
        onClick()
        inboxRV()
    }

    private fun inboxRV() {
        binding.rvInbox.adapter = AthleteInboxAdapter(this, object: AthleteInboxAdapter.AthleteInboxCallback {
            override fun onClick(position: Int) {
                callback?.getChatFragment()
            }
        })
    }

    private fun onClick() {
        binding.apply {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as onBottomNavigationClickAthlete
    }
}
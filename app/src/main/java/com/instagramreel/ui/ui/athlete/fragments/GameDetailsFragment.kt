package com.instagramreel.ui.ui.athlete.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.instagramreel.databinding.FragmentGameDetailsBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.athlete.kickoff.KickoffDetailsRequest
import com.instagramreel.ui.model.athlete.kickoff.KickoffJoinRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.dialog.ReasonDialog
import com.instagramreel.ui.utils.gameDetailDateFormat
import com.instagramreel.ui.utils.gameDetailTimeFormat
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.athlete.kickoff.KickoffViewModel

class GameDetailsFragment : Fragment(), View.OnClickListener {

    private var reasonDialog: ReasonDialog? = null
    lateinit var binding: FragmentGameDetailsBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private var kickoffViewModel: KickoffViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory(Application(), Repository())
        kickoffViewModel = ViewModelProvider(this, viewModelFactory).get(
            KickoffViewModel::class.java
        )
        kickoffViewModel?.kickoffDetails(
            KickoffDetailsRequest(
                arguments?.getInt("kickOffId")!!.toInt()
            )
        )
        setObserver()
        initUi()
    }

    private fun initUi() {
        onClickListener()
    }

    private fun setObserver() {
        kickoffViewModel?.kickOffDetails?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        binding.tvSchedule.text = it.data?.body?.kickoff?.game_name
                        binding.tvHosterName.text = it.data?.body?.kickoff?.hosted_by
                        binding.tvGameName.text = it.data?.body?.kickoff?.kicksport?.name
                        binding.tvGameDayAndTime.text =
                            gameDetailDateFormat(it.data?.body?.kickoff?.date.toString()) + " " + gameDetailTimeFormat(it.data?.body?.kickoff?.start_time.toString())
                        binding.tvGameCityName.text = it.data?.body?.kickoff?.location
                        binding.tvPrice.text = it.data?.body?.kickoff?.fees + "$"

                        Glide.with(requireContext()).load(it.data?.body?.kickoff?.game_image)
                            .into(binding.ivGameImage)

                        when (it.data?.body?.joinkickoff?.status) {
                            null -> {
                                binding.btInterested.visibility = View.VISIBLE
                                binding.btGoing.visibility = View.GONE
                                binding.btnCancel.visibility = View.GONE
                            }
                            0 -> {
                                binding.btInterested.visibility = View.GONE
                                binding.btGoing.visibility = View.VISIBLE
                                binding.btnCancel.visibility = View.GONE
                            }
                            1 -> {
                                binding.btInterested.visibility = View.GONE
                                binding.btGoing.visibility = View.GONE
                                binding.btnCancel.visibility = View.VISIBLE
                            }
                            2 -> {
                                binding.btInterested.visibility = View.GONE
                                binding.btGoing.visibility = View.GONE
                                binding.btnCancel.visibility = View.GONE

                                binding.tvReason.text = it.data.body.joinkickoff.reason.toString()
                            }
                        }
                    }
                    Status.LOADING -> {
                        //binding.shimmerFrameLayout.startShimmer()
                        BaseActivity.showLoader(requireActivity())
                    }
                    Status.ERROR -> {
                        BaseActivity.hideLoader()
                        //binding.shimmerFrameLayout.startShimmer()
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        kickoffViewModel?.kickOffJoin?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()

                        kickoffViewModel?.kickoffDetails(
                            KickoffDetailsRequest(
                                arguments?.getInt("kickOffId")!!.toInt()
                            )
                        )

                        reasonDialog?.dismiss()
                    }
                    Status.LOADING -> {
                        //binding.shimmerFrameLayout.startShimmer()
                        BaseActivity.showLoader(requireActivity())
                    }
                    Status.ERROR -> {
                        BaseActivity.hideLoader()
                        //binding.shimmerFrameLayout.startShimmer()
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun onClickListener() {
        binding.apply {
            ivCancel.setOnClickListener(this@GameDetailsFragment)
            btnCancel.setOnClickListener(this@GameDetailsFragment)
            btInterested.setOnClickListener(this@GameDetailsFragment)
            btGoing.setOnClickListener(this@GameDetailsFragment)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivCancel -> {
                findNavController().popBackStack()
            }
            binding.btInterested -> {
                kickoffViewModel?.joinKickoff(
                    KickoffJoinRequest(
                        arguments?.getInt("kickOffId")!!.toInt(), 0, null.toString()
                    )
                )
            }
            binding.btGoing -> {
                kickoffViewModel?.joinKickoff(
                    KickoffJoinRequest(
                        arguments?.getInt("kickOffId")!!.toInt(), 1, null.toString()
                    )
                )
            }
            binding.btnCancel -> {
                reasonDialog = ReasonDialog(requireContext(), object : ReasonDialog.Listener {
                    override fun onSelectVerifyKeyStatus(value: String) {
                        kickoffViewModel?.joinKickoff(
                            KickoffJoinRequest(
                                arguments?.getInt("kickOffId")!!.toInt(), 2, value
                            )
                        )
                    }
                })
                reasonDialog?.show()
            }
        }
    }
}
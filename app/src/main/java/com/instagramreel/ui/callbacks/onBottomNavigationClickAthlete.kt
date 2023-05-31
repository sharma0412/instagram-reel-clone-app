package com.instagramreel.ui.callbacks

import com.instagramreel.ui.model.athlete.kickoff.KickOffResponse

interface onBottomNavigationClickAthlete {
    fun getCurrentFragment(model: KickOffResponse.Body)
    fun getChatFragment()
    fun getAtheleteSettingsFragment()
}
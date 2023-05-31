package com.instagramreel.ui.model.athlete.kickoff

data class KickoffJoinRequest(
    val kickoff_id: Int,
    val status: Int,
    val reason: String
)
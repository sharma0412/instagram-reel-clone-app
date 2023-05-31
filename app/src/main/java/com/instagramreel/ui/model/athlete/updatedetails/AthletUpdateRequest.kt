package com.instagramreel.ui.model.athlete.updatedetails

data class AthletUpdateRequest(
    val fullname: String,
    val dob: String,
    val height: String,
    val position: String,
    val sport_id: ArrayList<SportId>,
    val weight: String,
    val bio: String,
    val profile_pic: String
)
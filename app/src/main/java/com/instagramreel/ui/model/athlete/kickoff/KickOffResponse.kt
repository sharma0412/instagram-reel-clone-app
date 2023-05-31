package com.instagramreel.ui.model.athlete.kickoff

import java.io.Serializable

data class KickOffResponse(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
) :Serializable{
    data class Body(
        val created_at: String,
        val date: String,
        val end_time: String,
        val fees: String,
        val game_image: String,
        val game_name: String,
        val hosted_by: String,
        val id: Int,
        val kicksport: Kicksport,
        val lat: String,
        val location: String,
        val long: String,
        val sport_id: String,
        val start_time: String,
        val updated_at: String
    ) :Serializable{
        data class Kicksport(
            val created_at: String,
            val id: Int,
            val name: String,
            val status: Int,
            val updated_at: String
        )
    }
}
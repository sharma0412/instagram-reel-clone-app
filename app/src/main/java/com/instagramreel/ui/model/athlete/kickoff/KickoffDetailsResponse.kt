package com.instagramreel.ui.model.athlete.kickoff

data class KickoffDetailsResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
) {
    data class Body(
        val joinkickoff: Joinkickoff,
        val kickoff: Kickoff
    ) {
        data class Joinkickoff(
            val created_at: String,
            val id: Int,
            val kickoff_id: String,
            val reason: Any,
            val status: Int,
            val updated_at: String,
            val user_id: String
        )

        data class Kickoff(
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
        ) {
            data class Kicksport(
                val created_at: String,
                val id: Int,
                val name: String,
                val status: Int,
                val updated_at: String
            )
        }
    }
}
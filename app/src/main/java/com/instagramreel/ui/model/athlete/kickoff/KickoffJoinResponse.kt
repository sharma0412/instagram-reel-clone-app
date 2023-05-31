package com.instagramreel.ui.model.athlete.kickoff

data class KickoffJoinResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
) {
    data class Body(
        val created_at: CreatedAt,
        val id: Int,
        val kickoff_id: Int,
        val status: Int,
        val updated_at: UpdatedAt,
        val user_id: Int
    ) {
        data class CreatedAt(
            val `val`: String
        )

        data class UpdatedAt(
            val `val`: String
        )
    }
}
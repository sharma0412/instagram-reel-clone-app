package com.instagramreel.ui.model.reels

data class UploadReelsResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
) {
    data class Body(
        val created_at: CreatedAt,
        val id: Int,
        val sport_id: String,
        val updated_at: UpdatedAt,
        val user_id: Int,
        val video_thumbnail: String,
        val video_url: String
    ) {
        data class CreatedAt(
            val `val`: String
        )

        data class UpdatedAt(
            val `val`: String
        )
    }
}
package com.momora.ai.data.network.dto

import com.google.gson.annotations.SerializedName

data class ChatRequestDto(
    @SerializedName("user_id")
    val userId: String = "default_user",
    @SerializedName("query")
    val query: String,
    @SerializedName("session_id")
    val sessionId: String = "default_session"
)

package com.momora.ai.data.network.dto

import com.google.gson.annotations.SerializedName
import com.momora.ai.presentation.chat.SourceCitation

data class ChatResponseDto(
    @SerializedName("query")
    val query: String,
    @SerializedName("answer")
    val answer: String,
    @SerializedName("sources")
    val sources: List<SourceCitationDto>,
    @SerializedName("timestamp")
    val timestamp: String
)

data class SourceCitationDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("confidence")
    val confidence: Int
) {
    fun toDomainModel(): SourceCitation {
        return SourceCitation(
            type = type,
            detail = detail,
            confidence = confidence
        )
    }
}

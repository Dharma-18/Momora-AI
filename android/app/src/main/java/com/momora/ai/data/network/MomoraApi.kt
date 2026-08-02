package com.momora.ai.data.network

import com.momora.ai.data.network.dto.ChatRequestDto
import com.momora.ai.data.network.dto.ChatResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface MomoraApi {
    @POST("/api/v1/chat")
    suspend fun sendChatQuery(
        @Body request: ChatRequestDto
    ): ChatResponseDto
}

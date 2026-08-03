package com.momora.ai.data.network

import com.momora.ai.data.network.dto.ChatRequestDto
import com.momora.ai.data.network.dto.ChatResponseDto
import com.momora.ai.data.network.dto.DocumentUploadResponseDto
import com.momora.ai.data.network.dto.TextIngestRequestDto
import com.momora.ai.data.network.dto.TextIngestResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MomoraApi {
    @POST("/api/v1/chat")
    suspend fun sendChatQuery(
        @Body request: ChatRequestDto
    ): ChatResponseDto

    @Multipart
    @POST("/api/v1/documents/upload")
    suspend fun uploadDocument(
        @Part file: MultipartBody.Part,
        @Part("source_type") sourceType: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("category") category: RequestBody
    ): DocumentUploadResponseDto

    @POST("/api/v1/documents/text")
    suspend fun ingestText(
        @Body request: TextIngestRequestDto
    ): TextIngestResponseDto
}

package com.momora.ai.data.network.dto

import com.google.gson.annotations.SerializedName

data class DocumentUploadResponseDto(
    @SerializedName("document_id")
    val documentId: String,
    @SerializedName("filename")
    val filename: String,
    @SerializedName("source_type")
    val sourceType: String,
    @SerializedName("num_chunks")
    val numChunks: Int,
    @SerializedName("message")
    val message: String
)

data class TextIngestRequestDto(
    @SerializedName("user_id")
    val userId: String = "default_user",
    @SerializedName("text")
    val text: String,
    @SerializedName("source_name")
    val sourceName: String,
    @SerializedName("source_type")
    val sourceType: String = "TextFile"
)

data class TextIngestResponseDto(
    @SerializedName("document_id")
    val documentId: String,
    @SerializedName("source_name")
    val sourceName: String,
    @SerializedName("num_chunks")
    val numChunks: Int,
    @SerializedName("message")
    val message: String
)

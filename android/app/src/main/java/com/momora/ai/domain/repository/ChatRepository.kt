package com.momora.ai.domain.repository

import com.momora.ai.presentation.chat.ChatMessage

interface ChatRepository {
    suspend fun sendMessage(query: String): Result<ChatMessage.AI>
}

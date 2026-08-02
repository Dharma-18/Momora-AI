package com.momora.ai.data.repository

import com.momora.ai.data.network.MomoraApi
import com.momora.ai.data.network.dto.ChatRequestDto
import com.momora.ai.domain.repository.ChatRepository
import com.momora.ai.presentation.chat.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val api: MomoraApi
) : ChatRepository {

    override suspend fun sendMessage(query: String): Result<ChatMessage.AI> = withContext(Dispatchers.IO) {
        try {
            val request = ChatRequestDto(query = query)
            val response = api.sendChatQuery(request)
            
            // Map the network DTO to our UI model
            val domainSources = response.sources.map { it.toDomainModel() }
            
            Result.success(
                ChatMessage.AI(
                    text = response.answer,
                    sources = domainSources
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

package com.momora.ai.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momora.ai.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // Add welcome message on start
        _uiState.update { state ->
            state.copy(
                messages = listOf(
                    ChatMessage.AI("Hello! I'm Momora, your personal AI second brain. What can I help you remember today?")
                )
            )
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // 1. Add user message to UI immediately
        val userMessage = ChatMessage.User(text)
        _uiState.update { state ->
            state.copy(
                messages = state.messages + userMessage,
                isLoading = true,
                error = null
            )
        }

        // 2. Make network call
        viewModelScope.launch {
            repository.sendMessage(text)
                .onSuccess { aiResponse ->
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + aiResponse,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = exception.localizedMessage ?: "Failed to get response"
                        )
                    }
                    // Add error message to chat
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + ChatMessage.AI("Sorry, I encountered a network error. Ensure the backend is running and adb reverse is setup.")
                        )
                    }
                }
        }
    }
}

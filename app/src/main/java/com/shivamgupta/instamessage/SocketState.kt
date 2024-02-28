package com.shivamgupta.instamessage

sealed class SocketState {
    data class Success(val data: String): SocketState()
    data class IsConnected(val connected: Boolean): SocketState()
}

data class MessageUiState(
    val messages: List<String> = emptyList(),
    val isConnected: Boolean = false
)
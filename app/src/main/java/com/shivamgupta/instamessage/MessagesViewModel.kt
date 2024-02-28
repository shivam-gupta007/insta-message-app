package com.shivamgupta.instamessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessagesViewModel(
    private val webSocketService: MessageWebSocketService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState get() = _uiState.asStateFlow()

    fun openConnection(){
        viewModelScope.launch {
            webSocketService.openSocketConnection().collect { state ->
                _uiState.update { currentUiState ->
                    when(state){
                        is SocketState.IsConnected -> {
                            currentUiState.copy(isConnected = state.connected)
                        }
                        is SocketState.Success -> {
                            val updatedMessages = currentUiState.messages.toMutableList()
                            updatedMessages.add(state.data)
                            currentUiState.copy(messages = updatedMessages)
                        }
                    }
                }
            }
        }
    }

    fun sendUserMessage(message: String) {
        viewModelScope.launch {
            webSocketService.sendMessage(message)
        }
    }

    fun closeConnection() {
        viewModelScope.launch {
            webSocketService.closeSocketConnection()
            _uiState.update {
                it.copy(isConnected = false, messages = emptyList())
            }
        }
    }
}

class MessagesViewModelFactory(
    private val webSocketService: MessageWebSocketService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MessagesViewModel(webSocketService) as T
    }
}
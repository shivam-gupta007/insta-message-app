package com.shivamgupta.instamessage

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

const val TAG = "MessageWebSocketService"

class MessageWebSocketService {

    companion object {
        private const val SOCKET_URL = "ws://insta-message-ktor.onrender.com/chat"
    }

    private val client = HttpClient {
        install(WebSockets)
    }

    private var currentSocketSession: WebSocketSession? = null

    fun openSocketConnection(): Flow<SocketState> = flow{
        client.wss(SOCKET_URL) {
            currentSocketSession = this

            emit(SocketState.IsConnected(true))

            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val message = frame.readText()
                        emit(SocketState.Success(message))
                        Log.d(TAG, "receiveMessages: message - $message")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "receiveMessages: exception - $e")
                emit(SocketState.Success("Error in fetching messages!"))
                emit(SocketState.IsConnected(false))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun closeSocketConnection(){
        client.close()
        currentSocketSession?.close()
    }

    suspend fun sendMessage(message: String){
        currentSocketSession?.send(message)
    }

}
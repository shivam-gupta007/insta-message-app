package com.shivamgupta.instamessage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shivamgupta.instamessage.ui.theme.InstaMessageTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MessagesViewModel> {
        MessagesViewModelFactory(MessageWebSocketService())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            InstaMessageTheme {
                val state = viewModel.uiState.collectAsState().value
                val messages = state.messages
                val isConnected = state.isConnected

                var text: String by remember { mutableStateOf("") }
                val context = LocalContext.current

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = if (isConnected) "Disconnect from the chat." else "Connect to the chat",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Button(
                            onClick = {
                                if(isConnected){
                                   viewModel.closeConnection()
                                   text = ""
                                } else {
                                   viewModel.openConnection()
                                }
                            },
                        ) {
                            Text(
                                text = if (isConnected) "Disconnect" else "Connect"
                            )
                        }

                        if (isConnected) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = text,
                                onValueChange = { text = it },
                            )

                            Button(onClick = {
                                if (text.isEmpty()) {
                                    Toast.makeText(
                                        context, "Please enter some message!", Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.sendUserMessage(message = text)
                                    text = ""
                                }
                            }) {
                                Text(text = "Send")
                            }
                        }

                        LazyColumn {
                            items(messages) {
                                UserMessage(text = it)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.closeConnection()
    }
}



@Preview(showBackground = true)
@Composable
fun InstaMessagePreview() {
    InstaMessageTheme {

    }
}
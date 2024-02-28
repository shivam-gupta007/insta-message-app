package com.shivamgupta.instamessage

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UserMessage(
    modifier: Modifier = Modifier,
    text: String
){

    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
fun UserMessagePreview(){
    UserMessage(
        text = "Hello, I am  Shivam Gupta,an Android Developer "
    )
}
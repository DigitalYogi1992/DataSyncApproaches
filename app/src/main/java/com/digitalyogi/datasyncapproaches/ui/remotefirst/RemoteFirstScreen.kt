package com.digitalyogi.datasyncapproaches.ui.remotefirst

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digitalyogi.datasyncapproaches.data.local.MyEntity

@Composable
fun RemoteFirstScreen(
    viewModel: RemoteFirstViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var textState by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                label = { Text("Enter Data") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if(textState.isNotEmpty()){
                    viewModel.addItem(textState)
                    textState = ""
                }else{
                    viewModel.setError("Please enter data")
                }
            }) {
                Text("Add Remote")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.fetchDataFromServer() }) {
            Text("Fetch From Server")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(items) { entity: MyEntity ->
                Text(
                    text = "ID: ${entity.id}, Content: ${entity.content}, Synced: ${entity.isSynced}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(errorMessage!!) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadLocalData()
    }
}

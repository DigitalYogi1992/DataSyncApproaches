package com.digitalyogi.datasyncapproaches.ui.offlinefirst

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digitalyogi.datasyncapproaches.data.local.MyEntity

@Composable
fun OfflineFirstScreen(
    viewModel: OfflineFirstViewModel = hiltViewModel()
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
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.syncData() }) {
            Text("Sync Unsynced Data")
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

    // Show any error message
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

    // Load local data on first composition
    LaunchedEffect(Unit) {
        viewModel.loadLocalData()
    }
}

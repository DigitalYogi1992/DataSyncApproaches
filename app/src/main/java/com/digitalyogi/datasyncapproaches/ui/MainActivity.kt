package com.digitalyogi.datasyncapproaches.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.digitalyogi.datasyncapproaches.ui.hybrid.HybridScreen
import com.digitalyogi.datasyncapproaches.ui.offlinefirst.OfflineFirstScreen
import com.digitalyogi.datasyncapproaches.ui.remotefirst.RemoteFirstScreen
import com.digitalyogi.datasyncapproaches.ui.theme.DataSyncApproachesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataSyncApproachesTheme {
                Surface {
                    // You can swap these out:
                    OfflineFirstScreen()
                    //  RemoteFirstScreen()
                    // HybridScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DataSyncApproachesTheme {
        // You can swap these out:
         OfflineFirstScreen()
        // RemoteFirstScreen()
       // HybridScreen()
    }
}
package app.storage.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun App() {
    var selectedStorage by remember { mutableStateOf<String?>(null) }

    if (selectedStorage == null) {
        HomeScreen(
            onStorageSelected = { selectedStorage = it }
        )
    } else {
        RunScreen(
            storageType = selectedStorage!!,
            onBack = { selectedStorage = null }
        )
    }
}

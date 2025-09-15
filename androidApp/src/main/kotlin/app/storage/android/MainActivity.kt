package app.storage.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.storage.repo.initializeRepositories
import app.storage.repo.initializeSqlDelightRepository
import app.storage.util.FilePaths

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FilePaths.initialize(this)
        initializeRepositories(this)
        initializeSqlDelightRepository(this)

        setContent {
            AndroidApp()
        }
    }
}

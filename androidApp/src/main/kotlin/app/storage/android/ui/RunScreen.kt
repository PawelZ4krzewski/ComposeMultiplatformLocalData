package app.storage.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.storage.android.ui.widgets.NoteEditor
import app.storage.android.ui.widgets.ProgressOverlay
import app.storage.db.NotesDatabase
import app.storage.model.Note
import app.storage.repo.FileRepository
import app.storage.repo.PrefsRepository
import app.storage.repo.SqlDelightRepository
import app.storage.sql.DriverFactory
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunScreen(
    storageType: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val repository = remember {
        when (storageType) {
            "Preferences" -> PrefsRepository(
                SharedPreferencesSettings(
                    context.getSharedPreferences(
                        "notes",
                        0
                    )
                )
            )

            "File JSON" -> FileRepository()
            "SQLite" -> SqlDelightRepository(NotesDatabase(DriverFactory(context).createDriver()))
            else -> throw IllegalArgumentException("Unknown storage type: $storageType")
        }
    }

    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showEditor by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    fun loadNotes() {
        scope.launch {
            isLoading = true
            try {
                notes = repository.getAll()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadNotes()
    }

    if (showEditor) {
        NoteEditor(
            note = editingNote,
            onSave = { note ->
                scope.launch {
                    isLoading = true
                    try {
                        if (editingNote == null) {
                            repository.insert(note.copy(id = System.currentTimeMillis()))
                        } else {
                            repository.update(note)
                        }
                        loadNotes()
                    } finally {
                        isLoading = false
                        showEditor = false
                        editingNote = null
                    }
                }
            },
            onCancel = {
                showEditor = false
                editingNote = null
            }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(storageType) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        listOf(
                            "Seed 10",
                            "List",
                            "Add",
                            "Edit first",
                            "Delete first",
                            "Clear"
                        )
                    ) { action ->
                        Button(
                            onClick = {
                                when (action) {
                                    "Seed 10" -> {
                                        scope.launch {
                                            isLoading = true
                                            try {
                                                repository.seedData(10)
                                                loadNotes()
                                            } finally {
                                                isLoading = false
                                            }
                                        }
                                    }

                                    "List" -> loadNotes()
                                    "Add" -> {
                                        editingNote = null
                                        showEditor = true
                                    }

                                    "Edit first" -> {
                                        if (notes.isNotEmpty()) {
                                            editingNote = notes.first()
                                            showEditor = true
                                        }
                                    }

                                    "Delete first" -> {
                                        if (notes.isNotEmpty()) {
                                            scope.launch {
                                                isLoading = true
                                                try {
                                                    repository.delete(notes.first().id)
                                                    loadNotes()
                                                } finally {
                                                    isLoading = false
                                                }
                                            }
                                        }
                                    }

                                    "Clear" -> {
                                        scope.launch {
                                            isLoading = true
                                            try {
                                                repository.clear()
                                                loadNotes()
                                            } finally {
                                                isLoading = false
                                            }
                                        }
                                    }
                                }
                            },
                            enabled = !isLoading,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(action)
                        }
                    }
                }

                if (notes.isEmpty() && !isLoading) {
                    Text(
                        text = "No notes",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        items(notes) { note ->
                            NoteItem(note = note)
                            if (note != notes.last()) {
                                HorizontalDivider(thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }

    if (isLoading) {
        ProgressOverlay()
    }
}

@Composable
private fun NoteItem(note: Note) {
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dateFormatter.format(Date(note.createdAt)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

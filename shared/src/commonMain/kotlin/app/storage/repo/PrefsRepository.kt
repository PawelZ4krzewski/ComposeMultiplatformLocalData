package app.storage.repo

import app.storage.model.Note
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class PrefsRepository(private val settings: Settings) : StorageRepository {

    override suspend fun getAll(): List<Note> = withContext(Dispatchers.Default) {
        val count = settings.getInt("notes_count", 0)
        (0 until count).mapNotNull { index ->
            val id = settings.getLong("note_${index}_id", -1)
            if (id == -1L) return@mapNotNull null

            Note(
                id = id,
                title = settings.getString("note_${index}_title", ""),
                content = settings.getString("note_${index}_content", ""),
                createdAt = settings.getLong("note_${index}_createdAt", 0),
                tags = settings.getString("note_${index}_tags", "").split(",")
                    .filter { it.isNotEmpty() }
            )
        }
    }

    override suspend fun insert(note: Note) = withContext(Dispatchers.Default) {
        val notes = getAll().toMutableList()
        notes.add(note)
        saveAll(notes)
    }

    override suspend fun update(note: Note) = withContext(Dispatchers.Default) {
        val notes = getAll().toMutableList()
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
            saveAll(notes)
        }
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.Default) {
        val notes = getAll().toMutableList()
        notes.removeAll { it.id == id }
        saveAll(notes)
    }

    override suspend fun clear() = withContext(Dispatchers.Default) {
        saveAll(emptyList())
    }

    override suspend fun seedData(count: Int) = withContext(Dispatchers.Default) {
        val notes = (1..count).map { i ->
            Note(
                id = i.toLong(),
                title = "Preferences Note $i",
                content = "Content for preferences note $i",
                createdAt = Clock.System.now().toEpochMilliseconds() - (i * 60000),
                tags = listOf("prefs", "tag$i")
            )
        }
        saveAll(notes)
    }

    private fun saveAll(notes: List<Note>) {
        settings.clear()
        settings.putInt("notes_count", notes.size)
        notes.forEachIndexed { index, note ->
            settings.putLong("note_${index}_id", note.id)
            settings.putString("note_${index}_title", note.title)
            settings.putString("note_${index}_content", note.content)
            settings.putLong("note_${index}_createdAt", note.createdAt)
            settings.putString("note_${index}_tags", note.tags.joinToString(","))
        }
    }
}

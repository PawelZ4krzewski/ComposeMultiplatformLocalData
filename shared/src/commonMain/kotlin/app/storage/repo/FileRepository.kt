package app.storage.repo

import app.storage.model.Note
import app.storage.util.FilePaths
import app.storage.util.JsonCodec
import app.storage.util.PlatformFileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class FileRepository : StorageRepository {

    private val filePath = FilePaths.getNotesJsonPath()

    override suspend fun getAll(): List<Note> = withContext(Dispatchers.Default) {
        try {
            val content = PlatformFileManager.readFile(filePath) ?: return@withContext emptyList()
            JsonCodec.decode(content)
        } catch (e: Exception) {
            emptyList()
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
                title = "File JSON Note $i",
                content = "Content for file JSON note $i",
                createdAt = Clock.System.now().toEpochMilliseconds() - (i * 60000),
                tags = listOf("file", "json", "tag$i")
            )
        }
        saveAll(notes)
    }

    private suspend fun saveAll(notes: List<Note>) {
        try {
            val content = JsonCodec.encode(notes)
            PlatformFileManager.writeFile(filePath, content)
        } catch (e: Exception) {
            // Log error if needed
        }
    }
}

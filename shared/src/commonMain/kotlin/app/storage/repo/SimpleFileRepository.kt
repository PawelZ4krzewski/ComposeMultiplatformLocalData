package app.storage.repo

import app.storage.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SimpleFileRepository : StorageRepository {

    override suspend fun getAll(): List<Note> = withContext(Dispatchers.Default) {
        // Tymczasowa implementacja bez Okio
        emptyList()
    }

    override suspend fun insert(note: Note) = withContext(Dispatchers.Default) {
        // TODO: Implementacja tymczasowa
    }

    override suspend fun update(note: Note) = withContext(Dispatchers.Default) {
        // TODO: Implementacja tymczasowa
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.Default) {
        // TODO: Implementacja tymczasowa
    }

    override suspend fun clear() = withContext(Dispatchers.Default) {
        // TODO: Implementacja tymczasowa
    }

    override suspend fun seedData(count: Int) = withContext(Dispatchers.Default) {
        // TODO: Implementacja tymczasowa
    }
}

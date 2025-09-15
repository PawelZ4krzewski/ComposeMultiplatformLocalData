package app.storage.repo

import app.storage.model.Note

interface StorageRepository {
    suspend fun getAll(): List<Note>
    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(id: Long)
    suspend fun clear()
    suspend fun seedData(count: Int)
}

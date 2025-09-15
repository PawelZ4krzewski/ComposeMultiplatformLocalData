package app.storage.repo

import app.storage.db.NotesDatabase
import app.storage.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SqlDelightRepository(private val database: NotesDatabase) : StorageRepository {

    override suspend fun getAll(): List<Note> = withContext(Dispatchers.Default) {
        database.notesQueries.selectAll().executeAsList().map { noteEntity ->
            Note(
                id = noteEntity.id,
                title = noteEntity.title,
                content = noteEntity.content,
                createdAt = noteEntity.created_at,
                tags = noteEntity.tags_json?.let {
                    Json.decodeFromString<List<String>>(it)
                } ?: emptyList(),
                priority = noteEntity.priority.toInt()
            )
        }
    }

    override suspend fun insert(note: Note) = withContext(Dispatchers.Default) {
        database.notesQueries.insert(
            id = note.id,
            title = note.title,
            content = note.content,
            created_at = note.createdAt,
            tags_json = Json.encodeToString(note.tags),
            priority = note.priority.toLong()
        )
    }

    override suspend fun update(note: Note) = withContext(Dispatchers.Default) {
        database.notesQueries.update(
            title = note.title,
            content = note.content,
            created_at = note.createdAt,
            tags_json = Json.encodeToString(note.tags),
            priority = note.priority.toLong(),
            id = note.id
        )
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.Default) {
        database.notesQueries.delete(id)
    }

    override suspend fun clear() = withContext(Dispatchers.Default) {
        database.notesQueries.deleteAll()
    }

    override suspend fun seedData(count: Int) = withContext(Dispatchers.Default) {
        database.transaction {
            (1..count).forEach { i ->
                database.notesQueries.insert(
                    id = i.toLong(),
                    title = "SQLite Note $i",
                    content = "Content for SQLite note $i",
                    created_at = Clock.System.now().toEpochMilliseconds() - (i * 60000),
                    tags_json = Json.encodeToString(listOf("sqlite", "db", "tag$i")),
                    priority = (i % 3).toLong() // priorities 0, 1, 2
                )
            }
        }
    }
}

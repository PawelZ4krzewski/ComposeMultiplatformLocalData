package app.storage.util

import app.storage.model.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonCodec {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun encode(notes: List<Note>): String = json.encodeToString(notes)

    fun decode(content: String): List<Note> = json.decodeFromString(content)
}

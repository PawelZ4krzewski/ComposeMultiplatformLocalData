package app.storage.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
    val tags: List<String>,
    val priority: Int = 0
)

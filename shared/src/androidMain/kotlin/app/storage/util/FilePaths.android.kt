package app.storage.util

import android.content.Context

actual object FilePaths {
    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context
    }

    actual fun getDocumentsDirectory(): String {
        return context.filesDir.absolutePath
    }

    actual fun getNotesJsonPath(): String {
        return "${getDocumentsDirectory()}/notes.json"
    }
}

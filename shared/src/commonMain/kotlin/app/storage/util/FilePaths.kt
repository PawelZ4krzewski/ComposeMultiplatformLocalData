package app.storage.util

expect object FilePaths {
    fun getDocumentsDirectory(): String
    fun getNotesJsonPath(): String
}

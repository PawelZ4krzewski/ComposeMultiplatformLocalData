package app.storage.util

// Expect/actual pattern for platform-specific file operations
expect object PlatformFileManager {
    fun readFile(filePath: String): String?
    fun writeFile(filePath: String, content: String): Boolean
    fun fileExists(filePath: String): Boolean
    fun createDirectories(dirPath: String): Boolean
}

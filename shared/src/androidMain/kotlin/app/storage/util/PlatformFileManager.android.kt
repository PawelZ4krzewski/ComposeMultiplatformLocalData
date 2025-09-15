package app.storage.util

import java.io.File

actual object PlatformFileManager {
    actual fun readFile(filePath: String): String? {
        return try {
            val file = File(filePath)
            if (file.exists()) file.readText() else null
        } catch (e: Exception) {
            null
        }
    }

    actual fun writeFile(filePath: String, content: String): Boolean {
        return try {
            val file = File(filePath)
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    actual fun fileExists(filePath: String): Boolean {
        return try {
            File(filePath).exists()
        } catch (e: Exception) {
            false
        }
    }

    actual fun createDirectories(dirPath: String): Boolean {
        return try {
            File(dirPath).mkdirs()
        } catch (e: Exception) {
            false
        }
    }
}

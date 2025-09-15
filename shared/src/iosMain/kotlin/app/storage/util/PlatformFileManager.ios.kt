package app.storage.util

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class)
actual object PlatformFileManager {
    actual fun readFile(filePath: String): String? {
        return try {
            NSString.stringWithContentsOfFile(filePath, NSUTF8StringEncoding, null)
        } catch (e: Exception) {
            null
        }
    }

    actual fun writeFile(filePath: String, content: String): Boolean {
        return try {
            val nsString = NSString.create(string = content)
            // Simplified approach - just write directly without creating parent directories for now
            nsString.writeToFile(filePath, true, NSUTF8StringEncoding, null)
        } catch (e: Exception) {
            false
        }
    }

    actual fun fileExists(filePath: String): Boolean {
        return NSFileManager.defaultManager.fileExistsAtPath(filePath)
    }

    actual fun createDirectories(dirPath: String): Boolean {
        return try {
            NSFileManager.defaultManager.createDirectoryAtPath(
                dirPath,
                true,
                null,
                null
            )
        } catch (e: Exception) {
            false
        }
    }
}

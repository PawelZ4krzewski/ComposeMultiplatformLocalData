package app.storage.util

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual object FilePaths {
    actual fun getDocumentsDirectory(): String {
        val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        return paths.firstOrNull() as? String ?: ""
    }

    actual fun getNotesJsonPath(): String {
        return "${getDocumentsDirectory()}/notes.json"
    }
}

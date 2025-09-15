package app.storage.repo

import com.russhwolf.settings.NSUserDefaultsSettings

actual fun createPrefsRepository(): PrefsRepository {
    return PrefsRepository(NSUserDefaultsSettings.Factory().create())
}

package app.storage.repo

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual fun createSettingsInstance(): Settings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}

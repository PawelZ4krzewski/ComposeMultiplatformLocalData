package app.storage.repo

import com.russhwolf.settings.Settings

object SettingsFactory {
    fun createSettings(): Settings = createSettingsInstance()
}

expect fun createSettingsInstance(): Settings

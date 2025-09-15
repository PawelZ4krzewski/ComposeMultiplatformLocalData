package app.storage.repo

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

// Temporary global context holder - in real app use DI
private var appContext: Context? = null

fun initializeAppContext(context: Context) {
    appContext = context.applicationContext
}

actual fun createSettingsInstance(): Settings {
    val context = appContext ?: throw IllegalStateException("App context not initialized")
    val sharedPrefs = context.getSharedPreferences("app_storage_settings", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPrefs)
}

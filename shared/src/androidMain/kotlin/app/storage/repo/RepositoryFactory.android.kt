package app.storage.repo

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings

private lateinit var context: Context

fun initializeRepositories(context: Context) {
    app.storage.repo.context = context
}

actual fun createPrefsRepository(): PrefsRepository {
    return PrefsRepository(SharedPreferencesSettings(context.getSharedPreferences("notes", 0)))
}

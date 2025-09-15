package app.storage.repo

import android.content.Context
import app.storage.db.NotesDatabase
import app.storage.sql.DriverFactory

private lateinit var sqlContext: Context

fun initializeSqlDelightRepository(context: Context) {
    sqlContext = context
}

actual fun createSqlDelightRepository(): SqlDelightRepository {
    val driver = DriverFactory(sqlContext).createDriver()
    return SqlDelightRepository(NotesDatabase(driver))
}

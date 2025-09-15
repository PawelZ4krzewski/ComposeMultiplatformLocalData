package app.storage.repo

import app.storage.db.NotesDatabase
import app.storage.sql.DriverFactory

actual fun createSqlDelightRepository(): SqlDelightRepository {
    val driver = DriverFactory().createDriver()
    return SqlDelightRepository(NotesDatabase(driver))
}

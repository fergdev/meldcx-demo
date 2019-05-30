package nz.co.meldcxtest.service

import androidx.room.Database
import androidx.room.RoomDatabase
import nz.co.meldcxtest.model.Capture

/**
 * Room app database.
 */
@Database(entities = [Capture::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun captureDao(): CaptureDao
}
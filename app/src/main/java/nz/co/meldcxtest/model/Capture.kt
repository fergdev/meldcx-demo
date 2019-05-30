package nz.co.meldcxtest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Capture dto. Contains information about the url and
 * location of the associated bitmap on disk.
 */
@Entity
data class Capture(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "fileName") val fileName: String,
    @ColumnInfo(name = "dateTime") val date: Long
)
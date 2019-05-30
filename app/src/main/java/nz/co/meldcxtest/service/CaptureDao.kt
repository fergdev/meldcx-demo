package nz.co.meldcxtest.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single
import nz.co.meldcxtest.model.Capture

/**
 * Data access object for the capture.
 */
@Dao
interface CaptureDao {

    @Query("SELECT * FROM Capture")
    fun getAllCaptures(): Single<List<Capture>>

    @Query("SELECT * FROM Capture WHERE uid=:id")
    fun getCapture(id: Int): Single<Capture>

    @Insert
    fun saveCapture(captureEntity: Capture)

    @Delete
    fun delete(captureEntity: Capture)
}
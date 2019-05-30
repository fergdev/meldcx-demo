package nz.co.meldcxtest.service

import android.graphics.Bitmap
import io.reactivex.Completable
import io.reactivex.Single
import nz.co.meldcxtest.model.Capture
import java.util.*

/**
 * Service interface to handle management of the different captures.
 */
interface ICaptureService {

    /**
     * Get all the saved captures.
     */
    fun getAllCaptures(): Single<List<Capture>>

    /**
     * Get a single capture base on give id.
     */
    fun getCapture(id: Int): Single<Capture>

    /**
     * Delete the given capture.
     */
    fun deleteCapture(capture: Capture): Completable

    /**
     * Save the given data to a capture.
     */
    fun saveCapture(url: String, bitmap: Bitmap, date: Date): Completable
}
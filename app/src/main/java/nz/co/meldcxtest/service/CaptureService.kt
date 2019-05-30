package nz.co.meldcxtest.service

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Room
import io.reactivex.Completable
import io.reactivex.Single
import nz.co.meldcxtest.CAPTURE_DIRECTORY
import nz.co.meldcxtest.model.Capture
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CaptureService(context: Context) : ICaptureService {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "capture-database"
    ).build()

    @Throws(Throwable::class)
    override fun saveCapture(url: String, bitmap: Bitmap, date: Date): Completable {
        return Completable.create {
            if (!CAPTURE_DIRECTORY.exists()) CAPTURE_DIRECTORY.mkdir()

            val timeStamp = FILE_DATE_FORMAT.format(Calendar.getInstance().time)
            val mCurrentPath = (CAPTURE_DIRECTORY.path + File.separator + "IMG_" + timeStamp + ".png")

            val fileOutputStream = FileOutputStream(File(mCurrentPath))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            db.captureDao().saveCapture(Capture(0, url, mCurrentPath, date.time))
            it.onComplete()
        }
    }

    override fun getAllCaptures(): Single<List<Capture>> {
        return db.captureDao().getAllCaptures()
    }

    override fun getCapture(id: Int): Single<Capture> {
        return db.captureDao().getCapture(id)
    }

    override fun deleteCapture(capture: Capture): Completable {
        return Completable.create {
            File(capture.fileName).delete()
            db.captureDao().delete(capture)
            it.onComplete()
        }
    }

    companion object {
        private val FILE_DATE_FORMAT = SimpleDateFormat("yyyyMMdd_HHmmss")
    }
}
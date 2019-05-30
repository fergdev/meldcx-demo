package nz.co.meldcxtest.ui.history

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import nz.co.meldcxtest.R
import nz.co.meldcxtest.databinding.CaptureItemBinding
import nz.co.meldcxtest.model.Capture
import java.util.*

/**
 * View holder to display a given capture.
 */
class CaptureViewHolder(captureBinding: CaptureItemBinding) : RecyclerView.ViewHolder(captureBinding.root) {

    val urlField = ObservableField<String>("")
    val captureFile = ObservableField<String>("")
    val date = ObservableField<String>("")
    val deleteVisible = ObservableBoolean(false)

    private var capture: Capture? = null
    private var captureDeletedListener: CaptureAdapter.CaptureListener? = null

    init {
        captureBinding.viewModel = this
    }

    /**
     * Bind the given capture and event listener to this view holder.
     */
    fun bind(
        capture: Capture, captureDeletedListener: CaptureAdapter.CaptureListener?
    ) {
        this.capture = capture
        this.captureDeletedListener = captureDeletedListener

        urlField.set(capture.url)
        captureFile.set(capture.fileName)
        date.set(Date(capture.date).toString())
        deleteVisible.set(true)
    }

    /**
     * Show the empty message view
     */
    fun showEmpty() {
        urlField.set(itemView.context.getString(R.string.empty_message))
        captureFile.set("")
        date.set("")
        deleteVisible.set(false)
        deleteVisible.set(false)
    }

    /**
     * Invoked from view when the delete button is pressed.
     */
    fun onDelete() {
        val capture = this.capture ?: return
        val captureDeletedListener = this.captureDeletedListener ?: return
        captureDeletedListener.onCaptureDeleted(capture)
    }

    /**
     * Invoked from view when the thumbnail is selected.
     */
    fun onThumbnail() {
        val capture = this.capture ?: return
        val captureListener = this.captureDeletedListener ?: return
        captureListener.onCaptureSelected(capture)
    }
}
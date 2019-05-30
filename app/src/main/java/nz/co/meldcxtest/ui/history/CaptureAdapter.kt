package nz.co.meldcxtest.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nz.co.meldcxtest.databinding.CaptureItemBinding
import nz.co.meldcxtest.model.Capture
import nz.co.meldcxtest.ui.util.BindableAdapter

/**
 * Adapter to display captures in recycler view. Has mechanism for
 * delete and select callbacks. Will show empty message when there
 * are no captures to display.
 */
class CaptureAdapter : RecyclerView.Adapter<CaptureViewHolder>(),
    BindableAdapter<List<Capture>> {
    private var captures = mutableListOf<Capture>()

    private var locationSelectedListener: CaptureListener? = null

    fun setCaptureDeletedListener(listener: CaptureListener) {
        this.locationSelectedListener = listener
    }

    override fun setData(data: List<Capture>) {
        this.captures.clear()
        this.captures.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaptureViewHolder {
        return CaptureViewHolder(
            CaptureItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (captures.isEmpty()) 0
        else 1
    }

    override fun getItemCount(): Int {
        return if (captures.isEmpty()) 1 else captures.size
    }

    override fun onBindViewHolder(holder: CaptureViewHolder, position: Int) {
        if (position == 0 && captures.isEmpty()) holder.showEmpty()
        else holder.bind(captures[position], locationSelectedListener)
    }

    interface CaptureListener {
        fun onCaptureDeleted(capture: Capture)
        fun onCaptureSelected(capture: Capture)
    }
}
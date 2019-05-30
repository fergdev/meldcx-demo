package nz.co.meldcxtest.ui.util

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import nz.co.meldcxtest.model.Capture
import nz.co.meldcxtest.ui.history.CaptureAdapter
import java.io.File

interface BindableAdapter<T> {
    fun setData(data: T)
}


@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}

@BindingAdapter("data")
fun setRecyclerViewData(recyclerView: RecyclerView, items: ObservableList<Capture>) {
    if (recyclerView.adapter is BindableAdapter<*>) {
        (recyclerView.adapter as CaptureAdapter).setData(items)
    }
}

@BindingAdapter("imageFile")
fun loadImageFile(view: ImageView, fileName: String) {
    if (fileName.isBlank()) {
        view.visibility = View.GONE
        return
    }
    view.visibility = View.VISIBLE
    Glide.with(view.context)
        .load(Uri.fromFile(File(fileName))).apply(RequestOptions())
        .into(view)
}



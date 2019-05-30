package nz.co.meldcxtest.ui.history

import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import nz.co.meldcxtest.model.Capture
import nz.co.meldcxtest.service.ICaptureService
import nz.co.meldcxtest.service.scheduler.ISchedulerProvider
import nz.co.weather.ui.base.BaseViewModel

/**
 * View model of for the history view.
 */
class HistoryViewModel(
    historyView: HistoryView,
    private val captureService: ICaptureService,
    private val schedulerProvider: ISchedulerProvider
) :
    BaseViewModel<HistoryView>(historyView) {

    private val data = mutableListOf<Capture>()
    val captures = ObservableArrayList<Capture>()
    val searchString = ObservableField<String>("")

    override fun onResume() {
        super.onResume()
        bindToLifeCycle(
            captureService.getAllCaptures()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    data.addAll(it)
                    filter(data, searchString.get())
                }, {
                    view.showError(it.localizedMessage)
                })
        )

        searchString.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                filter(data, searchString.get())
            }
        })
    }

    private fun filter(data: List<Capture>, query: String?) {
        captures.clear()
        if (query.isNullOrBlank()) captures.addAll(data)
        else captures.addAll(data.filter { it.url.contains(query) })
    }

    /**
     * Invoked from view when a capture is selected
     * to be deleted.
     */
    fun onCaptureDeleted(capture: Capture) {
        bindToLifeCycle(
            captureService.deleteCapture(capture)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    captures.remove(capture)
                }, {
                    view.showError(it.localizedMessage)
                })
        )
    }

    /**
     * Invoked from view when a capture thumbnail is pressed.
     */
    fun onCaptureSelected(capture: Capture) {
        view.setSelectedCapture(capture)
    }
}
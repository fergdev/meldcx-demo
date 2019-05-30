package nz.co.meldcxtest.ui.main

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import nz.co.meldcxtest.service.ICaptureService
import nz.co.meldcxtest.service.scheduler.ISchedulerProvider
import nz.co.weather.ui.base.BaseViewModel
import java.util.*

/**
 * View model for the main view.
 */
class MainViewModel(
    view: MainView,
    private val captureService: ICaptureService,
    private val schedulerProvider: ISchedulerProvider
) : BaseViewModel<MainView>(view) {

    val urlEditTextValue = ObservableField<String>("")
    val showLoading = ObservableBoolean(false)
    val cachedWebView = ObservableField<String>("")
    val cachedShowing = ObservableBoolean(false)

    override fun onResume() {
        super.onResume()
        urlEditTextValue.set(DEFAULT_URL)
        navigateToUrl(DEFAULT_URL)
    }

    /**
     * Invoked with go button.
     */
    fun onGo() {
        val url = urlEditTextValue.get() ?: return
        navigateToUrl(url)
    }

    /**
     * Invoked from capture button
     */
    fun onCapture() {
        val url = urlEditTextValue.get() ?: return
        bindToLifeCycle(
            captureService.saveCapture(url, view.captureWebView(), Date())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    view.showCaptureSuccess()
                }, {
                    onPageFinishedLoading()
                    view.showError(it.localizedMessage)
                })
        )
    }

    /**
     * Invoked from history button.
     */
    fun onHistory() {
        view.showHistory()
    }

    /**
     * Invoked when the webview is finished loading.
     */
    fun onPageFinishedLoading() {
        cachedShowing.set(false)
        showLoading.set(false)
    }

    private fun navigateToUrl(url: String) {
        if (url.isNotBlank()) {
            showLoading.set(true)
            view.navigateToUrl(url)
        }
    }

    /**
     * Invoked from the history view when a
     * capture has been selected.
     */
    fun onCaptureSelected(id: Int) {
        bindToLifeCycle(
            captureService.getCapture(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    cachedShowing.set(true)
                    cachedWebView.set(it.fileName)
                    urlEditTextValue.set(it.url)
                    navigateToUrl(it.url)
                }, {
                    onPageFinishedLoading()
                    view.showError(it.localizedMessage)
                })
        )
    }

    companion object {
        private const val DEFAULT_URL = "https://www.google.com"
    }
}
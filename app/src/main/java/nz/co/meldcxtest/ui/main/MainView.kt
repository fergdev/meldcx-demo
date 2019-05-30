package nz.co.meldcxtest.ui.main

import android.graphics.Bitmap
import nz.co.meldcxtest.ui.base.BaseView

/**
 * Interface for main view.
 */
interface MainView : BaseView {
    /**
     * Show the history view.
     */
    fun showHistory()

    /**
     * Navigate the webview to the given url.
     */
    fun navigateToUrl(urlRaw: String)

    /**
     * Capture bitmap from the webview.
     */
    fun captureWebView(): Bitmap

    /**
     * Show message to user that capture was a success.
     */
    fun showCaptureSuccess()
}

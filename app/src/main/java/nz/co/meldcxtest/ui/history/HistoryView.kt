package nz.co.meldcxtest.ui.history

import nz.co.meldcxtest.model.Capture
import nz.co.meldcxtest.ui.base.BaseView

/**
 * View interface for the capture history view.
 */
interface HistoryView : BaseView {

    /**
     * Sets the result of the capture and returns main view.
     */
    fun setSelectedCapture(capture: Capture)
}
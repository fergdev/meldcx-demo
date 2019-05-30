package nz.co.meldcxtest.ui.base

/**
 * Base view for view model pattern.
 */
interface BaseView {

    /**
     * Show an error dialog with the given message.
     *
     * @param message The message to show.
     */
    fun showError(message: String)

}


package nz.co.weather.ui.base

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import nz.co.meldcxtest.ui.base.BaseView

/**
 * BaseView logic class. Provides subclasses with lifecycle events
 * and rx binding to lifecycle with bindToLifecycle.
 *
 * @param view The view to invoke actions on.
 */
abstract class BaseViewModel<V : BaseView>(protected val view: V) {

    private val lifecycleManager = CompositeDisposable()

    open fun onCreate(state: Bundle?) {}

    open fun onStart() {}

    open fun onResume() {}

    open fun onPause(state: Bundle) {
        lifecycleManager.clear()
    }

    open fun onStop() {}

    fun bindToLifeCycle(disposable: Disposable) {
        lifecycleManager.add(disposable)
    }
}
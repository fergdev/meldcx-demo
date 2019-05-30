package nz.co.meldcxtest.ui.base

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import nz.co.meldcxtest.R
import nz.co.weather.ui.base.BaseViewModel

/**
 * Base activity to extend from. Requires sub classes to implement view model
 * pattern. Passes relevant events through to viewmodel's and manages lifecycle.
 */
abstract class BaseActivity<VM : BaseViewModel<*>> : AppCompatActivity() {

    protected lateinit var viewModel: VM

    abstract fun initViewLogic(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel = initViewLogic()
        viewModel.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onPause(outState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }.create()
            .show()
    }
}
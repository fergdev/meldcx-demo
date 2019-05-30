package nz.co.meldcxtest.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import nz.co.meldcxtest.databinding.ActivityHistoryBinding
import nz.co.meldcxtest.model.Capture
import nz.co.meldcxtest.service.CaptureService
import nz.co.meldcxtest.service.scheduler.SchedulerProvider
import nz.co.meldcxtest.ui.base.BaseActivity

/**
 * View to display the history of the captures.
 */
class HistoryActivity : BaseActivity<HistoryViewModel>(), HistoryView {
    override fun setSelectedCapture(capture: Capture) {
        val intent = Intent()
        intent.putExtra(KEY_CAPTURE_ID, capture.uid)
        setResult(1, intent)
        finish()
    }

    override fun initViewLogic(): HistoryViewModel {
        return HistoryViewModel(this, CaptureService(this), SchedulerProvider())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityHistoryBinding = ActivityHistoryBinding.inflate(LayoutInflater.from(this))
        setContentView(activityHistoryBinding.root)

        activityHistoryBinding.viewModel = viewModel
        val captureAdapter = CaptureAdapter()
        captureAdapter.setCaptureDeletedListener(object : CaptureAdapter.CaptureListener {
            override fun onCaptureSelected(capture: Capture) {
                viewModel.onCaptureSelected(capture)
            }

            override fun onCaptureDeleted(capture: Capture) {
                viewModel.onCaptureDeleted(capture)
            }
        })
        activityHistoryBinding.recyclerView.adapter = captureAdapter
    }

    companion object {
        const val KEY_CAPTURE_ID = "CAPTURE_ID"
    }
}



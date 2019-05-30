package nz.co.meldcxtest.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import nz.co.meldcxtest.R
import nz.co.meldcxtest.databinding.ActivityMainBinding
import nz.co.meldcxtest.service.CaptureService
import nz.co.meldcxtest.service.scheduler.SchedulerProvider
import nz.co.meldcxtest.ui.base.BaseActivity
import nz.co.meldcxtest.ui.history.HistoryActivity


/**
 * Main activity for app, responsible for displaying webview
 * and capturing 'captures'.
 */
class MainActivity : BaseActivity<MainViewModel>(), MainView {

    override fun showCaptureSuccess() {
        Toast.makeText(this, getString(R.string.capture_success), Toast.LENGTH_SHORT)
            .show()
    }

    private lateinit var binding: ActivityMainBinding

    override fun captureWebView(): Bitmap {
        val bitmap = Bitmap.createBitmap(binding.webview.width, binding.webview.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        webview.draw(canvas)
        return bitmap
    }

    override fun navigateToUrl(urlRaw: String) {
        binding.webview.loadUrl(urlRaw)
    }

    override fun showHistory() {
        startActivityForResult(Intent(this, HistoryActivity::class.java), CODE_HISTORY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_HISTORY && resultCode == 1 && data != null) {
            viewModel.onCaptureSelected(data.getIntExtra(HistoryActivity.KEY_CAPTURE_ID, 0))
        }
    }

    override fun initViewLogic(): MainViewModel {
        return MainViewModel(this, CaptureService(this), SchedulerProvider())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        binding.viewModel = viewModel

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.loadsImagesAutomatically = true
        binding.webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewModel.onPageFinishedLoading()
            }
        }

        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        binding.webview.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webview.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webview.destroy()
    }

    companion object {
        const val CODE_HISTORY = 123
    }
}

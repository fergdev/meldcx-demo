package nz.co.meldcxtest.ui.main

import android.graphics.Bitmap
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Single
import nz.co.meldcxtest.BaseUnitTest
import nz.co.meldcxtest.model.Capture
import nz.co.meldcxtest.service.ICaptureService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`

/**
 * Test for the main view model.
 */
class MainViewModelTest : BaseUnitTest() {

    @Mock
    private lateinit var view: MainView
    @Mock
    private lateinit var captureService: ICaptureService

    private lateinit var viewModel: MainViewModel

    override fun setup() {
        super.setup()

        viewModel = MainViewModel(view, captureService, schedulerProvider)
    }

    @Test
    fun `on resume - sets default url`() {
        //given
        //when
        viewModel.onResume()

        // then
        assertThat(viewModel.urlEditTextValue.get(), `is`("https://www.google.com"))
    }

    @Test
    fun `on resume - does navigation`() {
        //given
        //when
        viewModel.onResume()

        // then
        verify(view).navigateToUrl("https://www.google.com")
    }

    @Test
    fun `on resume - shows loading`() {
        //given
        //when
        viewModel.onResume()

        // then
        assertThat(viewModel.showLoading.get(), `is`(true))
    }

    @Test
    fun `on go - does navigation`() {
        //given
        viewModel.urlEditTextValue.set("hello.com")
        //when
        viewModel.onGo()

        // then
        assertThat(viewModel.showLoading.get(), `is`(true))
        verify(view).navigateToUrl("hello.com")
    }

    @Test
    fun `on go - empty does not do navigation`() {
        //given
        viewModel.urlEditTextValue.set("")
        reset(view)
        //when
        viewModel.onGo()

        // then
        assertThat(viewModel.showLoading.get(), `is`(false))
        verify(view, never()).navigateToUrl(anyString())
    }

    @Test
    fun `on capture - gets bitmap`() {
        //given

        val mockBitmap: Bitmap = mock()
        `when`(view.captureWebView()).thenReturn(mockBitmap)

        `when`(captureService.saveCapture(anyString(), any(), any()))
            .thenReturn(Completable.create { it.onComplete() })

        //when
        viewModel.onCapture()

        // then
        verify(view).captureWebView()
    }

    @Test
    fun `on capture - shows success`() {
        //given

        val mockBitmap: Bitmap = mock()
        `when`(view.captureWebView()).thenReturn(mockBitmap)

        `when`(captureService.saveCapture(anyString(), any(), any()))
            .thenReturn(Completable.create { it.onComplete() })

        //when
        viewModel.onCapture()
        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        // then
        verify(view).showCaptureSuccess()
    }

    @Test
    fun `on capture - shows error`() {
        //given

        val mockBitmap: Bitmap = mock()
        `when`(view.captureWebView()).thenReturn(mockBitmap)

        `when`(captureService.saveCapture(anyString(), any(), any()))
            .thenReturn(
                Completable.error(Throwable("error"))
            )

        //when
        viewModel.onCapture()
        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        // then
        verify(view).showError("error")
    }

    @Test
    fun `on history - does navigation`() {
        //given
        //when
        viewModel.onHistory()

        //then
        verify(view).showHistory()
    }

    @Test
    fun `on page finished load - hides loading`() {
        //given
        viewModel.onResume()
        //when
        viewModel.onPageFinishedLoading()

        //then
        assertThat(viewModel.showLoading.get(), `is`(false))
    }

    @Test
    fun `on capture selected - success updates view`() {
        // given
        `when`(captureService.getCapture(0)).thenReturn(Single.just(Capture(0, "url", "filename", 0L)))
        viewModel.onResume()

        //when
        viewModel.onCaptureSelected(0)

        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //then
        assertThat(viewModel.cachedShowing.get(), `is`(true))
        assertThat(viewModel.cachedWebView.get(), `is`("filename"))
        assertThat(viewModel.urlEditTextValue.get(), `is`("url"))
        verify(view).navigateToUrl("url")
    }

    @Test
    fun `on capture selected - error shows error`() {
        // given
        `when`(captureService.getCapture(0)).thenReturn(Single.error(Throwable("error")))
        viewModel.onResume()

        //when
        viewModel.onCaptureSelected(0)

        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //then
        verify(view).showError("error")
    }
}
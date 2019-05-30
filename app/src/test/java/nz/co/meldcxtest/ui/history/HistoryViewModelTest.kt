package nz.co.meldcxtest.ui.history

import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Completable
import io.reactivex.Single
import nz.co.meldcxtest.BaseUnitTest
import nz.co.meldcxtest.model.Capture
import nz.co.meldcxtest.service.ICaptureService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

/**
 * Test for the history view model.
 */
class HistoryViewModelTest : BaseUnitTest() {

    @Mock
    private lateinit var view: HistoryView
    @Mock
    private lateinit var captureService: ICaptureService

    private lateinit var viewModel: HistoryViewModel

    override fun setup() {
        super.setup()

        viewModel = HistoryViewModel(view, captureService, schedulerProvider)
    }

    @Test
    fun `on resume - requests captures`() {
        //given
        `when`(captureService.getAllCaptures())
            .thenReturn(Single.just(listOf(Capture(0, "url", "fileName", 0L))))
        //when
        viewModel.onResume()

        //then
        verify(captureService).getAllCaptures()
    }

    @Test
    fun `on data - displays data`() {
        //given
        `when`(captureService.getAllCaptures())
            .thenReturn(Single.just(listOf(Capture(0, "url", "fileName", 0L))))
        //when
        viewModel.onResume()
        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //then
        assertThat(viewModel.captures.size, `is`(1))
    }

    @Test
    fun `on error - displays error`() {
        //given
        `when`(captureService.getAllCaptures())
            .thenReturn(Single.error(Throwable("error")))
        //when
        viewModel.onResume()
        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //then
        verify(view).showError("error")
    }


    @Test
    fun `on search query - filters items`() {
        //given
        `when`(captureService.getAllCaptures())
            .thenReturn(Single.just(listOf(Capture(0, "not", "filename", 0L), Capture(0, "url", "fileName", 0L))))
        viewModel.onResume()

        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //when
        viewModel.searchString.set("url")

        //then
        assertThat(viewModel.captures.size, `is`(1))
    }

    @Test
    fun `on capture deleted - invokes delete`() {
        //given
        val toDelete = Capture(0, "not", "filename", 0L)
        `when`(captureService.getAllCaptures())
            .thenReturn(Single.just(listOf(toDelete, Capture(0, "url", "fileName", 0L))))
        `when`(captureService.deleteCapture(toDelete))
            .thenReturn(Completable.complete())

        viewModel.onResume()
        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //when
        viewModel.onCaptureDeleted(toDelete)

        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //then
        verify(captureService).deleteCapture(toDelete)
        assertThat(viewModel.captures.size, `is`(1))
    }

    @Test
    fun `on capture deleted - shows error`() {
        //given
        val toDelete = Capture(0, "not", "filename", 0L)
        `when`(captureService.getAllCaptures())
            .thenReturn(Single.just(listOf(toDelete, Capture(0, "url", "fileName", 0L))))
        `when`(captureService.deleteCapture(toDelete))
            .thenReturn(Completable.error(Throwable("error")))

        viewModel.onResume()
        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //when
        viewModel.onCaptureDeleted(toDelete)

        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //then
        verify(captureService).deleteCapture(toDelete)
        verify(view).showError("error")
        assertThat(viewModel.captures.size, `is`(2))
    }

    @Test
    fun `on capture selected - sets result`(){
        //given
        val toSelect = Capture(0, "not", "filename", 0L)
        `when`(captureService.getAllCaptures())
            .thenReturn(Single.just(listOf(toSelect, Capture(0, "url", "fileName", 0L))))

        viewModel.onResume()
        ioTestScheduler.triggerActions()
        uiTestScheduler.triggerActions()

        //when
        viewModel.onCaptureSelected(toSelect)

        //then
        verify(view).setSelectedCapture(toSelect)
    }
}
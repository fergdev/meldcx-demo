package nz.co.meldcxtest

import io.reactivex.schedulers.TestScheduler
import nz.co.meldcxtest.service.scheduler.ISchedulerProvider
import nz.co.meldcxtest.service.scheduler.TestSchedulerProvider
import org.junit.After
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Base unit test to handle mockito and RXJava setup/teardown.
 */
abstract class BaseUnitTest {

    protected lateinit var ioTestScheduler: TestScheduler
    protected lateinit var schedulerProvider: ISchedulerProvider
    protected lateinit var uiTestScheduler: TestScheduler
    protected lateinit var computationTestScheduler: TestScheduler
    @Before
    open fun setup() {
        MockitoAnnotations.initMocks(this)

        ioTestScheduler = TestScheduler()
        uiTestScheduler = TestScheduler()
        computationTestScheduler = TestScheduler()

        schedulerProvider = TestSchedulerProvider(
            computationTestScheduler = computationTestScheduler,
            ioTestScheduler = ioTestScheduler,
            uiTestScheduler = uiTestScheduler
        )
    }

    @After
    open fun tearDown() {
        Mockito.validateMockitoUsage()
    }
}
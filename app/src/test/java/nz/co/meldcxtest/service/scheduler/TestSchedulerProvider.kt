package nz.co.meldcxtest.service.scheduler

import io.reactivex.schedulers.TestScheduler

/**
 * Test variant scheduler provider.
 */
class TestSchedulerProvider(
    private val computationTestScheduler: TestScheduler = TestScheduler(),
    private val uiTestScheduler: TestScheduler = TestScheduler(),
    private val ioTestScheduler: TestScheduler = TestScheduler()
) : ISchedulerProvider {
    override fun computation() = computationTestScheduler
    override fun ui() = uiTestScheduler
    override fun io() = ioTestScheduler
}
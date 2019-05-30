package nz.co.meldcxtest.service.scheduler

import io.reactivex.Scheduler

/**
 * Interface for scheduler provider to abstract out
 * the different schedulers for testing.
 *
 * @see SchedulerProvider
 */
interface ISchedulerProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
}


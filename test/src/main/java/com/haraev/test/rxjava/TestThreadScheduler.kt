package com.haraev.test.rxjava

import com.haraev.core.common.ThreadScheduler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

open class TestThreadScheduler(
    private val scheduler : Scheduler = Schedulers.trampoline()
) : ThreadScheduler() {

    override fun ui(): Scheduler = scheduler

    override fun io(): Scheduler = scheduler
}

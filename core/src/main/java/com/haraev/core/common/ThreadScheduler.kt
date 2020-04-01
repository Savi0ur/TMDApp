package com.haraev.core.common

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class ThreadScheduler {

    protected open fun ui(): Scheduler = AndroidSchedulers.mainThread()

    protected open fun io(): Scheduler = Schedulers.io()

    fun <T> ioToUiSingle() = { upstream: Single<T> -> upstream.subscribeOn(io()).observeOn(ui()) }

    fun <T> ioToUiObservable() = { upstream: Observable<T> -> upstream.subscribeOn(io()).observeOn(ui()) }

    fun ioToUiCompletable() =
        { upstream: Completable -> upstream.subscribeOn(io()).observeOn(ui()) }
}

fun <T> Observable<T>.scheduleIoToUi(scheduler: ThreadScheduler): Observable<T> {
    return compose(scheduler.ioToUiObservable())
}

fun <T> Single<T>.scheduleIoToUi(scheduler: ThreadScheduler): Single<T> {
    return compose(scheduler.ioToUiSingle())
}

fun Completable.scheduleIoToUi(scheduler: ThreadScheduler): Completable {
    return compose(scheduler.ioToUiCompletable())
}
package com.haraev.core.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.haraev.core.aac.EventsQueue
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    val eventsQueue = EventsQueue()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    protected fun Disposable.autoDispose(): Disposable {
        compositeDisposable.add(this)
        return this
    }

    protected fun showMessage(@StringRes messageResId: Int) {
        val showMessageEvent = ShowMessage(messageResId)
        eventsQueue.offer(showMessageEvent)
    }

    protected fun showErrorMessage(@StringRes messageResId: Int) {
        val showErrorMessageEvent = ShowErrorMessage(messageResId)
        eventsQueue.offer(showErrorMessageEvent)
    }
}
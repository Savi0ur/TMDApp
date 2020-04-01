package com.haraev.core.aac

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe

fun <T> MutableLiveData<T>.onNext(next: T) {
    this.value = next
}

fun <T : Any> LiveData<T>.requireValue(): T = checkNotNull(value)

/**
 * Подписка на [LiveData].
 *
 * Пример подписки на изменения состояния:
 * ```
 *  lateinit var viewModel: MyViewModel
 *
 *  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *      super.onViewCreated(view, savedInstanceState)
 *      observe(viewModel.state, ::renderState)
 *  }
 * ```
 */
inline fun <reified T, LD : LiveData<T>> Fragment.observe(liveData: LD, crossinline block: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) { block(it) }
}

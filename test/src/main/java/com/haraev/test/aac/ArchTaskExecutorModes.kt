package com.haraev.test.aac

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor

@SuppressLint("RestrictedApi")
fun enableTestMode() {
    ArchTaskExecutor.getInstance()
        .setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
}

@SuppressLint("RestrictedApi")
fun disableTestMode() {
    ArchTaskExecutor.getInstance().setDelegate(null)
}
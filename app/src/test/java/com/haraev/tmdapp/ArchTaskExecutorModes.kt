package com.haraev.tmdapp

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor

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

fun disableTestMode() {
    ArchTaskExecutor.getInstance().setDelegate(null)
}
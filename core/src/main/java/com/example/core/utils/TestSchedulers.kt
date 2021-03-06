package com.example.core.utils

import com.example.core.di.schedulers.AppSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

object TestSchedulers {
    fun get(): AppSchedulers {
        return AppSchedulers(
            Schedulers.trampoline(),
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }
}
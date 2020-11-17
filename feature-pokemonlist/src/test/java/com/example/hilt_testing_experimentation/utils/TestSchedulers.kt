package com.example.hilt_testing_experimentation.utils

import com.example.feature_pokemonlist.di.schedulers.AppSchedulers
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
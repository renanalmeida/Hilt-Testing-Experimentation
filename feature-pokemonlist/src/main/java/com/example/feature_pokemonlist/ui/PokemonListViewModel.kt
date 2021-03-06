package com.example.feature_pokemonlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.DetailedPokemon
import com.example.core.getOffsetFromUrl
import com.example.core.di.schedulers.AppSchedulers
import com.example.feature_pokemonlist.domain.PokeRepository
import com.example.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokeRepository: PokeRepository,
    private val schedulers: AppSchedulers
) : ViewModel() {
    private val pokemonList = mutableListOf<DetailedPokemon>()

    private val _pokemon: MutableLiveData<Resource<List<DetailedPokemon>>> = MutableLiveData(Resource.loading())
    val pokemon: LiveData<Resource<List<DetailedPokemon>>>
        get() = _pokemon

    private val _nextPageOffset: MutableLiveData<Int?> = MutableLiveData(0)
    val nextPageOffset: LiveData<Int?>
        get() = _nextPageOffset

    private var disposable = CompositeDisposable()

    init {
        _pokemon.value = Resource.loading()
        loadPokemon()
    }

    fun loadPokemon(offset: Int = 0) {
        pokeRepository.getPokemonList(offset)
            .doOnSuccess { _nextPageOffset.postValue(it.next?.getOffsetFromUrl()) }
            .flattenAsObservable { it.pokemon }
            .flatMap { pokeRepository.getDetailedPokemon(it).toObservable() }
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.mainThread)
            .subscribe({ pokemon ->
                pokemonList.add(pokemon)
            },::setError,{
                _pokemon.value = Resource.success(pokemonList)
            })
            .addTo(disposable)
    }

    private fun setError(throwable: Throwable) {
        _pokemon.value = Resource.error(throwable.message ?: "")
    }

    fun loadMorePokemon(offset: Int) {
        loadPokemon(offset)
    }
}
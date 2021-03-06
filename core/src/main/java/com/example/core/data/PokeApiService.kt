package com.example.core.data

import com.example.core.data.model.detailedpokemondto.DetailedPokemonDto
import com.example.core.data.model.pokemonlistdto.PokemonListDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon?limit=20")
    fun getPokemon(@Query("offset") offset: Int): Single<PokemonListDto>

    @GET("pokemon/{name}")
    fun getDetailedPokemon(@Path("name") id: String): Single<DetailedPokemonDto>
}
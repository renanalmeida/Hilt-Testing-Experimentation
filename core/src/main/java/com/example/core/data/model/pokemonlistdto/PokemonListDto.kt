package com.example.core.data.model.pokemonlistdto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PokemonListDto {
    @SerializedName("count")
    @Expose
    var count: Long? = null

    @SerializedName("next")
    @Expose
    var next: String? = null

    @SerializedName("previous")
    @Expose
    var previous: String? = null

    @SerializedName("results")
    @Expose
    var results: List<PokemonDto> = emptyList()
}


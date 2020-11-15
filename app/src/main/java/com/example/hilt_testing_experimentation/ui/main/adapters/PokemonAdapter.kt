package com.example.hilt_testing_experimentation.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_pokemondetail.PokemonDetailActivity
import com.example.hilt_testing_experimentation.R
import com.example.hilt_testing_experimentation.databinding.ItemPokemonBinding
import com.example.hilt_testing_experimentation.di.analytics.Analytics
import com.example.hilt_testing_experimentation.di.imageloader.ImageLoader
import com.example.core.domain.detailedpokemon.DetailedPokemon
import javax.inject.Inject

class PokemonAdapter @Inject constructor(
    private val analytics: Analytics,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private val pokemon: MutableList<DetailedPokemon> = mutableListOf()

    fun updateItems(list: List<DetailedPokemon>) {
        pokemon.clear()
        pokemon.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return pokemon.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemon[position])
    }

    inner class ViewHolder(private val binding: ItemPokemonBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: DetailedPokemon) {
            binding.name.text = pokemon.name
            pokemon.sprites?.frontDefault?.let { imageUrl ->
                imageLoader.loadImage(
                    context = binding.root.context,
                    loadable = imageUrl,
                    placeholder = R.drawable.placeholder,
                    imageView = binding.image,
                    onResourceReady = { pokemon.name?.let { analytics.logImageView(it) } }
                )
            }
            binding.root.setOnClickListener {
                PokemonDetailActivity.start(pokemon)
            }
        }
    }
}
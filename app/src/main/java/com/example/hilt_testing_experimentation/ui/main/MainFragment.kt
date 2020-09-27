package com.example.hilt_testing_experimentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hilt_testing_experimentation.R
import com.example.hilt_testing_experimentation.databinding.MainFragmentBinding
import com.example.hilt_testing_experimentation.di.analytics.Analytics
import com.example.hilt_testing_experimentation.ui.main.adapters.LoadingAdapter
import com.example.hilt_testing_experimentation.ui.main.adapters.PokemonAdapter
import com.example.hilt_testing_experimentation.utils.Status
import com.example.hilt_testing_experimentation.utils.viewBinding
import com.example.hilt_testing_experimentation.utils.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by viewModels()

    private val binding by viewBinding(MainFragmentBinding::bind)

    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var loadingAdapter: LoadingAdapter
    private lateinit var concatAdapter: ConcatAdapter

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        analytics.logScreenView("Main fragment")

        viewModel.nextPageOffset.observe(viewLifecycleOwner) { nextPage ->
            loadingAdapter.nextPageOffset = nextPage
            if (nextPage == null) {
                loadingAdapter.notifyItemRemoved(0)
            }
        }

        viewModel.pokemon.observe(viewLifecycleOwner) { response ->
            binding.recyclerMain visibleIf response.isSuccess()
            binding.text visibleIf !response.isSuccess()
            binding.text.setOnClickListener {}

            when (response.status) {
                Status.SUCCESS -> {
                    pokemonAdapter.updateItems(response.data ?: emptyList())
                }
                Status.ERROR -> {
                    binding.text.text = "An error occurred, try again later"
                    binding.text.setOnClickListener { viewModel.loadPokemon() }
                }
                Status.LOADING -> {
                    binding.text.text = "Loading..."
                }
            }
        }
    }

    private fun setupRecycler() {
        pokemonAdapter = PokemonAdapter()
        loadingAdapter = LoadingAdapter(object: LoadingAdapter.VisibilityListener {
            override fun isVisible(nextPage: Int) {
                viewModel.loadMorePokemon(nextPage)
            }
        })
        concatAdapter = ConcatAdapter(pokemonAdapter, loadingAdapter)

        with(binding.recyclerMain) {
            adapter = concatAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItemViewType(position) == concatAdapter.adapters.size - 1) // is last item view type in the mergeadapter
                            2
                        else
                            1
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
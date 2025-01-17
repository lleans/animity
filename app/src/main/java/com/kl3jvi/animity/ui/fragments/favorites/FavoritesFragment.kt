package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    val viewModel: FavoritesViewModel by viewModels()
    private var binding: FragmentFavoritesBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        initViews()
        observeAniList()
    }

    private var shouldRefreshFavorites: Boolean = false

    private fun initViews() {
        binding?.swipeLayout?.setOnRefreshListener {
            shouldRefreshFavorites = !shouldRefreshFavorites
            observeAniList()
        }
    }

    private fun observeAniList() {
        collectFlow(viewModel.favoritesList) { favoritesUiState ->
            binding?.favoritesRecycler?.layoutManager = GridLayoutManager(requireContext(), 3)
            when (favoritesUiState) {
                is FavoritesUiState.Error -> {
                    showSnack(binding?.root, "Error getting favorites")
                    binding?.nothingSaved?.isVisible = true
                    binding?.swipeLayout?.isRefreshing = false
                }

                FavoritesUiState.Loading -> {
                    binding?.swipeLayout?.isRefreshing = true
                }

                is FavoritesUiState.Success -> {
                    binding?.swipeLayout?.isRefreshing = false
                    binding?.favoritesRecycler?.withModels { buildFavorites(favoritesUiState.data) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // letting go of the resources to avoid memory leak.
        binding = null
    }
}

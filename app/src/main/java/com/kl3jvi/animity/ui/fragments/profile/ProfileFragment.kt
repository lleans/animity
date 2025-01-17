package com.kl3jvi.animity.ui.fragments.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.ui.activities.login.LoginActivity
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.createFragmentMenu
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
    private var binding: FragmentProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        createFragmentMenu(menuLayout = R.menu.profile_menu) {
            when (it.itemId) {
                R.id.action_log_out -> {
                    viewModel.clearStorage() // Deletes saved token
                    requireActivity().launchActivity<LoginActivity> {
                        binding = null
                    }
                    requireActivity().finish()
                    true
                }
                else -> false
            }
        }
        getProfileData()
    }

    private fun getProfileData() {
        viewLifecycleOwner.collectFlow(viewModel.profileData) { userData ->
            binding?.profileRv?.withModels {
                when (userData) {
                    is ProfileDataUiState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "${userData.exception?.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    ProfileDataUiState.Loading -> {}

                    is ProfileDataUiState.Success -> {
                        buildProfile(userData = userData.data)
                    }
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

package ru.stan.nework.presentation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.databinding.FragmentUserBinding
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.presentation.addPost.UsersAdapter

@AndroidEntryPoint
class UserFragment :  Fragment() {

    private val viewModel: UserViewModel by viewModels()

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        initUsers()
        return binding.root
    }
    private fun initUsers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.users.collectLatest { users ->
                initAdapter(users)
            }
        }
    }

    private fun initAdapter(users: List<UserUI>) {
        val adapter = UserAdapter(users)
        binding.rvUsers.adapter = adapter
        binding.rvUsers.recycledViewPool.setMaxRecycledViews(
            UsersAdapter.VIEW_TYPE, UsersAdapter.MAX_POOL_SIZE
        )
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
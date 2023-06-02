package ru.stan.nework.presentation.users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.databinding.FragmentUserBinding
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.presentation.addPost.UsersAdapter
import ru.stan.nework.utils.BaseFragment

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding>() {

    override fun viewBindingInflate(): FragmentUserBinding =
        FragmentUserBinding.inflate(layoutInflater)

    private val viewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUsers()
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
}
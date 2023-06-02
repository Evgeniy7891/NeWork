package ru.stan.nework.presentation.addEvent.addSpeakers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentSpeakersBinding
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.presentation.addPost.CheckedListener
import ru.stan.nework.presentation.addPost.UsersAdapter
import ru.stan.nework.utils.BaseFragment
import ru.stan.nework.utils.SPEAKERS

@AndroidEntryPoint
class SpeakersFragment : BaseFragment<FragmentSpeakersBinding>() {
    override fun viewBindingInflate(): FragmentSpeakersBinding = FragmentSpeakersBinding.inflate(layoutInflater)

    private val viewModel: SpeakersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUsers()
        addUser()
    }

    private fun addUser() {
        binding.btnAddUser.setOnClickListener {
            viewModel.addUsers()
            viewModel.idUsers.observe(viewLifecycleOwner) { users ->
                val bundle = Bundle()
                bundle.putIntegerArrayList(SPEAKERS, users as ArrayList<Int>?)
                findNavController().navigate(R.id.action_speakersFragment_to_newEventFragment, bundle)
            }
        }
    }

    private fun initUsers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.users.collectLatest { users ->
                initAdapter(users)
            }
        }
    }

    private fun initAdapter(users: List<UserUI>) {
        val adapter = UsersAdapter(object : CheckedListener {
            override fun checked(id: Int) {
                viewModel.check(id)
            }
            override fun unChecked(id: Int) {
                viewModel.uncheck(id)
            }
        })
        binding.rvUsers.adapter = adapter
        binding.rvUsers.recycledViewPool.setMaxRecycledViews(
            UsersAdapter.VIEW_TYPE, UsersAdapter.MAX_POOL_SIZE
        )
        val newUser = adapter.itemCount < users.size
        adapter.submitList(users) {
            if (newUser) {
                binding.rvUsers.smoothScrollToPosition(0)
            }
        }
    }
}
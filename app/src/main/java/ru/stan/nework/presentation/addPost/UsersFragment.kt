package ru.stan.nework.presentation.addPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentUsersBinding
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.presentation.home.PostAdapter
import java.util.ArrayList

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels()

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(layoutInflater, container, false)
        initUsers()
        binding.btnAddUser.setOnClickListener {
            viewModel.addUsers()
            viewModel.idUsers.observe(viewLifecycleOwner) { users ->
                val bundle = Bundle()
                bundle.putIntegerArrayList("ID", users as ArrayList<Int>?)
                findNavController().navigate(R.id.action_usersFragment_to_postFragment, bundle)
            }
        }
        return binding.root
    }

    private fun initUsers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.users.collectLatest { users ->
                delay(500)
                initAdapter(users)
            }
        }
    }

        private fun initAdapter(users: List<UserUI>) {
            val adapter = UsersAdapter(users, object : CheckedListener {
                override fun checked(id: Int) {
                    viewModel.check(id)
                }

                override fun unChecked(id: Int) {
                    viewModel.uncheck(id)
                }
            })
            println("LIST - $users")
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
package ru.stan.nework.presentation.usersProfile.pager.posts

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.databinding.FragmentUserPostsBinding
import ru.stan.nework.presentation.account.pager.wall.WallAdapter
import ru.stan.nework.utils.BaseFragment

@AndroidEntryPoint
class UserPostsFragment(private val id: Long) : BaseFragment<FragmentUserPostsBinding>() {

    override fun viewBindingInflate(): FragmentUserPostsBinding = FragmentUserPostsBinding.inflate(layoutInflater)

    private lateinit var viewModel: UserPostsViewModel

    private lateinit var wallAdapter: WallAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[UserPostsViewModel::class.java]
        viewModel.getWall(id)
        initWall()
        initAdapter()

    }

    private fun initWall() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.posts.collectLatest { wall ->
                wallAdapter.submitList(wall)
            }
        }
    }

    private fun initAdapter() {
        wallAdapter = WallAdapter()
        binding.rvListPosts.adapter = wallAdapter
        val linearLayout = LinearLayoutManager(requireContext())
        linearLayout.reverseLayout = true
        binding.rvListPosts.layoutManager = linearLayout
        binding.rvListPosts.recycledViewPool.setMaxRecycledViews(
            WallAdapter.VIEW_TYPE, WallAdapter.MAX_POOL_SIZE
        )
    }
}
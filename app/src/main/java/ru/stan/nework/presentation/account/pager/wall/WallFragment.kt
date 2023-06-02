package ru.stan.nework.presentation.account.pager.wall

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.databinding.FragmentWallBinding
import ru.stan.nework.utils.BaseFragment

@AndroidEntryPoint
class WallFragment : BaseFragment<FragmentWallBinding>() {

    override fun viewBindingInflate(): FragmentWallBinding = FragmentWallBinding.inflate(layoutInflater)

    private val viewModel: WallViewModel by viewModels()

    private lateinit var wallAdapter: WallAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWall()
        initAdapter()
    }
    private fun initWall() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.wall.collectLatest { wall ->
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
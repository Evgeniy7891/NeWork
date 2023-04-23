package ru.stan.nework.presentation.account.pager.wall

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentHomeBinding
import ru.stan.nework.databinding.FragmentWallBinding
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.presentation.home.HomeViewModel
import ru.stan.nework.presentation.home.PostAdapter
@AndroidEntryPoint
class WallFragment : Fragment() {

    private lateinit var viewModel: WallViewModel
    private lateinit var wallAdapter: WallAdapter
    private var _binding: FragmentWallBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWallBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[WallViewModel::class.java]
        initWall()
        initAdapter()
        return binding.root
    }
    private fun initWall() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.wall.collectLatest { wall ->
                wallAdapter.submitList(wall)
            }
        }
    }

    private fun initAdapter() {
        wallAdapter = WallAdapter(object : OnListener{
            override fun getUsers(listId: List<Int>) {
            }
            override fun onRemove(post: Post) {
            }
            override fun onEdit(post: Post) {
            }
        })
        binding.rvListPosts.adapter = wallAdapter
        val linearLayout = LinearLayoutManager(requireContext())
        linearLayout.reverseLayout = true
        binding.rvListPosts.layoutManager = linearLayout
        binding.rvListPosts.recycledViewPool.setMaxRecycledViews(
            PostAdapter.VIEW_TYPE, PostAdapter.MAX_POOL_SIZE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
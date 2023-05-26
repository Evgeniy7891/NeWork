package ru.stan.nework.presentation.usersProfile.pager.posts

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
import ru.stan.nework.databinding.FragmentUserPostsBinding
import ru.stan.nework.databinding.FragmentWallBinding
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.presentation.account.pager.wall.OnListener
import ru.stan.nework.presentation.account.pager.wall.WallAdapter
import ru.stan.nework.presentation.account.pager.wall.WallViewModel

@AndroidEntryPoint
class UserPostsFragment(private val id: Long) : Fragment() {

    private lateinit var viewModel: UserPostsViewModel
    private lateinit var wallAdapter: WallAdapter
    private var _binding: FragmentUserPostsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserPostsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[UserPostsViewModel::class.java]
        viewModel.getWall(id)
        initWall()
        initAdapter()
        return binding.root
    }
    private fun initWall() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.posts.collectLatest { wall ->
                wallAdapter.submitList(wall)
            }
        }
    }

    private fun initAdapter() {
        wallAdapter = WallAdapter(object : OnListener {
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
            WallAdapter.VIEW_TYPE, WallAdapter.MAX_POOL_SIZE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
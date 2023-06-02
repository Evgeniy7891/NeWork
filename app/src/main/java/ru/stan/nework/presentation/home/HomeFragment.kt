package ru.stan.nework.presentation.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentHomeBinding
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.utils.BOTTONMENU
import ru.stan.nework.utils.BaseFragment
import ru.stan.nework.utils.POST
import ru.stan.nework.utils.USERS

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun viewBindingInflate(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewModel: HomeViewModel

    private lateinit var postAdapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        BOTTONMENU.isVisible = true

        initAdapter()
        initPosts()
        setupClickListener()
    }

    private fun initPosts() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest { posts ->
                postAdapter.submitData(posts)
            }
        }
    }

    private fun initAdapter() {
        postAdapter = PostAdapter(object : OnListener {
            override fun getUsers(listId: List<Int>) {
                val bundle = Bundle()
                bundle.putIntegerArrayList(USERS, listId as ArrayList<Int>?)
                findNavController().navigate(
                    R.id.action_homeFragment_to_usersBottomSheetFragment,
                    bundle
                )
            }

            override fun getUsersLikes(postId: Int) {
                var listId = arrayListOf<Int>()
                viewModel.getPost(postId)
                viewModel.post.observe(viewLifecycleOwner) {
                    if (it != null) {
                        listId = it as ArrayList<Int>
                    }
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    val bundle = Bundle()
                    bundle.putIntegerArrayList(USERS, listId)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_usersBottomSheetFragment,
                        bundle
                    )
                }, 500)
            }

            override fun onRemove(post: Post) {
                post.id.toLong().let { viewModel.deletePost(it) }
            }

            override fun onEdit(post: Post) {
                val bundle = Bundle()
                post.id.let { bundle.putInt(POST, it) }
                findNavController().navigate(R.id.action_homeFragment_to_postFragment, bundle)
            }
        })
        binding.rvListPosts.adapter = postAdapter
        binding.rvListPosts.recycledViewPool.setMaxRecycledViews(
            PostAdapter.VIEW_TYPE, PostAdapter.MAX_POOL_SIZE
        )

        binding.rvListPosts.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setupClickListener() {
        onLike = { post ->
            post.id.let {
                viewModel.likeById(it.toLong())
            }
        }
        disLike = { post ->
            post.id.let {
                viewModel.deleteLike(it.toLong())
            }
        }
    }
}
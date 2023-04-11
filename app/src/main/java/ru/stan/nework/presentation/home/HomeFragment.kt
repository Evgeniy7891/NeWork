package ru.stan.nework.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import retrofit2.http.POST
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentHomeBinding
import ru.stan.nework.domain.models.ui.post.Post
import java.util.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var postAdapter: PostAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initAdapter()
        initPosts()
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_postFragment)
        }

        binding.swipeRefresh.setOnClickListener {
           initPosts()
        }
        return binding.root
    }

    private fun initPosts() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.posts.collectLatest { posts ->
                delay(500)
                postAdapter.submitList(posts)
            }
        }
    }

    private fun initAdapter() {
        postAdapter = PostAdapter(object : OnListener{
            override fun getUsers(listId: List<Int>) {
                val bundle = Bundle()
                bundle.putIntegerArrayList("ID", listId as ArrayList<Int>?)
                findNavController().navigate(R.id.action_homeFragment_to_usersBottomSheetFragment, bundle)
            }
            override fun onRemove(post: Post) {
                viewModel.deletePost(post.id.toLong())
            }
            override fun onEdit(post: Post) {
                val bundle = Bundle()
                bundle.putInt("POST", post.id)
                findNavController().navigate(R.id.action_homeFragment_to_postFragment, bundle)
            }
        })
        binding.rvListPosts.adapter = postAdapter
        binding.rvListPosts.recycledViewPool.setMaxRecycledViews(
            PostAdapter.VIEW_TYPE, PostAdapter.MAX_POOL_SIZE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
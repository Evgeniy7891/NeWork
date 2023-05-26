package ru.stan.nework.presentation.usersProfile.pager.jobs

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
import ru.stan.nework.databinding.FragmentJobsBinding
import ru.stan.nework.databinding.FragmentUserJobsBinding
import ru.stan.nework.databinding.FragmentUserPostsBinding
import ru.stan.nework.presentation.account.pager.jobs.JobAdapter
import ru.stan.nework.presentation.account.pager.jobs.JobsViewModel
import ru.stan.nework.presentation.account.pager.wall.WallAdapter
import ru.stan.nework.presentation.usersProfile.pager.posts.UserPostsViewModel
@AndroidEntryPoint
class UserJobsFragment(private val id: Long) : Fragment() {

    private lateinit var viewModel: UserJobsViewModel
    private lateinit var jobAdapter: JobAdapter
    private var _binding: FragmentUserJobsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserJobsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[UserJobsViewModel::class.java]
        viewModel.getJobs(id)
        initWall()
        initAdapter()
        return binding.root
    }
    private fun initWall() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.jobs.collectLatest { jobs ->
                println("JOBS - $jobs")
                jobAdapter.submitList(jobs)
            }
        }
    }

    private fun initAdapter() {
        jobAdapter = JobAdapter()
        binding.rvJobs.adapter = jobAdapter
        val linearLayout = LinearLayoutManager(requireContext())
        linearLayout.reverseLayout = true
        binding.rvJobs.layoutManager = linearLayout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
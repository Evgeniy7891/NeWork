package ru.stan.nework.presentation.usersProfile.pager.jobs

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.databinding.FragmentUserJobsBinding
import ru.stan.nework.presentation.account.pager.jobs.JobAdapter
import ru.stan.nework.utils.BaseFragment

@AndroidEntryPoint
class UserJobsFragment(private val id: Long) : BaseFragment<FragmentUserJobsBinding>() {

    override fun viewBindingInflate(): FragmentUserJobsBinding = FragmentUserJobsBinding.inflate(layoutInflater)

    private lateinit var viewModel: UserJobsViewModel

    private lateinit var jobAdapter: JobAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[UserJobsViewModel::class.java]
        viewModel.getJobs(id)
        initWall()
        initAdapter()

    }

    private fun initWall() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.jobs.collectLatest { jobs ->
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
}
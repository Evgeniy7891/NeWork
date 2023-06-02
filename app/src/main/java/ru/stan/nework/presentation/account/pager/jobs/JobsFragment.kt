package ru.stan.nework.presentation.account.pager.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.databinding.FragmentJobsBinding
import ru.stan.nework.utils.BaseFragment

@AndroidEntryPoint
class JobsFragment : BaseFragment<FragmentJobsBinding>(){
    override fun viewBindingInflate(): FragmentJobsBinding = FragmentJobsBinding.inflate(layoutInflater)

    private val viewModel: JobsViewModel by viewModels()

    private lateinit var jobAdapter: JobAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
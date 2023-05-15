package ru.stan.nework.presentation.account.pager.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.databinding.FragmentJobsBinding

@AndroidEntryPoint
class JobsFragment : Fragment() {

    private lateinit var viewModel: JobsViewModel
    private lateinit var jobAdapter: JobAdapter
    private var _binding: FragmentJobsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[JobsViewModel::class.java]
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
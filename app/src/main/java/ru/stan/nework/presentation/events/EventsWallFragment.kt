package ru.stan.nework.presentation.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.databinding.FragmentEventsWallBinding

@AndroidEntryPoint
class EventsWallFragment : Fragment() {

    private lateinit var viewModel: EventsWallViewModel
    private lateinit var eventAdapter: EventAdapter
    private var _binding: FragmentEventsWallBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsWallBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[EventsWallViewModel::class.java]
        initAdapter()
        initEvents()
        return binding.root
    }

    private fun initAdapter() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collectLatest { events ->
                eventAdapter.submitList(events)
            }
        }
    }

    private fun initEvents() {
    eventAdapter = EventAdapter(object : ru.stan.nework.presentation.events.OnListener{

    })
        binding.rvListEvents.adapter = eventAdapter
        binding.rvListEvents.recycledViewPool.setMaxRecycledViews(
            EventAdapter.VIEW_TYPE, EventAdapter.MAX_POOL_SIZE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
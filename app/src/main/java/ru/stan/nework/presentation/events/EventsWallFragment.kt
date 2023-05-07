package ru.stan.nework.presentation.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentEventsWallBinding
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.presentation.home.disLike
import ru.stan.nework.presentation.home.onLike

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
        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate(R.id.action_eventsWallFragment_to_newEventFragment)
        }
        setupClickListener()
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
        eventAdapter = EventAdapter(object : ru.stan.nework.presentation.events.OnListener {
            override fun onRemove(event: Event) {
                event.id?.toLong()?.let { viewModel.removeEvent(it) }
            }
        })
        binding.rvListEvents.adapter = eventAdapter
        binding.rvListEvents.recycledViewPool.setMaxRecycledViews(
            EventAdapter.VIEW_TYPE, EventAdapter.MAX_POOL_SIZE
        )
    }

    private fun setupClickListener() {
        users = { listId ->
            val bundle = Bundle()
            bundle.putIntegerArrayList("ID", listId as ArrayList<Int>?)
            findNavController().navigate(
                R.id.action_eventsWallFragment_to_usersBottomSheetFragment,
                bundle
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
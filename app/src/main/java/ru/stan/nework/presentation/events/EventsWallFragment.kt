package ru.stan.nework.presentation.events

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentEventsWallBinding
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.utils.BOTTONMENU
import ru.stan.nework.utils.BaseFragment

@AndroidEntryPoint
class EventsWallFragment : BaseFragment<FragmentEventsWallBinding>() {

    override fun viewBindingInflate(): FragmentEventsWallBinding = FragmentEventsWallBinding.inflate(layoutInflater)

    private lateinit var viewModel: EventsWallViewModel

    private lateinit var eventAdapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BOTTONMENU.isVisible = true

        viewModel = ViewModelProvider(this)[EventsWallViewModel::class.java]

        initAdapter()
        initEvents()
        setupClickListener()
    }

    private fun initAdapter() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest (eventAdapter::submitData)
        }
    }

    private fun initEvents() {
        eventAdapter = EventAdapter(object : ru.stan.nework.presentation.events.OnListener {
            override fun onRemove(event: Event) {
                event.id.toLong().let { viewModel.removeEvent(it) }
            }
            override fun onEdit(event: Event) {
                val bundle = Bundle()
                event.id.let { bundle.putInt("EVENT", it) }
                findNavController().navigate(R.id.action_eventsWallFragment_to_newEventFragment, bundle)
            }

        })
        binding.rvListEvents.adapter = eventAdapter
        binding.rvListEvents.recycledViewPool.setMaxRecycledViews(
            EventAdapter.VIEW_TYPE, EventAdapter.MAX_POOL_SIZE
        )
    }

    private fun setupClickListener() {
        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate(R.id.action_eventsWallFragment_to_newEventFragment)
        }
        users = { listId ->
            val bundle = Bundle()
            bundle.putIntegerArrayList("ID", listId as ArrayList<Int>?)
            findNavController().navigate(
                R.id.action_eventsWallFragment_to_usersBottomSheetFragment,
                bundle
            )
        }
        onGo = { event ->
            event.id.let {
                viewModel.likeById(it.toLong())
            }
        }
        notGo = { event ->
            event.id.let {
                viewModel.deleteLike(it.toLong())
            }
        }
    }
}
package ru.stan.nework.presentation.account.pager.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentEventsBinding
import ru.stan.nework.databinding.FragmentWallBinding
import ru.stan.nework.presentation.account.pager.wall.WallAdapter
import ru.stan.nework.presentation.account.pager.wall.WallViewModel

class EventsFragment : Fragment() {

    private lateinit var viewModel: EventsViewModel
    private lateinit var eventAdapter: EventsAdapter
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}
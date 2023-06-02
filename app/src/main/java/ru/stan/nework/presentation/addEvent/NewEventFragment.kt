package ru.stan.nework.presentation.addEvent

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentNewEventBinding
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.presentation.users.UserAdapter
import ru.stan.nework.utils.BOTTONMENU
import ru.stan.nework.utils.BaseFragment
import ru.stan.nework.utils.DateHelper
import ru.stan.nework.utils.EVENTS
import ru.stan.nework.utils.SPEAKERS

@AndroidEntryPoint
class NewEventFragment : BaseFragment<FragmentNewEventBinding>() {
    override fun viewBindingInflate(): FragmentNewEventBinding =
        FragmentNewEventBinding.inflate(layoutInflater)

    private val viewModel: NewEventViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BOTTONMENU.isVisible = false

        initData()
        initClick()
    }

    private fun initData() {
        val userId = arguments?.getIntegerArrayList(SPEAKERS)
        if (userId != null) {
            viewModel.addUsrsId(userId)
            initUsers(userId)
        }

        val eventId = arguments?.getInt(EVENTS)
        if (eventId != null) {
            viewModel.eventInit(eventId)
            viewModel.newEvent.observe(viewLifecycleOwner) { event ->
                event.speakerIds?.let { speakers -> initUsers(speakers) }
                with(binding) {
                    event.content.let(etContentEvent::setText)
                    event.link.let(etLink::setText)
                    event.datetime.let(etData::setText)
                }
            }
        }
    }

    private fun initClick() {
        with(binding) {
            ibBack.setOnClickListener {
                findNavController().popBackStack()
            }
            etData.setOnClickListener {
                DateHelper.pickDate(etData, requireContext())
            }
            btnAddSpeakers.setOnClickListener {
                findNavController().navigate(R.id.action_newEventFragment_to_speakersFragment)
            }
            etLink.setOnClickListener {
                val link: String = etLink.text.toString()
                viewModel.addLink(link)
            }
            fbAdd.setOnClickListener {
                val content = etContentEvent.text.toString()
                val datetime = etData.text.toString()
                viewModel.addDateAndTime(datetime)
                val event = viewModel.newEvent.value!!.copy(content = content)
                if (content == "" || datetime == "") {
                    Snackbar.make(binding.root, "Заполнить все", Snackbar.LENGTH_SHORT).show()
                } else {
                    viewModel.createEvent(event)
                }
            }
        }
    }


    private fun initUsers(users: List<Int>) {
        users.map {
            viewModel.getUser(it.toLong())
        }
        viewModel.idUsers.observe(viewLifecycleOwner) {
            if (it != null) {
                initAdapter(it.toList())
            }
        }
    }

    private fun initAdapter(users: List<UserUI>) {
        val adapter = UserAdapter(users)
        binding.rvUsers.adapter = adapter
    }
}
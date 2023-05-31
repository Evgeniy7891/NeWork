package ru.stan.nework.presentation.addEvent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ru.stan.nework.utils.DateHelper

@AndroidEntryPoint
class NewEventFragment : Fragment() {

    private var _binding: FragmentNewEventBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    private val viewModel: NewEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewEventBinding.inflate(layoutInflater, container, false)
        BOTTONMENU.isVisible = false

        val userId = arguments?.getIntegerArrayList("ID")
        if (userId != null) {
            viewModel.addUsrsId(userId)
            initUsers(userId)
        }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val eventId = arguments?.getInt("EVENT")
        if (eventId != null) {
            viewModel.eventInit(eventId)
            viewModel.newEvent.observe(viewLifecycleOwner){ event ->
                event.speakerIds?.let { speakers -> initUsers(speakers) }
                with(binding){
                    event.content.let(etContentEvent::setText)
                    event.link.let(etLink::setText)
                    event.datetime.let(etData::setText)
                }
            }
        }

        binding.etData.setOnClickListener {
                DateHelper.pickDate(binding.etData, requireContext())
        }

        binding.btnAddSpeakers.setOnClickListener {
            findNavController().navigate(R.id.action_newEventFragment_to_speakersFragment)
        }

        binding.etLink.setOnClickListener {
            val link: String = binding.etLink.text.toString()
            viewModel.addLink(link)
        }

        binding.fbAdd.setOnClickListener {
            val content = binding.etContentEvent.text.toString()
            val datetime = binding.etData.text.toString()
            viewModel.addDateAndTime(datetime)
            val event = viewModel.newEvent.value!!.copy(content = content)
            if (content == "" || datetime == "") {
                Snackbar.make(binding.root, "Заполнить все", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.createEvent(event)
            }
        }
        return binding.root
    }
    private fun initUsers(users:List<Int>){
        users.map {
            viewModel.getUser(it.toLong())
        }
        viewModel.idUsers.observe(viewLifecycleOwner){
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
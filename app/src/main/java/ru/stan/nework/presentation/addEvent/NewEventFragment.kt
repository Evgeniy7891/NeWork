package ru.stan.nework.presentation.addEvent

import androidx.lifecycle.ViewModelProvider
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
import ru.stan.nework.databinding.FragmentPostBinding
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.presentation.addPost.PostViewModel
import ru.stan.nework.presentation.home.users.UsersHomeAdapter
import ru.stan.nework.utils.BOTTONMENU
import ru.stan.nework.utils.DateHelper
import java.util.ArrayList

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
            initAdapter(it)
        }
    }

    private fun initAdapter(users: List<UserUI>) {
        val adapter = UsersHomeAdapter(users)
        binding.rvUsers.adapter = adapter
    }
}
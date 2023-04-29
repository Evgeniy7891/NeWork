package ru.stan.nework.presentation.addEvent

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.R

@AndroidEntryPoint
class NewEventFragment : Fragment() {

    companion object {
        fun newInstance() = NewEventFragment()
    }

    private lateinit var viewModel: NewEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewEventViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
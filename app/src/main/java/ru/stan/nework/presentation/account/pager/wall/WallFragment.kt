package ru.stan.nework.presentation.account.pager.wall

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.stan.nework.R

class WallFragment : Fragment() {

    companion object {
        fun newInstance() = WallFragment()
    }

    private lateinit var viewModel: WallViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wall, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WallViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
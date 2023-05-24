package ru.stan.nework.presentation.usersProfile.pager.jobs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.stan.nework.R

class UserJobsFragment : Fragment() {

    companion object {
        fun newInstance() = UserJobsFragment()
    }

    private lateinit var viewModel: UserJobsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_jobs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserJobsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
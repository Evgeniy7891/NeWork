package ru.stan.nework.presentation.account.pager.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.databinding.FragmentSettingsBinding
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.presentation.account.pager.jobs.JobsViewModel
import ru.stan.nework.utils.DateHelper

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        binding.etStartJob.setOnClickListener {
            DateHelper.selectDateDialog(binding.etStartJob, requireContext())
            val startDate = binding.etStartJob.text.toString()
//            viewModel.addStartDate(startDate)
        }

        binding.etEndJob.setOnClickListener {
            DateHelper.selectDateDialog(binding.etEndJob, requireContext())
            val endDate = binding.etEndJob.text.toString()
//            viewModel.addEndDate(endDate)
        }

        binding.btnSave.setOnClickListener {

            if (binding.etNameJob.text.toString() == "" || binding.etPosition.text.toString() == "" || binding.etStartJob.text.toString() == "") {
                Snackbar.make(
                    binding.root,
                    "Пожалуйста укажите название компании, должность и начало работы",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
//                val id = if (viewModel.newJob.value!!.id == 0) {
//                    0
//                } else {
//                    viewModel.newJob.value!!.id
                }
                val name = binding.etNameJob.text?.trim().toString()
                val position = binding.etPosition.text?.trim().toString()
                val start = binding.etStartJob.text?.trim().toString()
                val finish = if (binding.etEndJob.text.toString() == "") {
                    null
                } else {
                    binding.etEndJob.text?.trim().toString()
                }
                val link = if (binding.etLink.text.toString() == "") {
                    null
                } else {
                    binding.etLink.text?.trim().toString()
                }
                val job = Job(id = viewModel.myId , name, position, start, finish, link)
                //viewModel.editJob(job)
                viewModel.addJob(job)
            }

        return binding.root
    }

}
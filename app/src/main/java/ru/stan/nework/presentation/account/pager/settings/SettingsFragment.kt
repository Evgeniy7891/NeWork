package ru.stan.nework.presentation.account.pager.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.databinding.FragmentSettingsBinding
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.presentation.account.AccountViewModel
import ru.stan.nework.utils.BaseFragment
import ru.stan.nework.utils.DateHelper

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override fun viewBindingInflate(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialClick()
    }

    private fun initialClick() {

        binding.etStartJob.setOnClickListener {
            DateHelper.selectDateDialog(binding.etStartJob, requireContext())
        }

        binding.etEndJob.setOnClickListener {
            DateHelper.selectDateDialog(binding.etEndJob, requireContext())
        }

        binding.btnSave.setOnClickListener {

            if (binding.etNameJob.text.toString() == "" || binding.etPosition.text.toString() == "" || binding.etStartJob.text.toString() == "") {
                Snackbar.make(
                    binding.root,
                    "Пожалуйста укажите название компании, должность и начало работы",
                    Snackbar.LENGTH_SHORT
                ).show()

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
                val job = Job(id = viewModel.myId, name, position, start, finish, link)
                viewModel.addJob(job)
            }
        }
    }
}
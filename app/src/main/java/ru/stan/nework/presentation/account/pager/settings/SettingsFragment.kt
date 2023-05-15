package ru.stan.nework.presentation.account.pager.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.databinding.FragmentSettingsBinding
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




        return binding.root
    }

}
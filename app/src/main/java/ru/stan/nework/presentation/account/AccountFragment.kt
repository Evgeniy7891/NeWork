package ru.stan.nework.presentation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.stan.nework.databinding.FragmentAccountBinding
import ru.stan.nework.presentation.account.pager.PagerAdapter

class AccountFragment : Fragment() {

    // private lateinit var viewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        initial()
        return binding.root
    }

    private fun initial() {
        binding.viewPager.adapter = PagerAdapter(requireActivity())
        TabLayoutMediator(binding.tab, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Posts"
                1 -> tab.text = "Events"
                2 -> tab.text = "Jobs"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
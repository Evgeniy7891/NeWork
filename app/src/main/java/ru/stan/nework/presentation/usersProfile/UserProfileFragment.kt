package ru.stan.nework.presentation.usersProfile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentAccountBinding
import ru.stan.nework.databinding.FragmentUserProfileBinding
import ru.stan.nework.presentation.account.pager.PagerAdapter
import ru.stan.nework.presentation.usersProfile.pager.PagerUsersAdapter

class UserProfileFragment : Fragment() {

   // private lateinit var viewModel: UserProfileViewModel

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        initial()
        return binding.root
    }

    private fun initial() {
        val id = arguments?.getLong("UserID")
        binding.viewPager.adapter = id?.let { PagerUsersAdapter(requireActivity(), it) }
        TabLayoutMediator(binding.tab, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Посты"
                1 -> tab.text = "Работа"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package ru.stan.nework.presentation.usersProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentUserProfileBinding
import ru.stan.nework.presentation.usersProfile.pager.PagerUsersAdapter
import ru.stan.nework.presentation.usersProfile.pager.jobs.UserJobsViewModel

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

   private lateinit var viewModel: UserProfileViewModel

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        initial()
        initUserInfo()
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun initial() {
        val id = arguments?.getLong("UserID")
        println("ID - $id")
        if (id != null) {
            viewModel.getUser(id)
        }
        binding.viewPager.adapter = id?.let { PagerUsersAdapter(requireActivity(), it) }
        TabLayoutMediator(binding.tab, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Посты"
                1 -> tab.text = "Работа"
            }
        }.attach()
    }
    private fun initUserInfo() {
        lifecycleScope.launchWhenCreated {
            viewModel.user.collectLatest { user ->
               binding.tvAuthor.text = user.name
                Glide.with(binding.ivAvatar)
                    .load(user.avatar)
                    .placeholder(R.drawable.ic_avatar)
                    .circleCrop()
                    .timeout(10_000)
                    .into(binding.ivAvatar)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
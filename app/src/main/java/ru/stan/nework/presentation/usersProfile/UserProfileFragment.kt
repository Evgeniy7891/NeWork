package ru.stan.nework.presentation.usersProfile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentUserProfileBinding
import ru.stan.nework.presentation.usersProfile.pager.PagerUsersAdapter
import ru.stan.nework.utils.BaseFragment

@AndroidEntryPoint
class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>() {

    override fun viewBindingInflate(): FragmentUserProfileBinding =
        FragmentUserProfileBinding.inflate(layoutInflater)

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initial()
        initUserInfo()
        initClick()
    }

    private fun initClick() {
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initial() {
        val id = arguments?.getLong("UserID")
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
}
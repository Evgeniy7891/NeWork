package ru.stan.nework.presentation.account

import android.content.SharedPreferences
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
import ru.stan.nework.databinding.FragmentAccountBinding
import ru.stan.nework.presentation.account.pager.PagerAdapter
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.utils.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    override fun viewBindingInflate() = FragmentAccountBinding.inflate(layoutInflater)

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initial()
        initInfo()
        exitForAccount()
    }
    private fun initial() {
        binding.viewPager.adapter = PagerAdapter(requireActivity())

        TabLayoutMediator(binding.tab, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Посты"
                1 -> tab.text = "Работа"
                2 -> tab.text = "Добавить работу"
            }
        }.attach()
    }
    private fun initInfo(){
        viewModel.data.observe(viewLifecycleOwner) {auth ->
            viewModel.getUser(auth.id)
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.user.collectLatest { user->
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
    private fun exitForAccount() {
        binding.ibExit.setOnClickListener {
            appAuth.removeAuth()
            findNavController().navigate(R.id.action_accountFragment_to_signInFragment)
        }
    }
}
package ru.stan.nework.presentation.account

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentAccountBinding
import ru.stan.nework.presentation.account.pager.PagerAdapter
import ru.stan.nework.presentation.usersProfile.UserProfileViewModel
import ru.stan.nework.providers.network.AppAuth
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    @Inject
    lateinit var prefs: SharedPreferences

    private lateinit var viewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        initial()
        initInfo()
        return binding.root
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
        viewModel.data.observe(this) {auth ->
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
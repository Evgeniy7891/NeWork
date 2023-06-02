package ru.stan.nework.presentation.home.users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.databinding.FragmentUsersBottomSheetBinding
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.utils.BaseDialogFragment

@AndroidEntryPoint
class UsersBottomSheetFragment :  BaseDialogFragment<FragmentUsersBottomSheetBinding>(){

    private val viewModel: UsersBottomSheetViewModel by viewModels()
    override fun viewBindingInflate(): FragmentUsersBottomSheetBinding = FragmentUsersBottomSheetBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = arguments?.getIntegerArrayList("ID")
        if (userId != null) {
            initUsers(userId)
        }
    }

    private fun initUsers(users:List<Int>){
        users.map {
            viewModel.getUser(it.toLong())
        }
        viewModel.idUsers.observe(viewLifecycleOwner){
            initAdapter(it)
        }
    }

    private fun initAdapter(users: List<UserUI>) {
        val adapter = UsersHomeAdapter(users)
        binding.rvUsers.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        viewModel.emptyList()
    }
}
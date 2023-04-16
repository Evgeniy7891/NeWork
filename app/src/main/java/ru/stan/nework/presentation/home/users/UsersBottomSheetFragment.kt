package ru.stan.nework.presentation.home.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.databinding.FragmentUsersBottomSheetBinding
import ru.stan.nework.domain.models.ui.user.UserUI

@AndroidEntryPoint
class UsersBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: UsersBottomSheetViewModel by viewModels()

    private var _binding: FragmentUsersBottomSheetBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        viewModel.emptyList()
    }
}
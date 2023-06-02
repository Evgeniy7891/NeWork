package ru.stan.nework.presentation.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentSignInBinding
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.utils.BOTTONMENU
import ru.stan.nework.utils.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    override fun viewBindingInflate(): FragmentSignInBinding = FragmentSignInBinding.inflate(layoutInflater)

    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var auth: AppAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.authenticated) findNavController().navigate(R.id.action_signInFragment_to_homeFragment)

        BOTTONMENU.isVisible = false
        initClick()
    }

    private fun initClick(){
        binding.btnSignIn.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            val name = binding.etName.text.toString()

            if (binding.etLogin.text.isNullOrBlank() || binding.etPassword.text.isNullOrBlank()) {
                Toast.makeText(activity, "Заполните все поля", Toast.LENGTH_LONG).show()
            } else {
                viewModel.register(login, password, name)
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }
        }

        binding.tvLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_logInFragment)
        }
    }
}
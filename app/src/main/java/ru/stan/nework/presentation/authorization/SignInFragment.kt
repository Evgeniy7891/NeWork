package ru.stan.nework.presentation.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentSignInBinding
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.utils.BOTTONMENU
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        BOTTONMENU.isVisible = false

        binding.btnSignIn.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            val name = binding.etName.text.toString()

            if (binding.etLogin.text.isNullOrBlank() || binding.etPassword.text.isNullOrBlank()) {
                Toast.makeText(
                    activity,
                    "Заполните все поля",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                viewModel.register(login, password, name)
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }
        }

        binding.tvLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_logInFragment)
        }


        if (viewModel.authenticated) findNavController().navigate(R.id.action_signInFragment_to_homeFragment)


        return binding.root
    }

}
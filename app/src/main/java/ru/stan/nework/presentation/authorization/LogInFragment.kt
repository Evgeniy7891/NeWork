package ru.stan.nework.presentation.authorization

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentLogInBinding

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private val viewModel: LogInViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLogInBinding.inflate(inflater, container, false)

        binding.btnSignIn.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val pass = binding.etPassword.text.toString()
            if (binding.etLogin.text.isNullOrBlank() || binding.etPassword.text.isNullOrBlank()) {
                Toast.makeText(
                    activity,
                    "Заполните все поля",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.authentication(login, pass)
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.entrance.observe(viewLifecycleOwner){
                        if(it) findNavController().navigate(R.id.action_logInFragment_to_homeFragment)
                        else  Snackbar.make(binding.root, "Неверное имя или пароль", Snackbar.LENGTH_SHORT).show()
                    }
                }, 2000L)
            }
        }
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onDestroyView() {
        parentFragment?.let { viewModel.entrance.removeObservers(it.viewLifecycleOwner) }
        super.onDestroyView()
    }
}
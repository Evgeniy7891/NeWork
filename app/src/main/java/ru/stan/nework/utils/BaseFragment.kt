package ru.stan.nework.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment <V : ViewBinding>: Fragment() {

    abstract fun viewBindingInflate(): V

    private var _binding: V? = null
    protected val binding: V get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = viewBindingInflate()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
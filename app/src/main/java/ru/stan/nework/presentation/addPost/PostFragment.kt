package ru.stan.nework.presentation.addPost

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentHomeBinding
import ru.stan.nework.databinding.FragmentPostBinding

class PostFragment : Fragment() {

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Cannot access view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(layoutInflater, container, false)

        binding.fbAdd.setOnClickListener {
            onAddButtonClicked()
        }

        return binding.root
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            binding.fbDone.visibility = View.VISIBLE
            binding.fbAttach.visibility = View.VISIBLE
            binding.fbCamera.visibility = View.VISIBLE
        } else {
            binding.fbDone.visibility = View.INVISIBLE
            binding.fbAttach.visibility = View.INVISIBLE
            binding.fbCamera.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            binding.fbDone.startAnimation(fromBottom)
            binding.fbAttach.startAnimation(fromBottom)
            binding.fbCamera.startAnimation(fromBottom)
            binding.fbAdd.startAnimation(rotateOpen)
        } else {
            binding.fbDone.startAnimation(toBottom)
            binding.fbAttach.startAnimation(toBottom)
            binding.fbCamera.startAnimation(toBottom)
            binding.fbAdd.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked){
            binding.fbDone.isClickable = true
            binding.fbAttach.isClickable = true
            binding.fbCamera.isClickable = true
        } else {
            binding.fbDone.isClickable = false
            binding.fbAttach.isClickable = false
            binding.fbCamera.isClickable = false
        }
    }
}
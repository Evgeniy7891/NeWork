package ru.stan.nework.presentation.addPost

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentPostBinding
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.MediaModel
import ru.stan.nework.utils.BOTTONMENU
import ru.stan.nework.utils.MediaHelper

@AndroidEntryPoint
class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), "Image pick error", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val uri = it.data?.data ?: return@registerForActivityResult
                    val resultFile = uri?.toFile()
                    val file = MultipartBody.Part.createFormData(
                        "file", resultFile?.name, resultFile!!.asRequestBody()
                    )
                    viewModel.changeMedia(uri, resultFile, AttachmentType.IMAGE)
                    viewModel.addMediaToPost(AttachmentType.IMAGE, file)
                }
            }
        }

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
        BOTTONMENU.isVisible = false
        val userId = arguments?.getIntegerArrayList("ID")
        if (userId != null) {
            viewModel.addUsrsId(userId)
        }
        val postId = arguments?.getInt("POST")
        if (postId != null) viewModel.postInit(postId)

        initPost()

        binding.fbAdd.setOnClickListener {
            onAddButtonClicked()
        }
        binding.fbDone.setOnClickListener {
            viewModel.createPost(binding.etContent.text.toString())
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().popBackStack()
            }, DELAY_OPEN_NEXT_SCREEN)
        }

        binding.fbAttach.setOnClickListener {
            ImagePicker.with(this).crop().compress(2048).provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf("image/png", "image/jpeg", "image/jpg")
                )
                .createIntent(photoLauncher::launch)
        }

        binding.fbCamera.setOnClickListener {
            ImagePicker.Builder(this).cameraOnly().maxResultSize(2048, 2048)
                .createIntent(photoLauncher::launch)
        }
        viewModel.media.observe(viewLifecycleOwner)
        { mediaModel ->
            if (mediaModel.uri == null) {
                return@observe
            }
            when (mediaModel.type) {
                AttachmentType.IMAGE -> {
                    binding.ivAttachment.setImageURI(mediaModel.uri)
                }

                AttachmentType.VIDEO -> {

                }

                AttachmentType.AUDIO -> {

                }

                null -> return@observe
            }
        }

        binding.ibAddUrers.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_usersFragment)
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
        if (!clicked) {
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
        if (!clicked) {
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
        if (!clicked) {
            binding.fbDone.isClickable = true
            binding.fbAttach.isClickable = true
            binding.fbCamera.isClickable = true
        } else {
            binding.fbDone.isClickable = false
            binding.fbAttach.isClickable = false
            binding.fbCamera.isClickable = false
        }
    }

    private fun initPost() {
        viewModel.newPost.observe(viewLifecycleOwner) { post ->
            with(binding) {
                post.content.let(etContent::setText)
                if (post.attachment?.url != "") {
                    when (post.attachment?.type) {
                        AttachmentType.VIDEO -> {
                            exo.visibility = View.VISIBLE
                            ivAttachment.visibility = View.GONE
                            val media = post.attachment!!.url?.let { MediaHelper(exo, it) }
                            media?.create()
                            exo.setOnClickListener {
                                media?.onPlay()
                            }
                        }

                        AttachmentType.IMAGE -> {
                            exo.visibility = View.GONE
                            ivAttachment.visibility = View.VISIBLE
                            Glide.with(ivAttachment)
                                .load(post.attachment!!.url)
                                .timeout(10_000)
                                .into(ivAttachment)
                        }

                        AttachmentType.AUDIO -> {
                            exo.visibility = View.VISIBLE
                            ivAttachment.visibility = View.GONE
                            val media = post.attachment!!.url?.let { MediaHelper(exo, it) }
                            media?.create()
                            exo.setOnClickListener {
                                media?.onPlay()
                            }
                        }

                        null -> {
                            exo.visibility = View.GONE
                            ivAttachment.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
    companion object {
        private const val DELAY_OPEN_NEXT_SCREEN = 2000L
    }
}
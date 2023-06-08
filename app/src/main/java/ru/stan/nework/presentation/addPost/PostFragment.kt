package ru.stan.nework.presentation.addPost

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.stan.nework.R
import ru.stan.nework.databinding.FragmentPostBinding
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.utils.BOTTONMENU
import ru.stan.nework.utils.BaseFragment
import ru.stan.nework.utils.MediaHelper
import ru.stan.nework.utils.POST
import ru.stan.nework.utils.USERS

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>() {

    override fun viewBindingInflate(): FragmentPostBinding =
        FragmentPostBinding.inflate(layoutInflater)

    private val viewModel: PostViewModel by viewModels()

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка выбора изображения",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val uri = it.data?.data ?: return@registerForActivityResult
                    val resultFile = uri.toFile()
                    val file = MultipartBody.Part.createFormData(
                        "file", resultFile.name, resultFile!!.asRequestBody()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BOTTONMENU.isVisible = false
        initPost()
        initData()
        initClick()
        initErrors()

        viewModel.media.observe(viewLifecycleOwner)
        { mediaModel ->
            if (mediaModel.uri == null) {
                return@observe
            }
            when (mediaModel.type) {
                AttachmentType.IMAGE -> {
                    binding.ivAttachment.setImageURI(mediaModel.uri)
                }

                AttachmentType.VIDEO -> {}
                AttachmentType.AUDIO -> {}
                null -> return@observe
            }
        }
    }

    private fun initErrors() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.errorMessage.collectLatest {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initClick() {

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fbAdd.setOnClickListener {
            onAddButtonClicked()
        }

        binding.fbDone.setOnClickListener {
            val text = binding.etContent.text.toString()
            if (text != "") {
                viewModel.createPost(binding.etContent.text.toString())
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().popBackStack()
                }, DELAY_OPEN_NEXT_SCREEN)
            } else initErrors()
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

        binding.ibAddUrers.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_usersFragment)
        }
    }

    private fun initData() {
        val userId = arguments?.getIntegerArrayList(USERS)
        if (userId != null) {
            viewModel.addUsrsId(userId)
        }
        val postId = arguments?.getInt(POST)
        if (postId != null) viewModel.postInit(postId)
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
            binding.ibAddUrers.visibility = View.VISIBLE
        } else {
            binding.fbDone.visibility = View.INVISIBLE
            binding.fbAttach.visibility = View.INVISIBLE
            binding.fbCamera.visibility = View.INVISIBLE
            binding.ibAddUrers.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fbDone.startAnimation(fromBottom)
            binding.fbAttach.startAnimation(fromBottom)
            binding.fbCamera.startAnimation(fromBottom)
            binding.fbAdd.startAnimation(rotateOpen)
            binding.ibAddUrers.startAnimation(fromBottom)
        } else {
            binding.fbDone.startAnimation(toBottom)
            binding.fbAttach.startAnimation(toBottom)
            binding.fbCamera.startAnimation(toBottom)
            binding.fbAdd.startAnimation(rotateClose)
            binding.ibAddUrers.startAnimation(toBottom)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.fbDone.isClickable = true
            binding.fbAttach.isClickable = true
            binding.fbCamera.isClickable = true
            binding.ibAddUrers.isClickable = true
        } else {
            binding.fbDone.isClickable = false
            binding.fbAttach.isClickable = false
            binding.fbCamera.isClickable = false
            binding.ibAddUrers.isClickable = false
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
        private const val DELAY_OPEN_NEXT_SCREEN = 500L
    }
}


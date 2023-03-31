package ru.stan.nework.presentation.home

import android.media.browse.MediaBrowser.MediaItem
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import ru.stan.nework.databinding.ItemPostBinding
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.utils.MediaHelper

class PostAdapter(private val listPosts: List<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPosts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = listPosts[position]
        holder.bind(post)
    }

    class ViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            with(binding) {
                tvAuthor.text = post.author
                tvTime.text = post.published
                tvContent.text = post.content
                when (post.attachment.type) {
                    AttachmentType.VIDEO -> {
                        exo.visibility = View.VISIBLE
                        val media = MediaHelper(exo, post.attachment.url)
                        media.create()
                        exo.setOnClickListener {
                            media.onPlay()
                        }
                    }
                    AttachmentType.IMAGE -> {
                        Glide.with(ivAtachment)
                            .load(post.attachment.url)
                            .timeout(5000)
                            .into(ivAtachment)
                    }
                    AttachmentType.AUDIO -> {
                        exo.visibility = View.VISIBLE
                        val media = MediaHelper(exo, post.attachment.url)
                        media.create()
                        exo.setOnClickListener {
                            media.onPlay()
                        }
                    }
                    else -> {
                        true
                    }
                }
            }
        }
    }
    companion object {
        const val MAX_POOL_SIZE = 20
        const val VIEW_TYPE = 0
    }
}
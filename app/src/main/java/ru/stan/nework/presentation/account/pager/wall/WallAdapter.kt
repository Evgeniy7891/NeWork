package ru.stan.nework.presentation.account.pager.wall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.stan.nework.databinding.ItemWallBinding
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.utils.MediaHelper

class WallAdapter :
    ListAdapter<Post, ViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    companion object {
        const val MAX_POOL_SIZE = 50
        const val VIEW_TYPE = 0
    }
}

class ViewHolder(
    private val binding: ItemWallBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            tvContent.text = post.content

            if (post.attachment?.url != "") {
                when (post.attachment?.type) {
                    AttachmentType.VIDEO -> {
                        exo.visibility = View.VISIBLE
                        ivAtachment.visibility = View.GONE
                        val media = post.attachment.url?.let { MediaHelper(exo, it) }
                        media?.create()
                        exo.setOnClickListener {
                            media?.onPlay()
                        }
                    }
                    AttachmentType.IMAGE -> {
                        exo.visibility = View.GONE
                        ivAtachment.visibility = View.VISIBLE
                        Glide.with(ivAtachment)
                            .load(post.attachment.url)
                            .timeout(10_000)
                            .into(ivAtachment)
                    }
                    AttachmentType.AUDIO -> {
                        exo.visibility = View.VISIBLE
                        ivAtachment.visibility = View.GONE
                        val media = post.attachment.url?.let { MediaHelper(exo, it) }
                        media?.create()
                        exo.setOnClickListener {
                            media?.onPlay()
                        }
                    }
                    null -> {
                        exo.visibility = View.GONE
                        ivAtachment.visibility = View.GONE
                    }
                }
            }
        }
    }
}


class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
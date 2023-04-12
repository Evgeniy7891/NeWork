package ru.stan.nework.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.stan.nework.R
import ru.stan.nework.databinding.ItemPostBinding
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.utils.MediaHelper

interface OnListener {
    fun getUsers(listId: List<Int>) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
}
var onLike: ((Post) -> Unit)? = null

class PostAdapter(private val onListener: OnListener) :
    ListAdapter<Post, ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onListener)
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
    private val binding: ItemPostBinding,
    private val onClickListener: OnListener
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(post: Post) {
        with(binding) {
            tvAuthor.text = post.author
            tvTime.text = post.published
            tvContent.text = post.content
            tvCountLiked.text = post.likeOwnerIds.size.toString()
            tvCountUsers.text = post.mentionIds.size.toString()
            ibUsers.setOnClickListener {
                onClickListener.getUsers(post.mentionIds)
            }
            tvCountLiked.setOnClickListener {
                onClickListener.getUsers(post.likeOwnerIds)
            }
            ibLiked.setOnClickListener {
                onLike?.invoke(post)
                if(!post.likedByMe) {
                    ibLiked.setImageResource(R.drawable.ic_liked_full)
                    val countLike = post.likeOwnerIds.size
                    tvCountLiked.text = (countLike + 1).toString()
                } else {
                    ibLiked.setImageResource(R.drawable.ic_liked)
                    tvCountLiked.text = (post.likeOwnerIds.size-1).toString()
                }
            }
            ibMenu.isVisible = post.ownedByMe
            ibMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_menu)
                    menu.setGroupVisible(R.id.menu_owned, post.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onClickListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onClickListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            Glide.with(ivAvatar)
                .load(post.authorAvatar)
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .timeout(10_000)
                .into(ivAvatar)
            println("Recycler VIEW!!! - ${post.author}, ${post.attachment?.type}, ${post.attachment?.url}")

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

    override fun getChangePayload(oldItem: Post, newItem: Post): Any = Unit
}
package ru.stan.nework.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
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

class PostAdapter(private val listPosts: List<Post>, private val onListener: OnListener) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onListener)
    }

    override fun getItemCount(): Int {
        return listPosts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = listPosts[position]
        holder.bind(post)
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
                ibMenu.isVisible = post.ownedByMe
                ibMenu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_menu)
                        menu.setGroupVisible(R.id.menu_owned, post.ownedByMe)
                        setOnMenuItemClickListener { item ->
                            when(item.itemId) {
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
                            .timeout(10_000)
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
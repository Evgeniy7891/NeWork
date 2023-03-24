package ru.stan.nework.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.stan.nework.databinding.ItemPostBinding
import ru.stan.nework.domain.models.ui.post.Post

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
            }
        }
    }
}
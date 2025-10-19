package com.igz.kotlin_story.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.igz.kotlin_story.R
import com.igz.kotlin_story.data.remote.model.Story
import com.igz.kotlin_story.databinding.ItemStoryBinding

class StoryAdapter(private val onClick: (View, Story) -> Unit) :
    ListAdapter<Story, StoryAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story) {
            binding.tvItemName.text = item.name
            Glide.with(binding.ivItemPhoto).load(item.photoUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivItemPhoto)
            binding.ivItemPhoto.transitionName = "photo_${'$'}{item.id}"
            binding.root.setOnClickListener { onClick(binding.ivItemPhoto, item) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean = oldItem == newItem
        }
    }
}

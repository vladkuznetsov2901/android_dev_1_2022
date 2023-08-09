package com.example.m18.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.m18.data.Photo
import com.example.m18.databinding.PhotoBinding

class PhotoAdapter :
    ListAdapter<Photo, PhotosAdapterViewHolder>(DiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapterViewHolder {
        val binding = PhotoBinding.inflate(LayoutInflater.from(parent.context))
        return PhotosAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosAdapterViewHolder, position: Int) {
        val photo = getItem(position)
        with(holder.binding) {
            Glide.with(photoView.context)
                .asBitmap()
                .load(photo.src)
                .into(photoView)
        }

    }

}

class PhotosAdapterViewHolder(val binding: PhotoBinding) :
    RecyclerView.ViewHolder(binding.root)

class DiffUtilCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
}
package com.example.m17.presentation.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.m17.data.Photo
import com.example.m17.databinding.PhotoItemBinding

class PhotosAdapter(
    private val onClick: (Photo) -> Unit,
) :
    PagingDataAdapter<Photo, PhotosAdapterViewHolder>(DiffUtilCallback()) {


    private var photos: List<Photo> = emptyList()
    fun setPhotos(photos: List<Photo>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapterViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context))
        return PhotosAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosAdapterViewHolder, position: Int) {
        val photo = photos[position]

        with(holder.binding) {
            Glide.with(cameraPhoto.context)
                .asBitmap()
                .load(photo.img_src)
                .into(cameraPhoto)
            roverText.text = ("Rover: " + photo.rover.name)
            cameraText.text = ("Camera: " + photo.camera.name)
            solText.text = ("Sol: " + photo.sol.toString())
            dateText.text = ("Date: " + photo.earth_date)
        }
        holder.binding.root.setOnClickListener {
            onClick(photo)
        }
    }

    override fun getItemCount(): Int = photos.size
}


class PhotosAdapterViewHolder(val binding: PhotoItemBinding) :
    RecyclerView.ViewHolder(binding.root)
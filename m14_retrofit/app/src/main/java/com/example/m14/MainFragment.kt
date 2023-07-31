package com.example.m14

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.m14.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parsingUser()

        binding.button.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            parsingUser()
        }

    }

    private fun parsingUser() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val response = RetrofitServices.searchUsersApi.getResultsOfUser()
                if (response.isSuccessful) {
                    val usersList = response.body()
                    for (result in usersList?.results!!) {

//                        binding.photo.load(result.picture.large)

                        Glide.with(requireContext())
                            .load(result.picture.large)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    p0: GlideException?,
                                    p1: Any?,
                                    target: Target<Drawable>?,
                                    p3: Boolean,
                                ): Boolean {
                                    Toast.makeText(
                                        context,
                                        "Error to load image",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressBar.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    p0: Drawable?,
                                    p1: Any?,
                                    target: Target<Drawable>?,
                                    p3: DataSource?,
                                    p4: Boolean,
                                ): Boolean {
                                    Toast.makeText(
                                        context,
                                        "Success",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressBar.visibility = View.GONE
                                    //do something when picture already loaded
                                    return false
                                }
                            })
                            .into(binding.photo)

                        binding.name.text =
                            getString(R.string.name) + result.name.first + " " + result.name.last
                        binding.phone.text = getString(R.string.phone) + result.phone
                        binding.email.text = getString(R.string.email) + result.email
                        binding.gender.text = getString(R.string.gender) + result.gender
                        binding.location.text =
                            getString(R.string.country) + result.location.country
                    }
                } else Log.d("API Response", "Failed!")
            }
        }


    }
}

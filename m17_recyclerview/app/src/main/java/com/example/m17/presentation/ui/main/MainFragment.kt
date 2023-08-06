package com.example.m17.presentation.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m17.R
import com.example.m17.data.Photo
import com.example.m17.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }

    private val photosAdapter = PhotosAdapter{
            photo -> onItemClick(photo)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = photosAdapter


        viewModel.photos.onEach {
            photosAdapter.setPhotos(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onItemClick(item: Photo){
        val bundle = bundleOf("photoURL" to item.img_src)
        findNavController().navigate(R.id.imageFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}
package com.example.m19.presentation

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.m19.R
import com.example.m19.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.LocationSource
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider

class MainFragment : Fragment() {
    private lateinit var fusedClient: FusedLocationProviderClient
    private var locationListener: LocationSource.OnLocationChangedListener? = null
    private var userLocation = Point()

    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.isNotEmpty() && map.values.all { it })
            startLocation()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {

            result.lastLocation?.let { location ->

                locationListener?.onLocationChanged(location)
                userLocation = Point(location.latitude, location.longitude)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocation() {
        val request = LocationRequest.create()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setInterval(1_000)

        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        MapKitFactory.setApiKey(MAP_API)
        MapKitFactory.initialize(context)

        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val map = binding.mapView.map
        val locationLayer =
            MapKitFactory.getInstance().createUserLocationLayer(binding.mapView.mapWindow)
        locationLayer.isVisible = true
        locationLayer.isAutoZoomEnabled = true
        viewModel.setAttractions()

        setMarkerInLocation(viewModel.attractionsList)


        binding.mapView.map.move(
            CameraPosition(userLocation, 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.LINEAR, 2F),
            null
        )

        binding.buttonPlus.setOnClickListener {
            val zoom = map.cameraPosition.zoom + 1.0f
            binding.mapView.map.move(
                CameraPosition(userLocation, zoom, 0.0f, 0.0f),
                Animation(Animation.Type.LINEAR, 1F),
                null
            )
        }

        binding.buttonMinus.setOnClickListener {
            val zoom = map.cameraPosition.zoom - 1.0f
            binding.mapView.map.move(
                CameraPosition(userLocation, zoom, 0.0f, 0.0f),
                Animation(Animation.Type.LINEAR, 1F),
                null
            )
        }

        val tapListener = object : GeoObjectTapListener, MapObjectTapListener {
            override fun onObjectTap(geoObjectTapEvent: GeoObjectTapEvent): Boolean {
                val attraction = viewModel.attractionsList.find {
                    it.name == geoObjectTapEvent.geoObject.name
                }
                attraction?.let { showAttractionDialog(it) }

                return false
            }

            override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
                TODO("Not yet implemented")
            }

        }

        mapObjectCollection.addTapListener(tapListener)



    }


    override fun onStart() {

        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()

        super.onStart()
        checkPermissions()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()

        super.onStop()


        fusedClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkPermissions() {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            startLocation()
        } else launcher.launch(REQUIRED_PERMISSIONS)
    }

    private fun setMarkerInLocation(attractionsList: ArrayList<Attraction>) {
        var attractionLocation: Point
        val marker = createBitmapFromVector(R.drawable.ic_pin_red)
        mapObjectCollection =
            binding.mapView.map.mapObjects
        for (attraction in attractionsList) {
            attractionLocation = Point(attraction.latitude, attraction.longitude)
            placemarkMapObject = mapObjectCollection.addPlacemark(
                attractionLocation,
                ImageProvider.fromBitmap(marker)
            )
            placemarkMapObject.opacity = 0.5f
            placemarkMapObject.setText(attraction.name.toString())
        }
    }

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(requireContext(), art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun showAttractionDialog(attraction: Attraction) {
        val dialogFragment = AttractionDialogFragment.newInstance(attraction)
        dialogFragment.show(childFragmentManager, "AttractionDialog")
    }


    companion object {
        fun newInstance() = MainFragment()
        private const val MAP_API = "8ed2a935-8bfe-47c0-9daf-d54b7f6ecccf"
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }


}
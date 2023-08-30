package com.example.m19.presentation

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            userLocation = Point(it.latitude, it.longitude)
            binding.mapView.map.move(
                CameraPosition(userLocation, 8F, 0f, 0f),
                Animation(Animation.Type.SMOOTH, 0.2f),
                null
            )
        }

        binding.buttonPlus.setOnClickListener {
            binding.mapView.map.cameraPosition.let {
                binding.mapView.map.move(
                    CameraPosition(it.target, it.zoom + 1, 0f, 0f),
                    Animation(Animation.Type.LINEAR, 1F),
                    null
                )

            }
        }

        binding.buttonMinus.setOnClickListener {
            binding.mapView.map.cameraPosition.let {
                binding.mapView.map.move(
                    CameraPosition(it.target, it.zoom - 1, 0f, 0f),
                    Animation(Animation.Type.LINEAR, 1F),
                    null
                )
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

    private val tapListener = object : GeoObjectTapListener, MapObjectTapListener {
        override fun onObjectTap(geoObjectTapEvent: GeoObjectTapEvent): Boolean {


            return false
        }

        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            for (attraction in viewModel.attractionsList) {
                if (Point(point.latitude, point.longitude) == Point(
                        attraction.latitude,
                        attraction.longitude
                    )
                ) {
                    attraction.let {
                        val dialog = AlertDialog.Builder(requireContext())
                            .setTitle(it.name)
                            .setMessage(it.title)
                            .setPositiveButton("Закрыть", null)
                            .create()
                        dialog.show()
                    }
                }
            }


            return false
        }
    }


    companion object {
        fun newInstance() = MainFragment()
        private const val MAP_API = "8ed2a935-8bfe-47c0-9daf-d54b7f6ecccf"
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }


}
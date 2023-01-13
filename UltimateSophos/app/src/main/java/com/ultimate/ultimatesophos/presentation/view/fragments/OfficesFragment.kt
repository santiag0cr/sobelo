package com.projects.sophosapp.presentation.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.projects.sophosapp.R
import com.projects.sophosapp.databinding.FragmentOfficesBinding
import com.ultimate.ultimatesophos.domain.definitions.Constants.OFFICES_INFO
import com.ultimate.ultimatesophos.domain.models.OfficesResponseDto

class OfficesFragment :
    Fragment(),
    OnMapReadyCallback {

    interface OfficesFragmentListener {
        fun showMessageFromOffices(
            title: String,
            message: String,
            acceptButtonMessage: String,
            rejectButtonMessage: String = "",
            acceptAction: () -> Unit = {},
            rejectAction: () -> Unit = {},
            cancelable: Boolean,
        )
    }

    private var _binding: FragmentOfficesBinding? = null
    private val binding get() = _binding!!
    private var officesInfo: OfficesResponseDto? = null
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOfficesBinding.inflate(inflater, container, false)
        arguments?.getParcelable<OfficesResponseDto>(OFFICES_INFO)?.let {
            officesInfo = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocation()
        setMapLocation()
    }

    private fun setMapLocation() {
        if (isLocationPermissionGranted() && map.isMyLocationEnabled) {
            setMapOnCurrentLocation()
        } else {
            setMapOnDefaultLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        initializeMap()
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
            return
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            (activity as OfficesFragmentListener).showMessageFromOffices(
                getString(R.string.location_title),
                getString(R.string.location_subtitle),
                getString(R.string.accept_message),
                acceptAction = { showLocationPermissionDialog() },
                cancelable = false
            )
        } else {
            (activity as OfficesFragmentListener).showMessageFromOffices(
                getString(R.string.location_title),
                getString(R.string.location_subtitle_negative),
                getString(R.string.accept_message),
                cancelable = false
            )
        }
    }

    private fun showLocationPermissionDialog() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.isMyLocationEnabled = true
                } else {
                    (activity as OfficesFragmentListener).showMessageFromOffices(
                        getString(R.string.location_title),
                        getString(R.string.location_subtitle_negative),
                        getString(R.string.accept_message),
                        cancelable = false
                    )
                }
            }
            else -> { }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun initializeMap() {
        officesInfo?.let {
            it.items.forEach { office ->
                map.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            office.latitude.toDouble(),
                            office.longitude.toDouble()
                        )
                    ).title(office.name)
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun setMapOnCurrentLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lastLocation = LatLng(location.latitude, location.longitude)
                val cameraPosition = CameraPosition(lastLocation, 10f, 0f, 0f)
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

    private fun setMapOnDefaultLocation() {
        val bogotaLocation = LatLng(4.5709, -74.2973)
        val cameraPosition = CameraPosition(bogotaLocation, 5f, 0f, 0f)
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 100
    }
}
